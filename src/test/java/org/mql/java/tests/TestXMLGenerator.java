package org.mql.java.tests;


import org.mql.java.parsers.parserImp.ProjectParser;
import org.mql.java.xml.XMLGenerator;

public class TestXMLGenerator {

	public TestXMLGenerator() {
		String completePath = "D:/MQL-2023/Java/";

		String projectName = "El Maalmi Ayoub-UML Diagrams Generator";
		ProjectParser projectParser = new ProjectParser(completePath + "/" + projectName);

		XMLGenerator xmlGenerator = new XMLGenerator();

		System.out.println(projectParser.getProject());
		projectParser.parse();

		String xmlOutput = completePath + projectName + "/resources/xmlDocument/project-UML.xml";

		xmlGenerator.generateXML(projectParser.getProject(), xmlOutput);

		System.out.println("Fichier XML généré avec succès : " + xmlOutput);
	}

	public static void main(String[] args) {
		new TestXMLGenerator();
	}
}
