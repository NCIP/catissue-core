
package com.krishagni.catissueplus.core.notification.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.notification.domain.CollectionProtocolStudyMapping;

public interface CPStudyMappingDao extends Dao<CollectionProtocolStudyMapping> {

	public Long getMappedCPId(String appName, String studyId);

}
