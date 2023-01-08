package pt.isec.amov.mathit.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import pt.isec.amov.mathit.model.data.Table
import pt.isec.amov.mathit.model.data.multiplayer.*
import pt.isec.amov.mathit.utils.*
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.io.*
import java.net.*
import java.util.LinkedList
import kotlin.concurrent.thread


/*
* multicast: send tcp ip, port, name of the lobby, players connected. Ex.
* {"host": "127.0.0.1", "5001","name": "player12345's lobby", "players": 4 }
*
* receive the above json and list it on the view
* */

@Suppress("DEPRECATION")
object ConnectionManager {
    const val PLAYERS_PROP = "players"
    const val STARTING_MULTIPLAYER = "starting_multiplayer"
    const val INITIATE_FRAGMENT = "initiate_fragment"
    const val NEXT_BOARD = "next_board"
    private const val multicastHost = "230.30.30.30"
    private const val multicastPort = 4004
    private lateinit var multicastSocket: MulticastSocket
    private lateinit var serverSocket: ServerSocket
    private var senderThread: Thread? = null
    private var keepSending = false
    private var keepReceiving = false
    private var keepConnected = false
    private var isHost = false
    private var localServerData: ServerData? = null
    private var serverList: ArrayList<ServerData> = ArrayList()
    private var connectedClients: ArrayList<Socket> = ArrayList()
    private lateinit var localPlayer: Player
    private lateinit var profilePicPath: String
    private var tableList: ArrayList<Table> = ArrayList()
    private var levelOfPlayer: HashMap<String, Int> = HashMap()

    private var currentBoards : LinkedList<ArrayList<String>> = LinkedList(ArrayList())
    private var bestCombinations : LinkedList<ArrayList<String>> = LinkedList(ArrayList())
    private var secondBestCombinations : LinkedList<ArrayList<String>> = LinkedList(ArrayList())

    var nextBoard : NextBoardData? = null

    private fun startMulticastSocket() {
        val group = InetAddress.getByName(multicastHost)
        multicastSocket = MulticastSocket(multicastPort)
        val inetSocketAddress = InetSocketAddress(group, multicastPort)
        val networkInterface = NetworkInterface.getByIndex(0)
        multicastSocket.joinGroup(inetSocketAddress, networkInterface)
    }

