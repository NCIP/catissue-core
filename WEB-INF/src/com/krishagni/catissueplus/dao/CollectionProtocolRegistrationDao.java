
package com.krishagni.catissueplus.dao;

import java.util.List;

import com.krishagni.catissueplus.events.specimencollectiongroups.SpecimenCollectionGroupInfo;

public interface CollectionProtocolRegistrationDao extends Dao<CollectionProtocolRegistrationDao> {

	public List<SpecimenCollectionGroupInfo> getSpecimenCollectiongroupsList(Long cprId);
}
