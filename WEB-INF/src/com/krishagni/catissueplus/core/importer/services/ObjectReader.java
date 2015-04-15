package com.krishagni.catissueplus.core.importer.services;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.catissueplus.core.common.util.CsvReader;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Field;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Record;

public class ObjectReader implements Closeable {
	private CsvReader csvReader;
	
	private ObjectSchema schema;
	
	private Class<?> objectClass;
	
	private String[] currentRow;
	
	public ObjectReader(String filePath, ObjectSchema schema) {
		try {
			this.csvReader = CsvFileReader.createCsvFileReader(filePath, true);
			this.schema = schema;
			this.objectClass = Class.forName(schema.getRecord().getName());			
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
	
	@Override
	public void close() throws IOException {
		csvReader.close();
	}
		
	private Object parseObject() {
		try {
			Map<String, Object> objectProps = parseObject(schema.getRecord(), "");
			return new ObjectMapper().convertValue(objectProps, objectClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
			props.put(subRec.getAttribute(), parseSubObjects(subRec, prefix));
		}
		
		return props;
	}
	
	private Map<String, Object> parseFields(Record record, String prefix) 
	throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();
		
		for (Field field : record.getFields()) {
			if (field.isMultiple()) {
				List<Object> values = new ArrayList<Object>();
				for (int idx = 1; true; ++idx) {
					String columnName = prefix + field.getCaption() + "#" + idx;
					Object value = getValue(field, columnName);
					if (value == null) {
						break;
					}
					
					values.add(value);
				}
				
				if (!values.isEmpty()) {
					props.put(field.getAttribute(), values);
				}				
			} else {
				String columnName = prefix + field.getCaption();
				Object value = getValue(field, columnName);
				if (value != null) {
					props.put(field.getAttribute(), value);
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
		boolean isBlank = StringUtils.isBlank(value);
		if (isBlank) {
			return null;
		} else if (field.getFormat() != null) {
			return new SimpleDateFormat(field.getFormat()).parse(value);
		} else {
			return value;
		}
	}
		
	public static void main(String[] args) 
	throws Exception {
		ObjectSchema schema = ObjectSchema.parseSchema("/home/vpawar/work/ka/catp/os/WEB-INF/resources/com/krishagni/catissueplus/core/administrative/schema/institute.xml");
		ObjectReader reader = new ObjectReader("/home/vpawar/Desktop/institute.csv", schema);
		
		Object obj = null;
		while ((obj = reader.next()) != null) {
			System.err.println(new ObjectMapper().writeValueAsString(obj));
			System.err.println("---");
		}
		
		IOUtils.closeQuietly(reader);
		System.err.println("--Done--");
	}
}