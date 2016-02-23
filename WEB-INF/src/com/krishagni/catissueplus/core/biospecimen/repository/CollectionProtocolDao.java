
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	public List<CollectionProtocol> getExpiringCps(Date fromDate, Date toDate);
	
	public CollectionProtocol getCpByCode(String code);

	public List<Long> getCpIdsBySiteIds(Collection<Long> siteIds);

	public Map<String, Object> getCpIds(String key, Object value);
	
	public List<Long> getSiteIdsByCpIds(Collection<Long> cpIds);

	public CollectionProtocolEvent getCpe(Long cpeId);
	
	public List<CollectionProtocolEvent> getCpes(Collection<Long> cpeIds);

	public CollectionProtocolEvent getCpeByEventLabel(Long cpId, String eventLabel);
	
	public CollectionProtocolEvent getCpeByEventLabel(String title, String label);
	
	public CollectionProtocolEvent getCpeByShortTitleAndEventLabel(String shortTitle, String label);
	
	public CollectionProtocolEvent getCpeByCode(String shortTitle, String code);

	public int getMinEventPoint(Long cpId);
	
	public void saveCpe(CollectionProtocolEvent cpe);
	
	public void saveCpe(CollectionProtocolEvent cpe, boolean flush);

	public SpecimenRequirement getSpecimenRequirement(Long requirementId);
	
	public SpecimenRequirement getSrByCode(String code);
	
	public List<CpWorkflowConfig> getCpWorkflows(Collection<Long> cpIds);

	public void saveCpWorkflows(CpWorkflowConfig cfg);
	
	public CpWorkflowConfig getCpWorkflows(Long cpId);
	
	public ConsentTier getConsentTier(Long consentId);
	
	public ConsentTier getConsentTierByStatement(Long cpId, String statement);
	
	public int getConsentRespsCount(Long consentId);
	
}
