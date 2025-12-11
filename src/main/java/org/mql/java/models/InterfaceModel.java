package org.mql.java.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InterfaceModel {
	private String interfaceName;
	private List<FieldModel> fields;
	private List<MethodModel> methods;
	private List<RelationModel> relations;

	public InterfaceModel() {
		fields = new ArrayList<>();
		methods = new ArrayList<>();
		relations = new ArrayList<>();
	}

	public InterfaceModel(Class<?> class1) {
		fields = new ArrayList<>();
		methods = new ArrayList<>();
		relations = new ArrayList<>();
		
		try {
			interfaceName = class1.getSimpleName();
		} catch (NoClassDefFoundError | Exception e) {
			String fullName = class1.getName();
			interfaceName = fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		}
		
		try {
			for (Field field : class1.getDeclaredFields()) {
				fields.add(new FieldModel(field));
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip fields if dependencies are missing
		}
		
		try {
			for (Method method : class1.getDeclaredMethods()) {
				methods.add(new MethodModel(method));
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip methods if dependencies are missing
		}
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}

	public List<MethodModel> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodModel> methods) {
		this.methods = methods;
	}

	public void setRelations(List<RelationModel> relations) {
		this.relations = relations;
	}

	public List<RelationModel> getRelations() {
		return relations;
	}

	public void addMethod(MethodModel methodModel) {
	this.methods.add(methodModel);
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Interface : ").append(getInterfaceName());
		for (FieldModel fieldModel : fields) {
			sb.append("\n\t\t\t").append(fieldModel);
		}
		for (MethodModel model : methods) {
			sb.append("\n\t\t\t").append(model);
		}
		return sb.toString();
	}


}
