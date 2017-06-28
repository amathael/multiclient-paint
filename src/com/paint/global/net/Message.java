package com.paint.global.net;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Admin on 15.02.2017.
 */
public class Message {

    private HashMap<String, String> tokens;

    public Message() {
        tokens = new HashMap<>();
    }

    public Message(String username, String text) {
        tokens = new HashMap<>();
        tokens.put("name", username);
        tokens.put("text", text);
    }

    public Message(String message) throws IllegalArgumentException {
        tokens = new HashMap<>();
        parseTokens(message);
    }

    public String asString() {
        return compileTokens();
    }

    public Set<String> asTokens() {
        return tokens.keySet();
    }

    public boolean hasToken(String name) {
        return tokens.containsKey(name);
    }

    public String getValue(String name) {
        return tokens.getOrDefault(name, null);
    }

    public Message addToken(String name, String value) {
        tokens.put(name, value);
        return this;
    }

    public void paint(Graphics g, int x, int y, Font font, Color color) {
        String text = getValue("text"), sender = getValue("name");

        if (sender != null) {
            text = sender + " : " + text;
        }
        g.setFont(font);

        g.setColor(color);
        g.drawString(text, x, y);
    }

    public Message parseTokens(String message) throws IllegalArgumentException {
        if (message == null) {
            return new Message();
        }
        char[] msg = message.toCharArray();

        int idx = 0;
        while (idx < message.length() && msg[idx] != '<') {
            idx++;
        }
        while (idx < msg.length) {
            idx++;
            int idx2 = idx;
            while (idx2 < message.length() && msg[idx2] != '>') {
                idx2++;
            }
            if (idx2 == msg.length) {
                throw new IllegalArgumentException("Incorrect message format");
            } else {
                String name = String.valueOf(Arrays.copyOfRange(msg, idx, idx2));
                idx2++;
                idx = idx2;
                while (idx < msg.length && msg[idx] != '<') {
                    idx++;
                }
                String value = String.valueOf(Arrays.copyOfRange(msg, idx2, idx));
                tokens.put(name, value);
            }
        }

        return this;
    }

    private String compileTokens() {
        StringBuilder builder = new StringBuilder();
        for (String name : tokens.keySet()) {
            builder.append("<").append(name).append(">");
            builder.append(tokens.get(name));
        }

        return builder.toString();
    }

}
