package com.stylesync.view.components;

import com.stylesync.view.StyleSyncColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern styled password field with icon and placeholder
 */
public class ModernPasswordField extends JPasswordField {
    private String placeholder;
    private Icon icon;

    public ModernPasswordField(String placeholder) {
        this.placeholder = placeholder;
        initialize();
    }

    public ModernPasswordField(String placeholder, Icon icon) {
        this.placeholder = placeholder;
        this.icon = icon;
        initialize();
    }

    private void initialize() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(StyleSyncColors.TEXT_PRIMARY);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                new EmptyBorder(12, icon != null ? 45 : 15, 12, 15)
        ));
        setPreferredSize(new Dimension(300, 45));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw icon
        if (icon != null) {
            icon.paintIcon(this, g2, 15, (getHeight() - icon.getIconHeight()) / 2);
        }

        // Draw placeholder
        if (getPassword().length == 0 && !isFocusOwner()) {
            g2.setColor(StyleSyncColors.TEXT_SECONDARY);
            g2.setFont(getFont());
            g2.drawString(placeholder, icon != null ? 45 : 15, 
                    (getHeight() + g2.getFontMetrics().getAscent()) / 2 - 2);
        }

        g2.dispose();
    }
}
