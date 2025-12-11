package org.mql.java.xml;

import org.mql.java.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class XMIGenerator {

	public void generateXMI(ProjectModel project, String outputPath) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement("xmi:XMI");
			root.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
			root.setAttribute("xmlns:uml", "http://www.omg.org/spec/UML/20090901");
			root.setAttribute("xmi:version", "2.1");
			document.appendChild(root);
			Element model = document.createElement("uml:Model");
			model.setAttribute("name", project.getName());
			root.appendChild(model);
			createPackageXMI(project.getPackagesList(), document, model);
			saveDocument(document, outputPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createPackageXMI(List<PackageModel> packages, Document document, Element parentElement) {
		for (PackageModel packageModel : packages) {
			Element packageElement = document.createElement("uml:Package");
			packageElement.setAttribute("name", packageModel.getName());
			parentElement.appendChild(packageElement);

			for (ClassModel classModel : packageModel.getClasses()) {
				packageElement.appendChild(createClassXMI(document, classModel));
			}

			for (InterfaceModel interfaceModel : packageModel.getInterfaces()) {
				packageElement.appendChild(createInterfaceXMI(document, interfaceModel));
			}

			for (Enumeration enumeration : packageModel.getEnumerations()) {
				packageElement.appendChild(createEnumerationXMI(document, enumeration));
			}

			for (AnnotationModel annotationModel : packageModel.getAnnotations()) {
				packageElement.appendChild(createAnnotationXMI(document, annotationModel));
			}
			for (RelationModel relation : packageModel.getRelations()) {
				packageElement.appendChild(createXmiRelation(document, relation));
			}

			if (packageModel.getPackages() != null) {
				createPackageXMI(packageModel.getPackages(), document, packageElement);
			}
		}
	}

	private Element createClassXMI(Document document, ClassModel classModel) {
		Element classElement = document.createElement("uml:Class");
		classElement.setAttribute("name", classModel.getName());

		for (FieldModel field : classModel.getFields()) {
			Element attributeElement = createAttributeXMI(document, field);
			classElement.appendChild(attributeElement);
		}

		for (MethodModel method : classModel.getMethods()) {
			Element operationElement = createOperationXMI(document, method);
			classElement.appendChild(operationElement);
		}

		for (ConstructorModel constructor : classModel.getConstructors()) {
			Element constructorElement = createConstructorOperationXMI(document, constructor);
			classElement.appendChild(constructorElement);
		}

		for (String interfaceName : classModel.getInterfacesImp()) {
			Element interfaceElement = document.createElement("implementedInterface");
			interfaceElement.setAttribute("name", interfaceName);
			classElement.appendChild(interfaceElement);
		}

		return classElement;
	}

	private Element createInterfaceXMI(Document document, InterfaceModel interfaceModel) {
		Element interfaceElement = document.createElement("uml:Interface");
		interfaceElement.setAttribute("name", interfaceModel.getInterfaceName());

		for (FieldModel field : interfaceModel.getFields()) {
			Element attributeElement = createAttributeXMI(document, field);
			interfaceElement.appendChild(attributeElement);
		}

		for (MethodModel method : interfaceModel.getMethods()) {
			Element operationElement = createOperationXMI(document, method);
			interfaceElement.appendChild(operationElement);
		}

		return interfaceElement;
	}

	private Element createXmiRelation(Document document, RelationModel relation) {
		Element relationElement = document.createElement("packagedElement");
		relationElement.setAttribute("xmi:type", "uml:" + relation.getRelationType());
		relationElement.setAttribute("xmi:id", UUID.randomUUID().toString());
		relationElement.setAttribute("source", relation.getSourceModel().toString());
		relationElement.setAttribute("target", relation.getTargetModel().toString());
		return relationElement;
	}

	private Node createAnnotationXMI(Document document, AnnotationModel annotationModel) {
		Element annotationElement = document.createElement("uml:Annotation");
		annotationElement.setAttribute("name", annotationModel.getName());

		return annotationElement;
	}

	private Element createEnumerationXMI(Document document, Enumeration enumeration) {
		Element enumerationElement = document.createElement("uml:Enumeration");
		enumerationElement.setAttribute("name", enumeration.getName());

		for (String literal : enumeration.getValuesEnum()) {
			Element ItemElement = createEnumerationItemXMI(document, literal);
			enumerationElement.appendChild(ItemElement);
		}

		return enumerationElement;
	}

	private Element createEnumerationItemXMI(Document document, String literal) {
		Element literalElement = document.createElement("uml:EnumerationLiteral");
		literalElement.setAttribute("name", literal);

		return literalElement;
	}

	private Element createAttributeXMI(Document document, FieldModel field) {
		Element fieldElement = document.createElement("ownedAttribute");
		fieldElement.setAttribute("xmi:type", "uml:Property");
		fieldElement.setAttribute("name", field.getName());

		String visibilityString = field.getNiveauVisiblity().toString();
		fieldElement.setAttribute("visibility", visibilityString);

		Element typeElement = document.createElement("type");
		typeElement.setAttribute("xmi:type", "uml:PrimitiveType");
		typeElement.setAttribute("href", "types.uml#" + field.getType());
																						
		fieldElement.appendChild(typeElement);

		if (field.isIterable()) {
			Element upperValueElement = document.createElement("upperValue");
			upperValueElement.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
			upperValueElement.setAttribute("value", "*");
			fieldElement.appendChild(upperValueElement);
		}
		fieldElement.setAttribute("isStatic", String.valueOf(field.isStatic()));
		fieldElement.setAttribute("isReadOnly", String.valueOf(field.isFinal()));

		return fieldElement;
	}

	private Element createOperationXMI(Document document, MethodModel method) {
		Element operationElement = document.createElement("uml:Operation");

		operationElement.setAttribute("name", method.getName());
		String visibility = method.getVisibility().toString();
		operationElement.setAttribute("visibility", visibility);

		operationElement.setAttribute("isStatic", String.valueOf(method.isStatic()));
		operationElement.setAttribute("isLeaf", String.valueOf(method.isFinal()));

		if (method.getTypeReturn() != null && !method.getTypeReturn().isEmpty()) {
			Element returnParamElement = createParameterXMI(document, "return", method.getTypeReturn());
			operationElement.appendChild(returnParamElement);
		}

		for (ParameterModel param : method.getParameters()) {
			Element paramElement = createParameterXMI(document, param.getName(), param.getType());
			operationElement.appendChild(paramElement);
		}

		return operationElement;
	}

	private Element createConstructorOperationXMI(Document document, ConstructorModel constructor) {
		Element constructorElement = document.createElement("ownedOperation");
		constructorElement.setAttribute("xmi:type", "uml:Operation");
		constructorElement.setAttribute("name", constructor.getName());
		constructorElement.setAttribute("isConstructor", "true");
		for (ParameterModel param : constructor.getParameterList()) {
			Element paramElement = createParameterXMI(document, param.getName(), param.getType());
			constructorElement.appendChild(paramElement);
		}

		return constructorElement;
	}

	private Element createParameterXMI(Document document, String name, String type) {
		Element parameterElement = document.createElement("ownedParameter");
		parameterElement.setAttribute("xmi:type", "uml:Parameter");
		parameterElement.setAttribute("name", name);
		parameterElement.setAttribute("type", type);
		return parameterElement;
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
