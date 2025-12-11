package org.mql.java.parsers.parserImp;

import org.mql.java.models.Enumeration;
import org.mql.java.utils.ClassesLoaderUtils;

public class EnumerationParser {
	private String name;
	public Enumeration enumeration;

	public EnumerationParser(Class<?> classe) {
		name = classe.getName();
		enumeration = new Enumeration(classe);
	}

	public EnumerationParser(String path, String name) {
		this(ClassesLoaderUtils.forName(path, name));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Enumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}

	@Override
	public String toString() {
		return "Enum : " + name;
	}

}
