package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class ParentSpecimenUniqueIdLabelToken extends AbstractSpecimenLabelToken {

	@Autowired
	private DaoFactory daoFactory;
	
	public ParentSpecimenUniqueIdLabelToken() {
		this.name = "PSPEC_UID";
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getLabel(Specimen specimen) {
		if (specimen.getParentSpecimen() == null) {
			return "";
		}
		
		String pidStr = specimen.getParentSpecimen().getId().toString();
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, pidStr);
		return uniqueId.toString();
	}
	
	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		return super.validateNumber(input, startIdx);
	}
}
