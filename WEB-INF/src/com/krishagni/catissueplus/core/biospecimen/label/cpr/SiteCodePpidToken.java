package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

public class SiteCodePpidToken extends AbstractPpidToken {
	
	public SiteCodePpidToken() {
		this.name = "SITE_CODE";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		List<ParticipantMedicalIdentifier> pmis = cpr.getParticipant().getPmisOrderedById();
		if (pmis.isEmpty()) {
			return StringUtils.EMPTY;
		}

		String siteCode = pmis.iterator().next().getSite().getCode();
		if (StringUtils.isBlank(siteCode)) {
			return StringUtils.EMPTY;
		}

		return siteCode;
	}
}
