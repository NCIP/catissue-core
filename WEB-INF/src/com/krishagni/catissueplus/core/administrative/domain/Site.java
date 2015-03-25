
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;


public class Site {
	private static final String ENTITY_NAME = "site";

	private Long id;

	private String name;
	
	private Institute institute;

	private String code;

	private String type;

	private String activityStatus;

	private String address;
    
	private Set<User> coordinators = new HashSet<User>();

	private Set<Visit> visits = new HashSet<Visit>();

	private Set<StorageContainer> storageContainers = new HashSet<StorageContainer>();
	
	private Set<CollectionProtocol> collectionProtocol = new HashSet<CollectionProtocol>();
	
	private Set<ParticipantMedicalIdentifier> pmiCollection = new HashSet<ParticipantMedicalIdentifier>();
	
	private Set<CollectionProtocolEvent> cpeCollection = new HashSet<CollectionProtocolEvent>();

	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	public String getCode() {
		return this.code; 
	}

	public void setCode(String code) { 
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getAddress() { 
		return address; 
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<User> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<User> coordinators) {
		this.coordinators = coordinators;
	}

	public Set<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}

	public Set<StorageContainer> getStorageContainers() {
		return storageContainers;
	}

	public void setStorageContainers(Set<StorageContainer> storageContainers) {
		this.storageContainers = storageContainers;
	}

	public Set<CollectionProtocol> getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(Set<CollectionProtocol> collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Set<ParticipantMedicalIdentifier> getPmiCollection() {
		return pmiCollection;
	}

	public void setPmiCollection(Set<ParticipantMedicalIdentifier> pmiCollection) {
		this.pmiCollection = pmiCollection;
	}

	public Set<CollectionProtocolEvent> getCpeCollection() {
		return cpeCollection;
	}

	public void setCpeCollection(Set<CollectionProtocolEvent> cpeCollection) {
		this.cpeCollection = cpeCollection;
	}

	public void update(Site other) {
		setName(other.getName());
		setInstitute(other.getInstitute());
		setCode(other.getCode());
		setType(other.getType());
		updateActivityStatus(other.getActivityStatus());
		CollectionUpdater.update(this.getCoordinators(), other.getCoordinators());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		List<DependentEntityDetail> dependentEntities = new ArrayList<DependentEntityDetail>();
		
		DependentEntityDetail.setDependentEntities(Visit.getEntityName(), getVisits().size(), dependentEntities);
		DependentEntityDetail.setDependentEntities(StorageContainer.getEntityName(), getStorageContainers().size(), dependentEntities);
		DependentEntityDetail.setDependentEntities(CollectionProtocol.getEntityName(), getCollectionProtocol().size(), dependentEntities);
		DependentEntityDetail.setDependentEntities(ParticipantMedicalIdentifier.getEntityName(), getPmiCollection().size(), dependentEntities);
		DependentEntityDetail.setDependentEntities(CollectionProtocolEvent.getEntityName(), getCpeCollection().size(), dependentEntities);
		
		return dependentEntities;
	}

	public void delete(boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
			ensureFreeOfDependencies();
		}
		
		this.setActivityStatus(activityStatus);
	}
	
	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) {
			return;
		}
		
		if (newActivityStatus.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			ensureFreeOfDependencies();
		}
		
		this.setActivityStatus(newActivityStatus);
	}
	
	private void ensureFreeOfDependencies() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw new OpenSpecimenException(ErrorType.USER_ERROR,SiteErrorCode.REF_ENTITY_FOUND);
		}
	}

}
