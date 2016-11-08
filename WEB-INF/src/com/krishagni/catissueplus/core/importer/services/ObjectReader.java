package com.krishagni.catissueplus.core.importer.services;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.CsvException;
import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.catissueplus.core.common.util.CsvReader;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.importer.domain.ImportJobErrorCode;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Field;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Record;

public class ObjectReader implements Closeable {
	private static final Log logger = LogFactory.getLog(ObjectReader.class);

	private static final String SET_TO_BLANK = "##set_to_blank##";
	
	private CsvReader csvReader;
	
	private ObjectSchema schema;
	
	private Class<?> objectClass;
	
	private String[] currentRow;
	
	private String dateFmt;
	
	private String timeFmt;
	
	private List<Integer> keyColumnIndices = new ArrayList<Integer>();

	public ObjectReader(String filePath, ObjectSchema schema, String dateFmt, String timeFmt) {
		try {
			this.csvReader = CsvFileReader.createCsvFileReader(filePath, true);
			this.schema = schema;
			if (StringUtils.isNotBlank(schema.getRecord().getName())) {
				this.objectClass = Class.forName(schema.getRecord().getName());
			}

			this.dateFmt = dateFmt;
			this.timeFmt = timeFmt;

			keyColumnIndices = schema.getKeyColumnNames().stream()
				.map(columnName -> getCsvColumnNames().indexOf(columnName))
				.collect(Collectors.toList());
		} catch (CsvException csvEx) {
			throw csvEx;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object next() {
		if (csvReader.next()) {
			currentRow = csvReader.getRow();
			return parseObject();
		} else {
			currentRow = null;
			return null;
		}
	}
	
	public List<String> getCsvColumnNames() {
		return new ArrayList<String>(Arrays.asList(csvReader.getColumnNames()));
	}
	
	public List<String> getCsvRow() {
		return new ArrayList<String>(Arrays.asList(currentRow));
	}
	
	public String getRowKey() {
		return keyColumnIndices.stream()
			.map(index -> currentRow[index])
			.collect(Collectors.joining("_"));
	}
	
	@Override
	public void close() throws IOException {
		csvReader.close();
	}
	
	public static String getSchemaFields(ObjectSchema schema) {
		List<String> columnNames = getSchemaFields(schema.getRecord(), "");
		return Utility.stringListToCsv(columnNames);
	}
			
	private Object parseObject() {
		try {
			Map<String, Object> objectProps = parseObject(schema.getRecord(), "");
			if (objectClass == null) {
				return objectProps;
			} else {
				if (schema.isFlattened()) {
					objectProps = inflateObjProps(objectProps);
				}

				return new ObjectMapper().convertValue(objectProps, objectClass);
			}			
		} catch (Exception e) {
			logger.error("Error parsing record CSV", e);
			throw OpenSpecimenException.userError(ImportJobErrorCode.RECORD_PARSE_ERROR, e.getLocalizedMessage());
		}
	}
	
	private Map<String, Object> parseObject(Record record, String prefix)
	throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		props.putAll(parseFields(record, prefix));
		
		if (record.getSubRecords() == null) {
			return props;
		}
		
		for (Record subRec : record.getSubRecords()) {
			Object subObjProps = parseSubObjects(subRec, prefix);
			if (subObjProps instanceof List<?>) {
				List<Map<String, Object>> subObjs = (List<Map<String, Object>>)subObjProps;
				if (!subObjs.isEmpty()) {
					props.put(subRec.getAttribute(), removeEmptyObjs(subObjs));
				}
			} else if (subObjProps instanceof Map<?, ?>) {
				Map<String, Object> subObj = (Map<String, Object>)subObjProps;
				if (!subObj.isEmpty()) {
					props.put(subRec.getAttribute(), nullOrObj(subObj));
				}
			}
		}
		
		return props;
	}
	
	private Map<String, Object> parseFields(Record record, String prefix)
	throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		
		for (Field field : record.getFields()) {
			if (field.isMultiple()) {
				List<Object> values = new ArrayList<Object>();
				boolean setToBlank = false;
				for (int idx = 1; true; ++idx) {
					String columnName = prefix + field.getCaption() + "#" + idx;
					Object value = getValue(field, columnName);
					if (isSetToBlankField(value)) {
						values.clear();
						setToBlank = true;
						break;
					} else if (value == null) {
						break;
					}
					
					values.add(value);
				}
				
				if (setToBlank) {
					props.put(field.getAttribute(), Collections.emptyList());
				} else if (!values.isEmpty()) {
					props.put(field.getAttribute(), values);
				}				
			} else {
				String columnName = prefix + field.getCaption();
				Object value = getValue(field, columnName);
				if (value != null) {
					props.put(field.getAttribute(), isSetToBlankField(value) ? null : value);
				}				
			}
		}
		
		return props;		
	}
	
	private Object parseSubObjects(Record record, String prefix) 
	throws Exception {		
		String newPrefix = prefix;
		if (StringUtils.isNotBlank(record.getCaption())) {
			newPrefix += record.getCaption() + "#";
		}
		
		Object result = null;
		if (record.isMultiple()) {
			List<Map<String, Object>> subObjects = new ArrayList<Map<String, Object>>();
			for (int idx = 1; true; ++idx) {
				Map<String, Object> subObject = parseObject(record, newPrefix + idx + "#");
				if (subObject.isEmpty()) {
					break;
				}
				
				subObjects.add(subObject);
			}			
			result = subObjects;
		} else {
			result = parseObject(record, newPrefix);
		}	
		
		return result;
	}
	
