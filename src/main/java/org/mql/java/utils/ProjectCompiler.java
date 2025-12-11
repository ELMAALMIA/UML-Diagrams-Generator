package org.mql.java.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ProjectCompiler {
	private static final Logger logger = Logger.getLogger(ProjectCompiler.class.getName());

	/**
	 * Compiles a Java project from source files
	 * @param projectPath The root path of the Java project
	 * @return true if compilation was successful, false otherwise
	 */
	public static boolean compileProject(String projectPath) {
		File projectDir = new File(projectPath);
		if (!projectDir.exists() || !projectDir.isDirectory()) {
			logger.warning("Project directory does not exist: " + projectPath);
			return false;
		}

		// Find source directory (src, src/main/java, or just root)
		File srcDir = findSourceDirectory(projectDir);
		if (srcDir == null) {
			logger.warning("No source directory found in: " + projectPath);
			return false;
		}

		// Create bin directory if it doesn't exist
		File binDir = new File(projectPath, "bin");
		if (!binDir.exists()) {
			binDir.mkdirs();
		}

		// Collect all Java source files
		List<File> javaFiles = collectJavaFiles(srcDir);
		if (javaFiles.isEmpty()) {
			logger.warning("No Java source files found in: " + srcDir.getAbsolutePath());
			return false;
		}

		logger.info("Found " + javaFiles.size() + " Java source files to compile");

		// Compile using Java Compiler API
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			logger.severe("Java compiler not available. Make sure you're running with JDK (not JRE).");
			return false;
		}

		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			Iterable<? extends javax.tools.JavaFileObject> compilationUnits = 
					fileManager.getJavaFileObjectsFromFiles(javaFiles);

			List<String> options = new ArrayList<>();
			options.add("-d");
			options.add(binDir.getAbsolutePath());
			options.add("-encoding");
			options.add("UTF-8");
			
			// Add classpath if needed
			String classpath = System.getProperty("java.class.path");
			if (classpath != null && !classpath.isEmpty()) {
				options.add("-cp");
				options.add(classpath);
			}

			javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(
					null, fileManager, null, options, null, compilationUnits);

			boolean success = task.call();
			
			if (success) {
				logger.info("Project compiled successfully to: " + binDir.getAbsolutePath());
			} else {
				logger.warning("Compilation failed. Check for errors in source files.");
			}
			
			return success;
		} catch (IOException e) {
			logger.severe("Error during compilation: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Finds the source directory in a project
	 */
	private static File findSourceDirectory(File projectDir) {
		// Common source directory patterns
		String[] possiblePaths = {
			"src/main/java",
			"src",
			"source",
			"Source"
		};

		for (String path : possiblePaths) {
			File srcDir = new File(projectDir, path);
			if (srcDir.exists() && srcDir.isDirectory() && containsJavaFiles(srcDir)) {
				return srcDir;
			}
		}

		// If no standard directory found, check if root contains Java files
		if (containsJavaFiles(projectDir)) {
			return projectDir;
		}

		return null;
	}

	/**
	 * Checks if a directory contains Java source files
	 */
	private static boolean containsJavaFiles(File dir) {
		try (Stream<Path> paths = Files.walk(Paths.get(dir.getAbsolutePath()))) {
			return paths.anyMatch(path -> path.toString().endsWith(".java"));
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Recursively collects all Java source files
	 */
	private static List<File> collectJavaFiles(File directory) {
		List<File> javaFiles = new ArrayList<>();
		collectJavaFilesRecursive(directory, javaFiles);
		return javaFiles;
	}

	/**
	 * Recursively collects Java files from a directory
	 */
	private static void collectJavaFilesRecursive(File directory, List<File> javaFiles) {
		File[] files = directory.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				// Skip common non-source directories
				String name = file.getName().toLowerCase();
				if (!name.equals("bin") && !name.equals("target") && !name.equals("build") 
						&& !name.equals(".git") && !name.equals(".svn")) {
					collectJavaFilesRecursive(file, javaFiles);
				}
			} else if (file.getName().endsWith(".java")) {
				javaFiles.add(file);
			}
		}
	}

	/**
	 * Checks if a project needs compilation (has src but no bin or bin is empty)
	 */
	public static boolean needsCompilation(String projectPath) {
		File projectDir = new File(projectPath);
		File binDir = new File(projectPath, "bin");
		
		// If bin doesn't exist or is empty, check if src exists
		if (!binDir.exists() || (binDir.exists() && binDir.listFiles() == null) 
				|| (binDir.exists() && binDir.listFiles().length == 0)) {
			File srcDir = findSourceDirectory(projectDir);
			return srcDir != null && containsJavaFiles(srcDir);
		}
		
		return false;
	}
}

