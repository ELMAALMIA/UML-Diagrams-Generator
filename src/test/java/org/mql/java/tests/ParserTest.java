package org.mql.java.tests;
import org.junit.jupiter.api.Test;
import org.mql.java.parsers.parserImp.ProjectParser;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testProjectParser() {
        String completePath = "D:/MQL-2023/Java/";
        String projectName = "El Maalmi Ayoub-UML Diagrams Generator";
        
        ProjectParser projectParser = new ProjectParser(completePath + "/" + projectName);
        assertNotNull(projectParser.getProject());
        

    }
}
