package com.stylesync.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel with rounded corners
 */
public class RoundedPanel extends JPanel {
    private int radius = 20;
    private Color backgroundColor = Color.WHITE;

    public RoundedPanel() {
        setOpaque(false);
    }

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    public RoundedPanel(int radius, Color bgColor) {
        this.radius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);

        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
}
