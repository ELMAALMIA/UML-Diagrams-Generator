package org.mql.java.parsers.visitor;

import org.mql.java.models.*;

/**
 * Visitor that collects statistics about the model
 * Example of Visitor pattern usage
 */
public class StatisticsVisitor implements ModelVisitor {
    private int classCount = 0;
    private int interfaceCount = 0;
    private int enumCount = 0;
    private int annotationCount = 0;
    private int packageCount = 0;
    private int relationCount = 0;
    
    @Override
    public void visit(ProjectModel project) {
        packageCount++;
        if (project.getPackagesList() != null) {
            for (PackageModel pkg : project.getPackagesList()) {
                visit(pkg);
            }
        }
    }
    
    @Override
    public void visit(PackageModel packageModel) {
        packageCount++;
        if (packageModel.getClasses() != null) {
            for (ClassModel cls : packageModel.getClasses()) {
                visit(cls);
            }
        }
        if (packageModel.getInterfaces() != null) {
            for (InterfaceModel intf : packageModel.getInterfaces()) {
                visit(intf);
            }
        }
        if (packageModel.getEnumerations() != null) {
            for (Enumeration enm : packageModel.getEnumerations()) {
                visit(enm);
            }
        }
        if (packageModel.getAnnotations() != null) {
            for (AnnotationModel ann : packageModel.getAnnotations()) {
                visit(ann);
            }
        }
        if (packageModel.getPackages() != null) {
            for (PackageModel pkg : packageModel.getPackages()) {
                visit(pkg);
            }
        }
        if (packageModel.getRelations() != null) {
            for (RelationModel rel : packageModel.getRelations()) {
                visit(rel);
            }
        }
    }
    
    @Override
    public void visit(ClassModel classModel) {
        classCount++;
        if (classModel.getRelations() != null) {
            for (RelationModel rel : classModel.getRelations()) {
                visit(rel);
            }
        }
    }
    
    @Override
    public void visit(InterfaceModel interfaceModel) {
        interfaceCount++;
        if (interfaceModel.getRelations() != null) {
            for (RelationModel rel : interfaceModel.getRelations()) {
                visit(rel);
            }
        }
    }
    
    @Override
    public void visit(Enumeration enumeration) {
        enumCount++;
    }
    
    @Override
    public void visit(AnnotationModel annotation) {
        annotationCount++;
    }
    
    @Override
    public void visit(RelationModel relation) {
        relationCount++;
    }
    
    public int getClassCount() { return classCount; }
    public int getInterfaceCount() { return interfaceCount; }
    public int getEnumCount() { return enumCount; }
    public int getAnnotationCount() { return annotationCount; }
    public int getPackageCount() { return packageCount; }
    public int getRelationCount() { return relationCount; }
    
    public String getStatistics() {
        return String.format(
            "Statistics:\n" +
            "  Classes: %d\n" +
            "  Interfaces: %d\n" +
            "  Enumerations: %d\n" +
            "  Annotations: %d\n" +
            "  Packages: %d\n" +
            "  Relations: %d",
            classCount, interfaceCount, enumCount, annotationCount, packageCount, relationCount
        );
    }
}

