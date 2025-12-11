package org.mql.java.utils;

import java.lang.reflect.Modifier;

import org.mql.java.enumerations.AccessModifier;


public class VisibilityUtils {
	public static  AccessModifier determineVisibility(int mod) {
		if (Modifier.isPublic(mod)) {
			return AccessModifier.PUBLIC;
		} else if (Modifier.isPrivate(mod)) {
			return AccessModifier.PRIVATE;
		} else if (Modifier.isProtected(mod)) {
			return AccessModifier.PROTECTED;
		}
		return AccessModifier.PACKAGE; 
		}
}
