# UML Diagrams Generator

A Java application that automatically generates UML class diagrams and package diagrams from compiled Java projects. The application uses Java Reflection API to analyze Java classes and creates visual UML representations with proper notation, relationships, and multiplicity indicators.

## Features

### Core Functionality
-  **Automatic Project Analysis**: Analyzes Java projects using Reflection API
-  **Class Diagram Generation**: Creates UML class diagrams with:
  - Classes, Interfaces, Enumerations, Annotations
  - Relationships (Inheritance, Implementation, Association, Aggregation, Composition)
  - Multiplicity indicators (1, 0..1, 1..*, 0..*)
  - Role names on associations
  - Proper UML notation and symbols
-  **Package Diagram Generation**: Creates UML package diagrams with:
  - Package hierarchy
  - Package dependencies (dashed arrows)
  - Package contents visualization
-  **XML/XMI Export**: Exports diagrams to XML and XMI 2.1 formats
-  **Automatic Compilation**: Automatically compiles Java projects if needed
-  **Missing Dependencies Handling**: Gracefully handles projects with missing dependencies

### Design Patterns & Architecture
-  **Strategy Pattern**: Different parsers for different element types
-  **Factory Pattern**: Centralized parser creation
-  **Builder Pattern**: Fluent model construction
-  **Visitor Pattern**: Model traversal and analysis
-  **Facade Pattern**: Simple analysis interface
-  **Singleton Pattern**: UI theme management
-  **Component Pattern**: Reusable UI components

### Code Quality
- **SOLID Principles**: All five principles applied
-  **Consistent Design System**: Unified UI theme across all components
-  **Error Handling**: Robust error handling for missing dependencies
-  **Clean Architecture**: Well-organized package structure

## Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **Maven 3.6+** (for building)
- **Compiled Java Project** (with `.class` files in `bin/` directory)


## Installation

### Using Maven

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd UML-Diagrams-Generator
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Package the application:**
   ```bash
   mvn clean package
   ```

4. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="org.mql.java.ui.ProjectUploader"
   ```

## Usage

### Running the Application

1. **Start the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="org.mql.java.ui.ProjectUploader"
   ```

2. **Upload a Java Project:**
   - Click "Upload Project" button
   - Select the root directory of your Java project
   - The application will check if the project is compiled
   - If not compiled, you'll be prompted to compile automatically

3. **View Diagrams:**
   - Click "Show Class Diagram" to view the class diagram
   - Click "Show Package Diagram" to view the package diagram
   - Click "Show Console Result" to view text output

4. **Export Diagrams:**
   - Click "Download XML File" to export as XML
   - Click "Download XMI File" to export as XMI 2.1 format

### Project Requirements

Your Java project should have:
- A `bin/` directory containing compiled `.class` files
- Or source files that can be automatically compiled

**Note:** The application requires compiled classes (`.class` files) to analyze. If your project is not compiled, the application can compile it automatically if JDK is available.

## UML Diagram Features

### Class Diagram

- **Elements Displayed:**
  - Classes with fields, methods, and constructors
  - Interfaces with methods
  - Enumerations with values
  - Annotations

- **Relationships:**
  - **Generalization** (Inheritance): `----|>` (Blue, solid line with filled arrow)
  - **Realization** (Implementation): `....|>` (Green, dashed line with open arrow)
  - **Association**: `---->` (Black, solid line with arrow)
  - **Aggregation**: `----o` (Orange, solid line with empty diamond)
  - **Composition**: `----*` (Red, solid line with filled diamond)

- **Multiplicity:**
  - Displayed as `1`, `0..1`, `1..*`, `0..*` on relationship lines
  - Automatically detected from arrays, collections, and optional types

- **Role Names:**
  - Field names displayed as role names on associations

### Package Diagram

- **Package Structure:**
  - Hierarchical display of packages
  - Package contents (classes, interfaces, enums)
  - UML tabbed notation

- **Dependencies:**
  - Dashed arrows showing package dependencies
  - Automatically detected from cross-package class usage

## Design Patterns Used

### 1. Strategy Pattern
Different parsing strategies for classes, interfaces, enums, and annotations.

### 2. Factory Pattern
Centralized creation of appropriate parsers based on element type.

### 3. Builder Pattern
Fluent construction of complex model objects.

### 4. Visitor Pattern
Traversal and analysis of model structures without modifying them.

### 5. Facade Pattern
Simple interface to complex analysis operations.

### 6. Singleton Pattern
Single instance of UI theme for consistent styling.

### 7. Component Pattern
Reusable UI components for consistent design.

## Architecture

The application follows SOLID principles:

- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Proper inheritance hierarchy
- **Interface Segregation**: Focused, specific interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

## Export Formats

### XML Format
Standard XML format containing all project structure, classes, interfaces, and relationships.

### XMI Format
XMI 2.1 format compatible with UML tools like Enterprise Architect, Visual Paradigm, etc.

## Troubleshooting

### Project Not Found
- Ensure your project has a `bin/` directory with compiled `.class` files
- Or allow automatic compilation when prompted

### Missing Dependencies
- The application will skip classes with missing dependencies
- A warning will be displayed in the console
- Other classes will still be analyzed

### Compilation Errors
- Ensure JDK (not just JRE) is installed
- Check that all project dependencies are available
- Review compilation errors in the console

## Building from Source

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn clean package

# Create executable JAR with dependencies
mvn clean package
# JAR will be in: target/uml-diagrams-generator-1.0.0-jar-with-dependencies.jar
```

## Running the JAR

```bash
java -jar target/uml-diagrams-generator-1.0.0-jar-with-dependencies.jar
```

## Development

### Adding New Element Types

1. Create a new strategy implementing `ElementParserStrategy`
2. Register it in `ParserFactory`
3. The system will automatically use it

### Adding New Analysis Operations

1. Create a new visitor implementing `ModelVisitor`
2. Use `DataAnalyzer.accept()` to apply it
3. Extract results from the visitor

### Extending UI Components

1. Use `ComponentFactory` to create components
2. Use `UITheme` for consistent styling
3. Follow existing component patterns

## Technologies Used

- **Java 8+**: Core language
- **Java Reflection API**: Class analysis
- **Java Swing**: User interface
- **Maven**: Build and dependency management
- **JUnit 5**: Testing framework
- **DOM/SAX**: XML processing



## Authors

[El Maalmi Ayoub : elmaalmiayoub@gmail.com]

## Acknowledgments

- UML 2.5 Specification for diagram standards
- Java Reflection API for dynamic analysis

---

**Last Updated:** 2025
