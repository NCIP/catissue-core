
package com.krishagni.catissueplus.core.extapp.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.extapp.domain.ExternalAppNotification;
import com.krishagni.catissueplus.core.extapp.events.NotificationDto;

public interface ExternalAppNotificationDao extends Dao<ExternalAppNotification> {

	public List<NotificationDto> getNotificationObjects();

}
