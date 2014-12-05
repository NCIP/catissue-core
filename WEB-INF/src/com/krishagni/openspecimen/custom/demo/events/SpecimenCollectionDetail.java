package com.krishagni.openspecimen.custom.demo.events;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public class SpecimenCollectionDetail {
	private CollectionProtocolRegistrationDetail cpr;
	
	private VisitDetail visit;
	
	private SpecimenDetail blood;
	
	private SpecimenDetail frozenTissue;
	
	public CollectionProtocolRegistrationDetail getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistrationDetail cpr) {
		this.cpr = cpr;
	}

	public VisitDetail getVisit() {
		return visit;
	}

	public void setVisit(VisitDetail visit) {
		this.visit = visit;
	}

	public SpecimenDetail getBlood() {
		return blood;
	}

	public void setBlood(SpecimenDetail blood) {
		this.blood = blood;
	}

	public SpecimenDetail getFrozenTissue() {
		return frozenTissue;
	}

	public void setFrozenTissue(SpecimenDetail frozenTissue) {
		this.frozenTissue = frozenTissue;
	}
}
