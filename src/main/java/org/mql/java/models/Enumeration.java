package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;

public class Enumeration {

	private String name;
	private List<String> valuesEnum;

	public Enumeration() {
		this.valuesEnum = new ArrayList<>();
	}

	public Enumeration(Class<?> classe) {
		this.valuesEnum = new ArrayList<>();
		
		try {
			name = classe.getSimpleName();
		} catch (NoClassDefFoundError | Exception e) {
			String fullName = classe.getName();
			name = fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		}

		try {
			Object[] constants = classe.getEnumConstants();
			if (constants != null) {
				for (Object value : constants) {
					valuesEnum.add(value.toString());
				}
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Skip enum values if dependencies are missing
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValuesEnum() {
		return valuesEnum;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("enumeration : ").append(getName());
		for (String string : valuesEnum) {
			sb.append("\n\t\t\t").append(string);
		}
		return sb.append("\n").toString();
	}
}
