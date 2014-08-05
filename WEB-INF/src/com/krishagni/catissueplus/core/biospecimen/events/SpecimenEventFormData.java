package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class SpecimenEventFormData {

    private Long formContextId;

    private List<SpecimenEventFormDataSummary> specimenEventFormDataList;

    public Long getFormContextId() {
        return formContextId;
    }

    public void setFormContextId(Long formContextId) {
        this.formContextId = formContextId;
    }

    public List<SpecimenEventFormDataSummary> getSpecimenEventFormDataList() {
        return specimenEventFormDataList;
    }

    public void setSpecimenEventFormDataList(List<SpecimenEventFormDataSummary> specimenEventFormDataList) {
        this.specimenEventFormDataList = specimenEventFormDataList;
    }
}
