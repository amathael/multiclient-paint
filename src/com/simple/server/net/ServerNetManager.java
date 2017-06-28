package com.simple.server.net;

import com.paint.global.net.Message;

import java.io.IOException;
import java.util.HashSet;

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
            msg.addToken("name", sender.getName());
            String newMessage = msg.asString();
            System.out.println(newMessage);

            for (ClientConnection receiver : connections) {
                receiver.sendMessage(newMessage);
            }
        }
    }

}
