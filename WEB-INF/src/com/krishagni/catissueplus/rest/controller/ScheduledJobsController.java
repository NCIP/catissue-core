package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

@Controller
@RequestMapping("/scheduled-jobs")
public class ScheduledJobsController {
	
	@Autowired
	private ScheduledJobService jobSvc;
	
	@Autowired
	private HttpServletRequest httpReq;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ScheduledJobDetail> getScheduledJobs(
			@RequestParam(value = "query", required = false) 
			String query,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxRecords", required = false, defaultValue = "100")
			int maxRecords
			) {
		ScheduledJobListCriteria criteria = new ScheduledJobListCriteria()
				.query(query)
				.startAt(startAt)
				.maxResults(maxRecords);
		
		ResponseEvent<List<ScheduledJobDetail>> resp = jobSvc.getScheduledJobs(getRequest(criteria));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail createScheduledJob(@RequestBody ScheduledJobDetail detail) {
		detail.setId(null);
		ResponseEvent<ScheduledJobDetail> resp = jobSvc.createScheduledJob(getRequest(detail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail deleteScheduledJob(@PathVariable("id") Long jobId) {
		ResponseEvent<ScheduledJobDetail> resp = jobSvc.deleteScheduledJob(getRequest(jobId));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(getSession(), payload);
	}
	
	private SessionDataBean getSession() {
		return (SessionDataBean) httpReq.getSession().getAttribute(Constants.SESSION_DATA);
	}
}