    private var strIPAddress: String = ""
    fun startServer(context: Context, name: String) {

        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val linkProperties = connectivityManager.getLinkProperties(connectivityManager.activeNetwork)
            val addresses = linkProperties!!.linkAddresses
            for (address in addresses) {
                val inetAddress = address.address
                if (inetAddress is Inet4Address) {
                    val ipAddress = inetAddress.hostAddress
                    if (ipAddress != null) {
                        strIPAddress = ipAddress
                        break
                    }
                }
            }
        } else {
            val wifiManager = context.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE)
                    as WifiManager
            val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
            strIPAddress = String.format("%d.%d.%d.%d",
                ip and 0xff,
                (ip shr 8) and 0xff,
                (ip shr 16) and 0xff,
                (ip shr 24) and 0xff
            )
        }

        serverSocket = ServerSocket(0) //automatic port
        localServerData = ServerData(
            strIPAddress, serverSocket.localPort,
        name)
        isHost = true
        startServerSender()
        startServerCommunication()
    }

    private fun startServerCommunication() {
        thread {
            Log.i("DEBUG-AMOV", "startServerCommunication: started client request handler")
            while (keepSending) {
                val clientSocket = serverSocket.accept()
                Log.i("DEBUG-AMOV", "startServerCommunication: Client connected")
                //add to list
                connectedClients.add(clientSocket)
                //startClientRequestHandler
                startClientRequestHandler(clientSocket)
            }
            Log.i("DEBUG-AMOV", "startServerCommunication: ended client request handler")
        }
    }

    //Server handler for clients
    private fun startClientRequestHandler(clientSocket: Socket?) {
        thread {
            while (clientSocket?.isClosed == false) {
                val receivedMessage = receiveFromSocket(clientSocket)
                Log.i("DEBUG-AMOV", "startClientRequestHandler: received request from client $receivedMessage")
                try {
                    val jsonObject = JSONObject(receivedMessage)
                    if (jsonObject.has("player_name")) {
                        //received player info
                        val player = jsonObjectToPlayer(jsonObject)
                        //receiveImage(player!!.name, clientSocket)
                        if (!PlayersData.contains(player!!)) {
                            PlayersData.addPlayer(player)
                            val handler = Handler(Looper.getMainLooper())
                            handler.post{
                                pcs.firePropertyChange(PLAYERS_PROP, null, null)
                            }
                        }
                        sendDataToAllClients(playerListToJsonObject(PlayersData.getPlayers()).toString())
                    } else if(jsonObject.has("current_board")) {
                        //when a player completes a board the server receives
                        //the number of the current board
                        //the tiles selected
                        //the name of the player
                        //the points the player currently has
                        //the level the player is currently in

                        val currentBoard = jsonObjectToNewMove(jsonObject)
                        val points = determineHowManyPointsWereWon(currentBoard?.tilesSelected, currentBoard?.currentBoard)
                        val nextBoard = currentBoards[currentBoard?.currentBoard!! + 1]
                        val jObj = nextBoardDataToJsonObject(NextBoardData(nextBoard, points, currentBoard.currentBoard + 1))
                        sendToSocket(clientSocket, jObj.toString())
                        PlayersData.updatePlayer(Player(currentBoard.username)
                            .also { it.level = currentBoard.level; it.score = (currentBoard.points + points).toLong()  })
                    }
                } catch (e:java.lang.Exception) {
                    Log.i("DEBUG-AMOV", "startClientRequestHandler: Something went wrong $e")
                }
                //val message = "Hello there little client"
                //sendToSocket(clientSocket, message)
            }
        }
    }

    //Client handler to receive server messages
    private fun startCommunication() {
        //receives data from the server
        Log.i("DEBUG-AMOV", "startCommunication: connected to a server")
        val jsonObjectOfPlayer = playerToJson(localPlayer)
        Thread.sleep(500)
        sendToSocket(socketClient!!, jsonObjectOfPlayer.toString())
        //sendImage(profilePicPath, socket!!)

        while(keepConnected && socketClient != null && socketClient?.isClosed == false) {
            Log.i("DEBUG-AMOV", "startCommunication: waiting for server message")
            val receivedObject = receiveFromSocket(socketClient!!)
            Log.i("DEBUG-AMOV", "startCommunication: Received message from server: $receivedObject")
            try {
                if(receivedObject == "start_game"){
                    val handler = Handler(Looper.getMainLooper())
                    handler.post{
                        pcs.firePropertyChange(INITIATE_FRAGMENT, null, null)
                    }

                    continue
                }

                val jsonObject = JSONObject(receivedObject)
                if (jsonObject.has("players")) {
                    //received list of players
                    val listPlayers = playerJsonObjectToPlayerList(jsonObject)
                    for (p in listPlayers){
                        PlayersData.addPlayer(p)
                    }
                    val handler = Handler(Looper.getMainLooper())
                    handler.post{
                        pcs.firePropertyChange(PLAYERS_PROP, null, null)
                        Log.i("DEBUG-AMOV", "startCommunication: firing property")
                    }
                    continue
                }
                if(jsonObject.has("next_level")) {
                    levelData = levelDataJsonObjectToLevelData(jsonObject)!!
                    val handler = Handler(Looper.getMainLooper())
                    handler.post{
                        pcs.firePropertyChange(STARTING_MULTIPLAYER, null, null)
                        Log.i("STARTING_MULTIPLAYER", "startCommunication: firing property")
                    }
                    continue
                }
                if(jsonObject.has("next_board")){
                    nextBoard = jsonObjectToNextBoardData(jsonObject)
                    val handler = Handler(Looper.getMainLooper())
                    handler.post{
                        pcs.firePropertyChange(NEXT_BOARD, null, null)
                        Log.i("DEBUG-AMOV", "startCommunication: firing property")
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.i("DEBUG-AMOV", "startCommunication: failed to parse json $e")
                keepConnected = false
            }
        }
    }

    private fun determineHowManyPointsWereWon(tilesSelected: ArrayList<String>?, currentBoard : Int?) : Int{
        if (bestCombinations[currentBoard!!].containsAll(tilesSelected!!)){
            return 2
        }
        if (secondBestCombinations[currentBoard].containsAll(tilesSelected)){
            return 1
        }
        return 0
    }

    fun askForNextBoard(currentBoard: Int, tilesSelected: ArrayList<String>, username : String,
                        points : Int, level : Int){
        val jsonObject = newMoveToJsonObject(NewMove(currentBoard, tilesSelected, username, points, level))
        send(socketClient!!, jsonObject.toString())
    }

    fun sendLevelDataToPlayers(levelData: NextLevelData){
        this.levelData = levelData
        val jsonObject = levelDataToJsonObject(levelData)
        sendDataToAllClients(jsonObject.toString())
    }

    private fun receiveImage(playerName: String, socket: Socket) {
        val inputStream = BufferedInputStream(socket.getInputStream())
        val file = File(playerName)
        val fileOutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var count: Int
        while (inputStream.read(buffer).also { count = it } > 0) {
            fileOutputStream.write(buffer, 0, count)
        }
        fileOutputStream.close()
    }

    fun closeEverything() {
        closeServer()
        closeServerSender()
        closeServerListener()
        keepConnected = false
    }

    private fun sendToSocket(socket: Socket, data: String) {
        val writer = PrintWriter(socket.getOutputStream())
        writer.println(data)
        writer.flush()
        Log.i("DEBUG-AMOV", "sendToSocket: sent message to client $data")
    }

    private fun send(socket: Socket, data: String) {
        val writer = PrintWriter(socket.getOutputStream())
        writer.println(data)
        Log.i("DEBUG-AMOV", "sendToSocket: sent message to client $data")
    }

    private fun receiveFromSocket(socket: Socket): String {
        return try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val receivedObject = reader.readLine()
            Log.i("DEBUG-AMOV", "receiveFromSocket: received request from client $receivedObject")
            receivedObject
        } catch (_:java.lang.Exception) {
            ""
        }
    }

    fun sendDataToAllClients(data: String) {
        thread {
            for(clientSocket in connectedClients) {
                sendToSocket(clientSocket, data)
                Log.i("SENDING TO ALL CLIENTS", "sendDataToAllClients: Sending Data to all clients: $data")
            }
        }
    }

    private  fun startServerSender() {
        Log.i("DEBUG-AMOV", "startServerSender:  started")
        keepSending = true
        if(!PlayersData.contains(localPlayer))
            PlayersData.addPlayer(localPlayer)
        thread {
            while (keepSending) {
                if (multicastSocket.isClosed) {
                    keepSending = false
                    keepReceiving = false
                }
                val jsonObject = serverDataToJson(localServerData!!)
                val jsonString = jsonObject.toString()
                val data = jsonString.toByteArray()
                val socket = DatagramSocket()
                val packet = DatagramPacket(
                    data,
                    data.size,
                    InetAddress.getByName(multicastHost),
                    multicastPort
                )
                try {
                    socket.send(packet)
                } catch (_: Exception) {
                }
                Log.i("DEBUG-AMOV", "startServerSender:  sent serverData")
                Thread.sleep(10000)
            }
        }
    }

    fun closeServer() {
        closeServerSender()
        if(!isHost)
            return
        if(!serverSocket.isClosed)
            serverSocket.close()
    }

    private fun closeServerSender() {
        keepSending = false
        if(this::multicastSocket.isInitialized &&!multicastSocket.isClosed)
            multicastSocket.close()
    }

    fun startServerListener(listView: ListView, localPlayer: Player?, imagePath: String?) {
        if(imagePath != null)
            profilePicPath = imagePath
        if (localPlayer == null)
            this.localPlayer = Player("temporary")
        else
            this.localPlayer = localPlayer
        Log.i("DEBUG-AMOV", "startServerListener:  started")
        startMulticastSocket()
        keepReceiving = true
        val buffer = ByteArray(1024)
        senderThread = thread {
            while (keepReceiving) {
                if(multicastSocket.isClosed) {
                    keepSending = false
                    keepReceiving = false
                }
                val packet = DatagramPacket(buffer, buffer.size)
                try {
                    multicastSocket.receive(packet)
                    val receivedObject = packet.data
                    val jsonString = String(receivedObject)
                    val jsonObject = JSONObject(jsonString)
                    val server = jsonObjectToServerData(jsonObject)
                    if (server?.host == strIPAddress)
                        continue
                    if (!serverList.contains(server))
                        server?.let { serverList.add(it) }
                    listServers(listView)
                } catch (_:Exception) {
                    keepSending = false
                    keepReceiving = false
                }
            }
        }
    }

    private fun listServers(listView: ListView) {
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(listView.context,
            android.R.layout.simple_list_item_1, serverList.toArray())
        val handler = Handler(Looper.getMainLooper())
        handler.post{
            listView.apply { adapter = arrayAdapter }
        }
    }

    fun closeServerListener() {
        keepReceiving = false
        serverList.clear()
    }

    private var socketClient: Socket? = null

    fun startClient(index: Int) : Boolean {
        isHost = false
        var result = true
        thread {
            if(index > serverList.size) {
                result = false
                return@thread
            }
            val serverData = serverList[index]
            try {
                socketClient = Socket(InetAddress.getByName(serverData.host), serverData.port)
                thread {
                    keepConnected = true
                    startCommunication()
                }
            } catch (e: IOException) {
                Log.i("DEBUG-AMOV", "startClient: failed to connect to server $e")
            }
        }
        return result
    }

    fun resetPlayers() {
        PlayersData.clear()
    }

    var levelData: NextLevelData? = null

    private fun sendImage(imagePath: String?, socket: Socket) {
        if(imagePath==null)
            return
        val outputStream = BufferedOutputStream(socket.getOutputStream())
        val fileInputStream = FileInputStream(imagePath)
        val buffer = ByteArray(1024)
        var count: Int
        while (fileInputStream.read(buffer).also { count = it } > 0) {
            outputStream.write(buffer, 0, count)
        }
        outputStream.flush()
        fileInputStream.close()
    }

    fun isHost(): Boolean {
        return isHost
    }

    var pcs: PropertyChangeSupport = PropertyChangeSupport(this)
    fun addPropertyChangeListener(
        property: String?,
        listener: PropertyChangeListener?
    ) {
        pcs.addPropertyChangeListener(property, listener)
    }

    fun getConnectedPlayers(): List<Player> {
        return PlayersData.getPlayers()
    }

    fun sendNextLevel(player: Player) {
        val message = playerToJson(player)
        thread { sendToSocket(socketClient!!, message.toString()) }
    }

    fun setAllBoards(boards : LinkedList<ArrayList<String>>){
        currentBoards.clear()
        currentBoards = boards
    }

    fun setBestCombinations(combinations : LinkedList<ArrayList<String>>){
        bestCombinations = combinations
    }

    fun setSecondBestCombinations(combinations : LinkedList<ArrayList<String>>){
        secondBestCombinations = combinations
    }
}