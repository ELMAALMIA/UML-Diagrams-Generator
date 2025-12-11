package org.mql.java.tests;


import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.mql.java.models.PackageModel;
import org.mql.java.models.ProjectModel;
import org.mql.java.parsers.parserImp.XMLParser;

public class ParseXMLFile {
	public ParseXMLFile() {
parseFileConsole();
	}

	void parseFileLogger() {
		XMLParser xmlParser = new XMLParser();
		String filePath = "D:\\MQL-2023\\Java\\El Maalmi Ayoub-UML Diagrams Generator\\resources\\xmlDocument\\project-UML.xml";
		ProjectModel project = xmlParser.parse(filePath);
		Logger logger = Logger.getLogger("ParseXML");


		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("resources/logs/parse-xml-Resultat.txt");
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);


		logger.addHandler(fileHandler);

			if (project != null) {

				logger.info("Nom du projet parsé : " + project.getName());
				List<PackageModel> list = project.getPackagesList();
				
				for (PackageModel packageModel : list) {
					logger.info(packageModel.toString());
				}

			} else {
				System.out.println("Échec de l'analyse XML.");
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	void parseFileConsole() {
		XMLParser xmlParser = new XMLParser();
		String filePath = "D:\\MQL-2023\\Java\\El Maalmi Ayoub-UML Diagrams Generator\\resources\\xmlDocument\\project-UML.xml";
		ProjectModel project = xmlParser.parse(filePath);
		if (project != null) {

			System.out.println("Nom du projet parsé : " + project.getName());
//			List<PackageModel> list = project.getPackagesList();
//			for (PackageModel packageModel : list) {
//				System.out.println(packageModel.toString());
//			}

		} else {
			System.out.println("Échec de l'analyse XML.");
		}
	}

	public static void main(String[] args) {
new ParseXMLFile();
	}

}
