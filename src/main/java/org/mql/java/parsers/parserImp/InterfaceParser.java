package org.mql.java.parsers.parserImp;

import java.util.List;

import org.mql.java.models.FieldModel;
import org.mql.java.models.InterfaceModel;
import org.mql.java.models.MethodModel;
import org.mql.java.utils.ClassesLoaderUtils;

public class InterfaceParser {

	private InterfaceModel interfaceModel;

	public InterfaceParser(String path, String name) {
		this(ClassesLoaderUtils.forName(path, name));
	}

	public InterfaceParser(Class<?> class1) {

		this.interfaceModel = new InterfaceModel(class1);
	}

	public String getName() {
		return interfaceModel.getInterfaceName();
	}

	public List<FieldModel> getFields() {
		return interfaceModel.getFields();
	}

	public List<MethodModel> getMethods() {
		return interfaceModel.getMethods();
	}
	
	public InterfaceModel getInterfaceModel() {
		return interfaceModel;
	}

	@Override
	public String toString() {
		return "Interface : " + getName();
	}
}
