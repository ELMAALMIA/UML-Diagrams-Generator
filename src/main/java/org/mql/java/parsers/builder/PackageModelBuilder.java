package org.mql.java.parsers.builder;

import org.mql.java.models.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern for constructing PackageModel
 * Separates construction from representation
 */
public class PackageModelBuilder {
    private PackageModel packageModel;
    
    public PackageModelBuilder(String packageName) {
        this.packageModel = new PackageModel(packageName);
    }
    
    public PackageModelBuilder addClass(ClassModel classModel) {
        packageModel.getClasses().add(classModel);
        return this;
    }
    
    public PackageModelBuilder addInterface(InterfaceModel interfaceModel) {
        packageModel.getInterfaces().add(interfaceModel);
        return this;
    }
    
    public PackageModelBuilder addEnumeration(Enumeration enumeration) {
        packageModel.getEnumerations().add(enumeration);
        return this;
    }
    
    public PackageModelBuilder addAnnotation(AnnotationModel annotation) {
        packageModel.getAnnotations().add(annotation);
        return this;
    }
    
    public PackageModelBuilder addPackage(PackageModel subPackage) {
        packageModel.getPackages().add(subPackage);
        return this;
    }
    
    public PackageModelBuilder addRelation(RelationModel relation) {
        packageModel.getRelations().add(relation);
        return this;
    }
    
    public PackageModelBuilder addRelations(List<RelationModel> relations) {
        packageModel.getRelations().addAll(relations);
        return this;
    }
    
    public PackageModel build() {
        return packageModel;
    }
}

