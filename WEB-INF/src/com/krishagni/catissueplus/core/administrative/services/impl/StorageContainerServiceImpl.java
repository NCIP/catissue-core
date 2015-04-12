package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.AssignPositionsOp;
import com.krishagni.catissueplus.core.administrative.events.ContainerMapExportDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.PositionTenantDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerMapExporter;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class StorageContainerServiceImpl implements StorageContainerService {
	private DaoFactory daoFactory;
	
	private StorageContainerFactory containerFactory;
	
	private ContainerMapExporter mapExporter;

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
	
	public void setMapExporter(ContainerMapExporter mapExporter) {
		this.mapExporter = mapExporter;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerSummary>> getStorageContainers(RequestEvent<StorageContainerListCriteria> req) {
		try {			
			StorageContainerListCriteria crit = req.getPayload();
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessContainerSites();
			if (siteIds != null && siteIds.isEmpty()) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
			
			List<StorageContainer> containers = daoFactory.getStorageContainerDao().getStorageContainers(crit);
			List<StorageContainerSummary> result = StorageContainerSummary.from(containers, crit.includeChildren());
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
			StorageContainer container = getContainer(req.getPayload());						
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
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
			
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
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
			AccessCtrlMgr.getInstance().ensureCreateContainerRights(container);
			
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
			AccessCtrlMgr.getInstance().ensureUpdateContainerRights(existing);			
			
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
			
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			
			CollectionProtocol cp = new CollectionProtocol();
			cp.setId(detail.getCpId());
			String specimenClass = detail.getSpecimenClass();
			String type = detail.getSpecimenType();
			boolean isAllowed = container.canContainSpecimen(cp, specimenClass, type);

			if (!isAllowed) {
				return ResponseEvent.userError(
						StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, 
						container.getName(), 
						Specimen.getDesc(specimenClass, type));
			} else {
				return ResponseEvent.response(isAllowed);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ContainerMapExportDetail> exportMap(RequestEvent<ContainerQueryCriteria> req) {
		try {
			StorageContainer container = getContainer(req.getPayload());						
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}
			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			
			File file = mapExporter.exportToFile(container);
			return ResponseEvent.response(new ContainerMapExportDetail(container.getName(), file));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<StorageContainerPositionDetail>> assignPositions(RequestEvent<AssignPositionsOp> req) {
		try {
			AssignPositionsOp op = req.getPayload();
			StorageContainer container = getContainer(op.getContainerId(), op.getContainerName());
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND);
			}			
			AccessCtrlMgr.getInstance().ensureUpdateContainerRights(container);
			
			List<StorageContainerPosition> positions = new ArrayList<StorageContainerPosition>();
			for (StorageContainerPositionDetail posDetail : op.getPositions()) {
				positions.add(getPosition(container, posDetail));				
			}
			
			container.assignPositions(positions);
			daoFactory.getStorageContainerDao().saveOrUpdate(container, true);
			return ResponseEvent.response(StorageContainerPositionDetail.from(container.getOccupiedPositions()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
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
			
			AccessCtrlMgr.getInstance().ensureDeleteContainerRights(existing);
			existing.delete();
			return ResponseEvent.response(StorageContainerDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private StorageContainer getContainer(ContainerQueryCriteria crit) {
		return getContainer(crit.getId(), crit.getName());
	}
	
	private StorageContainer getContainer(Long id, String name) {
		StorageContainer container = null;
		if (id != null) {
			container = daoFactory.getStorageContainerDao().getById(id);
		} else if (StringUtils.isNotBlank(name)) {
			container = daoFactory.getStorageContainerDao().getByName(name);
		}
		
		return container;
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
	
	private StorageContainerPosition getPosition(StorageContainer container, StorageContainerPositionDetail pos) {
		if (StringUtils.isBlank(pos.getPosOne()) || StringUtils.isBlank(pos.getPosTwo())) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_POSITIONS);
		}
		
		String entityType = pos.getOccuypingEntity();
		if (StringUtils.isBlank(entityType)) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.OCCUPYING_ENTITY_TYPE_REQUIRED);
		}
		
		String entityName = pos.getOccupyingEntityName();
		Long entityId = pos.getOccupyingEntityId();
		if (StringUtils.isBlank(entityName) && entityId == null) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.OCCUPYING_ENTITY_ID_OR_NAME_REQUIRED);
		}
		
		if (entityType.equalsIgnoreCase("specimen")) {
			return getSpecimenPosition(container, pos, entityId, entityName);
		} else if (entityType.equalsIgnoreCase("container")) {
			return getChildContainerPosition(container, pos, entityId, entityName);
		}
		
		throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ENTITY_TYPE, entityType);
	}
	
	private StorageContainerPosition getSpecimenPosition(
			StorageContainer container, 
			StorageContainerPositionDetail pos, 
			Long specimenId, 
			String label) {
		
		Specimen specimen = null;
		if (specimenId != null) {
			specimen = daoFactory.getSpecimenDao().getById(specimenId);
		} else if (StringUtils.isNotBlank(label)) {
			specimen = daoFactory.getSpecimenDao().getByLabel(label);
		}
		
		if (specimen == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND);
		}
		
		if (!container.canContain(specimen)) {
			throw OpenSpecimenException.userError(
					StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, 
					container.getName(), 
					specimen.getLabelOrDesc());
		}
		
		if (!container.canSpecimenOccupyPosition(specimen.getId(), pos.getPosOne(), pos.getPosTwo())) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE);
		}
		
		StorageContainerPosition position = container.createPosition(pos.getPosOne(), pos.getPosTwo());
		position.setOccupyingSpecimen(specimen);
		return position;		
	}
	
	private StorageContainerPosition getChildContainerPosition(
			StorageContainer container, 
			StorageContainerPositionDetail pos, 
			Long containerId, 
			String containerName) {
		
		StorageContainer childContainer = getContainer(containerId, containerName);
		if (childContainer == null) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NOT_FOUND);
		}
		
		if (!container.canContain(childContainer)) {
			throw OpenSpecimenException.userError(
					StorageContainerErrorCode.CANNOT_HOLD_CONTAINER, 
					container.getName(), 
					childContainer.getName());
		}
		
		if (!container.canContainerOccupyPosition(childContainer.getId(), pos.getPosOne(), pos.getPosTwo())) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.NO_FREE_SPACE);
		}
		
		StorageContainerPosition position = container.createPosition(pos.getPosOne(), pos.getPosTwo());
		position.setOccupyingContainer(childContainer);
		return position;
	}	
}