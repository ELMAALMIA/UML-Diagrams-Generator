package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {
	private String name;
	private List<PackageModel> packagesList;
	private List<RelationModel> relationsList;
	private String path;

	public ProjectModel() {
		packagesList = new ArrayList<>();
		relationsList = new ArrayList<>();
	}

	public ProjectModel(List<PackageModel> packagesList, List<RelationModel> relations, String name, String path) {
		this.packagesList = packagesList != null ? packagesList : new ArrayList<>();
		this.relationsList = relations != null ? relations : new ArrayList<>();
		this.name = name;
		this.path = path;
	}

	public List<PackageModel> getPackagesList() {
		return packagesList;
	}

	public void setPackagesList(List<PackageModel> packagesList) {
		this.packagesList = packagesList;
	}

	public List<RelationModel> getRelations() {
		return relationsList;
	}

	public void setRelations(List<RelationModel> relations) {
		this.relationsList = relations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

//    public static String getAbsolutePath() {
//        if (project != null)
//            return project.getPath();
//        return "";
//    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Project: ").append(name != null ? name : "Unnamed");
		for (PackageModel packageModel : packagesList) {
			if (packageModel != null) {
				sb.append("\n").append(packageModel.getName());
			}
		}
		return sb.toString();
	}
}