	private Object getValue(Field field, String columnName) 
	throws Exception {
		if (!csvReader.isColumnPresent(columnName)) {
			return null;
		}
		
		String value = csvReader.getColumn(columnName);
		if (StringUtils.isBlank(value)) {
			return null;
		} else if (value.trim().equals(SET_TO_BLANK)) {
			return SET_TO_BLANK;
		} else if (field.getType() != null && field.getType().equals("date")) {
			return parseDate(value);
		} else if (field.getType() != null && field.getType().equals("datetime")) {
			return parseDateTime(value);
		} else if (field.getType() != null && field.getType().equals("boolean")) {
			return (value.equalsIgnoreCase(MessageUtil.getInstance().getBooleanMsg(true)) || value.equalsIgnoreCase("true"))  ? true :
					(value.equalsIgnoreCase(MessageUtil.getInstance().getBooleanMsg(false)) || value.equalsIgnoreCase("false")) ? false : value;
		} else {
			return value;
		}
	}
		
	private List<Map<String, Object>> removeEmptyObjs(List<Map<String, Object>> objs) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> obj : objs) {
			if (nullOrObj(obj) != null) {
				result.add(obj);
			}
		}
		
		return result;
	}
	
	private Map<String, Object> nullOrObj(Map<String, Object> obj) {
		for (Map.Entry<String, Object> prop : obj.entrySet()) {
			if (prop.getValue() != null) {
				return obj;
			}
		}
		
		return null;
	}

	private Map<String, Object> inflateObjProps(Map<String, Object> input) {
		Map<String, Map<String, Object>> inflatedFields = new HashMap<>();
		Map<String, Object> result = new HashMap<>();

		for (Map.Entry<String, Object> entry : input.entrySet()) {
			String[] fieldParts = entry.getKey().split("\\.", 2);
			if (fieldParts.length == 1) {
				result.put(fieldParts[0], entry.getValue());
			} else {
				Map<String, Object> fields = inflatedFields.get(fieldParts[0]);
				if (fields == null) {
					fields = new HashMap<>();
					inflatedFields.put(fieldParts[0], fields);
				}

				fields.put(fieldParts[1], entry.getValue());
			}
		}

		for (Map.Entry<String, Map<String, Object>> inflatedField : inflatedFields.entrySet()) {
			result.put(inflatedField.getKey(), inflateObjProps(inflatedField.getValue()));
		}

		return result;
	}
		
	private static List<String> getSchemaFields(Record record, String prefix) {
		List<String> columnNames = new ArrayList<String>();
		
		for (Field field : record.getFields()) {
			String columnName = prefix + field.getCaption();
			if (field.isMultiple()) {
				columnNames.add(columnName + "#1");
				columnNames.add(columnName + "#2");
			} else {
				columnNames.add(columnName);
			}
		}
		
		if (record.getSubRecords() == null) {
			return columnNames;
		}
		
		for (Record subRecord : record.getSubRecords()) {
			String newPrefix = prefix;
			if (StringUtils.isNotBlank(subRecord.getCaption())) {
				newPrefix += subRecord.getCaption() + "#";
			}
			
			if (subRecord.isMultiple()) {
				columnNames.addAll(getSchemaFields(subRecord, newPrefix + "1#"));
				columnNames.addAll(getSchemaFields(subRecord, newPrefix + "2#"));
			} else {
				columnNames.addAll(getSchemaFields(subRecord, newPrefix));
			}			
		}
		
		return columnNames;				
	}
	
	private boolean isSetToBlankField(Object value) {
		return value instanceof String && ((String)value).trim().equals(SET_TO_BLANK);
	}
	
	private Date parseDateTime(String value) 
	throws ParseException {
		try {
			return parseDate(value, dateFmt + " " + timeFmt);
		} catch (ParseException e) {
			return parseDate(value, dateFmt);
		}		
	}
	
	private Date parseDate(String value) 
	throws ParseException {
		return parseDate(value, dateFmt);
	}
	
	private Date parseDate(String value, String fmt) 
	throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		sdf.setLenient(false);
		return sdf.parse(value);		
	}
			
	public static void main(String[] args) 
	throws Exception {
		ObjectSchema containerSchema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/container.xml");
		System.err.println("Container: " + ObjectReader.getSchemaFields(containerSchema));
		
		ObjectSchema instituteSchema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/institute.xml");
		System.err.println("Institute: " + ObjectReader.getSchemaFields(instituteSchema));
		
		ObjectSchema siteSchema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/site.xml");
		System.err.println("Site: " + ObjectReader.getSchemaFields(siteSchema));
		
		ObjectSchema userSchema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/user.xml");
		System.err.println("User: " + ObjectReader.getSchemaFields(userSchema));

		ObjectSchema userRolesSchema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/user-roles.xml");
		System.err.println("User Roles: " + ObjectReader.getSchemaFields(userRolesSchema));
		
	}
}