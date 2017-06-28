package com.paint.user.gui;

import com.paint.global.net.Message;
import com.paint.user.gui.brushes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by jetbrains on 02/26/2017.
 */
public class FieldPanel extends JPanel {

    private int MAX_WIDTH, MAX_HEIGHT;

    private Frame frame;
    private StatusBar statusBar;
    private int brush;

    private HashMap<String, ServerBrush> serverBrushes;
    private Brush[] myBrushes;
    private ArrayList<BufferedImage> layers;
    private BufferedImage transparentLayer;

    public static final Color
            BACKGROUND_COLOR = Color.white;

    FieldPanel(Frame frame, int width, int height) {
        this.frame = frame;
        MAX_WIDTH = width;
        MAX_HEIGHT = height;

        setDoubleBuffered(true);
        setBackground(Color.white);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaintAndShare("mouseExited", -1, -1, null, null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                myBrushes[brush].mousePressed(e.getButton(), e.getPoint());
                repaintAndShare("mousePressed", e.getButton(), -1, e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                myBrushes[brush].mouseReleased(e.getButton(), e.getPoint());
                repaintAndShare("mouseReleased", e.getButton(), -1, e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                myBrushes[brush].mouseDragged(e.getPoint());
                repaintAndShare("mouseDragged", -1, -1, e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                repaintAndShare("mouseMoved", -1, -1, e.getX(), e.getY());
            }
        });
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!myBrushes[brush].inputActive()) {
                    myBrushes[brush].mouseWheelMoved(-e.getWheelRotation());
                    repaintAndShare("mouseWheelMoved", -1, -1, null, null);
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    brush = (brush - 1 + myBrushes.length) % myBrushes.length;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    brush = (brush + 1) % myBrushes.length;
                } else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_M) {
                    myBrushes[brush].changeMarkersMode();
                }
                repaintAndShare("keyPressed", -1, e.getKeyCode(), null, null);
            }
        });

        initGraphics();
    }

    private void initGraphics() {
        myBrushes = new Brush[]{
                new FreeBrush(this),
                new ContinuousFreeBrush(this),
                new LineBrush(this),
                new RectangleBrush(this),
                new OvalBrush(this),
                new CircleBrush(this),
                new FillBrush(this)
        };
        serverBrushes = new HashMap<>();

        layers = new ArrayList<>();
        layers.add(new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_ARGB));
        Graphics graphics = layers.get(0).getGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, layers.get(0).getWidth(), layers.get(0).getHeight());
    }

    public void repaintAll() {
        repaint();
        statusBar.repaint();
    }

    private void repaintAndShare(String eventType, int button, int key, Integer mouseX, Integer mouseY) {
        repaintAll();
        Message msg = new Message();
        msg.addToken("eventType", eventType);
        msg.addToken("brushMode", myBrushes[brush].toString());
        msg.addToken("brushSize", String.valueOf(myBrushes[brush].getBrushSize()));
        if (mouseX != null) {
            msg.addToken("positionX", String.valueOf(mouseX));
            msg.addToken("positionY", String.valueOf(mouseY));
        }
        if (button != -1) {
            msg.addToken("button", String.valueOf(button));
        }
        if (key != -1) {
            msg.addToken("key", String.valueOf(key));
        }
        frame.getClientNetManager().sendToServer(msg);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(layers.get(0), 0, 0, null);
        if (myBrushes[brush].inputActive() || getMousePosition() != null) {
            myBrushes[brush].paintMarkers(getMousePosition(), g);
        }
        for (ServerBrush serverBrush : serverBrushes.values()) {
            if (serverBrush.mouseInside()) {
                try {
                    serverBrush.paintMarkers(g);
                } catch (ConcurrentModificationException e) {
                    // без понятия, почему эта дрянь вылетает
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void handleCommand(Message msg) {
        if (msg.hasToken("eventType")) {
            String name = msg.getValue("name");
            if (Objects.equals(name, frame.getUsername())) {
                return;
            }
            ServerBrush serverBrush = null;
            try {
                if (!serverBrushes.containsKey(name) || serverBrushes.get(name).getBrushClass() != Class.forName(msg.getValue("brushMode"))) {
                    serverBrush = createServerBrush(msg.getValue("brushMode"), Integer.parseInt(msg.getValue("brushSize")));
                    serverBrushes.put(name, serverBrush);
                } else {
                    serverBrush = serverBrushes.get(name);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            serverBrush.handleMessage(msg);
        } else if (msg.hasToken("disconnect")) {
            serverBrushes.remove(msg.getValue("name"));
        }
    }

    private Point getLocationInFrame() {
        return new Point(getLocation().x + getParent().getLocation().x, getLocation().y + getParent().getLocation().y);
    }

    private ServerBrush createServerBrush(String type, int size) {
        return new ServerBrush(this, type, size);
    }

    void bindStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    public BufferedImage getField() {
        return layers.get(0);
    }

    public int getFieldWidth() {
        return MAX_WIDTH;
    }

    public int getFieldHeight() {
        return MAX_HEIGHT;
    }

    Brush getBrush() {
        return myBrushes[brush];
    }

}
