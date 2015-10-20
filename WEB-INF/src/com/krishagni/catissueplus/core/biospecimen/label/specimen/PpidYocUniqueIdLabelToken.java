package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class PpidYocUniqueIdLabelToken extends AbstractSpecimenLabelToken {

	@Autowired
	private DaoFactory daoFactory;
	
	public PpidYocUniqueIdLabelToken() {
		this.name = "PPI_YOC_UID";
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public java.lang.String getLabel(Specimen specimen) {	
		String ppid = specimen.getVisit().getRegistration().getPpid();

		while (specimen.isAliquot() || specimen.isDerivative()) {
			specimen = specimen.getParentSpecimen();
		}

		Calendar cal = Calendar.getInstance();
		SpecimenCollectionEvent collEvent = specimen.getCollectionEvent();
		if (collEvent != null) {
			cal.setTime(collEvent.getTime());
		} else if (specimen.getCreatedOn() != null) {
			cal.setTime(specimen.getCreatedOn());
		}
		
		int yoc = cal.get(Calendar.YEAR);
		String key = ppid + "_" + yoc;
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, key);
		return uniqueId.toString();
	}
	
	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		return super.validateNumber(input, startIdx);
	}	
}
