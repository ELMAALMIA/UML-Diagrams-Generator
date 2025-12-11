package org.mql.java.enumerations;

// visibility 
public enum AccessModifier {
	PUBLIC("+"), PACKAGE("~"), PROTECTED("#"), PRIVATE("-");

	private String symbol;

	private AccessModifier(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public static AccessModifier getVisibility(String visibilityAttribute) {

		for (AccessModifier modifier : values()) {
			if (modifier.name().equalsIgnoreCase(visibilityAttribute)) {
				return modifier;
			}
		}

		return null;
	}
}