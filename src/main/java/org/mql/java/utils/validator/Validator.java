package org.mql.java.utils.validator;

import java.util.logging.Logger;

public interface Validator<T,U> {
    boolean isValid(T file,U logger);
}