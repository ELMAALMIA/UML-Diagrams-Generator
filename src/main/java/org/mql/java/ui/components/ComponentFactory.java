package org.mql.java.ui.components;

import javax.swing.*;
import java.awt.*;
import org.mql.java.ui.theme.UITheme;

/**
 * Factory pattern for creating UI components
 * Ensures consistency across the application
 */
public class ComponentFactory {
    private static final UITheme theme = UITheme.getInstance();
    
    /**
     * Create a styled button
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(theme.BUTTON_FONT);
        button.setPreferredSize(new Dimension(theme.BUTTON_WIDTH, theme.BUTTON_HEIGHT));
        button.setBackground(theme.PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Create a title panel
     */
    public static TitlePanel createTitlePanel(String title, String type) {
        return new TitlePanel(title, type);
    }
    
    /**
     * Create a title panel with italic style
     */
    public static TitlePanel createTitlePanel(String title, String type, boolean italic) {
        return new TitlePanel(title, type, italic);
    }
    
    /**
     * Create a content panel
     */
    public static ContentPanel createContentPanel() {
        return new ContentPanel();
    }
    
    /**
     * Create a styled label
     */
    public static JLabel createLabel(String text) {
        return createLabel(text, theme.NORMAL_FONT);
    }
    
    /**
     * Create a styled label with custom font
     */
    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(theme.TEXT_COLOR);
        return label;
    }
    
    /**
     * Create a styled dialog frame
     */
    public static JFrame createDialogFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(theme.BACKGROUND_COLOR);
        return frame;
    }
    
    /**
     * Create a styled panel
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(theme.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(
            theme.PADDING_MEDIUM, theme.PADDING_MEDIUM, 
            theme.PADDING_MEDIUM, theme.PADDING_MEDIUM));
        return panel;
    }
}

