
package com.krishagni.catissueplus.core.biospecimen.events;

import edu.common.dynamicextensions.napi.FormData;

public class SpecimenEventFormDataSummary {
	private String label;

	private String formData;

    private Long objectId;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) { 
		this.formData = formData != null ?formData.toJson():null; 
	}

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

}
