
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CollectionProtocolDao extends Dao<CollectionProtocol> {

	public List<CollectionProtocolSummary> getAllCollectionProtocols(boolean includePi, boolean includeStats);

	public CollectionProtocol getCollectionProtocol(String title);
	
	public CollectionProtocol getCpByShortTitle(String shortTitle);
	
	public List<CollectionProtocol> getCpsByShortTitle(Collection<String> shortTitles);

	public CollectionProtocolEvent getCpe(Long cpeId);

	public CollectionProtocolEvent getCpeByEventLabel(Long cpId, String eventLabel);
	
	public void saveCpe(CollectionProtocolEvent cpe);
	
	public void saveCpe(CollectionProtocolEvent cpe, boolean flush);

	public SpecimenRequirement getSpecimenRequirement(Long requirementId);
	
//	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByTitle(String title);
//	
//	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByShortTitle(String shortTitle);
//
//	public CollectionProtocol getCpByShortTitle(String shortTitle);
//	
//	public CollectionProtocol getCpByTitle(String shortTitle);
//	
//	public List<SpecimenRequirement> getSpecimenRequirments(Long cpeId);
//
//	public List<CollectionProtocolSummary> getChildProtocols(Long cpId);
//	

}
