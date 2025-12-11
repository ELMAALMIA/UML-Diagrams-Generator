package org.mql.java.tests;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import org.mql.java.models.PackageModel;

import org.mql.java.parsers.parserImp.ProjectParser;

public class ParseProjectTest {
	public ParseProjectTest() {
		
		parseLogger();

	}
	void parseConsole() {
		String completePath = "D:/MQL-2023/Java/";

		String projectName = "El Maalmi Ayoub-UML Diagrams Generator";
		ProjectParser projectParser = new ProjectParser(completePath + "/" + projectName);
		
	
		projectParser.parse();

		List<PackageModel> packagesList = projectParser.getProject().getPackagesList();
				
		for (PackageModel item : packagesList) {
//			item.getRelations().forEach(e->System.out.print(e));
//			item.showRelarion();
			System.out.println(item.getName());
			
			
		}
		parse(packagesList);
		
	}
	void parseLogger() {

		try {
		
			Logger logger = Logger.getLogger("ParseProject");


			FileHandler fileHandler = new FileHandler("resources/logs/parse-Console-Resultat.txt");

			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);

	
		logger.addHandler(fileHandler);

			logger.info("Le parsing du projet a commencé.");

			String completePath = "D:/MQL-2023/Java/";

			String projectName = "El Maalmi Ayoub-UML Diagrams Generator";
			
			ProjectParser projectParser = new ProjectParser(completePath + "/" + projectName);
			projectParser.parse();
			logger.info("Project name :"+ projectParser.getProject().getName());
			List<PackageModel> packagesList = projectParser.getProject().getPackagesList();

			
			parse( packagesList ,logger);

			logger.info("Le parsing du projet est terminé.");

	

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void parse(List<PackageModel> packagesList, Logger logger) {

		for (PackageModel item : packagesList) {
			logger.info("Nom du package : " + item.getName()+"\n"+item);
	
			if(item.getPackages() != null) {
				parse(item.getPackages(),logger);
			}
		}
		
	}
	public void parse(List<PackageModel> p) {
		
		for (PackageModel item : p) {

			System.out.println(item.getName());
			System.out.println(item.getRelations());
		//	System.out.println(item.getClasses());
			if(item.getPackages() != null) {
				parse(item.getPackages());
			}
			
			
		}
		
		
	}
	

	public static void main(String[] args) {
		new ParseProjectTest();
	}

}