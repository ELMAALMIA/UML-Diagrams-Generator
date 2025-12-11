package org.mql.java.xml;

import org.mql.java.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLGenerator {

	public void generateXML(ProjectModel project, String outputPath) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			// Root element
			Element root = document.createElement("project");
			root.setAttribute("name", project.getName());
			document.appendChild(root);

			createPackageList(project.getPackagesList(), document, root);

			// Save XML to file
			saveDocument(document, outputPath);

		} catch (Exception e) {
			System.err.println("Error generating XML: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to generate XML file", e);
		}
	}

	private void createPackageList(List<PackageModel> packages, Document document, Element parentElement) {
		for (PackageModel packageModel : packages) {
			Element packageElement = document.createElement("package");
			packageElement.setAttribute("name", packageModel.getName());

			for (ClassModel classModel : packageModel.getClasses()) {
				packageElement.appendChild(createClass(document, classModel));
			}
			for (InterfaceModel interfaceModel : packageModel.getInterfaces()) {
				packageElement.appendChild(createInterface(document, interfaceModel));
			}
			for (RelationModel relation : packageModel.getRelations()) {
				packageElement.appendChild(createRelation(document, relation));
			}
			for (Enumeration enumeration : packageModel.getEnumerations()) {
				packageElement.appendChild(createEnumuration(document, enumeration));
			}
			for (AnnotationModel annotationModel : packageModel.getAnnotations()) {
				packageElement.appendChild(createAnnotations(document, annotationModel));
			}

			// Recursive call for sub-packages
			if (packageModel.getPackages() != null) {
				createPackageList(packageModel.getPackages(), document, packageElement);
			}

			parentElement.appendChild(packageElement);
		}
	}

//    private Element createClass(Document document, ClassModel cls) {
//        Element classElement = document.createElement("class");
//        classElement.setAttribute("name", cls.getName());
//        return classElement;
//    }

	private Element createInterface(Document document, InterfaceModel intf) {
		Element interfaceElement = document.createElement("interface");
		interfaceElement.setAttribute("name", intf.getInterfaceName());
		for (FieldModel field : intf.getFields()) {
			Element fieldElement = createFieldElement(document, field);
			interfaceElement.appendChild(fieldElement);
		}

		for (MethodModel method : intf.getMethods()) {
			Element methodElement = createMethodElement(document, method);
			interfaceElement.appendChild(methodElement);
		}
		return interfaceElement;
	}

	private Element createRelation(Document document, RelationModel relation) {
		Element relationElement = document.createElement("relation");
		relationElement.setAttribute("type", relation.getRelationType().toString());
		relationElement.setAttribute("source", relation.getSourceModel().toString());
		relationElement.setAttribute("target", relation.getTargetModel().toString());
		return relationElement;
	}

	private Element createEnumuration(Document document, Enumeration enumeration) {
		Element enumerationElement = document.createElement("enumeration");
		enumerationElement.setAttribute("name", enumeration.getName());
		return enumerationElement;
	}

	private Element createAnnotations(Document document, AnnotationModel annotationModel) {
		Element annotationElement = document.createElement("annotation");
		annotationElement.setAttribute("name", annotationModel.getName());
		return annotationElement;
	}

	private Element createClass(Document document, ClassModel cls) {
		Element classElement = document.createElement("class");
		classElement.setAttribute("name", cls.getName());

		if (cls.getParent() != null) {
			Element parentElement = document.createElement("parent");
			parentElement.appendChild(document.createTextNode(cls.getParent()));
			classElement.appendChild(parentElement);
		}

		for (FieldModel field : cls.getFields()) {
			Element fieldElement = createFieldElement(document, field);
			classElement.appendChild(fieldElement);
		}

		for (MethodModel method : cls.getMethods()) {
			Element methodElement = createMethodElement(document, method);
			classElement.appendChild(methodElement);
		}

		for (ConstructorModel constructor : cls.getConstructors()) {
			Element constructorElement = createConstructorElement(document, constructor);
			classElement.appendChild(constructorElement);
		}

		for (String interfaceName : cls.getInterfacesImp()) {
			Element interfaceElement = document.createElement("interfaceImplemented");
			interfaceElement.appendChild(document.createTextNode(interfaceName));
			classElement.appendChild(interfaceElement);
		}

		return classElement;
	}

	private Element createFieldElement(Document document, FieldModel field) {
		Element fieldElement = document.createElement("field");
		fieldElement.setAttribute("name", field.getName());

		String visibilityString = field.getNiveauVisiblity().toString();
		String typeString = field.getType();
		String multiplicityString = field.isIterable() ? "[]" : "";

		if (field.isIterable()) {
			String genericType = field.getType();
			typeString = typeString + "<" + genericType + ">";
		}
		fieldElement.setAttribute("type", typeString);
		fieldElement.setAttribute("multiplicity", multiplicityString);

		fieldElement.setAttribute("visibility", visibilityString);
		fieldElement.setAttribute("isStatic", String.valueOf(field.isStatic()));
		fieldElement.setAttribute("isFinal", String.valueOf(field.isFinal()));

		return fieldElement;
	}

	private Element createMethodElement(Document document, MethodModel method) {
		Element methodElement = document.createElement("method");
		methodElement.setAttribute("name", method.getName());
		methodElement.setAttribute("returnType", method.getTypeReturn());
		methodElement.setAttribute("visibility", method.getVisibility().toString());
		methodElement.setAttribute("isStatic", String.valueOf(method.isStatic()));
		methodElement.setAttribute("isFinal", String.valueOf(method.isFinal()));
		for (ParameterModel parameterModel : method.getParameters()) {
			Element paramElement = document.createElement("parameter");
			paramElement.setAttribute("name", parameterModel.getName());
			paramElement.setAttribute("type", parameterModel.getType());
			methodElement.appendChild(paramElement);
		}
		return methodElement;
	}

	private Element createConstructorElement(Document document, ConstructorModel constructor) {
		Element constructorElement = document.createElement("constructor");
		constructorElement.setAttribute("name", constructor.getName());
		for (ParameterModel param : constructor.getParameterList()) {
			Element paramElement = document.createElement("parameter");
			paramElement.setAttribute("name", param.getName());
			paramElement.setAttribute("type", param.getType());
			constructorElement.appendChild(paramElement);
		}
		return constructorElement;
	}

	private void saveDocument(Document document, String outputPath) throws Exception {
		// Create parent directories if they don't exist
		File outputFile = new File(outputPath);
		File parentDir = outputFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(outputFile);
		transformer.transform(domSource, streamResult);
	}
}
