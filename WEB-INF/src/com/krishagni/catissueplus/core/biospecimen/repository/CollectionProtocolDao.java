
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolDao extends Dao<CollectionProtocol> {

	public List<CollectionProtocolSummary> getCollectionProtocols(CpListCriteria criteria);

	public CollectionProtocol getCollectionProtocol(String title);
	
	public CollectionProtocol getCpByShortTitle(String shortTitle);
	
	public List<CollectionProtocol> getCpsByShortTitle(Collection<String> shortTitles);
	
	public List<CollectionProtocol> getCpsByShortTitle(Collection<String> shortTitles, String siteName);

	public List<Long> getCpIdsBySiteIds(Collection<Long> siteIds);

	public CollectionProtocolEvent getCpe(Long cpeId);

	public CollectionProtocolEvent getCpeByEventLabel(Long cpId, String eventLabel);
	
	public CollectionProtocolEvent getCpeByEventLabel(String title, String label);
	
	public CollectionProtocolEvent getCpeByShortTitleAndEventLabel(String shortTitle, String label);
	
	public void saveCpe(CollectionProtocolEvent cpe);
	
	public void saveCpe(CollectionProtocolEvent cpe, boolean flush);

	public SpecimenRequirement getSpecimenRequirement(Long requirementId);
	
	public void saveCpWorkflows(CpWorkflowConfig cfg);
	
	public CpWorkflowConfig getCpWorkflows(Long cpId);
	
	public ConsentTier getConsentTier(Long consentId);
	
	public ConsentTier getConsentTierByStatement(Long cpId, String statement);
	
	public int getConsentRespsCount(Long consentId);
	
}
