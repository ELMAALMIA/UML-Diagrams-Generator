package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import org.mql.java.enumerations.AccessModifier;
import org.mql.java.utils.VisibilityUtils;

public class MethodModel {
	private String name;
	private List<ParameterModel> parameters;
	private int modifier;
	private AccessModifier visibility;
	private boolean isStatic;
	private boolean isFinal;
	private String typeReturn;

	public MethodModel() {
		this.parameters = new ArrayList<>();
	}

	public MethodModel(Method m) {
		this.parameters = new ArrayList<>();
		this.name = m.getName();

		addAll(m.getParameters());
		this.modifier = m.getModifiers();

		this.isStatic = Modifier.isStatic(this.modifier);
		this.isFinal = Modifier.isFinal(this.modifier);

		this.visibility = VisibilityUtils.determineVisibility(this.modifier);

		this.typeReturn = m.getReturnType().getSimpleName();
	}

	private void addAll(Parameter[] parametersadd) {
		for (Parameter parameter : parametersadd) {
			parameters.add(new ParameterModel(parameter));
		}

	}

	public List<ParameterModel> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccessModifier getVisibility() {
		return visibility;
	}

	public void setVisibility(AccessModifier visibility) {
		this.visibility = visibility;
	}

	public String getTypeReturn() {
		return typeReturn;
	}

	public void setTypeReturn(String typeReturn) {
		this.typeReturn = typeReturn;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getVisibility().getSymbol()).append(" ").append(getName());
		sb.append("(");
		for (int i = 0; i < parameters.size(); i++) {
			sb.append(parameters.get(i));
			if (i < parameters.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append(") : ").append(getTypeReturn());
		return sb.toString();
	}

}
