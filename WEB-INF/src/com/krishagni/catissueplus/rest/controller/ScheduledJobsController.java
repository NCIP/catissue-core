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
		
		return response(jobSvc.getScheduledJobs(getRequest(criteria)));
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail createScheduledJob(@RequestBody ScheduledJobDetail detail) {
		return response(jobSvc.createScheduledJob(getRequest(detail)));
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail updateScheduledJob(
			@PathVariable("id") 
			Long jobId,
			
			@RequestBody 
			ScheduledJobDetail detail) {

		detail.setId(jobId);
		return response(jobSvc.updateScheduledJob(getRequest(detail)));
	}


	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail getScheduledJob(
			@PathVariable("id") Long jobId) {
		return response(jobSvc.getScheduledJob(getRequest(jobId)));
	}


	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail deleteScheduledJob(@PathVariable("id") Long jobId) {
		return response(jobSvc.deleteScheduledJob(getRequest(jobId)));
	}

	@RequestMapping(method = RequestMethod.POST, value="/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail executeJob(@PathVariable("id") Long jobId) {
		return response(jobSvc.executeJob(getRequest(jobId)));
	}


	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
