package org.mql.java.models;

public class AnnotationModel {
	private String name;

	public AnnotationModel(Class<?> c) {
		try {
			name = c.getSimpleName();
		} catch (NoClassDefFoundError | Exception e) {
			String fullName = c.getName();
			name = fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
		}
	}

	public AnnotationModel() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Annotation " + getName();
	}

}
