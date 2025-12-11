
package org.mql.java.utils;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import org.mql.java.utils.validator.FileValidator;
import org.mql.java.utils.validator.Validator;

public class FilesUtils implements FileValidator<File, Logger> {
	public static final Logger logger = Logger.getLogger(FilesUtils.class.getName());
	private final Validator<File, Logger> projectValidator;
	private final Validator<File, Logger> packageValidator;
	private final Validator<File, Logger> classValidator;
	public FilesUtils() {
		this.projectValidator = null;
		this.packageValidator = null;
		this.classValidator = null;
		
	}

	public FilesUtils(Validator<File, Logger> projectValidator, Validator<File, Logger> packageValidator,
			Validator<File, Logger> classValidator) {
		this.projectValidator = projectValidator;
		this.packageValidator = packageValidator;
		this.classValidator = classValidator;
	}

	
	public boolean isAValidProject(File folder) {
		return projectValidator.isValid(folder, logger);
	}
	
	// a couriger dans le teste

//	public void getAllPackages(File folder, List<File> packages) {
//		if (doesFileExist(folder,logger)) {
//			for (File subFolder : folder.listFiles()) {
//				if (isAValidPackage(subFolder)) {
//					packages.add(subFolder);
//					getAllPackages(subFolder, packages);
//				}
//				if (!subFolder.isFile()) {
//					getAllPackages(subFolder, packages);
//				}
//			}
//		}
//	}

	public void getAllPackages(File folder, List<File> packages) {
	    if (folder != null && folder.exists() && folder.isDirectory()) {
	        File[] files = folder.listFiles();
	        if (files != null) {
	            for (File subFolder : files) {
	                if (isAValidPackage(subFolder)) {
	                    packages.add(subFolder);
	                    getAllPackages(subFolder, packages);
	                }
	                if (!subFolder.isFile()) {
	                    getAllPackages(subFolder, packages);
	                }
	            }
	        }
	    }
	}

	public boolean isAValidPackage(File folder) {
		return packageValidator.isValid(folder, logger);
	}

	public boolean isAValidClassFile(File file) {
		return classValidator.isValid(file, logger);
	}

	@Override
	public boolean doesFileExist(File file, Logger logger) {
		if (!file.exists()) {
			logger.info(file.getAbsolutePath() + " does not exist !");
			return false;
		}
		return true;
	}


}
