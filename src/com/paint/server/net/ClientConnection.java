package com.paint.server.net;

import com.paint.global.net.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Admin on 15.02.2017.
 */
class ClientConnection {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String name;
    private int accessLevel;
    private boolean connected;
    private volatile boolean initialized = false;

    ClientConnection(ServerNetManager server, Socket socket, int accessLevel) throws IOException {
        this.socket = socket;
        this.accessLevel = accessLevel;
        connected = true;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(() -> {
            try {
                name = new Message().parseTokens(receiveMessage()).getValue("init");
                initialized = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (connected) {
                try {
                    server.handleMessage(this, receiveMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    boolean isInitialized() {
        return initialized;
    }

    void markForRemove() throws IOException {
        socket.close();
        in.close();
        out.close();

        connected = false;
    }

    void sendMessage(String text) {
        out.println(text);
    }

    private String receiveMessage() throws IOException {
        try {
            return in.readLine();
        } catch (SocketException e) {
            return null;
        }
    }

    String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
