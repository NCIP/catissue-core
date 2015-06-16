package com.krishagni.catissueplus.core.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RegexValidator {
	
	private static final Map<String, Pattern> patternMap = new ConcurrentHashMap<String, Pattern>();

	public static boolean validate(String regex, String input) {
		Pattern pattern = patternMap.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex);
			patternMap.put(regex, pattern);
		}
		
		return pattern.matcher(input).matches();
	}
}
