package org.mql.java.parsers.parserImp;



import org.mql.java.models.ClassModel;

public class ClassParser {
    private ClassModel classModel;
    private String className ;
   

    public ClassParser(Class<?> class1) {
        //loadInheritanceChain(class1);
        //classModel.InheritanceChain(inheritanceChain);
        this.classModel = new ClassModel(class1);
        this.className=class1.getName();
        
    }
    
    @Override
    public String toString() {

    	return "class "+className;
    }

	public ClassModel getClassModel() {
		return classModel;
	}

	public void setClassModel(ClassModel classModel) {
		this.classModel = classModel;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
   

}
