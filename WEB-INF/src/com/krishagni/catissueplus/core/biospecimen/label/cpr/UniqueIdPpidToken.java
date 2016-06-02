package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class UniqueIdPpidToken extends AbstractPpidToken {

	@Autowired
	private DaoFactory daoFactory;
	
	public UniqueIdPpidToken() {
		this.name = "SYS_UID";
	}
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String getLabel(CollectionProtocolRegistration arg0, String... arg1) {
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId("Registration", getName());
		return uniqueId.toString();
	}

	@Override
	public int validate(Object object, String input, int startIdx, String ... args) {
		return super.validateNumber(input, startIdx);
	}
}
