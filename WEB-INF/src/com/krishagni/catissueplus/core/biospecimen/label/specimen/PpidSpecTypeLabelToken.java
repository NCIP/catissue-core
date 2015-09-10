package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class PpidSpecTypeLabelToken extends AbstractSpecimenLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public PpidSpecTypeLabelToken() {
		this.name = "PPI_SPEC_TYPE_UID";
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getLabel(Specimen specimen) {
		String ppid = specimen.getVisit().getRegistration().getPpid();
		String key = ppid + "_" + specimen.getSpecimenType();
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, key);

		return uniqueId == 1L ? StringUtils.EMPTY : uniqueId.toString();
	}

}
