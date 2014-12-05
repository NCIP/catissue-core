package com.krishagni.catissueplus.core.services.testdata;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateScgReportEvent;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.common.beans.SessionDataBean;


public class ScgTestData {

	public static final Object SCG_REPORTS = "scg reports";

	public static AddVisitEvent getAddVisitEventServerErr() {
		return getAddVisitEvent();
	}
	
	public static SpecimenCollectionGroup getScgToReturn() {
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setBarcode("new barcode");
		scg.setName("newName");
		Site site = new Site();
		site.setName("dsad");
		scg.setCollectionSite(site);
		scg.setCollectionProtocolEvent(new CollectionProtocolEvent());
		scg.setCollectionProtocolRegistration(new CollectionProtocolRegistration());
		return scg;
	}
	public static UpdateScgEvent getupdateScgEventServerErr() {
		return getUpdateScgEvent();
	}

	public static UpdateScgEvent getUpdateScgEvent() {
		UpdateScgEvent event = new UpdateScgEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setScgDetail(getVisitDetail(1l));
		return event;
	}

	public static User getUser() {
		User user = new User();
		return user;
	}
	
    public static Site getSite(String string) {
        Site site = new Site();
        site.setName(string);
        return site;
    }
    
	public static CollectionProtocolEvent getCpe() {
		CollectionProtocolEvent cpe = new CollectionProtocolEvent();
		cpe.setCollectionProtocol(getCp(1l));
		return cpe;
	}
	
	private static CollectionProtocol getCp(Long id) {
		CollectionProtocol cp = new CollectionProtocol();
		cp.setId(id);
		return cp;
	}

	public static CollectionProtocolRegistration getCpr(Long cpId) {
		CollectionProtocolRegistration registrationDetails = new CollectionProtocolRegistration();
		registrationDetails.setRegistrationDate(new Date());
		registrationDetails.setBarcode("barcode1");
		registrationDetails.setId(cpId);
		registrationDetails.setCollectionProtocol(getCp(cpId));
		return registrationDetails;
	}

	public static AddVisitEvent getAddVisitEvent() {
		Long cprId = 1l;
		AddVisitEvent event = new AddVisitEvent();
		event.setSessionDataBean(getSessionDataBean());
		event.setCprId(cprId);
		event.setVisit(getVisitDetail(cprId));
		return event;
	}

	private static VisitDetail getVisitDetail(Long cprId) {
		VisitDetail detail = new VisitDetail();
		detail.setCprId(cprId);
		detail.setCpeId(cprId);
		detail.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		detail.setBarcode("barcode");
		detail.setClinicalDiagnosis("Not Specified");
		detail.setClinicalStatus("Not Specified");
		detail.setComment("comment");
		detail.setName("name");
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
	
	public static UpdateScgReportEvent getUpdateScgReportEvent() {
		UpdateScgReportEvent event = new UpdateScgReportEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setDetail(getScgReportDetail());
		return event;
	}

	private static ScgReportDetail getScgReportDetail() {
		ScgReportDetail details = new ScgReportDetail();
		details.setDeIdentifiedReport("ABCDEFGHIJKLMNOPQRSTVWXYZ");
		details.setIdentifiedReport("ABCDEFGHIJKLMNOPQRSTVWXYZ");
		return details;
	}

	public static UpdateScgReportEvent getUpdateScgReportEventWithNullValues() {
		UpdateScgReportEvent event = new UpdateScgReportEvent();
		event.setId(1l);
		event.setSessionDataBean(getSessionDataBean());
		event.setDetail(new ScgReportDetail());
		return event;
	}

}
