
package com.krishagni.catissueplus.core.notification.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.notification.domain.ExternalApplication;

public interface ExternalApplicationDao extends Dao<ExternalApplication> {

	List<ExternalApplication> getAllExternalApps();
}
