package org.mql.java.parsers.factory;

import org.mql.java.parsers.strategy.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory pattern for creating appropriate parser strategies
 * Uses Chain of Responsibility to find the right parser
 */
public class ParserFactory {
    private static final List<ElementParserStrategy> strategies = new ArrayList<>();
    
    static {
        // Register all strategies in priority order
        strategies.add(new AnnotationParserStrategy());
        strategies.add(new EnumParserStrategy());
        strategies.add(new InterfaceParserStrategy());
        strategies.add(new ClassParserStrategy());
    }
    
    /**
     * Get the appropriate parser strategy for a given class
     * @param classFile The class to parse
     * @return The appropriate parser strategy, or null if none found
     */
    public static ElementParserStrategy getParser(Class<?> classFile) {
        for (ElementParserStrategy strategy : strategies) {
            if (strategy.canHandle(classFile)) {
                return strategy;
            }
        }
        return null;
    }
    
    /**
     * Register a custom parser strategy
     * @param strategy The strategy to register
     */
    public static void registerStrategy(ElementParserStrategy strategy) {
        strategies.add(0, strategy); // Add at beginning for priority
    }
}

