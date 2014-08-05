package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenEventFormDataEvent extends ResponseEvent {

    private SpecimenEventFormData specimenEventFormData;

    public SpecimenEventFormData getSpecimenEventFormData() {
        return specimenEventFormData;
    }

    public void setSpecimenEventFormData(SpecimenEventFormData specimenEventFormData) {
        this.specimenEventFormData = specimenEventFormData;
    }

    public static SpecimenEventFormDataEvent ok(SpecimenEventFormData specimenEventFormData) {
        SpecimenEventFormDataEvent res = new SpecimenEventFormDataEvent();
        res.setSpecimenEventFormData(specimenEventFormData);
        res.setStatus(EventStatus.OK);
        return res;
    }
}
