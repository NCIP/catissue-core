package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

public class CpSiteCodePpidToken extends AbstractPpidToken {

	public CpSiteCodePpidToken() {
		this.name = "CP_SITE_CODE";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		List<ParticipantMedicalIdentifier> pmis = cpr.getParticipant().getPmisOrderedById();
		if (pmis.isEmpty()) {
			return StringUtils.EMPTY;
		}

		String siteCode = null;
		Site mrnSite = pmis.iterator().next().getSite();
		for (CollectionProtocolSite cpSite : cpr.getCollectionProtocol().getSites()) {
			if (cpSite.getSite().equals(mrnSite)) {
				siteCode = cpSite.getCode();
				break;
			}
		}

		if (StringUtils.isBlank(siteCode)) {
			return StringUtils.EMPTY;
		}

		return siteCode;
	}
}
