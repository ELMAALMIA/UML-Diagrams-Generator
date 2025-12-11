package org.mql.java.parsers.analyzer;

import org.mql.java.models.ProjectModel;
import org.mql.java.parsers.visitor.ModelVisitor;
import org.mql.java.parsers.visitor.StatisticsVisitor;

import java.util.logging.Logger;

/**
 * Facade pattern for data analysis operations
 * Provides a simple interface to complex analysis operations
 */
public class DataAnalyzer {
    private static final Logger logger = Logger.getLogger(DataAnalyzer.class.getName());
    private ProjectModel projectModel;
    
    public DataAnalyzer(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }
    
    /**
     * Analyze the project and return statistics
     * @return Statistics as string
     */
    public String analyze() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitor.visit(projectModel);
        return visitor.getStatistics();
    }
    
    /**
     * Apply a custom visitor to the project model
     * @param visitor The visitor to apply
     */
    public void accept(ModelVisitor visitor) {
        visitor.visit(projectModel);
    }
    
    /**
     * Get analysis summary
     * @return Summary string
     */
    public String getSummary() {
        if (projectModel == null) {
            return "No project data available";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("Project: ").append(projectModel.getName()).append("\n");
        summary.append("Path: ").append(projectModel.getPath()).append("\n");
        summary.append("Packages: ").append(
            projectModel.getPackagesList() != null ? projectModel.getPackagesList().size() : 0
        ).append("\n");
        
        return summary.toString();
    }
}

