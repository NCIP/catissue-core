package com.krishagni.catissueplus.core.de.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Field;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Record;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaBuilder;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class ExtensionSchemaBuilder implements ObjectSchemaBuilder {
	private FormDao formDao;
	
	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	@Override
	@PlusTransactional	
	public ObjectSchema getObjectSchema(Map<String, Object> params) {
		String formName = (String)params.get("formName");
		if (StringUtils.isBlank(formName)) {
			throw OpenSpecimenException.userError(FormErrorCode.NAME_REQUIRED);
		}
		
		String entityType = (String)params.get("entityType");
		if (StringUtils.isBlank(entityType)) {
			throw OpenSpecimenException.userError(FormErrorCode.ENTITY_TYPE_REQUIRED);
		}
		
		Container form = Container.getContainer(formName);
		if (form == null) {
			throw OpenSpecimenException.userError(FormErrorCode.NOT_FOUND);
		}
		
		return getObjectSchema(form, entityType);
	}
	
	@PlusTransactional
	public ObjectSchema getObjectSchema(String entityType) {
		List<Long> ids = formDao.getFormIds(-1L, entityType);
		if (ids.isEmpty()) {
			return null;
		}
		
		Container form = Container.getContainer(ids.get(0));
		Record attrs = getFormRecord(form, false);
		attrs.setAttribute("attrsMap");
		
		Record record = new Record();
		record.setCaption(form.getCaption());
		record.setSubRecords(Collections.singletonList(attrs));
		
		ObjectSchema objectSchema = new ObjectSchema();
		objectSchema.setRecord(record);
		return objectSchema;
	}
	
	private  ObjectSchema getObjectSchema(Container form, String entityType) {
		Record record = new Record();  
		
		List<Field> fields = new ArrayList<Field>();
		fields.add(getField("recordId", "Record ID"));
		
		if (entityType.equals("Participant")) {
			fields.add(getField("cpShortTitle", "Collection Protocol"));
			fields.add(getField("ppid", "PPID"));
		} else if (entityType.equals("SpecimenCollectionGroup")) {
			fields.add(getField("visitName", "Visit Name"));
		} else if (entityType.equals("Specimen") || entityType.equals("SpecimenEvent")) {
			fields.add(getField("specimenLabel", "Specimen Label"));
		}
		
		record.setFields(fields);
		
		Record formValueMap = getFormRecord(form);
		formValueMap.setAttribute("formValueMap");
		record.setSubRecords(Collections.singletonList(formValueMap));
		
		ObjectSchema objectSchema = new ObjectSchema();
		objectSchema.setRecord(record);
		return objectSchema;
	}
	
	private Record getFormRecord(Container form) {
		return getFormRecord(form, true);
	}
	
	private Record getFormRecord(Container form, boolean useUdn) {
		List<Field> fields = new ArrayList<Field>();
		List<Record> subRecords = new ArrayList<Record>();
		
		for (Control ctrl : form.getOrderedControlList()) {
			if (ctrl instanceof SubFormControl) {
				subRecords.add(getSubRecord((SubFormControl)ctrl, useUdn));
			} else {
				fields.add(getField(ctrl, useUdn));
			}
		}
		
		Record record = new Record();
		record.setFields(fields);
		record.setSubRecords(subRecords);
		record.setName(form.getName());
		return record;
	}
	
	
	private Field getField(String attr, String caption) {
		return getField(attr, caption, false);		
	}
	
	private Field getField(Control ctrl, boolean useUdn) {
		return getField(
				useUdn ? ctrl.getUserDefinedName() : ctrl.getName() ,
				ctrl.getCaption(),
				ctrl instanceof MultiSelectControl);
	}
	
	private Record getSubRecord(SubFormControl ctrl, boolean useUdn) {
		Record subRec = getFormRecord(ctrl.getSubContainer(), useUdn);
		subRec.setAttribute(useUdn ? ctrl.getUserDefinedName() : ctrl.getName());
		subRec.setCaption(ctrl.getCaption());
		subRec.setMultiple(true);
		return subRec;		
	}
	
	private Field getField(String attr, String caption, boolean multiple) {
		Field field = new Field();
		field.setAttribute(attr);
		field.setCaption(caption);
		field.setMultiple(multiple);
		return field;
	}	
}
