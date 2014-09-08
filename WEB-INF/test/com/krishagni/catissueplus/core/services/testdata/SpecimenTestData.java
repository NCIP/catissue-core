
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ExternalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ExternalIdentifierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.StorageLocation;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenEvent;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.beans.SessionDataBean;

public class SpecimenTestData {

	public static CreateSpecimenEvent getCreateSpecimenEventEmptyLabel() {
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		SpecimenDetail detail = getSpecimenDetail();
		detail.setLabel(null);
		event.setSessionDataBean(getSessionDataBean());
		event.setScgId(1l);
		event.setSpecimenDetail(detail);
		return event;
	}

	public static CreateSpecimenEvent getCreateChildEventInvalidParentData() {
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		SpecimenDetail detail = getSpecimenDetail();
		detail.setParentSpecimenId(null);
		detail.setLineage("Derived");
		event.setSessionDataBean(getSessionDataBean());
		event.setSpecimenDetail(detail);
		return event;
	}

	public static CreateSpecimenEvent getCreateChildEventInvalidParent() {
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		SpecimenDetail detail = getSpecimenDetail();
		detail.setParentSpecimenId(1l);
		detail.setLineage("Derived");
		event.setSessionDataBean(getSessionDataBean());
		event.setSpecimenDetail(detail);
		return event;
	}

	public static Specimen getSpecimenToReturn() {
		Specimen specimen = new Specimen();
		specimen.setLabel("new label");
		specimen.setBarcode("new barcode");
		specimen.setAvailableQuantity((double) 10);
		specimen.setInitialQuantity((double) 10);

		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setId(1L);
		externalIdentifier.setName("new External Id");
		externalIdentifier.setValue("1");
		externalIdentifier.setSpecimen(specimen);
		HashSet<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();
		externalIdentifierCollection.add(externalIdentifier);
		specimen.setExternalIdentifierCollection(externalIdentifierCollection);
		return specimen;
	}

	public static StorageContainer getContainerToReturn() {
		StorageContainer container = new StorageContainer();
		return container;
	}

	public static SpecimenRequirement getRequirement() {
		SpecimenRequirement requirement = new SpecimenRequirement();
		return requirement;
	}

	public static SpecimenCollectionGroup getScgToReturn() {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setBarcode("new barcode");
		scg.setName("newName");
		scg.setCollectionProtocolRegistration(getCpr());
		return scg;
	}

	private static CollectionProtocolRegistration getCpr() {
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setCollectionProtocol(getCp());
		return cpr;
	}

	private static CollectionProtocol getCp() {
		CollectionProtocol cp =new CollectionProtocol();
		return cp;
	}

	public static UpdateSpecimenEvent getUpdateSpecimenEvent() {
		UpdateSpecimenEvent event = new UpdateSpecimenEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setSpecimenDetail(getSpecimenDetail());
		return event;
	}

	public static CreateSpecimenEvent getCreateSpecimenEvent() {
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setSpecimenDetail(getSpecimenDetail());
		return event;
	}

	private static SpecimenDetail getSpecimenDetail() {
		SpecimenDetail detail = new SpecimenDetail();
		detail.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		detail.setAvailableQuantity(1.0);
		detail.setBarcode("barcode");
		detail.setCollectionStatus(Status.SPECIMEN_COLLECTION_STATUS_COLLECTED.getStatus());
		detail.setComment("comment");
		detail.setContainerName("containerName");
		detail.setCreatedOn(new Date());
		detail.setInitialQuantity(1.0);
		detail.setLabel("label");
		detail.setLineage("New");
		detail.setPathologicalStatus("Not Specified");
		detail.setRequirementId(1l);
		detail.setScgId(1l);
		detail.setSpecimenClass("Fluid");
		detail.setSpecimenType("Feces");
		detail.setTissueSide("Not Specified");
		detail.setTissueSite("Not Specified");

		List<ExternalIdentifierDetail> identifierDetails = new ArrayList<ExternalIdentifierDetail>();
		ExternalIdentifierDetail identifierDetail = new ExternalIdentifierDetail();
		identifierDetail.setName("nnn");
		identifierDetail.setValue("vvv");
		identifierDetails.add(identifierDetail);
		detail.setExternalIdentifierDetails(identifierDetails);

		return detail;
	}

	public static User getUser() {
		User user = new User();
		return user;
	}

	private static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static CreateSpecimenEvent getCreateChildSpecimenEvent() {
		CreateSpecimenEvent event = new CreateSpecimenEvent();
		SpecimenDetail detail = getSpecimenDetail();
//		detail.setScgId(null);
		detail.setParentSpecimenId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setSpecimenDetail(detail);
		return event;
	}

	public static CreateAliquotEvent getCreateAliquotEvent() {
		CreateAliquotEvent event = new CreateAliquotEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setAliquotDetail(getAliquotDetail());
		return event;
	}

	public static CreateAliquotEvent getCreateAliquotToTestInsufficientSpecimenCount() {
		CreateAliquotEvent event = new CreateAliquotEvent();
		event.setSessionDataBean(getSessionDataBean());
		AliquotDetail aliquotDetail = getAliquotDetail();
		aliquotDetail.setNoOfAliquots(100);
		event.setAliquotDetail(aliquotDetail);
		return event;
	}

	public static CreateAliquotEvent getCreateAliquotWithCountZero() {
		CreateAliquotEvent event = new CreateAliquotEvent();
		event.setSessionDataBean(getSessionDataBean());
		AliquotDetail aliquotDetail = getAliquotDetail();
		aliquotDetail.setNoOfAliquots(0);
		event.setAliquotDetail(aliquotDetail);
		return event;
	}

	public static AliquotDetail getAliquotDetail() {
		AliquotDetail aliquotDetail = new AliquotDetail();

		List<StorageLocation> storageLocationList = new ArrayList<StorageLocation>();
		StorageLocation location = new StorageLocation();
		location.setContainerName("New Container");
		location.setPositionX("X");
		location.setPositionY("Y");
		storageLocationList.add(location);

		aliquotDetail.setStorageLocations(storageLocationList);
		aliquotDetail.setNoOfAliquots(1);
		aliquotDetail.setQuantityPerAliquot((double) 1);
		return aliquotDetail;
	}

	public static CreateAliquotEvent getCreateAliquotWithFullContainer() {
		CreateAliquotEvent event = new CreateAliquotEvent();
		event.setSessionDataBean(getSessionDataBean());
		AliquotDetail aliquotDetail = getAliquotWithFullContainer();
		event.setAliquotDetail(aliquotDetail);
		return event;
	}

	public static AliquotDetail getAliquotWithFullContainer() {
		AliquotDetail aliquotDetail = new AliquotDetail();
		List<StorageLocation> storageLocationList = new ArrayList<StorageLocation>();
		StorageLocation location = new StorageLocation();
		location.setContainerName("New Container");
		location.setPositionX("X");
		location.setPositionY("Y");
		storageLocationList.add(location);

		aliquotDetail.setNoOfAliquots(1);
		aliquotDetail.setQuantityPerAliquot((double) 1);
		aliquotDetail.setStorageLocations(storageLocationList);
		return aliquotDetail;
	}

}
