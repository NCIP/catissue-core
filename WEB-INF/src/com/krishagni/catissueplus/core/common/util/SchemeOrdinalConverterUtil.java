package com.krishagni.catissueplus.core.common.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class SchemeOrdinalConverterUtil {
	private static final SchemeOrdinalConverterUtil instance = new SchemeOrdinalConverterUtil();

	private SchemeOrdinalConverterUtil() {

	}

	public static Integer toOrdinal(String scheme, String number) {
		return instance.getConverter(scheme).toOrdinal(number);
	}

	public static String fromOrdinal(String scheme, Integer ordinal) {
		return instance.getConverter(scheme).fromOrdinal(ordinal);
	}

	private SchemeOrdinalConverter getConverter(String scheme) {
		return converters.get(scheme);
	}

	private Map<String, SchemeOrdinalConverter> converters = new HashMap<String, SchemeOrdinalConverter>() {
		private static final long serialVersionUID = -1198152629671796530L;

		{
			put("Numbers", new NumberSchemeOrdinalConverter());
			put("Alphabets Upper Case", new AlphabetSchemeOrdinalConverter(true));
			put("Alphabets Lower Case", new AlphabetSchemeOrdinalConverter(false));
			put("Roman Upper Case", new RomanSchemeOrdinalConverter(true));
			put("Roman Lower Case", new RomanSchemeOrdinalConverter(false));
		}
	};

	private interface SchemeOrdinalConverter {
		public Integer toOrdinal(String number);

		public String fromOrdinal(Integer ordinal);
	}

	private class NumberSchemeOrdinalConverter implements SchemeOrdinalConverter {
		@Override
		public Integer toOrdinal(String number) {
			if (number == null) {
				return null;
			}

			try {
				return Integer.parseInt(number);
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(nfe);
			}
		}

		@Override
		public String fromOrdinal(Integer ordinal) {
			if (ordinal == null || ordinal < 0) {
				throw new IllegalArgumentException("Only positive integers allowed");
			}

			return ordinal.toString();
		}
	}


	private class RomanSchemeOrdinalConverter implements SchemeOrdinalConverter {
		private final Map<String, Integer> romanLiterals = new LinkedHashMap<String, Integer>() {
			private static final long serialVersionUID = 685666506457371647L;

			{
				put("m", 1000);
				put("cm", 900);
				put("d",  500);
				put("cd", 400);
				put("c",  100);
				put("xc",  90);
				put("l",   50);
				put("xl",  40);
				put("x",   10);
				put("ix",   9);
				put("v",    5);
				put("iv",   4);
				put("i",    1);
			}
		};

		private boolean upper;

		public RomanSchemeOrdinalConverter(boolean upper) {
			this.upper = upper;
		}

		@Override
		public Integer toOrdinal(String number) {
			number = number.toLowerCase();

			int result = 0;
			int len = number.length(), idx = len;
			while (idx > 0) {
				--idx;
				if (idx == len - 1) {
					Integer val = romanLiterals.get(number.substring(idx, idx + 1));
					if (val == null) {
						throw new IllegalArgumentException("Invalid roman number: " + number);
					}
					result += val;
				} else {
					Integer current = romanLiterals.get(number.substring(idx, idx + 1));
					Integer ahead = romanLiterals.get(number.substring(idx + 1, idx + 2));
					if (current == null || ahead == null) {
						throw new IllegalArgumentException("Invalid roman number: " + number);
					}

					if (current < ahead) {
						result -= current;
					} else {
						result += current;
					}
				}
			}

			return result;
		}

		@Override
		public String fromOrdinal(Integer ordinal) {
			if (ordinal == null || ordinal <= 0) {
				throw new IllegalArgumentException("Only positive integers allowed");
			}

			StringBuilder result = new StringBuilder();
			int num = ordinal;
			for (Map.Entry<String, Integer> literal : romanLiterals.entrySet()) {
				while (num >= literal.getValue()) {
					result.append(literal.getKey());
					num -= literal.getValue();
				}
			}

			return upper ? result.toString().toUpperCase() : result.toString();
		}

	}

	private class AlphabetSchemeOrdinalConverter implements SchemeOrdinalConverter {
		private final char[] alphabets = {
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};

		private boolean upper;

		public AlphabetSchemeOrdinalConverter(boolean upper) {
			this.upper = upper;
		}

		@Override
		public Integer toOrdinal(String number) {
			number = number.toLowerCase();

			if (!StringUtils.isAlpha(number)) {
				throw new IllegalArgumentException("Invalid alphabet number: " + number);
			}

			int len = number.length();
			int base = 1, result = 0;
			while (len > 0) {
				len--;

				int charAt = number.charAt(len);
				result = result + (charAt - 'a' + 1) * base;
				base *= 26;
			}

			return result;
		}

		@Override
		public String fromOrdinal(Integer ordinal) {
			if (ordinal == null || ordinal <= 0) {
				throw new IllegalArgumentException("Only positive ordinal numbers allowed");
			}

			StringBuilder result = new StringBuilder();
			int num = ordinal;
			while (num > 0) {
				result.insert(0, alphabets[(num - 1) % 26]);
				num = (num - 1) / 26;
			}

			return upper ? result.toString().toUpperCase() : result.toString();
		}
	}
}