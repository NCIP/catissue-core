package com.krishagni.catissueplus.core.importer.domain;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

public class ObjectSchema {	
	private String name;
	
	private Record record;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
	
	public List<Record> getExtensionRecord() {
		return getExtensionRecord(record);
	}
	
	public String getKeyColumnName() {
		for (Field field : record.getFields()) {
			if (field.isKey()) {
				return field.getCaption();
			}
		}
		
		return null;
	}
	
	public static ObjectSchema parseSchema(String filePath) {
		XStream parser = getSchemaParser();		
		return (ObjectSchema)parser.fromXML(new File(filePath));
	}
	
	public static ObjectSchema parseSchema(InputStream in) {
		XStream parser = getSchemaParser();
		return (ObjectSchema)parser.fromXML(in);
	}
	
	private static XStream getSchemaParser() {
		XStream xstream = new XStream(new Dom4JDriver());
		
		xstream.alias("object-schema", ObjectSchema.class);
		
		xstream.alias("record", Record.class);
		xstream.aliasAttribute(Record.class, "type", "type");
		xstream.aliasAttribute(Record.class, "entityType", "entityType");
		xstream.addImplicitCollection(Record.class, "subRecords", "record", Record.class);
		
		xstream.alias("field", Field.class);
		xstream.addImplicitCollection(Record.class, "fields", "field", Field.class);
		
		return xstream;
	}
	
	private List<Record> getExtensionRecord(Record record) {
		List<Record> extnRecords = new ArrayList<Record>();
		for (Record subRecord : record.getSubRecords()) {
			if (subRecord.getType() != null && subRecord.getType().equals("extensions")) {
				extnRecords.add(subRecord);
			}
			
			extnRecords.addAll(getExtensionRecord(subRecord));
		}
		
		return extnRecords;
	}

	public static class Record {
		private String name;
		
		private String attribute;
		
		private String caption;
		
		private String type;
		
		private String entityType;
		
		private boolean multiple;
		
		private List<Record> subRecords;
		
		private List<Field> fields;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getEntityType() {
			return entityType;
		}

		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}

		public boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}

		public List<Record> getSubRecords() {
			return subRecords == null ? Collections.<Record>emptyList() : subRecords;
		}

		public void setSubRecords(List<Record> subRecords) {
			this.subRecords = subRecords;
		}

		public List<Field> getFields() {
			return fields == null ? Collections.<Field>emptyList() : fields;
		}

		public void setFields(List<Field> fields) {
			this.fields = fields;
		}
				
	}
	
	public static class Field {
		private String caption;
		
		private String attribute;
		
		private String type;
		
		private boolean multiple;
		
		private boolean key;

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}

		public boolean isKey() {
			return key;
		}

		public void setKey(boolean key) {
			this.key = key;
		}
	}
}
