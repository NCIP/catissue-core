package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.util.NumUtil;

public class SpecimenReturnEvent extends SpecimenEvent {
	private BigDecimal quantity;

	private StorageContainer storageLocation;

	@Autowired
	private DaoFactory daoFactory;

	public SpecimenReturnEvent(Specimen specimen) {
		super(specimen);
	}

	public BigDecimal getQuantity() {
		loadRecordIfNotLoaded();
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		loadRecordIfNotLoaded();
		this.quantity = quantity;
	}

	public StorageContainer getStorageLocation() {
		loadRecordIfNotLoaded();
		return storageLocation;
	}

	public void setStorageLocation(StorageContainer storageLocation) {
		loadRecordIfNotLoaded();
		this.storageLocation = storageLocation;
	}
	@Override
	public String getFormName() {
		return "SpecimenReturnEvent";
	}

	@Override
	protected Map<String, Object> getEventAttrs() {
		Map<String, Object> attrs = new HashMap<>();
		attrs.put("quantity", quantity);
		attrs.put("location", storageLocation.getId());
		return attrs;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		this.quantity = new BigDecimal(attrValues.get("quantity").toString());
		Long containerId = new Long(attrValues.get("location").toString());
		this.storageLocation = daoFactory.getStorageContainerDao().getById(containerId);
	}

	public static SpecimenReturnEvent createForDistributionOrderItem(DistributionOrderItem item) {
		SpecimenReturnEvent event = new SpecimenReturnEvent(item.getSpecimen());
		event.setId(item.getId());
		return event;
	}
}
