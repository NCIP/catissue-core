package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

@Configurable
public class SpecimenTransferEvent extends SpecimenEvent {
	private StorageContainer fromContainer;
	
	private int fromDimensionOne;
	
	private int fromDimensionTwo;
	
	private StorageContainer toContainer;
	
	private int toDimensionOne;
	
	private int toDimensionTwo;
	
	@Autowired
	private DaoFactory daoFactory;

	public SpecimenTransferEvent(Specimen specimen) {
		super(specimen);
	}

	public StorageContainer getFromContainer() {
		loadRecordIfNotLoaded();
		return fromContainer;
	}

	public void setFromContainer(StorageContainer fromContainer) {
		this.fromContainer = fromContainer;
	}

	public int getFromDimensionOne() {		
		loadRecordIfNotLoaded();
		return fromDimensionOne;
	}

	public void setFromDimensionOne(int fromDimensionOne) {
		this.fromDimensionOne = fromDimensionOne;
	}

	public int getFromDimensionTwo() {
		loadRecordIfNotLoaded();
		return fromDimensionTwo;
	}

	public void setFromDimensionTwo(int fromDimensionTwo) {
		this.fromDimensionTwo = fromDimensionTwo;
	}
	
	public void setFromPosition(StorageContainerPosition from) {
		setFromContainer(from.getContainer());
		setFromDimensionOne(from.getPosOneOrdinal());
		setFromDimensionTwo(from.getPosTwoOrdinal());
	}

	public StorageContainer getToContainer() {
		loadRecordIfNotLoaded();
		return toContainer;
	}

	public void setToContainer(StorageContainer toContainer) {
		this.toContainer = toContainer;
	}

	public int getToDimensionOne() {
		loadRecordIfNotLoaded();
		return toDimensionOne;
	}

	public void setToDimensionOne(int toDimensionOne) {
		this.toDimensionOne = toDimensionOne;
	}

	public int getToDimensionTwo() {
		loadRecordIfNotLoaded();
		return toDimensionTwo;
	}

	public void setToDimensionTwo(int toDimensionTwo) {
		this.toDimensionTwo = toDimensionTwo;
	}
	
	public void setToPosition(StorageContainerPosition to) {
		setToContainer(to.getContainer());
		setToDimensionOne(to.getPosOneOrdinal());
		setToDimensionTwo(to.getPosTwoOrdinal());
	}
	
	@Override
	public Map<String, Object> getEventAttrs() {
		Map<String, Object> eventAttrs = new HashMap<String, Object>();
		if (fromContainer != null) {
			eventAttrs.put("fromContainer", fromContainer.getId());
			eventAttrs.put("fromDimensionOne", fromDimensionOne);
			eventAttrs.put("fromDimensionTwo", fromDimensionTwo);
		}
		
		if (toContainer != null) {
			eventAttrs.put("toContainer", toContainer.getId());
			eventAttrs.put("toDimensionOne", toDimensionOne);
			eventAttrs.put("toDimensionTwo", toDimensionTwo);			
		}

		return eventAttrs;
	}

	@Override
	public void setEventAttrs(Map<String, Object> attrValues) {
		Number fromContainerId = (Number)attrValues.get("fromContainer");
		if (fromContainerId != null) {
			setFromContainer(getContainer(fromContainerId));
			setFromDimensionOne(getInt(attrValues.get("fromDimensionOne")));
			setFromDimensionTwo(getInt(attrValues.get("fromDimensionTwo"))); 
		}
		
		Number toContainerId = (Number)attrValues.get("toContainer");
		if (toContainerId != null) {
			setToContainer(getContainer(toContainerId));
			setToDimensionOne(getInt(attrValues.get("toDimensionOne"))); 
			setToDimensionTwo(getInt(attrValues.get("toDimensionTwo")));
		}
	}

	@Override
	public String getFormName() {
		return "SpecimenTransferEvent";
	}
			
	public static List<SpecimenTransferEvent> getFor(Specimen specimen) {
		List<Long> recIds = new SpecimenTransferEvent(specimen).getRecordIds();		
		if (CollectionUtils.isEmpty(recIds)) {
			return Collections.emptyList();
		}
		
		List<SpecimenTransferEvent> events = new ArrayList<SpecimenTransferEvent>();
		for (Long recId : recIds) {
			SpecimenTransferEvent event = new SpecimenTransferEvent(specimen);
			event.setId(recId);
			events.add(event);
		}
		
		return events;		
	}
	
	private StorageContainer getContainer(Number containerId) {
		return daoFactory.getStorageContainerDao().getById(containerId.longValue());
	}
	
	private int getInt(Object number) {
		return ((Number)number).intValue();
	}
}
