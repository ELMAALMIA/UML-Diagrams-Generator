package org.mql.java.ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import org.mql.java.models.*;
import org.mql.java.ui.theme.UITheme;

public class PackageUi extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final UITheme theme = UITheme.getInstance();
    
    private Map<String, PackageItem> packageMap;
    private Map<String, PackageModel> packageModelMap;
    private List<PackageDependency> dependencies;
    private static final int PADDING = 50;
    private static final int PACKAGE_WIDTH = theme.PACKAGE_WIDTH;
    private static final int PACKAGE_HEIGHT = theme.PACKAGE_HEIGHT;
    private static final int COLS = 4;

    public PackageUi(List<PackageModel> rootPackages) {
        packageMap = new HashMap<>();
        packageModelMap = new HashMap<>();
        dependencies = new ArrayList<>();
        
        setLayout(null); // Use null layout for custom positioning
        setBackground(theme.BACKGROUND_COLOR);
        
        collectPackages(rootPackages);
        detectDependencies(rootPackages);
        drawPackages();
        setPreferredSize(calculatePreferredSize());
    }

    private void collectPackages(List<PackageModel> pkgs) {
        for (PackageModel p : pkgs) {
            if (p != null && p.getName() != null) {
                packageModelMap.put(p.getName(), p);
                if (p.getPackages() != null) {
                    collectPackages(p.getPackages());
                }
            }
        }
    }

    private void detectDependencies(List<PackageModel> pkgs) {
        for (PackageModel pkg : pkgs) {
            if (pkg == null || pkg.getName() == null) continue;
            
            String pkgName = pkg.getName();
            
            // Check all relations in this package
            for (RelationModel relation : pkg.getRelations()) {
                String sourcePackage = getPackageName(relation.getSourceModel().getName());
                String targetPackage = getPackageName(relation.getTargetModel().getName());
                
                // If classes are in different packages, there's a dependency
                if (sourcePackage != null && targetPackage != null && 
                    !sourcePackage.equals(targetPackage) &&
                    !sourcePackage.startsWith("java.") && 
                    !targetPackage.startsWith("java.")) {
                    
                    // Check if target package exists in our project
                    if (packageModelMap.containsKey(targetPackage)) {
                        PackageDependency dep = new PackageDependency(sourcePackage, targetPackage);
                        if (!dependencies.contains(dep)) {
                            dependencies.add(dep);
                        }
                    }
                }
            }
            
            // Check classes in this package for cross-package references
            for (ClassModel cls : pkg.getClasses()) {
                // Check parent class
                if (cls.getParent() != null && !cls.getParent().equals("java.lang.Object")) {
                    String parentPackage = getPackageName(cls.getParent());
                    if (parentPackage != null && !parentPackage.equals(pkgName) &&
                        !parentPackage.startsWith("java.") && packageModelMap.containsKey(parentPackage)) {
                        PackageDependency dep = new PackageDependency(pkgName, parentPackage);
                        if (!dependencies.contains(dep)) {
                            dependencies.add(dep);
                        }
                    }
                }
                
                // Check implemented interfaces
                for (String iface : cls.getInterfacesImp()) {
                    String ifacePackage = getPackageName(iface);
                    if (ifacePackage != null && !ifacePackage.equals(pkgName) &&
                        !ifacePackage.startsWith("java.") && packageModelMap.containsKey(ifacePackage)) {
                        PackageDependency dep = new PackageDependency(pkgName, ifacePackage);
                        if (!dependencies.contains(dep)) {
                            dependencies.add(dep);
                        }
                    }
                }
            }
            
            // Recursively check sub-packages
            if (pkg.getPackages() != null) {
                detectDependencies(pkg.getPackages());
            }
        }
    }

    private String getPackageName(String fullClassName) {
        if (fullClassName == null) return null;
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(0, lastDot) : "";
    }

    private void drawPackages() {
        int x = PADDING;
        int y = PADDING;
        int col = 0;
        
        for (Map.Entry<String, PackageModel> entry : packageModelMap.entrySet()) {
            PackageItem packageItem = new PackageItem(entry.getValue());
            packageItem.setBounds(x, y, PACKAGE_WIDTH, PACKAGE_HEIGHT);
            add(packageItem);
            packageMap.put(entry.getKey(), packageItem);
            
            col++;
            if (col >= COLS) {
                col = 0;
                y += PACKAGE_HEIGHT + PADDING;
                x = PADDING;
            } else {
                x += PACKAGE_WIDTH + PADDING;
            }
        }
    }

    private Dimension calculatePreferredSize() {
        int totalPackages = packageModelMap.size();
        int rows = (int) Math.ceil((double) totalPackages / COLS);
        int width = (COLS * (PACKAGE_WIDTH + PADDING)) + PADDING;
        int height = (rows * (PACKAGE_HEIGHT + PADDING)) + PADDING;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw package dependencies (dashed arrows)
        for (PackageDependency dep : dependencies) {
            drawPackageDependency(g2d, dep);
        }
    }

    private void drawPackageDependency(Graphics2D g2d, PackageDependency dep) {
        PackageItem sourcePkg = packageMap.get(dep.fromPackage);
        PackageItem targetPkg = packageMap.get(dep.toPackage);
        
        if (sourcePkg == null || targetPkg == null || sourcePkg == targetPkg) {
            return;
        }
        
        Point sourcePoint = getPackageConnectionPoint(sourcePkg, targetPkg);
        Point targetPoint = getPackageConnectionPoint(targetPkg, sourcePkg);
        
        if (sourcePoint == null || targetPoint == null) {
            return;
        }
        
        // Draw dashed arrow (UML dependency)
        g2d.setColor(theme.DEPENDENCY_COLOR);
        Stroke dashed = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{8, 4}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(sourcePoint.x, sourcePoint.y, targetPoint.x, targetPoint.y);
        
        // Draw arrow head
        drawDependencyArrow(g2d, sourcePoint, targetPoint);
        
        g2d.setStroke(new BasicStroke(1.0f));
    }

    private Point getPackageConnectionPoint(Component comp, Component target) {
        if (comp == null || !comp.isVisible()) {
            return null;
        }
        Rectangle bounds = comp.getBounds();
        Rectangle targetBounds = target.getBounds();
        
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        
        int targetCenterX = targetBounds.x + targetBounds.width / 2;
        int targetCenterY = targetBounds.y + targetBounds.height / 2;
        
        int dx = targetCenterX - centerX;
        int dy = targetCenterY - centerY;
        
        double angle = Math.atan2(dy, dx);
        int edgeX = centerX + (int)(bounds.width / 2 * Math.cos(angle));
        int edgeY = centerY + (int)(bounds.height / 2 * Math.sin(angle));
        
        return new Point(edgeX, edgeY);
    }

    private void drawDependencyArrow(Graphics2D g2d, Point from, Point to) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int arrowLength = 12;
        int arrowWidth = 6;
        
        double arrowAngle1 = angle - Math.PI + Math.atan(arrowWidth / (double) arrowLength);
        double arrowAngle2 = angle - Math.PI - Math.atan(arrowWidth / (double) arrowLength);
        
        int x1 = (int) (to.x + arrowLength * Math.cos(arrowAngle1));
        int y1 = (int) (to.y + arrowLength * Math.sin(arrowAngle1));
        int x2 = (int) (to.x + arrowLength * Math.cos(arrowAngle2));
        int y2 = (int) (to.y + arrowLength * Math.sin(arrowAngle2));
        
        g2d.drawLine(to.x, to.y, x1, y1);
        g2d.drawLine(to.x, to.y, x2, y2);
    }

    private static class PackageDependency {
        String fromPackage;
        String toPackage;

        PackageDependency(String from, String to) {
            this.fromPackage = from;
            this.toPackage = to;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PackageDependency that = (PackageDependency) obj;
            return Objects.equals(fromPackage, that.fromPackage) &&
                   Objects.equals(toPackage, that.toPackage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromPackage, toPackage);
        }
    }

    public static void displayPackageDiagram(String projectName, List<PackageModel> rootPackages) {
        JFrame frame = new JFrame("Package Diagram: " + projectName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PackageUi packageUi = new PackageUi(rootPackages);
        JScrollPane scrollPane = new JScrollPane(packageUi);
        frame.add(scrollPane);
        frame.setSize(new Dimension(1200, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
