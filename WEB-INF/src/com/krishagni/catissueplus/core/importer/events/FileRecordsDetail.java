package com.krishagni.catissueplus.core.importer.events;

import java.util.List;

import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;

public class FileRecordsDetail {
	private String fileId;

	private List<ObjectSchema.Field> fields;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public List<ObjectSchema.Field> getFields() {
		return fields;
	}

	public void setFields(List<ObjectSchema.Field> fields) {
		this.fields = fields;
	}
}
