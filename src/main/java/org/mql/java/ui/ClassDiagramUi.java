package org.mql.java.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mql.java.models.*;
import org.mql.java.enumerations.RelationType;
import org.mql.java.ui.theme.UITheme;

public class ClassDiagramUi extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();
    
	private List<ClassModel> classes;
    private List<InterfaceModel> interfaces;
    private List<RelationModel> relations;
    private List<Enumeration> enumerations;
    private Map<String, Component> componentMap;
    private static final int PADDING = 50;
    private static final int COMPONENT_WIDTH = theme.COMPONENT_WIDTH;
    private static final int COMPONENT_HEIGHT = theme.COMPONENT_HEIGHT;
    private static final int COLS = 3;

    public ClassDiagramUi(List<PackageModel> packageModels) {
        classes = new ArrayList<>();
        interfaces = new ArrayList<>();
        enumerations = new ArrayList<>();
        relations = new ArrayList<>();
        componentMap = new HashMap<>();
        parse(packageModels);
        
        setLayout(null); // Use null layout for custom positioning
        setBackground(theme.BACKGROUND_COLOR);
        setPreferredSize(calculatePreferredSize());
        
        drawComponents();
    }

    private void parse(List<PackageModel> packageModels) {
        for (PackageModel packageModel : packageModels) {
            classes.addAll(packageModel.getClasses());
            interfaces.addAll(packageModel.getInterfaces());
            enumerations.addAll(packageModel.getEnumerations());
            
            // Filter relations to only include those within the project
            for (RelationModel relation : packageModel.getRelations()) {
                if (isProjectRelation(relation)) {
                    relations.add(relation);
                }
            }

            if (packageModel.getPackages() != null) {
                parse(packageModel.getPackages());
            }
        }
    }
    
    private boolean isProjectRelation(RelationModel relation) {
        String sourceName = relation.getSourceModel().getName();
        String targetName = relation.getTargetModel().getName();
        
        // Skip Java standard library classes
        if (sourceName.startsWith("java.") || targetName.startsWith("java.") ||
            sourceName.startsWith("javax.") || targetName.startsWith("javax.")) {
            return false;
        }
        
        // Check if both source and target are in our parsed classes/interfaces
        boolean sourceFound = findClassOrInterface(sourceName) != null;
        boolean targetFound = findClassOrInterface(targetName) != null;
        
        return sourceFound && targetFound;
    }
    
    private Object findClassOrInterface(String className) {
        String simpleName = className.contains(".") ? className.substring(className.lastIndexOf('.') + 1) : className;
        
        for (ClassModel cls : classes) {
            if (cls.getName().equals(simpleName) || cls.getName().equals(className)) {
                return cls;
            }
        }
        for (InterfaceModel intf : interfaces) {
            if (intf.getInterfaceName().equals(simpleName) || intf.getInterfaceName().equals(className)) {
                return intf;
            }
        }
        for (Enumeration enm : enumerations) {
            if (enm.getName().equals(simpleName) || enm.getName().equals(className)) {
                return enm;
            }
        }
        return null;
    }

    private void drawComponents() {
        int x = PADDING;
        int y = PADDING;
        int col = 0;
        
        // Draw classes
        for (ClassModel classModel : classes) {
            ClassDesigner classDesigner = new ClassDesigner(classModel);
            classDesigner.setBounds(x, y, COMPONENT_WIDTH, COMPONENT_HEIGHT);
            add(classDesigner);
            componentMap.put(classModel.getName(), classDesigner);
            
            col++;
            if (col >= COLS) {
                col = 0;
                y += COMPONENT_HEIGHT + PADDING;
                x = PADDING;
            } else {
                x += COMPONENT_WIDTH + PADDING;
            }
        }
        
        // Draw interfaces
        for (InterfaceModel interfaceModel : interfaces) {
            InterfaceDesigner interfaceDesigner = new InterfaceDesigner(interfaceModel);
            interfaceDesigner.setBounds(x, y, COMPONENT_WIDTH, COMPONENT_HEIGHT);
            add(interfaceDesigner);
            componentMap.put(interfaceModel.getInterfaceName(), interfaceDesigner);
            
            col++;
            if (col >= COLS) {
                col = 0;
                y += COMPONENT_HEIGHT + PADDING;
                x = PADDING;
            } else {
                x += COMPONENT_WIDTH + PADDING;
            }
        }
        
        // Draw enumerations
        for (Enumeration enumModel : enumerations) {
            EnumerationDesigner enumerationDesigner = new EnumerationDesigner(enumModel);
            enumerationDesigner.setBounds(x, y, COMPONENT_WIDTH, COMPONENT_HEIGHT);
            add(enumerationDesigner);
            componentMap.put(enumModel.getName(), enumerationDesigner);
            
            col++;
            if (col >= COLS) {
                col = 0;
                y += COMPONENT_HEIGHT + PADDING;
                x = PADDING;
            } else {
                x += COMPONENT_WIDTH + PADDING;
            }
        }
    }
    
    private Dimension calculatePreferredSize() {
        int totalComponents = classes.size() + interfaces.size() + enumerations.size();
        int rows = (int) Math.ceil((double) totalComponents / COLS);
        int width = (COLS * (COMPONENT_WIDTH + PADDING)) + PADDING;
        int height = (rows * (COMPONENT_HEIGHT + PADDING)) + PADDING;
        return new Dimension(width, height);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw relations
        for (RelationModel relation : relations) {
            drawRelation(g2d, relation);
        }
    }
    
    private void drawRelation(Graphics2D g2d, RelationModel relation) {
        String sourceSimpleName = relation.getSourceModel().getSimpleName();
        String targetSimpleName = relation.getTargetModel().getSimpleName();
        
        Component sourceComp = componentMap.get(sourceSimpleName);
        Component targetComp = componentMap.get(targetSimpleName);
        
        // Try to find by full name if simple name doesn't work
        if (sourceComp == null) {
            String sourceFullName = relation.getSourceModel().getName();
            sourceSimpleName = sourceFullName.contains(".") ? 
                sourceFullName.substring(sourceFullName.lastIndexOf('.') + 1) : sourceFullName;
            sourceComp = componentMap.get(sourceSimpleName);
        }
        
        if (targetComp == null) {
            String targetFullName = relation.getTargetModel().getName();
            targetSimpleName = targetFullName.contains(".") ? 
                targetFullName.substring(targetFullName.lastIndexOf('.') + 1) : targetFullName;
            targetComp = componentMap.get(targetSimpleName);
        }
        
        if (sourceComp == null || targetComp == null || sourceComp == targetComp) {
            return;
        }
        
        Point sourcePoint = getConnectionPoint(sourceComp, targetComp);
        Point targetPoint = getConnectionPoint(targetComp, sourceComp);
        
        if (sourcePoint == null || targetPoint == null) {
            return;
        }
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        
        // Use the enhanced drawing method that includes labels
        drawRelationWithLabels(g2d, relation, sourcePoint, targetPoint);
    }
    
    private Point getConnectionPoint(Component comp, Component target) {
        if (comp == null || !comp.isVisible()) {
            return null;
        }
        Rectangle bounds = comp.getBounds();
        Rectangle targetBounds = target.getBounds();
        
        // Calculate center
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        
        // Calculate direction to target
        int targetCenterX = targetBounds.x + targetBounds.width / 2;
        int targetCenterY = targetBounds.y + targetBounds.height / 2;
        
        int dx = targetCenterX - centerX;
        int dy = targetCenterY - centerY;
        
        // Find edge point based on direction
        double angle = Math.atan2(dy, dx);
        int edgeX = centerX + (int)(bounds.width / 2 * Math.cos(angle));
        int edgeY = centerY + (int)(bounds.height / 2 * Math.sin(angle));
        
        return new Point(edgeX, edgeY);
    }
    
    private void drawGeneralization(Graphics2D g2d, Point from, Point to) {
        // Solid line with closed arrow (inheritance) - arrow points to parent
        g2d.setColor(theme.GENERALIZATION_COLOR);
        g2d.drawLine(from.x, from.y, to.x, to.y);
        drawArrow(g2d, from, to, true, true);
    }
    
    private void drawRealization(Graphics2D g2d, Point from, Point to) {
        // Dashed line with open arrow (interface implementation) - arrow points to interface
        g2d.setColor(theme.REALIZATION_COLOR);
        Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(from.x, from.y, to.x, to.y);
        drawArrow(g2d, from, to, true, false);
        g2d.setStroke(new BasicStroke(2.0f));
    }
    
    private void drawComposition(Graphics2D g2d, Point from, Point to) {
        // Solid line with filled diamond - diamond at source, arrow to target
        g2d.setColor(theme.COMPOSITION_COLOR);
        g2d.drawLine(from.x, from.y, to.x, to.y);
        drawDiamond(g2d, from, to, true);
    }
    
    private void drawAggregation(Graphics2D g2d, Point from, Point to) {
        // Solid line with empty diamond - diamond at source, arrow to target
        g2d.setColor(theme.AGGREGATION_COLOR);
        g2d.drawLine(from.x, from.y, to.x, to.y);
        drawDiamond(g2d, from, to, false);
    }
    
    private void drawAssociation(Graphics2D g2d, Point from, Point to) {
        // Simple solid line with arrow
        g2d.setColor(theme.ASSOCIATION_COLOR);
        g2d.drawLine(from.x, from.y, to.x, to.y);
        drawArrow(g2d, from, to, false, false);
    }
    
    private void drawRelationWithLabels(Graphics2D g2d, RelationModel relation, Point from, Point to) {
        // Draw the relation line first
        RelationType type = relation.getRelationType();
        
        switch (type) {
            case GENERALIZATION:
                drawGeneralization(g2d, from, to);
                break;
            case REALIZATION:
                drawRealization(g2d, from, to);
                break;
            case COMPOSITION:
                drawComposition(g2d, from, to);
                break;
            case AGGREGATION:
                drawAggregation(g2d, from, to);
                break;
            case ASSOCIATION:
            default:
                drawAssociation(g2d, from, to);
                break;
        }
        
        // Draw multiplicity labels and role name (only for associations, aggregations, compositions)
        if (type == RelationType.ASSOCIATION || type == RelationType.AGGREGATION || type == RelationType.COMPOSITION) {
            drawMultiplicityLabels(g2d, relation, from, to);
        }
    }
    
    private void drawMultiplicityLabels(Graphics2D g2d, RelationModel relation, Point from, Point to) {
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font(originalFont.getName(), Font.PLAIN, 10));
        g2d.setColor(Color.BLACK);
        
        // Calculate midpoint
        int midX = (from.x + to.x) / 2;
        int midY = (from.y + to.y) / 2;
        
        // Draw target multiplicity (near target end)
        String targetMult = formatMultiplicity(relation.getTargetMultiplicity());
        if (targetMult != null && !targetMult.equals("1")) {
            int targetX = from.x + (to.x - from.x) * 3 / 4;
            int targetY = from.y + (to.y - from.y) * 3 / 4;
            g2d.drawString(targetMult, targetX, targetY - 5);
        }
        
        // Draw source multiplicity (near source end) - usually "1" so we can skip if default
        String sourceMult = formatMultiplicity(relation.getSourceMultiplicity());
        if (sourceMult != null && !sourceMult.equals("1")) {
            int sourceX = from.x + (to.x - from.x) * 1 / 4;
            int sourceY = from.y + (to.y - from.y) * 1 / 4;
            g2d.drawString(sourceMult, sourceX, sourceY - 5);
        }
        
        // Draw role name if available (near the line)
        if (relation.getRoleName() != null && !relation.getRoleName().isEmpty()) {
            // Position role name closer to source
            int roleX = from.x + (to.x - from.x) * 1 / 3;
            int roleY = from.y + (to.y - from.y) * 1 / 3;
            // Offset perpendicular to the line
            double angle = Math.atan2(to.y - from.y, to.x - from.x);
            double perpAngle = angle + Math.PI / 2;
            int offsetX = (int)(15 * Math.cos(perpAngle));
            int offsetY = (int)(15 * Math.sin(perpAngle));
            g2d.drawString(relation.getRoleName(), roleX + offsetX, roleY + offsetY);
        }
        
        g2d.setFont(originalFont);
    }
    
    private String formatMultiplicity(String mult) {
        if (mult == null || mult.isEmpty()) {
            return "1";
        }
        // Convert "0..*" to "0..*", "1" to "1", etc.
        if (mult.equals("0..*") || mult.equals("*") || mult.equals("n")) {
            return "0..*";
        }
        if (mult.equals("0..1")) {
            return "0..1";
        }
        if (mult.equals("1..*")) {
            return "1..*";
        }
        return mult;
    }
    
    private void drawArrow(Graphics2D g2d, Point from, Point to, boolean filled, boolean closed) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int arrowLength = 12;
        int arrowWidth = 7;
        
        // Adjust arrow position to be at the edge, not center
        int arrowX = to.x;
        int arrowY = to.y;
        
        double arrowAngle1 = angle - Math.PI + Math.atan(arrowWidth / (double) arrowLength);
        double arrowAngle2 = angle - Math.PI - Math.atan(arrowWidth / (double) arrowLength);
        
        int x1 = (int) (arrowX + arrowLength * Math.cos(arrowAngle1));
        int y1 = (int) (arrowY + arrowLength * Math.sin(arrowAngle1));
        int x2 = (int) (arrowX + arrowLength * Math.cos(arrowAngle2));
        int y2 = (int) (arrowY + arrowLength * Math.sin(arrowAngle2));
        
        if (closed) {
            int[] xPoints = {arrowX, x1, x2};
            int[] yPoints = {arrowY, y1, y2};
            if (filled) {
                g2d.fillPolygon(xPoints, yPoints, 3);
            } else {
                g2d.drawPolygon(xPoints, yPoints, 3);
            }
        } else {
            g2d.drawLine(arrowX, arrowY, x1, y1);
            g2d.drawLine(arrowX, arrowY, x2, y2);
        }
    }
    
    private void drawDiamond(Graphics2D g2d, Point from, Point to, boolean filled) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int diamondSize = 10;
        
        // Position diamond near the source (from point)
        int centerX = (int) (from.x + diamondSize * Math.cos(angle));
        int centerY = (int) (from.y + diamondSize * Math.sin(angle));
        
        // Create diamond shape perpendicular to the line
        double perpAngle = angle + Math.PI / 2;
        int[] xPoints = {
            centerX,
            (int) (centerX + diamondSize * Math.cos(perpAngle)),
            (int) (centerX + diamondSize * Math.cos(angle)),
            (int) (centerX + diamondSize * Math.cos(perpAngle + Math.PI))
        };
        int[] yPoints = {
            centerY,
            (int) (centerY + diamondSize * Math.sin(perpAngle)),
            (int) (centerY + diamondSize * Math.sin(angle)),
            (int) (centerY + diamondSize * Math.sin(perpAngle + Math.PI))
        };
        
        if (filled) {
            g2d.fillPolygon(xPoints, yPoints, 4);
        } else {
            g2d.drawPolygon(xPoints, yPoints, 4);
        }
    }

    public static void displayClassDiagram(String projectName, List<PackageModel> rootPackages) {
        JFrame frame = new JFrame("Class Diagram: " + projectName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ClassDiagramUi classDiagramUi = new ClassDiagramUi(rootPackages);
        JScrollPane scrollPane = new JScrollPane(classDiagramUi);
        frame.add(scrollPane);
        frame.setSize(new Dimension(1200, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
