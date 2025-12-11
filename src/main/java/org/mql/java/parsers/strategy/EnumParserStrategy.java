package org.mql.java.parsers.strategy;

import org.mql.java.models.Enumeration;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.parserImp.EnumerationParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for parsing Java enumerations
 */
public class EnumParserStrategy implements ElementParserStrategy {
    
    @Override
    public Object parse(Class<?> classFile) {
        EnumerationParser parser = new EnumerationParser(classFile);
        return parser.getEnumeration();
    }
    
    @Override
    public List<RelationModel> getRelations(Class<?> classFile) {
        // Enums typically don't have relations in the same way
        return new ArrayList<>();
    }
    
    @Override
    public boolean canHandle(Class<?> classFile) {
        return classFile.isEnum();
    }
}

