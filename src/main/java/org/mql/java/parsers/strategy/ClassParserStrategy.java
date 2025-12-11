package org.mql.java.parsers.strategy;

import org.mql.java.models.ClassModel;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.parserImp.ClassParser;
import org.mql.java.parsers.parserImp.RelationParser;

import java.util.List;

/**
 * Strategy for parsing Java classes
 */
public class ClassParserStrategy implements ElementParserStrategy {
    
    @Override
    public Object parse(Class<?> classFile) {
        ClassParser parser = new ClassParser(classFile);
        ClassModel classModel = parser.getClassModel();
        classModel.setRelations(getRelations(classFile));
        return classModel;
    }
    
    @Override
    public List<RelationModel> getRelations(Class<?> classFile) {
        RelationParser relationParser = new RelationParser(classFile);
        return relationParser.getRelations();
    }
    
    @Override
    public boolean canHandle(Class<?> classFile) {
        return !classFile.isInterface() && 
               !classFile.isEnum() && 
               !classFile.isAnnotation();
    }
}

