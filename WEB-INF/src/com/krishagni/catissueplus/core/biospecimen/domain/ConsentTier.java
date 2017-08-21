package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.List;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;

@Configurable
@Audited
public class ConsentTier extends BaseEntity {
	private String statement;
	
	private CollectionProtocol collectionProtocol;
	
	private String activityStatus;

	@Autowired 
	private DaoFactory daoFactory;
		
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		CollectionProtocolDao cpDao = daoFactory.getCollectionProtocolDao(); 
		int responseCount = cpDao.getConsentRespsCount(this.getId());

		return DependentEntityDetail
				.listBuilder()
				.add(ConsentTierResponse.getEntityName(), responseCount)
				.build();
	}
	
	public ConsentTier copy() {
		ConsentTier result = new ConsentTier();
		result.setStatement(getStatement());
		result.setActivityStatus(getActivityStatus());
		return result;
	}
}
