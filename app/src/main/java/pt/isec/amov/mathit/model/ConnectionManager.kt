package pt.isec.amov.mathit.model

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.MulticastSocket
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

/*
* multicast: send tcp ip, port, name of the lobby, players connected. Ex.
* {"host": "127.0.0.1", "5001","name": "salgueirinho's lobby", "players": 4 }
*
* receive the above json and list it on the view
* */

class ConnectionManager(var modelManager: ModelManager) {
    companion object {
        private var multicastHost = "230.30.30.30"
        private var multicastPort = 4004
    }
    private var multicastSocket: MulticastSocket
    private lateinit var serverSocket: ServerSocket

    init {
        var group = InetAddress.getByName(multicastHost)
        multicastSocket = MulticastSocket(multicastPort)
        var inetSocketAddress = InetSocketAddress(group, multicastPort)
        var networkInterface = NetworkInterface.getByIndex(0)
        multicastSocket.joinGroup(inetSocketAddress, networkInterface)
    }

    fun startServer() {
        serverSocket = ServerSocket(0) //automatic port
        //startsServerListener
        //startMulticastSender
    }

    fun startServerListener() {
        thread {

        }
    }

    fun startClient(host: String, port: Int) {
        val socket = Socket()
        socket.connect(InetSocketAddress(host, port), 5000)
        thread {
            startCommunication(socket)
        }
    }

    private fun startCommunication(socket: Socket) {
        //todo
    }
}