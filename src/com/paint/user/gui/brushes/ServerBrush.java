package com.paint.user.gui.brushes;

import com.paint.global.net.Message;
import com.paint.user.gui.FieldPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by jetbrains on 03/04/2017.
 */
public class ServerBrush {

    private volatile Brush virtualBrush;
    private volatile Point virtualMousePosition;
    private FieldPanel field;

    public ServerBrush(FieldPanel field, String type, int size) {
        this.field = field;
        try {
            virtualBrush = (Brush) Class.forName(type).getConstructor(field.getClass(), int.class).newInstance(field, size);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        virtualMousePosition = null;
    }

    public void handleMessage(Message msg) {
        if (msg.hasToken("positionX")) {
            virtualMousePosition = new Point((int) Double.parseDouble(msg.getValue("positionX")),
                    (int) Double.parseDouble(msg.getValue("positionY")));
        }
        virtualBrush.setBrushSize(Integer.parseInt(msg.getValue("brushSize")));
        switch (msg.getValue("eventType")) {
            case "mousePressed":
                virtualBrush.mousePressed(Integer.parseInt(msg.getValue("button")), virtualMousePosition);
                break;
            case "mouseReleased":
                virtualBrush.mouseReleased(Integer.parseInt(msg.getValue("button")), virtualMousePosition);
                break;
            case "mouseDragged":
                virtualBrush.mouseDragged(virtualMousePosition);
                break;
            case "keyPressed":
                if (Integer.parseInt(msg.getValue("key")) == KeyEvent.VK_M) {
                    virtualBrush.changeMarkersMode();
                }
                field.repaintAll();
                break;
            case "mouseExited":
                virtualMousePosition = null;
            default:
                break;
        }
        field.repaint();
    }

    public boolean mouseInside() {
        return virtualMousePosition != null;
    }

    public void paintMarkers(Graphics g) {
        virtualBrush.paintMarkers(virtualMousePosition, g);
    }

    public Class getBrushClass() {
        return virtualBrush.getClass();
    }

}
