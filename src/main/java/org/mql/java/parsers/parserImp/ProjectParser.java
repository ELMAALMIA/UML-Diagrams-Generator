package org.mql.java.parsers.parserImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mql.java.models.PackageModel;
import org.mql.java.models.ProjectModel;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.Parser;
import org.mql.java.utils.StringUtils;

public class ProjectParser implements Parser {
	private static final Logger logger = Logger.getLogger(ProjectParser.class.getName());

	private String projectPath;
	private ProjectModel project;

	public ProjectParser(String path) {
		project = new ProjectModel();
		this.projectPath = path;
		project.setName(getNameProject(path));
		project.setPath(path);
	}
	
	private String getNameProject(String path) {
		return StringUtils.extractProjectName(path);
	}

	@Override
	public void parse() {	
		File srcDirectory = new File(projectPath + "/bin");
		List<PackageModel> packages = new ArrayList<>();

		if (srcDirectory.exists() && srcDirectory.isDirectory()) {
			File[] subDirectories = srcDirectory.listFiles();
			if (subDirectories != null) {
				for (File subDirectory : subDirectories) {
					if (subDirectory != null && subDirectory.isDirectory()) {
						PackageParser p = new PackageParser(projectPath, subDirectory.getName());
						p.parse();
						PackageModel packageModel = p.getPackageModel();
						if (packageModel != null) {
							packages.add(packageModel);
						}
					}
				}
			}
		} else {
			logger.warning("Invalid project path or bin directory does not exist: " + projectPath);
		}

		project.setPackagesList(packages);
	}


	public ProjectModel getProject() {
		return project;
	}

	public String getProjectPath() {
		return projectPath;
	}

}
