package com.krishagni.catissueplus.core.importer.domain;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	public static ObjectSchema parseSchema(String filePath) {
		XStream parser = getSchemaParser();		
		return (ObjectSchema)parser.fromXML(new File(filePath));
	}
	
	public static void main(String[] args) 
	throws Exception {
		ObjectSchema schema = ObjectSchema.parseSchema("/home/vpawar/Desktop/cpr_schema.xml");
		System.err.println(new ObjectMapper().writeValueAsString(schema));		
	}
	
	private static XStream getSchemaParser() {
		XStream xstream = new XStream(new Dom4JDriver());
		
		xstream.alias("object-schema", ObjectSchema.class);
		
		xstream.alias("record", Record.class);
		xstream.addImplicitCollection(Record.class, "subRecords", "record", Record.class);
		
		xstream.alias("field", Field.class);
		xstream.addImplicitCollection(Record.class, "fields", "field", Field.class);
		
		return xstream;
	}

	public static class Record {
		private String name;
		
		private String attribute;
		
		private String caption;
		
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

		public boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}

		public List<Record> getSubRecords() {
			return subRecords;
		}

		public void setSubRecords(List<Record> subRecords) {
			this.subRecords = subRecords;
		}

		public List<Field> getFields() {
			return fields;
		}

		public void setFields(List<Field> fields) {
			this.fields = fields;
		}
				
	}
	
	public static class Field {
		private String caption;
		
		private String attribute;
		
		private String format;
		
		private boolean multiple;

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

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(boolean multiple) {
			this.multiple = multiple;
		}		
	}
}
