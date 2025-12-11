package org.mql.java.utils.validator.validatorImp;

import java.io.File;
import java.util.logging.Logger;

import org.mql.java.utils.validator.FileValidator;
import org.mql.java.utils.validator.Validator;

public class PackageValidator implements Validator<File,Logger>, FileValidator<File,Logger> {


	public PackageValidator() {

	}

	@Override
    public boolean isValid(File folder,Logger logger) {
		if (!doesFileExist(folder,logger)) {
			logger.info(folder.getAbsolutePath() + " is not a valid package.");
			return false;
		}
		return true;

	}

	@Override
	public boolean doesFileExist(File file,Logger logger) {
		if (!file.exists()) {
			logger.info(file.getAbsolutePath() + " does not exist !");
			return false;
		}
		return true;
	}

}
