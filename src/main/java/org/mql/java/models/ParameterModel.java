package org.mql.java.models;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.mql.java.utils.GenericTypeUtils;

public class ParameterModel {
	private String name;
	private String type;

	public ParameterModel() {
	}

	public ParameterModel(Parameter p) {
		this.name = p.getName();
		this.type = p.getType().getSimpleName();
		Type genericType = p.getParameterizedType();
		if (genericType instanceof ParameterizedType) {
			String genericTypeName = GenericTypeUtils.getGenericType(genericType);
			if (!genericTypeName.isEmpty()) {
				this.type += "<" + genericTypeName + ">";
			}
		}
	}

	public ParameterModel(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name + " : " + type;
	}
}
