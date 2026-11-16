package com.stylesync.view;

import java.awt.*;

/**
 * Color palette for StyleSync application
 */
public class StyleSyncColors {
    // Primary Colors - Purple/Pink Theme
    public static final Color PRIMARY = new Color(138, 86, 211); // Purple
    public static final Color PRIMARY_DARK = new Color(102, 51, 153);
    public static final Color PRIMARY_LIGHT = new Color(186, 148, 230);
    
    public static final Color SECONDARY = new Color(255, 105, 180); // Hot Pink
    public static final Color SECONDARY_LIGHT = new Color(255, 182, 219);
    
    public static final Color ACCENT = new Color(255, 192, 203); // Light Pink
    public static final Color ACCENT_DARK = new Color(255, 105, 180);
    
    // Background Colors
    public static final Color BACKGROUND = new Color(250, 247, 252);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color SIDEBAR_DARK = new Color(45, 45, 68);
    public static final Color SIDEBAR_ITEM_HOVER = new Color(60, 60, 85);
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51);
    public static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_LIGHT = new Color(100, 100, 100);
    
    // UI Element Colors
    public static final Color BORDER = new Color(230, 230, 230);
    public static final Color BORDER_LIGHT = new Color(240, 240, 240);
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color ERROR = new Color(244, 67, 54);
    public static final Color INFO = new Color(33, 150, 243);
    
    // Gradient Colors
    public static final Color GRADIENT_START = new Color(186, 148, 230);
    public static final Color GRADIENT_END = new Color(138, 86, 211);
    
    // Create gradient paint
    public static GradientPaint createGradient(int width, int height) {
        return new GradientPaint(0, 0, GRADIENT_START, width, height, GRADIENT_END);
    }
    
    public static GradientPaint createHorizontalGradient(int width) {
        return new GradientPaint(0, 0, GRADIENT_START, width, 0, GRADIENT_END);
    }
    
    public static GradientPaint createVerticalGradient(int height) {
        return new GradientPaint(0, 0, GRADIENT_START, 0, height, GRADIENT_END);
    }
}
