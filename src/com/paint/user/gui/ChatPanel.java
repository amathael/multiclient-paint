package com.paint.user.gui;

import com.paint.global.net.Message;
import com.paint.user.net.ClientNetManager;
import com.paint.global.utils.TargetedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.Objects;

import static com.paint.global.utils.Utils.transparentColor;

/**
 * Created by jetbrains on 02/26/2017.
 */
public class ChatPanel extends JPanel {

    private static int
            CONTAINS,
            TEXT_FIELD_PAD = 2,
            XMARGIN = 3,
            YMARGIN = 2,
            FONT_SIZE = 10,
            ROW_HEIGHT = FONT_SIZE + YMARGIN * 2;

    private static Color
            DEFAULT_COLOR = Color.black,
            UNKNOWN_COLOR = transparentColor(Color.lightGray),
            SENDER_COLOR = new Color(0, 175, 0),
            SERVER_COLOR = new Color(175, 0, 0),
            TEXT_FIELD_BACKGROUND_COLOR = new Color(235, 235, 235),
            SCROLLBAR_COLOR = TEXT_FIELD_BACKGROUND_COLOR;

    private final Font font = new Font("Arial", Font.PLAIN, FONT_SIZE);

    private int scrollPosition;
    private LinkedList<Message> messages;

    private volatile TargetedListener listener;

    private Frame frame;
    private ClientNetManager clientNetManager;
    private JTextField textField;

    ChatPanel(Frame frame, int width, int rows) {
        this.frame = frame;
        clientNetManager = frame.getClientNetManager();
        CONTAINS = rows;

        setDoubleBuffered(true);
        setBackground(Color.white);

        int height = computeHeight(rows);
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        textField = new JTextField(100);
        textField.setFont(font);
        textField.setBackground(TEXT_FIELD_BACKGROUND_COLOR);
        textField.setSize(new Dimension(width - 2 * XMARGIN, ROW_HEIGHT + 2 * YMARGIN));
        textField.setPreferredSize(new Dimension(width - 2 * XMARGIN, ROW_HEIGHT + 2 * YMARGIN));
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (textField.getText().length() > 0) {
                        handleInput(textField.getText());
                        textField.setText("");
                    }
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollPosition -= e.getWheelRotation();
                scrollPosition = Math.max(0, Math.min(scrollPosition, Math.max(0, messages.size() - CONTAINS)));
                repaint();
            }
        });

        setLayout(new BorderLayout());
        add(textField, BorderLayout.SOUTH);

        messages = new LinkedList<>();
    }

    private void handleInput(String text) {
        if (!listener.isCompleted())  {
            //noinspection unchecked
            listener.answer(text);
        } else {
            Message msg = new Message("me", text);
            frame.getClientNetManager().sendToServer(msg);
        }
    }

    void requestName() {
        addMessage(new Message(null, "Please, enter your username..."));
        repaint();
        listener = new TargetedListener() {
            @Override
            public void answer(String value) {
                frame.setUsername(value);
                complete();
            }
        };
    }

    static int computeHeight(int rows) {
        return ROW_HEIGHT * (rows + 1) + TEXT_FIELD_PAD + YMARGIN * 2;
    }

    private Color getMessageColor(String sender, String username) {
        if (sender == null) {
            return UNKNOWN_COLOR;
        } else {
            if (Objects.equals(username, sender)) {
                return SENDER_COLOR;
            } else if (Objects.equals(sender, "server")) {
                return SERVER_COLOR;
            } else {
                return DEFAULT_COLOR;
            }
        }
    }

    public void addMessage(Message msg) {
        messages.add(msg);
        resetScroll();
    }

    private void resetScroll() {
        scrollPosition = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = messages.size() - 1 - scrollPosition; i > -1; i--) {
            int line = messages.size() - i - 1 - scrollPosition;
            if (line >= CONTAINS) {
                break;
            }
            messages.get(i).paint(g, XMARGIN, getHeight() - line * (ROW_HEIGHT) - textField.getHeight() - YMARGIN - TEXT_FIELD_PAD, font,
                    getMessageColor(messages.get(i).getValue("name"), frame.getUsername()));
        }

        paintScrollBar(g);
    }

    private void paintScrollBar(Graphics g) {
        int minLength = ROW_HEIGHT, length = CONTAINS * ROW_HEIGHT;
        double pixelsPerMessage = (double) (length - minLength) / Math.max(1, messages.size() - CONTAINS + 1);
        int exactLength = minLength + (int) pixelsPerMessage;
        g.setColor(SCROLLBAR_COLOR);
        g.fillRect(getWidth() - XMARGIN - 5, 1 + length - (int) (pixelsPerMessage * scrollPosition) - exactLength, 5, exactLength);
    }

}
