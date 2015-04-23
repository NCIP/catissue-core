package com.krishagni.catissueplus.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class Utility {
	public static String getDisabledValue(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		
		return value + "_" + getCurrentTimeStamp();
	}

	private static String getCurrentTimeStamp() {
		return new SimpleDateFormat().format(Calendar.getInstance().getTime());
	}
	
	public static Long numberToLong(Object number) {
		if (number == null) {
			return null;
		}

		if (!(number instanceof Number)) {
			throw new IllegalArgumentException("Input object is not a number");
		}

		return ((Number)number).longValue();
	}
	
	public static boolean isEmptyOrSuperset(Set<?> leftOperand, Set<?> rightOperand) {
		if (CollectionUtils.isEmpty(leftOperand)) {
			return true;
		}
		
		return leftOperand.containsAll(rightOperand);		
	}	
	
	public static String appendTimestamp(String name) {
		Calendar cal = Calendar.getInstance();
		name = name + "_" + cal.getTimeInMillis();
		return name;
	}

	public static String getInputStreamDigest(InputStream in) 
	throws IOException {
		return DigestUtils.md5Hex(getInputStreamBytes(in));
	}
	
	public static String getResourceDigest(String resource) 
	throws IOException {
		InputStream in = null;
		try {
			in = getResourceInputStream(resource);
			return getInputStreamDigest(in);
		} finally {
			IOUtils.closeQuietly(in);
		}		
	}
	
	public static byte[] getInputStreamBytes(InputStream in) 
	throws IOException {
		ByteArrayOutputStream bout = null;
		try {
			bout = new ByteArrayOutputStream();
			IOUtils.copy(in, bout);
			return bout.toByteArray();
		} finally {
			IOUtils.closeQuietly(bout);
		}
	}
	
	public static InputStream getResourceInputStream(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);		
	}
}
