package com.simple.user.net;

import com.paint.global.net.Message;
import com.paint.user.gui.Frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Admin on 15.02.2017.
 */
public class UserNetManager {

    private static String HOST = "127.0.0.1";
    private int DEFAULT_PORT = 7777;
    private Socket socketToServer;
    private Frame frame;

    private BufferedReader in;
    private PrintWriter out;

    public UserNetManager(Frame frame) throws IOException {
        this.frame = frame;
        socketToServer = new Socket(HOST, DEFAULT_PORT);

        in = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
        out = new PrintWriter(socketToServer.getOutputStream(), true);

        new Thread(() -> {
            while (true) {
                if (!socketToServer.isClosed()) {
                    try {
                        handleMessage(in.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendToServer(Message msg) {
        out.println(msg.asString());
        System.err.println("sent " + msg.asString());
    }

    private void handleMessage(String message) {
        try {
            Message msg = new Message(message);
            if (msg.hasToken("text")) {
//                frame.getChatPanel().addMessage(new Message(message));
//                frame.getChatPanel().resetScroll();
//                frame.repaint();
            } else {
//                frame.getFieldPanel().handleCommand(msg);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(message);
        }
    }

}
