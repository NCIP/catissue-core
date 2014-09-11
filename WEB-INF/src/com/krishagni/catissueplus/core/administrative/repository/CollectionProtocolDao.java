
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public interface CollectionProtocolDao extends Dao<CollectionProtocol> {

	public List<CollectionProtocolSummary> getAllCollectionProtocols();

	public CollectionProtocol getCollectionProtocol(Long cpId);

	public CollectionProtocolEvent getCpe(Long cpeId);

	public SpecimenRequirement getSpecimenRequirement(Long requirementId);

	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByTitle(String title);

	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByShortTitle(String shortTitle);
	
	public List<SpecimenRequirement> getSpecimenRequirments(Long cpeId);

	public List<CollectionProtocolSummary> getChildProtocols(Long cpId);
}
