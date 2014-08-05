
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.EquipmentFactory;
import com.krishagni.catissueplus.core.administrative.events.AllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.GetEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GotEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateEquipmentEvent;
import com.krishagni.catissueplus.core.administrative.services.EquipmentService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class EquipmentServiceImpl implements EquipmentService {

	private static final String DISPLAY_NAME = "display name";

	private static final String DEVICE_NAME = "device name";

	private static final String EQUIPMENT_ID = "equipment id";

	private EquipmentFactory equipmentFactory;

	private DaoFactory daoFactory;

	public void setEquipmentFactory(EquipmentFactory equipmentFactory) {
		this.equipmentFactory = equipmentFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public GotEquipmentEvent getEquipment(GetEquipmentEvent event) {
		Equipment equipment;
		if (event.getId() != null) {
			equipment = daoFactory.getEquipmentDao().getEquipment(event.getId());
			if (equipment == null) {
				return GotEquipmentEvent.notFound(event.getId());
			}

		}
		else {
			equipment = daoFactory.getEquipmentDao().getEquipment(event.getDisplayName());
			if (equipment == null) {
				return GotEquipmentEvent.notFound(event.getDisplayName());
			}
		}
		EquipmentDetails details = EquipmentDetails.fromDomain(equipment);

		return GotEquipmentEvent.ok(details);
	}

	@Override
	@PlusTransactional
	public AllEquipmentEvent getAllEquipments(ReqAllEquipmentEvent event) {
		List<Equipment> equipments = daoFactory.getEquipmentDao().getAllEquipments(event.getMaxResults());
		List<EquipmentDetails> result = new ArrayList<EquipmentDetails>();

		for (Equipment equipment : equipments) {
			result.add(EquipmentDetails.fromDomain(equipment));
		}

		return AllEquipmentEvent.ok(result);
	}

	@Override
	@PlusTransactional
	public EquipmentCreatedEvent createEquipment(CreateEquipmentEvent reqEvent) {

		try {
			Equipment equipment = equipmentFactory.create(reqEvent.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();

			ensureUniqueDisplayName(equipment.getDisplayName(), exceptionHandler);
			ensureUniqueDeviceName(equipment.getDeviceName(), exceptionHandler);
			ensureUniqueEquipmentId(equipment.getEquipmentId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getEquipmentDao().saveOrUpdate(equipment);

			return EquipmentCreatedEvent.ok(EquipmentDetails.fromDomain(equipment));
		}
		catch (ObjectCreationException ex) {
			return EquipmentCreatedEvent.invalidRequest(ex.getMessage(), ex.getErroneousFields());
		}
		catch (Exception ex) {
			return EquipmentCreatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public EquipmentUpdatedEvent updateEquipment(UpdateEquipmentEvent reqEvent) {

		try {
			Long id = reqEvent.getId();

			Equipment oldEquipment = daoFactory.getEquipmentDao().getEquipment(id);

			if (oldEquipment == null) {
				return EquipmentUpdatedEvent.notFound(id);
			}

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			Equipment equipment = equipmentFactory.create(reqEvent.getDetails());
			checkDisplayName(oldEquipment.getDisplayName(), equipment.getDisplayName(), exceptionHandler);
			checkDeviceName(oldEquipment.getDeviceName(), equipment.getDeviceName(), exceptionHandler);
			checkEquipmentId(oldEquipment.getEquipmentId(), equipment.getEquipmentId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			oldEquipment.update(equipment);
			daoFactory.getEquipmentDao().saveOrUpdate(oldEquipment);

			return EquipmentUpdatedEvent.ok(EquipmentDetails.fromDomain(oldEquipment));
		}
		catch (ObjectCreationException ex) {
			return EquipmentUpdatedEvent.invalidRequest(ex.getMessage(), ex.getErroneousFields());
		}
		catch (Exception ex) {
			return EquipmentUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public EquipmentUpdatedEvent patchEquipment(PatchEquipmentEvent reqEvent) {
		try {
			Long id = reqEvent.getId();

			Equipment oldEquipment = daoFactory.getEquipmentDao().getEquipment(id);

			if (oldEquipment == null) {
				return EquipmentUpdatedEvent.notFound(id);
			}
			String oldEquipmentId = oldEquipment.getEquipmentId();
			String oldDisplayName = oldEquipment.getDisplayName();
			String oldDeviceName = oldEquipment.getDeviceName();
			Equipment equipment = equipmentFactory.patch(oldEquipment, reqEvent.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkDisplayName(oldDisplayName, reqEvent.getDetails().getDisplayName(), exceptionHandler);
			checkDeviceName(oldDeviceName, reqEvent.getDetails().getDeviceName(), exceptionHandler);
			checkEquipmentId(oldEquipmentId, equipment.getEquipmentId(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getEquipmentDao().saveOrUpdate(equipment);
			return EquipmentUpdatedEvent.ok(EquipmentDetails.fromDomain(equipment));
		}
		catch (ObjectCreationException exception) {
			return EquipmentUpdatedEvent.invalidRequest(EquipmentErrorCode.ERRORS.message(), exception.getErroneousFields());
		}
		catch (Exception e) {
			return EquipmentUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public EquipmentDeletedEvent deleteEquipment(DeleteEquipmentEvent reqEvent) {
		try {
			Equipment equipment;
			if (reqEvent.getId() != null) {
				equipment = daoFactory.getEquipmentDao().getEquipment(reqEvent.getId());
				if (equipment == null) {
					return EquipmentDeletedEvent.notFound(reqEvent.getId());
				}
			}
			else {
				equipment = daoFactory.getEquipmentDao().getEquipment(reqEvent.getDisplayName());
				if (equipment == null) {
					return EquipmentDeletedEvent.notFound(reqEvent.getDisplayName());
				}
			}
			equipment.delete();
			daoFactory.getEquipmentDao().saveOrUpdate(equipment);
			return EquipmentDeletedEvent.ok();
		}
		catch (Exception e) {
			return EquipmentDeletedEvent.serverError(e);
		}
	}

	private void ensureUniqueDeviceName(String deviceName, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getEquipmentDao().isUniqueDeviceName(deviceName)) {
			exceptionHandler.addError(EquipmentErrorCode.DUPLICATE_DEVICE_NAME, DEVICE_NAME);
		}
	}

	private void ensureUniqueDisplayName(String displayName, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getEquipmentDao().isUniqueDisplayName(displayName)) {
			exceptionHandler.addError(EquipmentErrorCode.DUPLICATE_DISPLAY_NAME, DISPLAY_NAME);
		}
	}

	private void ensureUniqueEquipmentId(String equipmentId, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getEquipmentDao().isUniqueEquipmentId(equipmentId)) {
			exceptionHandler.addError(EquipmentErrorCode.DUPLICATE_EQUIPMENT_ID, EQUIPMENT_ID);
		}
	}

	private void checkDeviceName(String oldDeviceName, String deviceName, ObjectCreationException exceptionHandler) {
		if (!(oldDeviceName.equals(deviceName))) {
			ensureUniqueDeviceName(deviceName, exceptionHandler);
		}

	}

	private void checkDisplayName(String oldDisplayName, String displayName, ObjectCreationException exceptionHandler) {
		if (!(oldDisplayName.equals(displayName))) {
			ensureUniqueDisplayName(displayName, exceptionHandler);
		}
	}

	private void checkEquipmentId(String oldEquipmentId, String equipmentId, ObjectCreationException exceptionHandler) {
		if (!(oldEquipmentId.equals(equipmentId))) {
			ensureUniqueEquipmentId(equipmentId, exceptionHandler);
		}
	}

}
