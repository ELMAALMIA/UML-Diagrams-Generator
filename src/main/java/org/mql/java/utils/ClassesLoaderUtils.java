package org.mql.java.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ClassesLoaderUtils {
	private static final Logger logger = Logger.getLogger(ClassesLoaderUtils.class.getName());

	public static Class<?> forName(String projectPath, String className) {
		if (projectPath == null || className == null) {
			logger.warning("Invalid parameters: projectPath or className is null");
			return null;
		}

		try {
			Path binPath = Paths.get(projectPath, "bin");
			if (!binPath.toFile().exists()) {
				logger.warning("Bin directory does not exist: " + binPath);
				return null;
			}

			try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { binPath.toUri().toURL() })) {
				Class<?> loadedClass = urlClassLoader.loadClass(className);
				// Try to verify the class can be used (check for missing dependencies)
				try {
					loadedClass.getSimpleName();
				} catch (NoClassDefFoundError e) {
					logger.warning("Class " + className + " has missing dependencies: " + e.getMessage());
					return null;
				}
				return loadedClass;
			}
		} catch (ClassNotFoundException e) {
			logger.fine("Class not found: " + className);
			return null;
		} catch (NoClassDefFoundError e) {
			logger.warning("Class " + className + " has missing dependencies: " + e.getMessage());
			return null;
		} catch (Exception e) {
			logger.warning("Error loading class " + className + ": " + e.getMessage());
			return null;
		}
	}
}
