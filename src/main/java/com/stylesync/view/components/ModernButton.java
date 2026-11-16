package com.stylesync.view.components;

import com.stylesync.view.StyleSyncColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern styled button with rounded corners and gradient
 */
public class ModernButton extends JButton {
    private boolean isHovered = false;
    private boolean isGradient = true;
    private Color backgroundColor;

    public ModernButton(String text) {
        super(text);
        initialize();
    }

    public ModernButton(String text, boolean gradient) {
        super(text);
        this.isGradient = gradient;
        initialize();
    }

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.isGradient = false;
        this.backgroundColor = bgColor;
        initialize();
    }

    private void initialize() {
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 45));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw shadow
        if (isHovered) {
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(2, 4, width - 4, height - 4, 25, 25);
        }

        // Draw background
        if (isGradient) {
            GradientPaint gradient = StyleSyncColors.createHorizontalGradient(width);
            g2.setPaint(gradient);
        } else {
            g2.setColor(backgroundColor != null ? backgroundColor : StyleSyncColors.PRIMARY);
        }

        if (isHovered) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        }

        g2.fillRoundRect(0, 0, width, height, 25, 25);

        g2.dispose();
        super.paintComponent(g);
    }
}
