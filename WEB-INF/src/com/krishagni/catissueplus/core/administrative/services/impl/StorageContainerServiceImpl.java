package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqContainerOccupiedPositionsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class StorageContainerServiceImpl implements StorageContainerService {
	private DaoFactory daoFactory;
	
	private StorageContainerFactory containerFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public StorageContainerFactory getContainerFactory() {
		return containerFactory;
	}

	public void setContainerFactory(StorageContainerFactory containerFactory) {
		this.containerFactory = containerFactory;
	}

	@Override
	@PlusTransactional
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent req) {
		try {
			StorageContainerListCriteria listCrit = new StorageContainerListCriteria()
				.startAt(req.getStartAt())
				.maxResults(req.getMaxRecords())
				.onlyFreeContainers(req.isOnlyFreeContainers())
				.parentContainerId(req.getParentContainerId())
				.siteName(req.getSiteName())
				.query(req.getName())
				.anyLevelContainers(req.isAnyLevelContainers());
			
			List<StorageContainer> containers = daoFactory.getStorageContainerDao().getStorageContainers(listCrit); 
			return StorageContainersEvent.ok(StorageContainerSummary.from(containers));
		} catch (Exception e) {
			return StorageContainersEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent req) {
		try {
			Long containerId = req.getContainerId();
			
			StorageContainer container = daoFactory.getStorageContainerDao().getById(containerId);			
			if (container == null) {
				return StorageContainerEvent.notFound(containerId);
			}
			
			return StorageContainerEvent.ok(StorageContainerDetail.from(container));			
		} catch (Exception e) {
			return StorageContainerEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ContainerOccupiedPositionsEvent getOccupiedPositions(ReqContainerOccupiedPositionsEvent req) {
		try {
			Long containerId = req.getContainerId();
			
			StorageContainer container = daoFactory.getStorageContainerDao().getById(containerId);
			if (container == null) {
				return ContainerOccupiedPositionsEvent.notFound();
			}
			
			Set<StorageContainerPosition> positions = container.getOccupiedPositions();
			return ContainerOccupiedPositionsEvent.ok(StorageContainerPositionDetail.from(positions));
		} catch (Exception e) {
			return ContainerOccupiedPositionsEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent req) {
		try {
			StorageContainerDetail input = req.getContainer();
			
			StorageContainer container = containerFactory.createStorageContainer(input);
			ensureUniqueConstraints(container);
			
			daoFactory.getStorageContainerDao().saveOrUpdate(container, true);
			return StorageContainerCreatedEvent.ok(StorageContainerDetail.from(container));
		} catch (ObjectCreationException oce) {
			return StorageContainerCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			return StorageContainerCreatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public StorageContainerUpdatedEvent updateStorageContainer(UpdateStorageContainerEvent req) {
		try {
			StorageContainerDetail input = req.getContainer();
			
			Long id = input.getId();
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(id);
			if (existing == null) {
				return StorageContainerUpdatedEvent.notFound(id);
			}
						
			StorageContainer container = containerFactory.createStorageContainer(input);
			ensureUniqueConstraints(container);
			
			existing.update(container);			
			daoFactory.getStorageContainerDao().saveOrUpdate(existing, true);
			return StorageContainerUpdatedEvent.ok(StorageContainerDetail.from(existing));			
		} catch (ObjectCreationException oce) {
			return StorageContainerUpdatedEvent.badRequest(oce);
		} catch (IllegalArgumentException iae) {
			return StorageContainerUpdatedEvent.badRequest(iae);
		} catch (Exception e) {
			return StorageContainerUpdatedEvent.serverError(e);
		}
	}
		
	private void ensureUniqueConstraints(StorageContainer container) {
		ObjectCreationException oce = new ObjectCreationException();
		
		if (!isUniqueName(container)) {
			oce.addError(StorageContainerErrorCode.NOT_UNIQUE, "name");
		}
		
		if (!isUniqueBarcode(container)) {
			oce.addError(StorageContainerErrorCode.NOT_UNIQUE, "barcode");
		}
		
		oce.checkErrorAndThrow();		
	}
	
	private boolean isUniqueName(StorageContainer container) {
		StorageContainer existing = daoFactory.getStorageContainerDao().getStorageContainerByName(container.getName());
		if (existing == null) {
			return true; // no container by this name
		} else if (container.getId() == null) {
			return false; // a different container by this name exists 
		} else if (existing.getId().equals(container.getId())) {
			return true; // same container
		} else {
			return false;
		}
	}
	
	private boolean isUniqueBarcode(StorageContainer container) {
		String barcode = container.getBarcode();
		if (barcode == null) {
			return true;
		}
		
		StorageContainer existing = daoFactory.getStorageContainerDao().getStorageContainerByBarcode(barcode);
		if (existing == null) {
			return true;
		} else if (container.getId() == null) {
			return false;
		} else if (existing.getId().equals(container.getId())) {
			return true;
		} else {
			return false;
		}
	}
}