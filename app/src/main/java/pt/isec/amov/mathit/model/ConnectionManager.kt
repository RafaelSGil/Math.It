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
import pt.isec.amov.mathit.model.data.Player
import pt.isec.amov.mathit.model.data.ServerData
import pt.isec.amov.mathit.utils.jsonObjectToServerData
import pt.isec.amov.mathit.utils.serverDataToJson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.*
import kotlin.concurrent.thread


/*
* multicast: send tcp ip, port, name of the lobby, players connected. Ex.
* {"host": "127.0.0.1", "5001","name": "player12345's lobby", "players": 4 }
*
* receive the above json and list it on the view
* */

object ConnectionManager {
    private var multicastHost = "230.30.30.30"
    private var multicastPort = 4004
    private lateinit var multicastSocket: MulticastSocket
    private lateinit var serverSocket: ServerSocket
    private var senderThread: Thread? = null
    private var keepSending = false
    private var keepReceiving = false
    private var keepConnected = false
    private var isHost = false
    private var localServerData: ServerData? = null
    private var serverList: ArrayList<ServerData> = ArrayList()
    private var playersList: ArrayList<Player> = ArrayList()
    private var connectedClients: ArrayList<Socket> = ArrayList()


    private fun startMulticastSocket() {
        val group = InetAddress.getByName(multicastHost)
        multicastSocket = MulticastSocket(multicastPort)
        val inetSocketAddress = InetSocketAddress(group, multicastPort)
        val networkInterface = NetworkInterface.getByIndex(0)
        multicastSocket.joinGroup(inetSocketAddress, networkInterface)
    }

    var strIPAddress: String = ""
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

    private fun startClientRequestHandler(clientSocket: Socket?) {
        thread {
            while (clientSocket?.isClosed == false) {
                val receivedMessage = receiveFromSocket(clientSocket)
                Log.i("DEBUG-AMOV", "startClientRequestHandler: received request from client $receivedMessage")
                val message = "Hello there little client"
                sendToSocket(clientSocket, message)
            }
        }
    }

    private fun sendToSocket(socket: Socket, data: String) {
        val writer = PrintWriter(socket.getOutputStream())
        writer.println(data)
        writer.flush()
        Log.i("DEBUG-AMOV", "sendToSocket: sent message to client $data")
    }

    private fun receiveFromSocket(socket: Socket): String {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val receivedObject = reader.readLine()
        Log.i("DEBUG-AMOV", "receiveFromSocket: received request from client $receivedObject")
        return receivedObject
    }

    private fun sendDataToAllClients() {
        thread {
            for(clientSocket in connectedClients) {
                val message = "Hello There dashjdasjd sadh kasjdh as"
                sendToSocket(clientSocket, message)
                Log.i("DEBUG-AMOV", "sendDataToAllClients: Sending Data to all clients: $message")
            }
        }
    }

    private  fun startServerSender() {
        Log.i("DEBUG-AMOV", "startServerSender:  started")
        keepSending = true
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
        if(!serverSocket.isClosed)
            serverSocket.close()
    }

    private fun closeServerSender() {
        keepSending = false
    }

    fun startServerListener(listView: ListView) {
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
                multicastSocket.receive(packet)
                val receivedObject = packet.data
                val jsonString = String(receivedObject)
                val jsonObject = JSONObject(jsonString)
                val server = jsonObjectToServerData(jsonObject)
                if(server.host == strIPAddress)
                    continue
                if(!serverList.contains(server))
                    serverList.add(server)
                Log.i("DEBUG-AMOV", "startServerListener:  received serverData")
                listServers(listView)
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
    }

    private var socket: Socket? = null

    fun startClient(index: Int) {
        isHost = false
        thread {
            val serverData = serverList[index]
            try {
                socket = Socket(InetAddress.getByName(serverData.host), serverData.port)
                thread {
                    keepConnected = true
                    startCommunication()
                }
            } catch (e: IOException) {
                Log.i("DEBUG-AMOV", "startClient: falied to connect to server $e")
            }
        }
    }

    private fun startCommunication() {
        Log.i("DEBUG-AMOV", "startCommunication: connected to a server")
        val initMessage = "Hello there server"
        sendToSocket(socket!!, initMessage)
        while(keepConnected && socket != null && socket?.isClosed == false) {
            Log.i("DEBUG-AMOV", "startCommunication: waiting for server message")
            val receivedObject = receiveFromSocket(socket!!)
            Log.i("DEBUG-AMOV", "startCommunication: Received message from server: $receivedObject")
        }
    }

    fun isHost(): Boolean {
        return isHost;
    }
}