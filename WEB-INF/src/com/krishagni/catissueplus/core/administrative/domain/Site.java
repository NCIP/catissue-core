
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class Site extends BaseExtensionEntity {
	public static final String EXTN = "SiteExtension";

	private static final String ENTITY_NAME = "site";

	private String name;
	
	private Institute institute;

	private String code;

	private String type;

	private String activityStatus;

	private String address;
	
	private Set<User> coordinators = new HashSet<User>();

	private Set<Visit> visits = new HashSet<Visit>();

	private Set<StorageContainer> storageContainers = new HashSet<StorageContainer>();
	
	private Set<CollectionProtocol> collectionProtocols = new HashSet<CollectionProtocol>();
	
	private Set<Participant> participants = new HashSet<Participant>();
	
	private Set<CollectionProtocolEvent> collectionProtocolEvents = new HashSet<CollectionProtocolEvent>();
	
	private Set<DistributionOrder> distributionOrders = new HashSet<DistributionOrder>();

	public static String getEntityName() {
		return ENTITY_NAME;
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

	@NotAudited
	public Set<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}

	@NotAudited
	public Set<StorageContainer> getStorageContainers() {
		return storageContainers;
	}

	public void setStorageContainers(Set<StorageContainer> storageContainers) {
		this.storageContainers = storageContainers;
	}
	
	@NotAudited
	public Set<CollectionProtocol> getCollectionProtocols() {
		return collectionProtocols;
	}

	public void setCollectionProtocols(Set<CollectionProtocol> collectionProtocols) {
		this.collectionProtocols = collectionProtocols;
	}

	@NotAudited
	public Set<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}

	@NotAudited
	public Set<CollectionProtocolEvent> getCollectionProtocolEvents() {
		return collectionProtocolEvents;
	}

	public void setCollectionProtocolEvents(
			Set<CollectionProtocolEvent> collectionProtocolEvents) {
		this.collectionProtocolEvents = collectionProtocolEvents;
	}

	@NotAudited
	public Set<DistributionOrder> getDistributionOrders() {
		return distributionOrders;
	}

	public void setDistributionOrders(Set<DistributionOrder> distributionOrders) {
		this.distributionOrders = distributionOrders;
	}

	public void update(Site other) {
		setName(other.getName());
		setInstitute(other.getInstitute());
		setCode(other.getCode());
		setType(other.getType());
		setAddress(other.getAddress());
		setExtension(other.getExtension());
		updateActivityStatus(other.getActivityStatus());
		CollectionUpdater.update(this.getCoordinators(), other.getCoordinators());
	}
	
	//TODO: need to check few more dependencies like user, distribution order
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail
				.listBuilder()
				.add(Visit.getEntityName(), getVisits().size())
				.add(StorageContainer.getEntityName(), getStorageContainers().size())
				.add(CollectionProtocol.getEntityName(), getCollectionProtocols().size())
				.add(Participant.getEntityName(), getParticipants().size())
				.add(CollectionProtocolEvent.getEntityName(), getCollectionProtocolEvents().size())
				.add(DistributionOrder.getEntityName(), getDistributionOrders().size())
				.build();
	}

	public void delete(boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
			ensureFreeOfDependencies();
			
			setName(Utility.getDisabledValue(getName(), 255));
			if (getCode() != null) {
				setCode(Utility.getDisabledValue(getCode(), 50));
			}
		}
		
		setActivityStatus(activityStatus);
	}
	
	@Override
	public String getEntityType() {
		return EXTN;
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
			throw new OpenSpecimenException(ErrorType.USER_ERROR, SiteErrorCode.REF_ENTITY_FOUND);
		}
	}
}
