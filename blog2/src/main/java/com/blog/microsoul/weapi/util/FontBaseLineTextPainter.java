package com.blog.microsoul.weapi.util;

import org.jbarcode.paint.TextPainter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author xtiger (xtiger@microsoul.com) 16-1-25
 */
public class FontBaseLineTextPainter implements TextPainter {
    private static TextPainter instance;

    private FontBaseLineTextPainter() {
    }

    public static TextPainter getInstance() {
        if (instance == null) {
            instance = new FontBaseLineTextPainter();
        }

        return instance;
    }

    public void paintText(BufferedImage var1, String var2, int fontSize) {
        Graphics var4 = var1.getGraphics();
        Font var5 = new Font("monospace", 0, fontSize);
        var4.setFont(var5);
        FontMetrics var6 = var4.getFontMetrics();
        int var7 = var6.getHeight();
        int var8 = (var1.getWidth() - var6.stringWidth(var2)) / 2;
        var4.setColor(Color.WHITE);
        var4.fillRect(0, 0, var1.getWidth(), var1.getHeight() * 1 / 20);
        var4.fillRect(0, var1.getHeight() - var7 * 9 / 10, var1.getWidth(), var7 * 9 / 10);
        var4.setColor(Color.BLACK);
        var4.drawString(var2, var8, var1.getHeight() - var7 / 10);
    }
}
