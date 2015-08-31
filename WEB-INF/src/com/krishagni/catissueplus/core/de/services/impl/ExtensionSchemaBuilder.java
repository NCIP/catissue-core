package com.krishagni.catissueplus.core.de.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Field;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema.Record;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaBuilder;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class ExtensionSchemaBuilder implements ObjectSchemaBuilder {

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
	
	private ObjectSchema getObjectSchema(Container form, String entityType) {
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
		List<Field> fields = new ArrayList<Field>();
		List<Record> subRecords = new ArrayList<Record>();
		
		Map<Integer, List<Control>> controlsMap = new TreeMap<Integer, List<Control>>();
		for(Control ctrl: form.getControls()) {
			int sequence = ctrl.getSequenceNumber();
			List<Control> controls = controlsMap.get(sequence);
			if (controls == null) {
				controls = new ArrayList<Control>();
				controlsMap.put(sequence, controls);
			}
			controls.add(ctrl);
		}

		for (Map.Entry<Integer, List<Control>> entry : controlsMap.entrySet()) {
			for (Control ctrl: entry.getValue()) {
				if (ctrl instanceof SubFormControl) {
					subRecords.add(getSubRecord((SubFormControl)ctrl));
				} else {
					fields.add(getField(ctrl));
				}
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
	
	private Field getField(Control ctrl) {
		return getField(
				ctrl.getUserDefinedName(),
				ctrl.getCaption(),
				ctrl instanceof MultiSelectControl);
	}
	
	private Record getSubRecord(SubFormControl ctrl) {
		Record subRec = getFormRecord(ctrl.getSubContainer());
		subRec.setAttribute(ctrl.getUserDefinedName());
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
