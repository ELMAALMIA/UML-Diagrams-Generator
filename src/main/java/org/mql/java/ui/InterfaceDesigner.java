package org.mql.java.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.mql.java.models.InterfaceModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.FieldModel;
import org.mql.java.ui.components.ComponentFactory;
import org.mql.java.ui.components.ContentPanel;
import org.mql.java.ui.components.TitlePanel;
import org.mql.java.ui.theme.UITheme;

public class InterfaceDesigner extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();

    private InterfaceModel interfaceModel;

    public InterfaceDesigner(InterfaceModel interfaceModel) {
        this.interfaceModel = interfaceModel;
        setLayout(new BorderLayout());
        setOpaque(false);
        drawInterfaceTitle();
        drawAttributesAndMethods();
        setPreferredSize(new Dimension(theme.COMPONENT_WIDTH, getPreferredSize().height));
    }

    public InterfaceModel getInterfaceModel() {
		return interfaceModel;
	}
    
    private void drawInterfaceTitle() {
        TitlePanel titlePanel = ComponentFactory.createTitlePanel(
            "«interface» " + interfaceModel.getInterfaceName(), "interface", true);
        add(titlePanel, BorderLayout.NORTH);
    }

    private void drawAttributesAndMethods() {
        ContentPanel contentPanel = ComponentFactory.createContentPanel();
        
        // Draw attributes
        for (FieldModel field : interfaceModel.getFields()) {
            String fieldText = field.getNiveauVisiblity().getSymbol() + " " + 
                             field.getName() + ": " + field.getType();
            contentPanel.addItem(fieldText, theme.SMALL_FONT);
        }
        
        contentPanel.addSeparator();
        
        // Draw methods
        for (MethodModel method : interfaceModel.getMethods()) {
            String methodText = method.getVisibility().getSymbol() + " " + method.getName() + "()";
            if (method.getTypeReturn() != null && !method.getTypeReturn().equals("void")) {
                methodText += " : " + method.getTypeReturn();
            }
            contentPanel.addItem(methodText, theme.SMALL_FONT);
        }

        add(contentPanel, BorderLayout.CENTER);
    }
}
