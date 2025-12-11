package org.mql.java.parsers.strategy;

import org.mql.java.models.InterfaceModel;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.parserImp.InterfaceParser;
import org.mql.java.parsers.parserImp.RelationParser;

import java.util.List;

/**
 * Strategy for parsing Java interfaces
 */
public class InterfaceParserStrategy implements ElementParserStrategy {
    
    @Override
    public Object parse(Class<?> classFile) {
        InterfaceParser parser = new InterfaceParser(classFile);
        InterfaceModel interfaceModel = parser.getInterfaceModel();
        interfaceModel.setRelations(getRelations(classFile));
        return interfaceModel;
    }
    
    @Override
    public List<RelationModel> getRelations(Class<?> classFile) {
        RelationParser relationParser = new RelationParser(classFile);
        return relationParser.getRelations();
    }
    
    @Override
    public boolean canHandle(Class<?> classFile) {
        return classFile.isInterface();
    }
}

