package org.mql.java.ui.theme;

import java.awt.Color;
import java.awt.Font;

/**
 * Singleton pattern for UI theme management
 * Ensures consistent design across all UI components
 */
public class UITheme {
    private static UITheme instance;
    
    // Color Palette
    public final Color PRIMARY_COLOR = new Color(70, 130, 180);      // Steel Blue
    public final Color SECONDARY_COLOR = new Color(100, 149, 237);    // Cornflower Blue
    public final Color SUCCESS_COLOR = new Color(144, 238, 144);     // Light Green
    public final Color WARNING_COLOR = new Color(255, 218, 185);      // Peach
    public final Color ERROR_COLOR = new Color(255, 99, 71);          // Tomato
    public final Color BACKGROUND_COLOR = new Color(255, 255, 255);   // White
    public final Color PANEL_BACKGROUND = new Color(245, 245, 250);   // Light Gray-Blue
    public final Color BORDER_COLOR = new Color(50, 50, 50);         // Dark Gray
    public final Color TEXT_COLOR = new Color(30, 30, 30);            // Dark Gray
    public final Color LIGHT_BACKGROUND = new Color(250, 250, 255);   // Very Light Blue
    
    // Class-specific colors
    public final Color CLASS_HEADER = new Color(119, 158, 203);      // Blue
    public final Color INTERFACE_HEADER = new Color(144, 238, 144);  // Green
    public final Color ENUM_HEADER = new Color(250, 128, 114);       // Salmon
    public final Color PACKAGE_HEADER = new Color(255, 248, 220);    // Cornsilk
    public final Color CONTENT_BACKGROUND = new Color(240, 248, 255); // Alice Blue
    
    // Relation colors
    public final Color GENERALIZATION_COLOR = new Color(0, 0, 200);   // Blue
    public final Color REALIZATION_COLOR = new Color(0, 150, 0);     // Green
    public final Color ASSOCIATION_COLOR = new Color(0, 0, 0);        // Black
    public final Color AGGREGATION_COLOR = new Color(150, 100, 0);   // Orange
    public final Color COMPOSITION_COLOR = new Color(150, 0, 0);     // Red
    public final Color DEPENDENCY_COLOR = new Color(100, 100, 100);  // Gray
    
    // Fonts
    public final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public final Font ITALIC_FONT = new Font("Segoe UI", Font.ITALIC, 12);
    public final Font BOLD_ITALIC_FONT = new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12);
    public final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    public final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 10);
    public final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Spacing
    public final int PADDING_SMALL = 5;
    public final int PADDING_MEDIUM = 10;
    public final int PADDING_LARGE = 15;
    public final int BORDER_WIDTH = 2;
    public final int COMPONENT_SPACING = 10;
    
    // Dimensions
    public final int BUTTON_HEIGHT = 35;
    public final int BUTTON_WIDTH = 200;
    public final int COMPONENT_WIDTH = 250;
    public final int COMPONENT_HEIGHT = 200;
    public final int PACKAGE_WIDTH = 200;
    public final int PACKAGE_HEIGHT = 150;
    
    private UITheme() {
        // Private constructor for singleton
    }
    
    public static UITheme getInstance() {
        if (instance == null) {
            synchronized (UITheme.class) {
                if (instance == null) {
                    instance = new UITheme();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get color for element type
     */
    public Color getColorForType(String type) {
        switch (type.toLowerCase()) {
            case "class":
                return CLASS_HEADER;
            case "interface":
                return INTERFACE_HEADER;
            case "enum":
            case "enumeration":
                return ENUM_HEADER;
            case "package":
                return PACKAGE_HEADER;
            default:
                return PRIMARY_COLOR;
        }
    }
}

