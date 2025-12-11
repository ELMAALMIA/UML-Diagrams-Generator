package org.mql.java.ui.components;

import javax.swing.*;
import java.awt.*;
import org.mql.java.ui.theme.UITheme;

/**
 * Reusable content panel component
 * Provides consistent styling for content areas
 */
public class ContentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();
    
    public ContentPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(theme.CONTENT_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(
            theme.PADDING_SMALL, theme.PADDING_SMALL, 
            theme.PADDING_SMALL, theme.PADDING_SMALL));
    }
    
    public void addItem(String text) {
        addItem(text, theme.NORMAL_FONT);
    }
    
    public void addItem(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(theme.TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(theme.PADDING_SMALL));
    }
    
    public void addSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
        add(Box.createVerticalStrut(theme.PADDING_SMALL));
    }
}

