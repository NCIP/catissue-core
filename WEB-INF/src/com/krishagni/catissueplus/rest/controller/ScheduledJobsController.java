package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.krishagni.catissueplus.core.administrative.events.JobExportDetail;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobRunDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.common.dynamicextensions.nutility.IoUtil;

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
			int maxRecords) {
		
		ScheduledJobListCriteria criteria = new ScheduledJobListCriteria()
				.query(query)
				.startAt(startAt)
				.maxResults(maxRecords);		
		return response(jobSvc.getScheduledJobs(getRequest(criteria)));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Long> getScheduledJobsCount(
			@RequestParam(value = "query", required = false) 
			String query) {
		
		ResponseEvent<Long> resp = jobSvc.getScheduledJobsCount(getRequest(new ScheduledJobListCriteria().query(query)));
		return Collections.singletonMap("count", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail getScheduledJob(
			@PathVariable("id") 
			Long jobId) {
		
		return response(jobSvc.getScheduledJob(getRequest(jobId)));
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

	@RequestMapping(method = RequestMethod.DELETE, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail deleteScheduledJob(
			@PathVariable("id") 
			Long jobId) {
		
		return response(jobSvc.deleteScheduledJob(getRequest(jobId)));
	}

	@RequestMapping(method = RequestMethod.GET, value="{jobId}/runs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ScheduledJobRunDetail> getScheduledJobRuns(
			@PathVariable(value = "jobId") 
			Long jobId,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxRecords", required = false, defaultValue = "100")
			int maxRecords) {
		
		JobRunsListCriteria criteria = new JobRunsListCriteria()
				.startAt(startAt)
				.maxResults(maxRecords)
				.scheduledJobId(jobId);
		return response(jobSvc.getJobRuns(getRequest(criteria)));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "{jobId}/runs/{runId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobRunDetail getJobRun(
			@PathVariable("jobId")
			Long jobId,
			
			@PathVariable("runId")
			Long runId) {
		return response(jobSvc.getJobRun(getRequest(runId)));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="{jobId}/runs")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobDetail executeJob(
			@PathVariable("jobId") 
			Long jobId, 
			
			@RequestBody 
			Map<String, String> body) {

		ScheduledJobRunDetail detail = new ScheduledJobRunDetail();
		detail.setJobId(jobId);
		detail.setRtArgs(body.get("args"));
		return response(jobSvc.executeJob(getRequest(detail)));
	}
		
	
	@RequestMapping(method = RequestMethod.GET, value = "{jobId}/runs/{runId}/result-file")
	@ResponseStatus(HttpStatus.OK)
	public void downloadExportDataFile(
			@PathVariable("jobId")
			Long jobId,
			
			@PathVariable("runId")
			Long runId,
			
			HttpServletResponse response) {
		
		JobExportDetail detail = response(jobSvc.getJobResultFile(getRequest(runId)));

		File file = detail.getFile();
		response.setContentType(Utility.getContentType(file));
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			IoUtil.copy(in, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
