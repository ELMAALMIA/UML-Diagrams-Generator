package org.mql.java.parsers.parserImp;

import org.mql.java.enumerations.AccessModifier;
import org.mql.java.enumerations.RelationType;
import org.mql.java.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import java.util.List;
import java.util.Vector;

public class XMLParser {

	public ProjectModel parse(String xmlFilePath) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new File(xmlFilePath));

			Element root = document.getDocumentElement();
			String projectName = root.getAttribute("name");

			ProjectModel project = new ProjectModel();
			project.setName(projectName);

			NodeList packageNodes = root.getElementsByTagName("package");
			List<PackageModel> packages = new Vector<>(); 

			for (int i = 0; i < packageNodes.getLength(); i++) {
				Element packageElement = (Element) packageNodes.item(i);
				PackageModel packageModel = parsePackage(packageElement);
				packages.add(packageModel);
			}

			project.setPackagesList(packages);
			List<RelationModel> relations = new Vector<>();
			for (PackageModel packageModel : packages) {
				relations.addAll(packageModel.getRelations());
				
			}
			project.setRelations(relations);

			return project;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PackageModel parsePackage(Element packageElement) {
		String packageName = packageElement.getAttribute("name");
	

		PackageModel packageModel = new PackageModel();
		packageModel.setName(packageName);

		NodeList classNodes = packageElement.getElementsByTagName("class");

		List<ClassModel> classes = new Vector<>();

		for (int i = 0; i < classNodes.getLength(); i++) {
			Element classElement = (Element) classNodes.item(i);
			ClassModel classModel = parseClass(classElement);
			
			classes.add(classModel);
		
	
		}
	
	
		packageModel.setClasses(classes);
	

		NodeList interfaceNodes = packageElement.getElementsByTagName("interface");
		List<InterfaceModel> interfaces = new Vector<>();

		for (int i = 0; i < interfaceNodes.getLength(); i++) {
			Element interfaceElement = (Element) interfaceNodes.item(i);
			InterfaceModel interfaceModel = parseInterface(interfaceElement);
			interfaces.add(interfaceModel);
		}

		packageModel.setInterfaces(interfaces);

		NodeList relationNodes = packageElement.getElementsByTagName("relation");
		List<RelationModel> relations = new Vector<>();
	
		for (int i = 0; i < relationNodes.getLength(); i++) {
			Element relationElement = (Element) relationNodes.item(i);

			RelationModel relationModel = parseRelation(relationElement);
			relations.add(relationModel);
		}

		packageModel.setRelations(relations);

		NodeList enumerationNodes = packageElement.getElementsByTagName("enumeration");
		List<Enumeration> enumerations = new Vector<>();

		for (int i = 0; i < enumerationNodes.getLength(); i++) {
			Element enumerationElement = (Element) enumerationNodes.item(i);
			Enumeration enumerationModel = parseEnumeration(enumerationElement);
			enumerations.add(enumerationModel);
		}

		packageModel.setEnumerations(enumerations);

		NodeList annotationNodes = packageElement.getElementsByTagName("annotation");
		List<AnnotationModel> annotations = new Vector<>();

		for (int i = 0; i < annotationNodes.getLength(); i++) {
			Element annotationElement = (Element) annotationNodes.item(i);
			AnnotationModel annotationModel = parseAnnotation(annotationElement);
			annotations.add(annotationModel);
		}

		packageModel.setAnnotations(annotations);

		return packageModel;
	}

	private AnnotationModel parseAnnotation(Element annotationElement) {
		String annotationName = annotationElement.getAttribute("name");
		AnnotationModel annotationModel = new AnnotationModel();
		annotationModel.setName(annotationName);
		return annotationModel;
	}

	private Enumeration parseEnumeration(Element enumerationElement) {
		String enumerationName = enumerationElement.getAttribute("name");
		Enumeration enumeration = new Enumeration();
		enumeration.setName(enumerationName);
		return enumeration;
	}

	private RelationModel parseRelation(Element relationElement) {
		String relationTypeString = relationElement.getAttribute("type");
		String sourceName = relationElement.getAttribute("source");
		String targetName = relationElement.getAttribute("target");
		RelationType relationType = RelationType.valueOf(relationTypeString);

		RelationModel relation = new RelationModel();

		relation.setRelationType(relationType);
		String[] sourceNameArray = sourceName.split(" ");
		String[] targetNameArray = targetName.split(" ");

		
		try {
		    Class<?> sourceModel = Class.forName(sourceNameArray[1]);
		    Class<?> targetModel = Class.forName(targetNameArray[1]);
		    
		    relation.setSourceModel(sourceModel);
		    relation.setTargetModel(targetModel);
		    
		} catch (ClassNotFoundException e) {
		
		    e.printStackTrace();
		}


		return relation;
	}

	private InterfaceModel parseInterface(Element interfaceElement) {
	    String interfaceName = interfaceElement.getAttribute("name");
	    InterfaceModel interfaceModel = new InterfaceModel();
	    interfaceModel.setInterfaceName(interfaceName);
	    NodeList methodElements = interfaceElement.getElementsByTagName("method");


	    for (int i = 0; i < methodElements.getLength(); i++) {
	        Element methodElement = (Element) methodElements.item(i);
	        MethodModel methodModel = parseMethod(methodElement);
	        interfaceModel.addMethod(methodModel);
	    }

	    return interfaceModel;
	}


	private ClassModel parseClass(Element classElement) {

		String className = classElement.getAttribute("name");

		ClassModel classModel = new ClassModel();
		classModel.setName(className);

		Element parentElement = (Element) classElement.getElementsByTagName("parent").item(0);
		if (parentElement != null) {
			String parentClassName = parentElement.getTextContent();
			classModel.setParent(parentClassName);
		}

		NodeList fieldElements = classElement.getElementsByTagName("field");
		for (int i = 0; i < fieldElements.getLength(); i++) {
			Element fieldElement = (Element) fieldElements.item(i);
			FieldModel fieldModel = parseField(fieldElement);
			classModel.addField(fieldModel);
		}

		NodeList methodElements = classElement.getElementsByTagName("method");
		for (int i = 0; i < methodElements.getLength(); i++) {
			Element methodElement = (Element) methodElements.item(i);
			MethodModel methodModel = parseMethod(methodElement);
			classModel.addMethod(methodModel);
		}

		NodeList constructorElements = classElement.getElementsByTagName("constructor");
		for (int i = 0; i < constructorElements.getLength(); i++) {
			Element constructorElement = (Element) constructorElements.item(i);
			ConstructorModel constructorModel = parseConstructor(constructorElement);
			classModel.addConstructor(constructorModel);
		}

		NodeList interfaceElements = classElement.getElementsByTagName("interfaceImplemented");
		for (int i = 0; i < interfaceElements.getLength(); i++) {
			Element interfaceElement = (Element) interfaceElements.item(i);
			String interfaceName = interfaceElement.getTextContent();
			classModel.addInterfacesImp(interfaceName);
		}

		return classModel;
	}

	private ConstructorModel parseConstructor(Element constructorElement) {

		ConstructorModel constructorModel = new ConstructorModel();

		String constructorName = constructorElement.getAttribute("name");

		constructorModel.setName(constructorName);

		NodeList parameterNodes = constructorElement.getElementsByTagName("parameter");
		List<ParameterModel> parameters = new Vector<>();

		for (int i = 0; i < parameterNodes.getLength(); i++) {
			Element parameterElement = (Element) parameterNodes.item(i);
			String paramName = parameterElement.getAttribute("name");
			String paramType = parameterElement.getAttribute("type");
			ParameterModel parameterModel = new ParameterModel();
			parameterModel.setName(paramName);
			parameterModel.setType(paramType);
			parameters.add(parameterModel);
		}

		constructorModel.setParameteeList(parameters);

		return constructorModel;
	}

	private MethodModel parseMethod(Element methodElement) {
		MethodModel methodModel = new MethodModel();

		String methodName = methodElement.getAttribute("name");
		String returnType = methodElement.getAttribute("returnType");

		String v = methodElement.getAttribute("visibility");
		AccessModifier accessModifier = AccessModifier.getVisibility(v);

		methodModel.setName(methodName);
		methodModel.setTypeReturn(returnType);
		methodModel.setVisibility(accessModifier);
		NodeList parameterNodes = methodElement.getElementsByTagName("parameter");
		List<ParameterModel> parameters = new Vector<>();

		for (int i = 0; i < parameterNodes.getLength(); i++) {
			Element parameterElement = (Element) parameterNodes.item(i);
			String paramName = parameterElement.getAttribute("name");
			String paramType = parameterElement.getAttribute("type");

			ParameterModel parameterModel = new ParameterModel();
			parameterModel.setName(paramName);
			parameterModel.setType(paramType);
			parameters.add(parameterModel);
		}

		methodModel.setParameters(parameters);
		return methodModel;

	}

	private FieldModel parseField(Element fieldElement) {
		String fieldName = fieldElement.getAttribute("name");
		String fieldType = fieldElement.getAttribute("type");
		String fieldVisibility = fieldElement.getAttribute("visibility");
		boolean isStatic = Boolean.parseBoolean(fieldElement.getAttribute("isStatic"));
		boolean isFinal = Boolean.parseBoolean(fieldElement.getAttribute("isFinal"));
		boolean isIterable = fieldElement.getAttribute("multiplicity").equals("[]");

		AccessModifier accessModifier = AccessModifier.getVisibility(fieldVisibility);

		FieldModel fieldModel = new FieldModel();
		fieldModel.setName(fieldName);
		fieldModel.setType(fieldType);
		fieldModel.setNiveauVisiblity(accessModifier);
		fieldModel.setStatic(isStatic);
		fieldModel.setFinal(isFinal);
		fieldModel.setIterable(isIterable);

		return fieldModel;
	}

	
}
