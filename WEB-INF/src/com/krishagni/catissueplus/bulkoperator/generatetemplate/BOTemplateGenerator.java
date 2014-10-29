package com.krishagni.catissueplus.bulkoperator.generatetemplate;

import edu.common.dynamicextensions.domain.nui.*;
import com.krishagni.catissueplus.bulkoperator.export.BulkOperationSerializer;
import com.krishagni.catissueplus.bulkoperator.metadata.Attribute;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetaData;
import com.krishagni.catissueplus.bulkoperator.metadata.HookingInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BOTemplateGenerator {

	private String operationName;
	
	private Container container;
	
	private BulkOperationMetaData metaData = new BulkOperationMetaData();

	public static enum EntityType {
		PARTICIPANT, SCG, SPECIMEN
	}
	Map<String, String> customFields = new HashMap<String, String>();
    
	private List<String> csvColumnNames = new ArrayList<String>();

	
	public BOTemplateGenerator(String operationName, Container container, Map<String, String> customFields) {
		this.operationName = operationName;
		this.container = container;
		this.customFields = customFields;
	}

	
	public BulkOperationMetaData generate() throws IOException {
		BulkOperationClass boClass  = getBoClass(container, container.getName());
		boClass.setHookingInformation(getCustomFields());
        boClass.setBatchSize(10000);
        boClass.setTemplateName(operationName);
		metaData.addBulkOperationClass(boClass);
		
		return metaData;
	}
	
	public String getTemplateXml() throws IOException {
		BulkOperationSerializer serializer = new BulkOperationSerializer(metaData);
		return(serializer.serialize());
	}

	public String getTemplateCsv() throws IOException {
		StringBuilder csvHeader = new StringBuilder();
		
		for (String colName : csvColumnNames) {
			csvHeader.append(colName).append(",");
		}
		if (csvHeader.length() > 0) {
			csvHeader.delete(csvHeader.length() - 1, csvHeader.length());
		}
		
		return csvHeader.toString();
	}
	
	private BulkOperationClass getBoClass(Container c, String className) {
		BulkOperationClass boClass = new BulkOperationClass();
		boClass.setClassName(className);

		for (Control ctrl : c.getControls()) {
			if (ctrl instanceof Label) {
				continue;
			}
			if (ctrl instanceof SubFormControl) {
				// TODO :: need to handle mainForm->subForm->hierarchy 
				Container sfContainer = ((SubFormControl) ctrl).getSubContainer();
                String sfClassName = new StringBuilder(c.getName()).append("->")
                                            .append(sfContainer.getName()).toString();

                boClass.getReferenceAssociationCollection().add(getBoClass(sfContainer, sfClassName));
			} else if (ctrl instanceof MultiSelectControl) {
				// TODO :: How to get mscontrol class name and cardinamity.
				BulkOperationClass multiSelect = new BulkOperationClass();
                multiSelect.setClassName(ctrl.getUserDefinedName());
				multiSelect.getAttributeCollection().add(getAttribute(ctrl, className));
				boClass.getReferenceAssociationCollection().add(multiSelect);
			} else {
				boClass.getAttributeCollection().add(getAttribute(ctrl, className));
			}
		}
		
		return boClass;
	}

	private HookingInformation getCustomFields() {
		HookingInformation hookingInformation = new HookingInformation();
        Attribute attr = null;
        for (Entry<String, String> entry : customFields.entrySet()) {
            attr = new Attribute();
            String columnName = entry.getKey();
            attr.setName(entry.getValue());
            attr.setCsvColumnName(columnName);
            csvColumnNames.add(columnName );

            hookingInformation.getAttributeCollection().add(attr);
        }

		return hookingInformation;
	}
	
	
	private Attribute getAttribute(Control ctrl, String className) {
		Attribute attr = new Attribute();
		Boolean isSubForm = className.contains("->") ? true : false;
		String columnName = ctrl.getCaption().replace(" ", "_");
		attr.setName(ctrl.getUserDefinedName());
		attr.setCsvColumnName(columnName);

    	if (ctrl instanceof DatePicker) {
			DatePicker dateCtrl = (DatePicker) ctrl;
			attr.setFormat(dateCtrl.getFormat());
		}

        if (ctrl instanceof MultiSelectControl) {
            columnName = isSubForm ? (columnName + "#1#1") : (columnName + "#1");
            csvColumnNames.add(columnName );
        } else {
           columnName = isSubForm ? (columnName + "#1") : (columnName);
           csvColumnNames.add(columnName);
        }

		return attr;
	}
}
