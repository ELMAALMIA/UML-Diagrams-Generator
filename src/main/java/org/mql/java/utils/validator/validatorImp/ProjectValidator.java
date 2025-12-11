package org.mql.java.utils.validator.validatorImp;


import java.io.File;
import java.util.logging.Logger;

import org.mql.java.utils.validator.FileValidator;
import org.mql.java.utils.validator.Validator;

public class ProjectValidator implements Validator<File,Logger>, FileValidator<File,Logger> {


    public ProjectValidator() {

    }

    @Override
    public boolean isValid(File project,Logger logger) {
        if (!doesFileExist(project,logger)) {
            logger.info(project.getAbsolutePath() + " is not a valid project folder.");
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