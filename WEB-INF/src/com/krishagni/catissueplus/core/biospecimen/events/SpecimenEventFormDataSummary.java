
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class SpecimenEventFormDataSummary {
	private String label;

	private List<String> formRecords = new ArrayList<String>();

    private Long objectId;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getFormRecords() {
		return formRecords;
	}

	public void setFormRecords(List<String> formRecords) {
		this.formRecords = formRecords;
	}

	public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
    
    /*select count(*),fc.is_multirecord as isMultirecord from catissue_form_record_entry r 
    left join  catissue_form_context fc on  r.form_ctxt_id = fc.identifier and r.object_id = 65 and r.activity_status = 'ACTIVE'
    where fc.entity_type = 'SpecimenEvent' and fc.identifier = 32*/

}
