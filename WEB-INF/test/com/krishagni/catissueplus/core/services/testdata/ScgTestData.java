package com.krishagni.catissueplus.core.services.testdata;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.common.beans.SessionDataBean;


public class ScgTestData {

	public static CreateScgEvent getCreateScgEventServerErr() {
		return getCreateScgEvent();
	}
	
	public static SpecimenCollectionGroup getScgToReturn() {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setBarcode("new barcode");
		scg.setName("newName");
		return scg;
	}
	public static UpdateScgEvent getupdateScgEventServerErr() {
		return getUpdateScgEvent();
	}

	public static UpdateScgEvent getUpdateScgEvent() {
		UpdateScgEvent event = new UpdateScgEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setScgDetail(getScgDetail(1l));
		return event;
	}

	public static User getUser() {
		User user = new User();
		return user;
	}
	public static CollectionProtocolEvent getCpe() {
		CollectionProtocolEvent cpe = new CollectionProtocolEvent();
		return cpe;
	}
	
	public static CollectionProtocolRegistration getCpr(Long cpId) {
		CollectionProtocolRegistration registrationDetails = new CollectionProtocolRegistration();
		registrationDetails.setRegistrationDate(new Date());
		registrationDetails.setBarcode("barcode1");
		registrationDetails.setId(cpId);
		return registrationDetails;
	}

	public static CreateScgEvent getCreateScgEvent() {
		Long cprId = 1l;
		CreateScgEvent event = new CreateScgEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setCprId(cprId);
		event.setScgDetail(getScgDetail(cprId));
		return event;
	}

	private static ScgDetail getScgDetail(Long cprId) {
		ScgDetail detail = new ScgDetail();
		detail.setCprId(cprId);
		detail.setCpeId(cprId);
		detail.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		detail.setBarcode("barcode");
		detail.setClinicalDiagnosis("Not Specified");
		detail.setClinicalStatus("Not Specified");
		detail.setCollectionComments("collectionComments");
		detail.setCollectionContainer("CPT");
		detail.setCollectionProcedure("Lavage");
		detail.setCollectionSiteName("Site");
		detail.setCollectionStatus(Status.SCG_COLLECTION_STATUS_COMPLETED.getStatus());
		detail.setCollectionTimestamp(new Date());
		detail.setCollectorName("admin@admin.com");
		detail.setComment("comment");
		detail.setName("name");
		detail.setReceivedComments("receivedComments");
		detail.setReceivedQuality("Clotted");
		detail.setReceivedTimestamp(new Date());
		detail.setReceiverName("admin@admin.com");
		detail.setSurgicalPathologyNumber("surgicalPathologyNumber");
		return detail;
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
}
