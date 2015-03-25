package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.PositionTenantDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

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
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req) {
		try {			
			List<StorageContainer> containers = daoFactory.getStorageContainerDao().getStorageContainers(req.getPayload());
			List<StorageContainerSummary> result = StorageContainerSummary.from(containers, req.getPayload().includeChildren());
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> getStorageContainer(RequestEvent<ContainerQueryCriteria> req) {
		try {
			ContainerQueryCriteria crit = req.getPayload();
			
			StorageContainer container = null;
			if (crit.getId() != null) {
				container = daoFactory.getStorageContainerDao().getById(crit.getId());
			} else if (StringUtils.isNotBlank(crit.getName())) {
				container = daoFactory.getStorageContainerDao().getByName(crit.getName());
			}
						
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(StorageContainerDetail.from(container));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerPositionDetail>> getOccupiedPositions(RequestEvent<Long> req) {
		try {
			Long containerId = req.getPayload();
			
			StorageContainer container = daoFactory.getStorageContainerDao().getById(containerId);
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			Set<StorageContainerPosition> positions = container.getOccupiedPositions();
			return ResponseEvent.response(StorageContainerPositionDetail.from(positions));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> createStorageContainer(RequestEvent<StorageContainerDetail> req) {
		try {
			StorageContainerDetail input = req.getPayload();
			
			StorageContainer container = containerFactory.createStorageContainer(input);
			ensureUniqueConstraints(container);
			container.validateRestrictions();
			
			daoFactory.getStorageContainerDao().saveOrUpdate(container, true);
			return ResponseEvent.response(StorageContainerDetail.from(container));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> updateStorageContainer(RequestEvent<StorageContainerDetail> req) {
		try {
			StorageContainerDetail input = req.getPayload();
			
			Long id = input.getId();
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(id);
			if (existing == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
						
			StorageContainer container = containerFactory.createStorageContainer(input);
			ensureUniqueConstraints(container);
			
			existing.update(container);			
			daoFactory.getStorageContainerDao().saveOrUpdate(existing, true);
			return ResponseEvent.response(StorageContainerDetail.from(existing));			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> isAllowed(RequestEvent<PositionTenantDetail> req) {
		try {
			PositionTenantDetail detail = req.getPayload();
			StorageContainer container = null;
			
			if (detail.getContainerId() != null) {
				container = daoFactory.getStorageContainerDao().getById(detail.getContainerId());
			} else if (StringUtils.isNotBlank(detail.getContainerName())) {
				container = daoFactory.getStorageContainerDao().getByName(detail.getContainerName());
			}
			
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			CollectionProtocol cp = new CollectionProtocol();
			cp.setId(detail.getCpId());
			boolean isAllowed = container.canContainSpecimen(cp, detail.getSpecimenClass(), detail.getSpecimenType());

			if (!isAllowed) {
				return ResponseEvent.userError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN);
			} else {
				return ResponseEvent.response(isAllowed);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerDetail> deleteStorageContainer(RequestEvent<Long> req) {
		try {
			StorageContainer existing = daoFactory.getStorageContainerDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			existing.delete();
			return ResponseEvent.response(StorageContainerDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
			
	private void ensureUniqueConstraints(StorageContainer container) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueName(container)) {
			ose.addError(StorageContainerErrorCode.DUP_NAME);
		}
		
		if (!isUniqueBarcode(container)) {
			ose.addError(StorageContainerErrorCode.DUP_BARCODE);
		}
		
		ose.checkAndThrow();		
	}
	
	private boolean isUniqueName(StorageContainer container) {
		StorageContainer existing = daoFactory.getStorageContainerDao().getByName(container.getName());
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
		
		StorageContainer existing = daoFactory.getStorageContainerDao().getByBarcode(barcode);
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