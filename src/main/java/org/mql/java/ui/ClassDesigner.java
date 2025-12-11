package org.mql.java.ui;
import javax.swing.*;

import org.mql.java.models.ClassModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.ui.components.ComponentFactory;
import org.mql.java.ui.components.ContentPanel;
import org.mql.java.ui.components.TitlePanel;
import org.mql.java.ui.theme.UITheme;

import java.awt.*;

public class ClassDesigner extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();

    private ClassModel classModel;

    public ClassDesigner(ClassModel classModel) {
        this.classModel = classModel;
        setLayout(new BorderLayout());
        setOpaque(false);
        drawClassTitle();
        drawAttributesAndMethods();
        setPreferredSize(new Dimension(theme.COMPONENT_WIDTH, getPreferredSize().height));
    }

    public ClassModel getClassModel() {
		return classModel;
	}
    
    private void drawClassTitle() {
        TitlePanel titlePanel = ComponentFactory.createTitlePanel(
            "«class» " + classModel.getName(), "class");
        add(titlePanel, BorderLayout.NORTH);
    }

    private void drawAttributesAndMethods() {
        ContentPanel contentPanel = ComponentFactory.createContentPanel();
        
        // Show inheritance if exists
        if (classModel.getParent() != null && !classModel.getParent().equals("java.lang.Object")) {
            String parentName = classModel.getParent();
            if (parentName.contains(".")) {
                parentName = parentName.substring(parentName.lastIndexOf('.') + 1);
            }
            contentPanel.addItem("extends " + parentName, theme.SMALL_FONT);
            contentPanel.addSeparator();
        }
        
        // Show interfaces if implemented
        if (!classModel.getInterfacesImp().isEmpty()) {
            for (String interfaceName : classModel.getInterfacesImp()) {
                String simpleName = interfaceName;
                if (simpleName.contains(".")) {
                    simpleName = simpleName.substring(simpleName.lastIndexOf('.') + 1);
                }
                contentPanel.addItem("implements " + simpleName, theme.SMALL_FONT);
            }
            contentPanel.addSeparator();
        }
        
        // Draw attributes
        for (FieldModel field : classModel.getFields()) {
            if (field.getName().contains("$")) continue;
            
            String fieldText = field.getNiveauVisiblity().getSymbol() + " " + 
                             field.getName() + ": " + field.getType();
            if (field.isStatic()) fieldText = "{static} " + fieldText;
            if (field.isFinal()) fieldText = "{final} " + fieldText;
            contentPanel.addItem(fieldText, theme.SMALL_FONT);
        }
        
        contentPanel.addSeparator();
        
        // Draw constructors
        for (org.mql.java.models.ConstructorModel constructor : classModel.getConstructors()) {
            contentPanel.addItem(constructor.toString(), theme.SMALL_FONT);
        }
        
        if (!classModel.getConstructors().isEmpty()) {
            contentPanel.addSeparator();
        }
        
        // Draw methods
        for (MethodModel method : classModel.getMethods()) {
            if (method.getName().contains("$")) continue;
            
            String methodText = method.getVisibility().getSymbol() + " " + method.getName() + "()";
            if (method.getTypeReturn() != null && !method.getTypeReturn().equals("void")) {
                methodText += " : " + method.getTypeReturn();
            }
            if (method.isStatic()) {
                methodText = "{static} " + methodText;
            }
            contentPanel.addItem(methodText, theme.SMALL_FONT);
        }

        add(contentPanel, BorderLayout.CENTER);
    }
}
