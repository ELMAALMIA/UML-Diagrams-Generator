package org.mql.java.utils.validator;



public interface FileValidator<T,U> {
    boolean doesFileExist(T file,U logger);
}
