package org.mql.java.parsers.parserImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.models.AnnotationModel;
import org.mql.java.models.ClassModel;
import org.mql.java.models.Enumeration;
import org.mql.java.models.InterfaceModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.RelationModel;
import org.mql.java.parsers.Parser;
import org.mql.java.utils.ClassesLoaderUtils;

public class PackageParser implements Parser {

	private String packageName;
	private List<PackageModel> packages;
	private List<ClassModel> classes;
	private List<InterfaceModel> interfaces;
	private List<Enumeration> enumerations;
	private List<AnnotationModel> annotations;
	private PackageModel packageModel;
	private List<RelationModel> relations;
	private List<RelationModel> relationlistsProject;
	private String path;

	public PackageParser(String path, String packageName) {
		this.packageName = packageName;
		this.path = path;
		this.packages = new ArrayList<>();
		this.classes = new ArrayList<>();
		this.interfaces = new ArrayList<>();
		this.enumerations = new ArrayList<>();
		this.annotations = new ArrayList<>();
		this.packageModel = new PackageModel(packageName);
		this.relations = new ArrayList<>();
		this.relationlistsProject = new ArrayList<>();
	}

	@Override
	public void parse() {
		String packagePath = packageName.replace(".", "/");
		File dir = new File(path + "/bin/" + packagePath);

		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}

		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file == null) {
				continue;
			}

			String name = file.getName().replace(".class", "");
			String fullName = packageName + "." + name;

			if (file.isFile() && file.getName().endsWith(".class")) {
				Class<?> classFile = ClassesLoaderUtils.forName(path, fullName);
				if (classFile == null) {
					continue;
				}

				try {
					// Check if class can be properly analyzed
					classFile.getSimpleName(); // This will throw if dependencies are missing
					
					// Use Strategy pattern with Factory
					org.mql.java.parsers.factory.ParserFactory factory = 
						new org.mql.java.parsers.factory.ParserFactory();
					org.mql.java.parsers.strategy.ElementParserStrategy strategy = 
						org.mql.java.parsers.factory.ParserFactory.getParser(classFile);
					
					if (strategy != null) {
						Object parsedElement = strategy.parse(classFile);
						List<RelationModel> elementRelations = strategy.getRelations(classFile);
						
						// Add to appropriate collection based on type
						if (parsedElement instanceof ClassModel) {
							classes.add((ClassModel) parsedElement);
							relations.addAll(elementRelations);
						} else if (parsedElement instanceof InterfaceModel) {
							interfaces.add((InterfaceModel) parsedElement);
							relations.addAll(elementRelations);
						} else if (parsedElement instanceof Enumeration) {
							enumerations.add((Enumeration) parsedElement);
						} else if (parsedElement instanceof AnnotationModel) {
							annotations.add((AnnotationModel) parsedElement);
						}
					}
				} catch (NoClassDefFoundError | Exception e) {
					// Skip classes with missing dependencies
					System.err.println("Warning: Could not analyze class " + fullName + 
						" due to missing dependencies: " + e.getMessage());
					continue;
				}
			} else if (file.isDirectory()) {
				PackageParser packageParser = new PackageParser(path, fullName);
				packageParser.parse();
				packages.add(packageParser.getPackageModel());
			}
		}

		// Use Builder pattern for constructing PackageModel
		org.mql.java.parsers.builder.PackageModelBuilder builder = 
			new org.mql.java.parsers.builder.PackageModelBuilder(packageName);
		
		for (ClassModel cls : classes) {
			builder.addClass(cls);
		}
		for (InterfaceModel intf : interfaces) {
			builder.addInterface(intf);
		}
		for (Enumeration enm : enumerations) {
			builder.addEnumeration(enm);
		}
		for (AnnotationModel ann : annotations) {
			builder.addAnnotation(ann);
		}
		for (PackageModel pkg : packages) {
			builder.addPackage(pkg);
		}
		builder.addRelations(relations);
		
		packageModel = builder.build();
		relationlistsProject.addAll(relations);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageModel(PackageModel packageModel) {
		this.packageModel = packageModel;
	}

	public List<RelationModel> getRelations() {
		return relationlistsProject;
	}

	public PackageModel getPackageModel() {
		return packageModel;
	}
}
