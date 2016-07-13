package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeSummary;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ContainerTypeService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ContainerTypeServiceImpl implements ContainerTypeService {
	private DaoFactory daoFactory;
	
	private ContainerTypeFactory containerTypeFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setContainerTypeFactory(ContainerTypeFactory containerTypeFactory) {
		this.containerTypeFactory = containerTypeFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<ContainerTypeSummary>> getContainerTypes(RequestEvent<ContainerTypeListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadContainerTypeRights();
			List<ContainerType> types = daoFactory.getContainerTypeDao().getTypes(req.getPayload());
			return ResponseEvent.response(ContainerTypeSummary.from(types));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Long> getContainerTypesCount(RequestEvent<ContainerTypeListCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadContainerTypeRights();
			return ResponseEvent.response(daoFactory.getContainerTypeDao().getTypesCount(req.getPayload()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ContainerTypeDetail> getContainerType(RequestEvent<EntityQueryCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureReadContainerTypeRights();
			EntityQueryCriteria crit = req.getPayload();
			ContainerType containerType = getContainerType(crit.getId(), crit.getName());
			return ResponseEvent.response(ContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ContainerTypeDetail> createContainerType(RequestEvent<ContainerTypeDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			ContainerTypeDetail input = req.getPayload();
			ContainerType containerType = containerTypeFactory.createContainerType(input);
			ensureUniqueConstraints(null, containerType);
			
			daoFactory.getContainerTypeDao().saveOrUpdate(containerType, true);
			return ResponseEvent.response(ContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ContainerTypeDetail> updateContainerType(RequestEvent<ContainerTypeDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			ContainerTypeDetail input = req.getPayload();
			ContainerType existing = getContainerType(input.getId(), input.getName());
			ContainerType containerType = containerTypeFactory.createContainerType(input);
			ensureUniqueConstraints(existing, containerType);
			
			existing.update(containerType);
			daoFactory.getContainerTypeDao().saveOrUpdate(existing);
			return ResponseEvent.response(ContainerTypeDetail.from(existing));
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
			ContainerType existing = getContainerType(req.getPayload(), null);
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ContainerTypeDetail> deleteContainerType(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			ContainerType existing = getContainerType(req.getPayload(), null);
			existing.delete();
			return ResponseEvent.response(ContainerTypeDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private ContainerType getContainerType(Long id, String name) {
		ContainerType containerType = null;
		Object key = null;
		if (id != null) {
			containerType = daoFactory.getContainerTypeDao().getById(id);
			key = id;
		} else if (StringUtils.isNotBlank(name)) {
			containerType = daoFactory.getContainerTypeDao().getByName(name);
			key = name;
		}
		
		if (containerType == null) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.NOT_FOUND, key);
		}
		
		return containerType;
	}
	
	private void ensureUniqueConstraints(ContainerType existing, ContainerType newContainerType) {
		if (existing != null && existing.getName().equals(newContainerType.getName())) {
			return;
		}
		
		ContainerType containerType = daoFactory.getContainerTypeDao().getByName(newContainerType.getName());
		if (containerType != null) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.DUP_NAME, newContainerType.getName());
		}
	}
	
}
