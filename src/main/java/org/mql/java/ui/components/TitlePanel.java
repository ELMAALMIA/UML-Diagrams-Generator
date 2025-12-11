package org.mql.java.ui.components;

import javax.swing.*;
import java.awt.*;
import org.mql.java.ui.theme.UITheme;

/**
 * Reusable title panel component
 * Factory pattern for creating consistent title panels
 */
public class TitlePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();
    
    public TitlePanel(String title, String type) {
        this(title, type, false);
    }
    
    public TitlePanel(String title, String type, boolean italic) {
        setLayout(new FlowLayout(FlowLayout.LEFT, theme.PADDING_SMALL, theme.PADDING_SMALL));
        setBorder(BorderFactory.createMatteBorder(
            theme.BORDER_WIDTH, theme.BORDER_WIDTH, theme.BORDER_WIDTH, theme.BORDER_WIDTH, 
            theme.BORDER_COLOR));
        setBackground(theme.getColorForType(type));
        
        JLabel titleLabel = new JLabel(title);
        if (italic && type.equals("interface")) {
            titleLabel.setFont(theme.BOLD_ITALIC_FONT);
        } else {
            titleLabel.setFont(theme.HEADER_FONT);
        }
        titleLabel.setForeground(theme.TEXT_COLOR);
        add(titleLabel);
    }
}

