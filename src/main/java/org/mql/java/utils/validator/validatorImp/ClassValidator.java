package org.mql.java.utils.validator.validatorImp;

import java.io.File;
import java.util.logging.Logger;

import org.mql.java.utils.validator.FileValidator;
import org.mql.java.utils.validator.Validator;

public class ClassValidator implements Validator<File,Logger>, FileValidator<File,Logger> {


    public ClassValidator() {

    }

    @Override
    public boolean isValid(File file,Logger logger) {
        if (!doesFileExist(file,logger)) {
            logger.info(file.getAbsolutePath() + " is not a valid class file.");
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