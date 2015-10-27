package com.krishagni.catissueplus.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.krishagni.catissueplus.core.common.PdfUtil;

public class Utility {
	public static String getDisabledValue(String value, int maxLength) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		
		if (maxLength < 14) {
			throw new IllegalArgumentException("Max length should be at least 14 characters");
		}
		
		int valueMaxLength = maxLength - 14;
		if (value.length() > valueMaxLength) {
			value = value.substring(0, valueMaxLength);
		}
		
		return value + "_" + Calendar.getInstance().getTimeInMillis();
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
	
	public static String stringListToCsv(Collection<String> elements) {
		return stringListToCsv(elements.toArray(new String[0]), true);
	}
		
	public static String stringListToCsv(Collection<String> elements, boolean quotechar) {
		return stringListToCsv(elements.toArray(new String[0]), quotechar);
	}
	
	public static String stringListToCsv(String[] elements) {
		return stringListToCsv(elements, true);
	}
	
	public static String stringListToCsv(String[] elements, boolean quotechar) {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = null;
		try {
			if (quotechar) {
				csvWriter = new CSVWriter(writer);
			} else {
				csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			}
			csvWriter.writeNext(elements);
			csvWriter.flush();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (Exception e) {					
				}				
			}
		}		
	}
	
	public static long getTimezoneOffset() {
		Calendar cal = Calendar.getInstance();
		return -1 * cal.get(Calendar.ZONE_OFFSET);		
	}
	
	public static void sendToClient(HttpServletResponse httpResp, String fileName, File file) {
		InputStream in = null;
		try {
			String fileType = getContentType(file);
			httpResp.setContentType(fileType);
			httpResp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	
			in = new FileInputStream(file);
			IOUtils.copy(in, httpResp.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static String getContentType(File file) {
		FileNameMap contentTypesMap = URLConnection.getFileNameMap();
		return contentTypesMap.getContentTypeFor(file.getAbsolutePath());
	}
	
	
	public static String getFileText(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			String contentType = getContentType(file);
			return getString(in, contentType);
		} catch (Exception e) {
			throw new RuntimeException("Error getting file text", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static String getString(InputStream in, String contentType) {
		String fileText = null;
		try {
			if (StringUtils.isBlank(contentType) || !contentType.equals("application/pdf")) {
				fileText = IOUtils.toString(in);
			} else {
				fileText = PdfUtil.getText(in);
			}
			return fileText;
		} catch (IOException e) {
			throw new RuntimeException("Error getting file text", e);
		}	
	}	
	
	public static String getDateString(Date date) {
		return new SimpleDateFormat(ConfigUtil.getInstance().getDeDateFmt()).format(date);
	}

	public static Date chopSeconds (Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T collect(Collection<?> collection, String propertyName, boolean returnSet) {
		Collection<?> result = CollectionUtils.collect(collection, new BeanToPropertyValueTransformer(propertyName));
		if (returnSet) {
			return (T) new HashSet(result);
		}
		
		return (T) result;
	}
	
	public static <T> T collect(Collection<?> collection, String propertyName) {
		return collect(collection, propertyName, false);
    }

	public static Integer getAge(Date birthDate) {
		if (birthDate == null) {
			return null;
		}
		
		Calendar currentDate = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(birthDate);

		int currentYear = currentDate.get(Calendar.YEAR);
		int birthYear = dob.get(Calendar.YEAR);
		int age = currentYear - birthYear;
		
		int currentMonth = currentDate.get(Calendar.MONTH);
		int birthMonth = dob.get(Calendar.MONTH);
		if (currentMonth < birthMonth  ||
				(currentMonth == birthMonth && currentDate.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))) {
		  age--;
		}
		
		return age;
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null) {
			return true;
		}
		
		return map.isEmpty();
	}

}
