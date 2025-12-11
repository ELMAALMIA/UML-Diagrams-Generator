package org.mql.java.parsers.parserImp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.mql.java.enumerations.RelationType;
import org.mql.java.models.RelationModel;

public class RelationParser {

	private Class<?> c;
	private List<RelationModel> relations;

	public RelationParser(Class<?> c) {
		this.c = c;
		this.relations = analyseClass(c);
	}

	private List<RelationModel> analyseClass(Class<?> c2) {
		List<RelationModel> relationsList = new ArrayList<>();

		try {
			// Analyze field relationships
			for (Field field : c2.getDeclaredFields()) {
				try {
					Class<?> fieldType = field.getType();
					
					// Skip primitive types and arrays of primitives
					if (fieldType.isPrimitive()) {
						continue;
					}
					
					// Handle arrays - get component type
					if (fieldType.isArray()) {
						Class<?> componentType = fieldType.getComponentType();
						if (componentType.isPrimitive()) {
							continue;
						}
						fieldType = componentType;
					}
					
					// Skip Java standard library classes (we only want project classes)
					if (fieldType.getName().startsWith("java.") || 
					    fieldType.getName().startsWith("javax.") ||
					    fieldType.getName().startsWith("sun.")) {
						continue;
					}
					
					// Check if field type can be accessed (no missing dependencies)
					try {
						fieldType.getSimpleName();
					} catch (NoClassDefFoundError e) {
						continue; // Skip if dependency is missing
					}
					
					RelationType relationType = determineFieldRelationType(field);
					String sourceMult = "1";
					String targetMult = determineMultiplicity(field);
					String roleName = field.getName();
					relationsList.add(new RelationModel(c2, fieldType, relationType, sourceMult, targetMult, roleName));
				} catch (NoClassDefFoundError | Exception e) {
					// Skip this field if there are issues
					continue;
				}
			}

			// Inheritance relationship
			try {
				Class<?> parentClass = c2.getSuperclass();
				if (parentClass != null && !parentClass.getName().equals("java.lang.Object")) {
					// Only include if it's a project class and accessible
					if (!parentClass.getName().startsWith("java.") && 
					    !parentClass.getName().startsWith("javax.")) {
						try {
							parentClass.getSimpleName(); // Check accessibility
							relationsList.add(
									new RelationModel(c2, parentClass, RelationType.GENERALIZATION));
						} catch (NoClassDefFoundError e) {
							// Skip if parent has missing dependencies
						}
					}
				}
			} catch (NoClassDefFoundError | Exception e) {
				// Skip inheritance if there are issues
			}
			
			// Interface implementation relationships
			try {
				for (Class<?> interfaceClass : c2.getInterfaces()) {
					// Only include if it's a project interface and accessible
					if (!interfaceClass.getName().startsWith("java.") && 
					    !interfaceClass.getName().startsWith("javax.")) {
						try {
							interfaceClass.getSimpleName(); // Check accessibility
							relationsList.add(
									new RelationModel(c2, interfaceClass, RelationType.REALIZATION));
						} catch (NoClassDefFoundError e) {
							// Skip if interface has missing dependencies
						}
					}
				}
			} catch (NoClassDefFoundError | Exception e) {
				// Skip interfaces if there are issues
			}
		} catch (NoClassDefFoundError | Exception e) {
			// Return empty list if class analysis fails completely
			return relationsList;
		}

		return relationsList;
	}

	private RelationType determineFieldRelationType(Field field) {
		if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
			return RelationType.COMPOSITION;
		} else if (field.getType().isArray() || Iterable.class.isAssignableFrom(field.getType())) {
			return RelationType.AGGREGATION;
		} else {
			return RelationType.ASSOCIATION;
		}
	}

	private String determineMultiplicity(Field field) {
		Class<?> fieldType = field.getType();
		
		// Array type - many
		if (fieldType.isArray()) {
			return "0..*";
		}
		
		// Collection types - many
		if (java.util.Collection.class.isAssignableFrom(fieldType) ||
		    java.util.List.class.isAssignableFrom(fieldType) ||
		    java.util.Set.class.isAssignableFrom(fieldType) ||
		    java.util.Queue.class.isAssignableFrom(fieldType)) {
			return "0..*";
		}
		
		// Map type - many (key-value pairs)
		if (java.util.Map.class.isAssignableFrom(fieldType)) {
			return "0..*";
		}
		
		// Optional types - 0..1
		if (fieldType.getName().equals("java.util.Optional")) {
			return "0..1";
		}
		
		// Default: single instance
		return "1";
	}

	public List<RelationModel> getRelations() {
		return relations;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (RelationModel relation : relations) {
			sb.append(relation).append("\n");
		}
		return sb.toString();
	}
}
