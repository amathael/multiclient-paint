package com.paint.server.net;

import com.paint.global.net.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Admin on 15.02.2017.
 */
class ConnectionManager {

    private ServerSocket connectionWaiter;
    private volatile HashSet<ClientConnection> connections;

    ConnectionManager(ServerNetManager server, int port, HashSet<ClientConnection> connections) throws IOException {
        connectionWaiter = new ServerSocket(port);
        this.connections = connections;

        new Thread(() -> {
            while (true) {
                try {
                    Socket newSocket = connectionWaiter.accept();
                    ClientConnection newConnection = new ClientConnection(server, newSocket, 0);
                    connections.add(newConnection);
                    new Thread(() -> {
                        while (!newConnection.isInitialized()) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message msg = new Message("server", "Welcome, " + newConnection.getName() + "!");
                        newConnection.sendMessage(msg.asString());
                    }).start();

                    System.err.println("connected " + newConnection.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void removeConnection(ClientConnection connection) throws IOException {
        connection.markForRemove();
        connections.remove(connection);
        System.err.println("disconnected " + connection.getName());
    }

}
