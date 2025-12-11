package org.mql.java.models;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.mql.java.enumerations.AccessModifier;
import org.mql.java.utils.GenericTypeUtils;
import org.mql.java.utils.VisibilityUtils;

public class FieldModel {
	private String name;
	private AccessModifier niveauVisiblity;
	private String type;
	private boolean isStatic;
	private boolean isFinal;
	private Multiplicity multiplicity;
	private String genericType;
	public FieldModel() {
		
	}

	public FieldModel(Field f) {
		name = f.getName();

		setType(f.getType().getSimpleName());
		setNiveauVisiblity(VisibilityUtils.determineVisibility(f.getModifiers()));
		setStatic(Modifier.isStatic(f.getModifiers()));
		setFinal(Modifier.isFinal(f.getModifiers()));
		setGenericType(GenericTypeUtils.getGenericType(f.getGenericType()));
		multiplicity = new Multiplicity();
		if (isIterable()) {
			multiplicity.setUpperBound("n");
		}
	}


	public boolean isIterable() {
		return getType().equals("List") || getType().equals("Set") || getType().equals("Queue")
				|| getType().equals("Deque") || getType().endsWith("[]");
	}

	public boolean isMultiple() {
		return multiplicity != null && multiplicity.isMultiple();
	}

	public void setGenericType(String genericType) {
		this.genericType = genericType;
	}

	private String getGenericType() {

		return genericType;
	}

//


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccessModifier getNiveauVisiblity() {
		return niveauVisiblity;
	}

	public void setNiveauVisiblity(AccessModifier niveauVisiblity) {
		this.niveauVisiblity = niveauVisiblity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Multiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}
	public void setIterable(boolean isIterable) {
	    if (isIterable) {
	        if (getType().equals("List") || getType().equals("Set") || getType().equals("Queue")
	                || getType().equals("Deque") || getType().endsWith("[]")) {
	            multiplicity = new Multiplicity();
	            multiplicity.setUpperBound("n");
	        }
	    } else {
	        multiplicity = null;
	    }
	}


	@Override
	public String toString() {
		String visibilityString = getNiveauVisiblity().getSymbol();
		String typeString = getType();
		String multiplicityString = isIterable() ? "[]" : "";

		if (isIterable()) {
			String genericType = getGenericType();
			typeString = typeString + "<" + genericType + ">";
		}

		return visibilityString + " " + getName() + " : " + typeString + multiplicityString;
	}


}
