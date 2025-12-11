package org.mql.java.models;

import org.mql.java.enumerations.RelationType;

public class RelationModel {
	private Class<?> sourceModel;
	private Class<?> targetModel;
	private RelationType relationType;
	private String sourceMultiplicity; // Multiplicity at source end (e.g., "1", "0..1", "1..*")
	private String targetMultiplicity; // Multiplicity at target end (e.g., "1", "0..1", "1..*")
	private String roleName; // Role name on the relation (field name)

	public RelationModel(Class<?> sourceModel, Class<?> targetModel, RelationType relationType) {
		this.sourceModel = sourceModel;
		this.targetModel = targetModel;
		this.relationType = relationType;
		this.sourceMultiplicity = "1";
		this.targetMultiplicity = "1";
	}

	public RelationModel(Class<?> sourceModel, Class<?> targetModel, RelationType relationType, 
	                     String sourceMultiplicity, String targetMultiplicity, String roleName) {
		this.sourceModel = sourceModel;
		this.targetModel = targetModel;
		this.relationType = relationType;
		this.sourceMultiplicity = sourceMultiplicity != null ? sourceMultiplicity : "1";
		this.targetMultiplicity = targetMultiplicity != null ? targetMultiplicity : "1";
		this.roleName = roleName;
	}

	// Getters et Setters

	public RelationModel() {
		this.sourceMultiplicity = "1";
		this.targetMultiplicity = "1";
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public Class<?> getSourceModel() {
		return sourceModel;
	}

	public Class<?> getTargetModel() {
		return targetModel;
	}

	public String getSourceMultiplicity() {
		return sourceMultiplicity;
	}

	public void setSourceMultiplicity(String sourceMultiplicity) {
		this.sourceMultiplicity = sourceMultiplicity != null ? sourceMultiplicity : "1";
	}

	public String getTargetMultiplicity() {
		return targetMultiplicity;
	}

	public void setTargetMultiplicity(String targetMultiplicity) {
		this.targetMultiplicity = targetMultiplicity != null ? targetMultiplicity : "1";
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setTargetModel(Class<?> targetModel) {
		this.targetModel = targetModel;
	}

	public void setSourceModel(Class<?> sourceModel) {
		this.sourceModel = sourceModel;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	@Override
	public String toString() {
		return "Relation  : \t" + getRelationType() + "\t" + sourceModel.getSimpleName() + "-----------------"
				+ getRelationType().getSymbol() + targetModel.getSimpleName() + "\n";
	}
}
