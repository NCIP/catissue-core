package com.krishagni.catissueplus.core.common.util;

public class Util {
	 public static Long numberToLong(Object number) {
         if (number == null) {
                 return null;
         }

         if (!(number instanceof Number)) {
                 throw new IllegalArgumentException("Input object is not a number");
         }

         return ((Number)number).longValue();
	 }

}
