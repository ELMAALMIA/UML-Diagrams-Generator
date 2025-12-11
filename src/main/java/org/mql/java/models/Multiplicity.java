package org.mql.java.models;

public class Multiplicity {
    private String lowerBound;
    private String upperBound;

    public Multiplicity() {
        this.lowerBound = "1";
        this.upperBound = "1";
    }

    public Multiplicity(String lowerBound, String upperBound) {
        this.lowerBound = isValidBound(lowerBound) ? lowerBound : "1";
        this.upperBound = isValidBound(upperBound) ? upperBound : "1";
    }

    private boolean isValidBound(String bound) {
        return bound.matches("\\d+|\\*|n");
    }

    public void setUpperBound(String upperBound) {
        if (isValidBound(upperBound)) {
        	this.upperBound = upperBound;
        }
    }
    public String getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(String lowerBound) {
        if (isValidBound(lowerBound)) {
            this.lowerBound = lowerBound;
        }
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public boolean isMultiple() {

        return upperBound.equals("*") || upperBound.equalsIgnoreCase("n") || isNumericMultiple(upperBound);
    }

    private boolean isNumericMultiple(String bound) {
        try {
            int num = Integer.parseInt(bound);
            return num > 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[" + lowerBound + ".." + (upperBound.equals("*") ? "*" : upperBound) + "]";
    }
}