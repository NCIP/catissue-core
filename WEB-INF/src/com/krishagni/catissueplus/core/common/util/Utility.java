package com.krishagni.catissueplus.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.BufferedInputStream;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.common.PdfUtil;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

public class Utility {
	private static final String key = "0pEN@eSEncRyPtKy";
	
	private static final SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
	
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
		CsvWriter csvWriter = null;
		try {
			if (quotechar) {
				csvWriter = CsvFileWriter.createCsvFileWriter(writer);
			} else {
				csvWriter =  CsvFileWriter.createCsvFileWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
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
	
	public static void writeKeyValuesToCsv(OutputStream out, Map<String, String> keyValues) {
		StringWriter strWriter = new StringWriter();
		CsvWriter csvWriter= null;

		try {
			csvWriter = CsvFileWriter.createCsvFileWriter(strWriter);
			for (Map.Entry<String, String> keyValue : keyValues.entrySet()) {
				csvWriter.writeNext(new String[] {keyValue.getKey(), keyValue.getValue()});
			}
			csvWriter.flush();
			out.write(strWriter.toString().getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(strWriter);
			IOUtils.closeQuietly(csvWriter);
		}
	}
	
	public static long getTimezoneOffset() {
		Calendar cal = Calendar.getInstance();
		return -1 * cal.get(Calendar.ZONE_OFFSET);		
	}

	public static void sendToClient(HttpServletResponse httpResp, String filename, File file) {
		sendToClient(httpResp, filename, file, false);
	}

	public static void sendToClient(HttpServletResponse httpResp, String filename, File file, boolean deleteOnSend) {
		try {
			sendToClient(httpResp, filename, getContentType(file), file);
		} finally {
			if (deleteOnSend && file != null) {
				file.delete();
			}
		}
	}

	public static void sendToClient(HttpServletResponse httpResp, String filename, String contentType, File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			sendToClient(httpResp, filename, contentType, in);
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static void sendToClient(HttpServletResponse httpResp, String filename, String contentType, InputStream in) {
		try {
			if (StringUtils.isNotBlank(contentType)) {
				httpResp.setContentType(contentType);
			}

			if (StringUtils.isNotBlank(filename)) {
				httpResp.setHeader("Content-Disposition", "attachment;filename=" + filename);
			}

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
	
	public static String getDateTimeString(Date date) {
		return new SimpleDateFormat(ConfigUtil.getInstance().getDateTimeFmt()).format(date);
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

	public static <T> boolean isEmptyOrSameAs(Collection<T> collection, T element) {
		int size = collection.size();
		if (size > 1) {
			return false;
		}

		return size == 0 || collection.contains(element);
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
		return map == null || map.isEmpty();
	}
	
	public static Date chopTime(Date date) {
		if (date == null) {
			return null;
		}
		
		return DateUtils.truncate(date, Calendar.DATE);
	}

	public static Date getEndOfDay (Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static char getFieldSeparator() {
		return ConfigUtil.getInstance().getCharSetting("common", "field_separator", CSVWriter.DEFAULT_SEPARATOR); 
	}
	
	public static String encrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedValue = cipher.doFinal(value.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encryptedValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String decrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedValue = Base64.getDecoder().decode(value.getBytes());
			return new String(cipher.doFinal(decodedValue));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isQuoted(String input) {
		if (StringUtils.isBlank(input) || input.length() < 2) {
			return false;
		}

		return (input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"') ||
				(input.charAt(0) == '\'' && input.charAt(input.length() - 1) == '\'');
	}

	public static Map<String, Object> jsonToMap(String json) {
		try {
			if (StringUtils.isBlank(json)) {
				return Collections.emptyMap();
			}

			return new ObjectMapper().readValue(json, new TypeReference<HashMap<String, Object>>(){});
		} catch (Exception e) {
			throw new RuntimeException("Error parsing JSON into Map:\n" + json, e);
		}
	}

	public static String mapToJson(Map<String, Object> map) {
		try {
			if (map == null) {
				map = Collections.emptyMap();
			}

			return new ObjectMapper().writeValueAsString(map);
		} catch (IOException e) {
			throw new RuntimeException("Error on converting Map to JSON", e);
		}
	}

	public static List<String> diff(BaseExtensionEntity obj1, BaseExtensionEntity obj2, List<String> fields) {
		Map<String, Object> map1 = getExtnAttrValues(obj1);
		Map<String, Object> map2 = getExtnAttrValues(obj2);

		return fields.stream().filter(field -> {
			if (!field.startsWith("extensionDetail.attrsMap.")) {
				return !equals(obj1, obj2, field);
			} else {
				field = field.substring("extensionDetail.attrsMap.".length());
				return !equals(map1.get(field), map2.get(field));
			}
		}).collect(Collectors.toList());
	}

	public static List<String> equals(Object obj1, Object obj2, List<String> fields) {
		return fields.stream().filter(field -> !equals(obj1, obj2, field)).collect(Collectors.toList());
	}

	private static Map<String, Object> getExtnAttrValues(BaseExtensionEntity obj) {
		if (obj.getExtension() != null) {
			return obj.getExtension().getAttrValues();
		}

		return Collections.emptyMap();
	}

	private static boolean equals(Object obj1, Object obj2, String fieldName) {
		try {
			Object val1 = PropertyUtils.getProperty(obj1, fieldName);
			Object val2 = PropertyUtils.getProperty(obj2, fieldName);
			return equals(val1, val2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean equals(Object val1, Object val2) {
		if (val1 instanceof Collection || val2 instanceof Collection) {
			return equalCollections((Collection)val1, (Collection)val2);
		} else if (val1 instanceof Map || val2 instanceof Map) {
			return equalMaps((Map)val1, (Map)val2);
		} else if (val1 instanceof Date) {
			return DateUtils.isSameDay((Date)val1, (Date)val2);
		} else if (val1 == val2) {
			return true;
		} else if ((val1 != null && val2 == null) || val1 == null) {
			return false;
		} else if (val1 instanceof BaseEntity) {
			return ((BaseEntity) val1).sameAs(val2);
		} else {
			//
			// if-else is hack for DE date fields in bulk import
			//
			if (val1 instanceof String && val2 instanceof Long) {
				val2 = val2.toString();
			} else if (val1 instanceof Long && val2 instanceof String) {
				val1 = val1.toString();
			}

			return val1.equals(val2);
		}
	}

	private static boolean equalCollections(Collection<?> coll1, Collection<?> coll2) {
		if (coll1 == coll2) {
			return true;
		} else if (coll1 == null) {
			return coll2.isEmpty();
		} else if (coll2 == null) {
			return coll1.isEmpty();
		} else if (coll1.isEmpty() && coll2.isEmpty()) {
			return true;
		} else if (coll1.size() != coll2.size()) {
			return false;
		}

		return containsAll(coll1, coll2);
	}

	private static boolean equalMaps(Map<?, ?> map1, Map<?, ?> map2) {
		if (map1 == map2) {
			return true;
		} else if (map1 == null) {
			return map2.isEmpty();
		} else if (map2 == null) {
			return map1.isEmpty();
		} else if (map1.isEmpty() && map2.isEmpty()) {
			return true;
		} else if (map1.size() != map2.size()) {
			return false;
		}

		for (Map.Entry<?, ?> e : map1.entrySet()) {
			if (!equals(e.getValue(), map2.get(e.getKey()))) {
				return false;
			}
		}

		return true;
	}

	private static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
		for (Object e : coll2) {
			if (!contains(coll1, e)) {
				return false;
			}
		}

		return true;
	}

	private static boolean contains(Collection<?> coll, Object obj) {
		if (obj == null) {
			for (Object e : coll) {
				if (e == null) {
					return true;
				}
			}
		} else {
			for (Object e : coll) {
				if (equals(e, obj)) {
					return true;
				}
			}
		}

		return false;
	}

	public static String detectFileCharset(String file) {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			return detectFileCharset(in);
		} catch (IOException ioe) {
			throw new RuntimeException("Error while detecting character set", ioe);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static String detectFileCharset(InputStream in) {
		try {
			CharsetDetector detector = new CharsetDetector();
			detector.setText(in);

			CharsetMatch match = detector.detect();
			return match != null ? match.getName() : "UTF-8";
		} catch (IOException ioe) {
			throw new RuntimeException("Error detecting character set", ioe);
		}
	}
	
	public static long daysBetween(Date start, Date end) {
		return TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS);
	}
}
