package org.mql.java.parsers.visitor;

/**
 * Interface for model elements that can accept visitors
 * Part of Visitor pattern implementation
 */
public interface ModelElement {
    void accept(ModelVisitor visitor);
}

