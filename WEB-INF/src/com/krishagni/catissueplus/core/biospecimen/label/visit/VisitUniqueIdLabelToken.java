package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class VisitUniqueIdLabelToken extends AbstractVisitLabelToken {

	@Autowired
	private DaoFactory daoFactory;
			
	public VisitUniqueIdLabelToken() {
		this.name = "SYS_UID";
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId("Visit", getName());
		return "" + uniqueId.toString();
	}
	
	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		return super.validateNumber(input, startIdx);
	}	
}