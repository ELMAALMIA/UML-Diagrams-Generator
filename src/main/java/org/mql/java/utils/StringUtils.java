package org.mql.java.utils;

import java.io.File;

public class StringUtils {

    public static String getSimpleName(String s) {
        int s1 = s.lastIndexOf('.');
        if (s1 >= 0) {
            return s.substring(s1 + 1);
        }
        return s;
    }
    public static String extractProjectName(String path) {

        String[] pathArray = path.split("/");

       
        if (pathArray.length > 0) {
            return pathArray[pathArray.length - 1];
        } else {
            return "";
        }
    }
}

