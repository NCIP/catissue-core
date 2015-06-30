package com.krishagni.openspecimen.custom.le.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.openspecimen.custom.le.events.SpecimenAndFrozenEventDetail;
import com.krishagni.openspecimen.custom.le.events.SpecimenAndFrozenEventDetail.EventDetail;
import com.krishagni.openspecimen.custom.le.services.SpecimenCollectionService;

public class SpecimenCollectionServiceImpl implements SpecimenCollectionService {
	
	private VisitService visitService;
	
	private SpecimenService specimenSvc;
	
	private ConfigurationService cfgSvc;
	
	public void setVisitService(VisitService visitService) {
		this.visitService = visitService;
	}
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> collectVisitsAndSpecimens(RequestEvent<List<VisitSpecimenDetail>> req) {
		List<VisitSpecimenDetail> responses = new ArrayList<VisitSpecimenDetail>();
		
		for (VisitSpecimenDetail detail : req.getPayload()) {
			RequestEvent<VisitSpecimenDetail> subReq = new RequestEvent<VisitSpecimenDetail>(detail);
			ResponseEvent<VisitSpecimenDetail> subResp = visitService.collectVisitAndSpecimens(subReq);
			if (!subResp.isSuccessful()) {
				return ResponseEvent.error(subResp.getError());
			}
						
			responses.add(subResp.getPayload());
		}
		
		return new ResponseEvent<List<VisitSpecimenDetail>>(responses);
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenAndFrozenEventDetail> collectChildSpecimens(RequestEvent<SpecimenAndFrozenEventDetail> req) {
		try {		
			SpecimenAndFrozenEventDetail detail = req.getPayload();
			
			//
			// Step 1: collect child specimens
			//
			RequestEvent<List<SpecimenDetail>> collectChildSpmnsReq = new RequestEvent<List<SpecimenDetail>>(detail.getSpecimens());
			ResponseEvent<List<SpecimenDetail>> collectChildSpmnsResp = specimenSvc.collectSpecimens(collectChildSpmnsReq);
			collectChildSpmnsResp.throwErrorIfUnsuccessful();
	
			//
			// Step 2: add frozen events
			//
			addFrozenEvents(getReqSpecimenIdMap(collectChildSpmnsResp.getPayload()), detail.getEvents());
			
			//
			// Step 3: construct response
			//			
			SpecimenAndFrozenEventDetail respDetail = new SpecimenAndFrozenEventDetail();
			respDetail.setSpecimens(collectChildSpmnsResp.getPayload());
			respDetail.setEvents(detail.getEvents());
			return ResponseEvent.response(respDetail);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private Map<String, Long> getReqSpecimenIdMap(List<SpecimenDetail> specimens) {
		Map<String, Long> result = new HashMap<String, Long>();
		
		if (specimens == null) {
			return result;
		}
		
		for (SpecimenDetail specimen : specimens) {
			result.put(specimen.getVisitId() + "-" + specimen.getReqId(), specimen.getId());
			result.putAll(getReqSpecimenIdMap(specimen.getChildren()));			
		}
		
		return result;
	}
	
	private void addFrozenEvents(Map<String, Long> reqSpecimenIdMap, List<EventDetail> events) {
		for (EventDetail event : events) {
			Long specimenId = event.getSpecimenId();
			if (specimenId == null && event.getVisitId() != null && event.getReqId() != null) {
				specimenId = reqSpecimenIdMap.get(event.getVisitId() + "-" + event.getReqId());
				event.setSpecimenId(specimenId);
			}

			if (specimenId == null) {
				continue;
			}
			
			Long userId = AuthUtil.getCurrentUser().getId();
			if (event.getUser() != null && event.getUser().getId() != null) {
				userId = event.getUser().getId();
			}
			
			Date time = Calendar.getInstance().getTime();
			if (event.getTime() != null) {
				time = event.getTime();
			}
			
			event.setId(addFrozenEvent(specimenId, userId, time));
		}
	}
		
	private Long addFrozenEvent(Long specimenId, Long userId, Date time) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("user", userId);
		values.put("time", new SimpleDateFormat(cfgSvc.getDeDateTimeFormat()).format(time));		
		return DeObject.saveFormData("SpecimenFrozenEvent", "SpecimenEvent", -1L, specimenId, values);		
	}
}
