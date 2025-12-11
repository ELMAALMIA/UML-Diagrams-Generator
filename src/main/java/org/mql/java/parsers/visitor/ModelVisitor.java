package org.mql.java.parsers.visitor;

import org.mql.java.models.*;

/**
 * Visitor pattern interface for traversing and processing model elements
 * Allows adding new operations without modifying model classes
 */
public interface ModelVisitor {
    void visit(ProjectModel project);
    void visit(PackageModel packageModel);
    void visit(ClassModel classModel);
    void visit(InterfaceModel interfaceModel);
    void visit(Enumeration enumeration);
    void visit(AnnotationModel annotation);
    void visit(RelationModel relation);
}

