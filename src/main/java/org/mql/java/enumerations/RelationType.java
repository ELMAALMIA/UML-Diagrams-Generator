package org.mql.java.enumerations;

public enum RelationType  {
    DEPENDENCY(".....>"),
    ASSOCIATION("----"),
    AGGREGATION("----o"),
    COMPOSITION("----*"),

    GENERALIZATION("----|>"),
    REALIZATION("....|>");
	

    private final String symbol;

    private RelationType(String symbol) {
        this.symbol = symbol;
     
    }

    public String getSymbol() {
        return this.symbol;
    }
    
}
