package org.mql.java.models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.parsers.parserImp.RelationParser;

public class ClassModel implements ModelInterface {
	private String name;
	private List<FieldModel> fields;
	private List<MethodModel> methods;
	private List<ConstructorModel> constructors;
	private List<String> interfacesImp;
	private List<String> inheritanceChain;
	private String parent;
	private List<RelationModel> relations;

	public ClassModel() {
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.interfacesImp = new ArrayList<>();
		this.inheritanceChain = new ArrayList<>();
		this.relations = new ArrayList<>();
	}

	public ClassModel(Class<?> c) {
		this.fields = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.constructors = new ArrayList<>();
		this.interfacesImp = new ArrayList<>();
		this.inheritanceChain = new ArrayList<>();
		this.relations = new ArrayList<>();
		
		try {
			this.name = c.getSimpleName();
		} catch (NoClassDefFoundError | Exception e) {
			// Fallback to full name if simple name fails (missing dependencies)
			String fullName = c.getName();
			this.name = fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		}
	
		try {
			Class<?> superclass = c.getSuperclass();
			this.parent = (superclass != null) ? superclass.getName() : null;
		} catch (NoClassDefFoundError | Exception e) {
			this.parent = null;
		}

		try {
			for (Field field : c.getDeclaredFields()) {
				this.fields.add(new FieldModel(field));
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip fields if there are missing dependencies
		}
		
		try {
			for (Method method : c.getDeclaredMethods()) {
				this.methods.add(new MethodModel(method));
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip methods if there are missing dependencies
		}

		try {
			String className = this.name;
			for (Constructor<?> constructor : c.getDeclaredConstructors()) {
				this.constructors.add(new ConstructorModel(constructor, className));
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip constructors if there are missing dependencies
		}

		try {
			for (Class<?> interfaceClass : c.getInterfaces()) {
				this.interfacesImp.add(interfaceClass.getName());
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip interfaces if there are missing dependencies
		}
		
		try {
			inheritanceChain = new ArrayList<>();
			Class<?> current = c;
			while (current != null) {
				inheritanceChain.add(current.getName());
				current = current.getSuperclass();
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip inheritance chain if there are missing dependencies
		}
	}

	public String getName() {
		return name;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public List<MethodModel> getMethods() {
		return methods;
	}

	public void addField(FieldModel fieldModel) {
		fields.add(fieldModel);
	}

	public void setMethods(List<MethodModel> methods) {
		this.methods = methods;
	}

	public void addMethod(MethodModel m) {
		this.methods.add(m);
	}

	public List<ConstructorModel> getConstructors() {
		return constructors;
	}

	public void setRelations(List<RelationModel> relations) {
		this.relations = relations;
	}

	public List<RelationModel> getRelations() {
		return relations;
	}

	public void addAllRelations(List<RelationModel> newRelations) {
		if (newRelations != null) {
			this.relations.addAll(newRelations);
		}
	}

	public void setConstructors(List<ConstructorModel> constructors) {
		this.constructors = constructors;
	}

	public List<String> getInterfacesImp() {
		return interfacesImp;
	}

	public void setInterfacesImp(List<String> interfacesImp) {
		this.interfacesImp = interfacesImp;
	}

	public List<String> getInheritanceChain() {
		return inheritanceChain;
	}

	public void setInheritanceChain(List<String> inheritanceChain) {
		this.inheritanceChain = inheritanceChain;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}

	public void addInterfacesImp(String i) {
		interfacesImp.add(i);
	}

	public void addConstructor(ConstructorModel constructorModel) {
		constructors.add(constructorModel);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n\t\t\t--------------------------------------------------");
		for (FieldModel f : fields) {
			sb.append("\n\t\t\t").append(f);
		}
		sb.append("\n\t\t\t--------------------------------------------------");
		for (ConstructorModel c : constructors) {
			sb.append("\n\t\t\t").append(c);
		}
		for (MethodModel methodModel : methods) {
			sb.append("\n\t\t\t").append(methodModel);
		}
		sb.append("\n\t\t\t--------------------------------------------------");
		return "Class :  " + getName() + sb.toString() + " ";
	}

	@Override
	public void getXml() {
		// TODO: Implement XML generation for class model
	}





}
