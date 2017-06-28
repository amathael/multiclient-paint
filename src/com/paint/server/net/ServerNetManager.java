package com.paint.server.net;

import com.paint.global.net.Message;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by Admin on 15.02.2017.
 */
public class ServerNetManager {

    private int DEFAULT_PORT = 7777;

    private ConnectionManager connectionManager;
    private volatile HashSet<ClientConnection> connections;

    public static void main(String[] args) throws IOException, InterruptedException {
        new ServerNetManager();
    }

    private ServerNetManager() throws IOException, InterruptedException {
        connections = new HashSet<>();
        connectionManager = new ConnectionManager(this, DEFAULT_PORT, connections);

        System.err.println("starting");
    }

    void handleMessage(ClientConnection sender, String message) throws IOException {
        if (message == null) {
            connectionManager.removeConnection(sender);
        } else {
            Message msg = new Message(message);

            for (ClientConnection receiver : connections) {
                if (receiver.isInitialized()) {
                    if (receiver.isInitialized() && (!Objects.equals(receiver.getName(), msg.getValue("name")) || msg.hasToken("text"))) {
                        receiver.sendMessage(message);
                    }
                }
            }
        }
    }

}
