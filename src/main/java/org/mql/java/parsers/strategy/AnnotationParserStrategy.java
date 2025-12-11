package org.mql.java.parsers.strategy;

import org.mql.java.models.AnnotationModel;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.parserImp.AnnotationParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for parsing Java annotations
 */
public class AnnotationParserStrategy implements ElementParserStrategy {
    
    @Override
    public Object parse(Class<?> classFile) {
        AnnotationParser parser = new AnnotationParser(classFile);
        return parser.getAnnotation();
    }
    
    @Override
    public List<RelationModel> getRelations(Class<?> classFile) {
        // Annotations typically don't have relations
        return new ArrayList<>();
    }
    
    @Override
    public boolean canHandle(Class<?> classFile) {
        return classFile.isAnnotation();
    }
}

