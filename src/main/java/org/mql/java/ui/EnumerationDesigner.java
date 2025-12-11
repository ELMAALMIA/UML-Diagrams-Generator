package org.mql.java.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.mql.java.models.Enumeration;
import org.mql.java.ui.components.ComponentFactory;
import org.mql.java.ui.components.ContentPanel;
import org.mql.java.ui.components.TitlePanel;
import org.mql.java.ui.theme.UITheme;

public class EnumerationDesigner extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();

    private Enumeration enumModel;

    public EnumerationDesigner(Enumeration enumModel) {
        this.enumModel = enumModel;
        setLayout(new BorderLayout());
        setOpaque(false);
        drawEnumTitle();
        drawConstants();
        setPreferredSize(new Dimension(theme.COMPONENT_WIDTH, getPreferredSize().height));
    }

    private void drawEnumTitle() {
        TitlePanel titlePanel = ComponentFactory.createTitlePanel(
            "«enumeration» " + enumModel.getName(), "enum");
        add(titlePanel, BorderLayout.NORTH);
    }

    private void drawConstants() {
        ContentPanel constantsPanel = ComponentFactory.createContentPanel();

        for (String constant : enumModel.getValuesEnum()) {
            constantsPanel.addItem(constant, theme.SMALL_FONT);
        }

        add(constantsPanel, BorderLayout.CENTER);
    }
}
