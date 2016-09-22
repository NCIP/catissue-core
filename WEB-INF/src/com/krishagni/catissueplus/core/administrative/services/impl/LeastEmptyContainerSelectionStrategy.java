package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.ContainerSelectorCriteria;
import com.krishagni.catissueplus.core.administrative.events.TenantDetail;
import com.krishagni.catissueplus.core.administrative.services.ContainerSelectionStrategy;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

@Configurable
public class LeastEmptyContainerSelectionStrategy implements ContainerSelectionStrategy {
	@Autowired
	private DaoFactory daoFactory;

	@Override
	public StorageContainer getContainer(TenantDetail criteria, Boolean aliquotsInSameContainer) {
		int freePositions = 1;
		if (aliquotsInSameContainer != null && aliquotsInSameContainer && criteria.getNumOfAliquots() > 1) {
			freePositions = criteria.getNumOfAliquots();
		}

		List<Long> containerIds = daoFactory.getStorageContainerDao().getLeastEmptyContainerId(
			new ContainerSelectorCriteria()
				.cpId(criteria.getCpId())
				.specimenClass(criteria.getSpecimenClass())
				.type(criteria.getSpecimenType())
				.minFreePositions(freePositions)
				.reservedLaterThan(ignoreReservationsBeforeDate())
				.numContainers(1));
		if (CollectionUtils.isEmpty(containerIds)) {
			return null;
		}

		return daoFactory.getStorageContainerDao().getById(containerIds.get(0));
	}

	private Date ignoreReservationsBeforeDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		return cal.getTime();
	}
}