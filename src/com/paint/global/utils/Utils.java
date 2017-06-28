package com.paint.global.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Admin on 15.02.2017.
 */
public final class Utils {

    public static void paintLine(Point p1, Point p2, int brushSize, Color color, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(color);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public static void paintRectangle(Point p1, Point p2, int brushSize, Color color, boolean fill, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.setColor(color);
        int x1 = Math.min(p1.x, p2.x), x2 = Math.max(p1.x, p2.x), y1 = Math.min(p1.y, p2.y), y2 = Math.max(p1.y, p2.y);
        if (fill) {
            g2d.fillRect(x1, y1, x2 - x1, y2 - y1);
        } else {
            g2d.drawRect(x1, y1, x2 - x1, y2 - y1);
        }
    }

    public static void paintOval(Point p1, Point p2, int brushSize, Color color, boolean fill, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(color);
        int x1 = Math.min(p1.x, p2.x), x2 = Math.max(p1.x, p2.x), y1 = Math.min(p1.y, p2.y), y2 = Math.max(p1.y, p2.y);
        if (fill) {
            g2d.fillOval(x1, y1, x2 - x1, y2 - y1);
        } else {
            g2d.drawOval(x1, y1, x2 - x1, y2 - y1);
        }
    }

    public static void paintCircle(Point center, Point border, int brushSize, Color color, boolean fill, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(color);
        int radius = (int) Math.sqrt((border.x - center.x) * (border.x - center.x) + (border.y - center.y) * (border.y - center.y));
        if (fill) {
            g2d.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
        } else {
            g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius *  2);
        }
    }

    public static void drawCircle(Point center, int diameter, Color color, Graphics g) {
        g.setColor(color);
        g.drawOval(center.x - diameter / 2, center.y - diameter / 2, diameter, diameter);
    }

    public static void fillCircle(Point center, int diameter, Color color, Graphics g) {
        g.setColor(color);
        g.fillOval(center.x - diameter / 2, center.y - diameter / 2, diameter, diameter);
    }

    public static void drawSquare(Point center, int length, Color color, Graphics g) {
        g.setColor(color);
        g.drawRect(center.x - length / 2, center.y - length / 2, length, length);
    }

    public static void fillSquare(Point center, int length, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(center.x - length / 2, center.y - length / 2, length, length);
    }

    public static void paintBorderedSquare(int x, int y, int size, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
        g.setColor(transparentColor((brightenedColor(color))));
        g.drawRect(x, y, size, size);
    }

    public static void fillSpace(Point point, Color color, BufferedImage image) {
        int oldColor = image.getRGB(point.x, point.y), newColor = color.getRGB();
        if (oldColor != newColor) {
            fillSpace(point.x, point.y, image.getRGB(point.x, point.y), color.getRGB(), image);
        }
    }

    private static void fillSpace(int x, int y, int oldColor, int newColor, BufferedImage image) {
        image.setRGB(x, y, newColor);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx * dy == 0 &&
                        x + dx > -1 && x + dx < image.getWidth() &&
                        y + dy > -1 && y + dy < image.getHeight() &&
                        image.getRGB(x + dx, y + dy) == oldColor) {
                    fillSpace(x + dx, y + dy, oldColor, newColor, image);
                }
            }
        }
    }

    public static Color brightenedColor(Color color) {
        return new Color((255 + color.getRed()) / 2, (255 + color.getGreen()) / 2, (255 + color.getBlue()) / 2, color.getAlpha());
    }

    public static Color darkenedColor(Color color) {
        return new Color(3 * color.getRed() / 4, 3 * color.getGreen() / 4, 3 * color.getBlue() / 4, color.getAlpha());
    }

    public static Color transparentColor(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 4);
    }

    public static Color inversedColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

}
