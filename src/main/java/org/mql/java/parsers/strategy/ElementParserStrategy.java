package org.mql.java.parsers.strategy;

import org.mql.java.models.RelationModel;
import java.util.List;

/**
 * Strategy pattern interface for parsing different Java elements
 * Each element type (Class, Interface, Enum, Annotation) has its own parsing strategy
 */
public interface ElementParserStrategy {
    /**
     * Parse a Java class element
     * @param classFile The class to parse
     * @return The parsed model object
     */
    Object parse(Class<?> classFile);
    
    /**
     * Get relations for the parsed element
     * @param classFile The class file
     * @return List of relations
     */
    List<RelationModel> getRelations(Class<?> classFile);
    
    /**
     * Check if this strategy can handle the given class type
     * @param classFile The class to check
     * @return true if this strategy can parse it
     */
    boolean canHandle(Class<?> classFile);
}

