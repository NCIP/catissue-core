package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class PpidUniqueIdLabelToken extends AbstractSpecimenLabelToken {
	
	@Autowired
	private DaoFactory daoFactory;
	
	public PpidUniqueIdLabelToken() {
		this.name = "PPI_UID";
	}

	@Override
	public String getLabel(Specimen specimen) {
		String ppid = specimen.getVisit().getRegistration().getPpid();
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, ppid);
		return uniqueId.toString();
	}
	
	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		return super.validateNumber(input, startIdx);
	}
}
