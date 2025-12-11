package org.mql.java.parsers.parserImp;

import java.text.Annotation;

import org.mql.java.models.AnnotationModel;
import org.mql.java.utils.ClassesLoaderUtils;

public class AnnotationParser {
	private String name;
	public AnnotationModel annotation;
	public AnnotationParser(String projectPath, String annotationName) {
		this(ClassesLoaderUtils.forName(projectPath, annotationName));
	}

	public AnnotationParser(Class<?> classe) {
		annotation= new  AnnotationModel(classe);
	}

	
	public AnnotationModel getAnnotation() {
		return annotation;
	}

	public void setAnnotation(AnnotationModel annotation) {
		this.annotation = annotation;
	}

	@Override
	public String toString() {
		return "Annotation : " + name;
	}
}
