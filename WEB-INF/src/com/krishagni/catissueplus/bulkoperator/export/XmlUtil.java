package com.krishagni.catissueplus.bulkoperator.export;

import java.util.Map;
import java.util.Map.Entry;

public class XmlUtil {
	
	/**
	 * Emits <element> 
	 */	
	public static void writeElementStart(StringBuilder xmlBuilder, String element) {
		String elementStart = new StringBuilder()
			.append("<").append(element).append(">").toString();
		write(xmlBuilder, elementStart);
	}

	/**
	 * Emits <element attr1="value1" attr2="value2">
	 */
	public static void writeElementStart(StringBuilder xmlBuilder, String tag, Map<String, String> attrs) {
		
		StringBuilder startTag = new StringBuilder().append("<").append(tag);
		for(Entry<String, String> attr : attrs.entrySet()) {
			startTag.append(" ")
				.append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
		}
		startTag.append(">");
		write(xmlBuilder,startTag.toString());
	}
	
	/**
	 * Emits </element>
	 */
	public static void writeElementEnd(StringBuilder xmlBuilder, String element) {
		String elementStart = new StringBuilder()
			.append("</").append(element).append(">").toString();
		write(xmlBuilder, elementStart);
	}
	

	/**
	 * Emits <element>value</element>
	 */
	public static void writeElement(StringBuilder xmlBuilder, String element, Object value) {
		if(value == null) {
			return;
		}
		
		String elementStr = new StringBuilder()
			.append("<").append(element).append(">")
			.append(value)
			.append("</").append(element).append(">")
			.toString();
		
		write(xmlBuilder, elementStr);
	}

	/**
	 * Emits <element attr1="value1" attr2="value2">value</element>
	 */
	public static void writeElement(StringBuilder xmlBuilder, String element, Object value, Map<String, String> attrs) {
		StringBuilder elementStr = new StringBuilder();
		
		elementStr.append("<").append(element);
		for (Map.Entry<String, String> attr : attrs.entrySet()) {
			elementStr.append(" ")
				.append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
		}

		if (value != null) {
			elementStr.append(">").append(value).append("</").append(element).append(">");
		} else {
			elementStr.append("/>");
		}
		
		write(xmlBuilder, elementStr.toString());
	}
	
	private static void write(StringBuilder xmlBuilder, String value) {
		try {
			xmlBuilder.append(value);
		} catch (Exception e) {
			throw new RuntimeException("Error writing value " + value, e);
		}
	}
}
