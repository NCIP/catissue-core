package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

public class SiteCodePpidToken extends AbstractPpidToken {
	
	public SiteCodePpidToken() {
		this.name = "SITE_CODE";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		Set<ParticipantMedicalIdentifier> pmis = new TreeSet<ParticipantMedicalIdentifier>(cpr.getParticipant().getPmis());
		if (pmis.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		ParticipantMedicalIdentifier pmi = pmis.iterator().next();
		return pmi.getSite().getCode();
	}
}
