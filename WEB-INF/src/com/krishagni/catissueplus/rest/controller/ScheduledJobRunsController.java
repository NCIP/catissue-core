package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.JobExportDetail;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobRunDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.nutility.IoUtil;

@Controller
@RequestMapping("/scheduled-job-runs")
public class ScheduledJobRunsController {
	@Autowired
	private ScheduledJobService jobSvc;
	
	@Autowired
	private HttpServletRequest httpReq;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ScheduledJobRunDetail> getScheduledJobRuns(
			@RequestParam(value = "scheduledJobId", required = false) 
			Long scheduledJobId,
			
			@RequestParam(value = "startAt", required = false, defaultValue = "0")
			int startAt,
			
			@RequestParam(value = "maxRecords", required = false, defaultValue = "100")
			int maxRecords
			) {
		JobRunsListCriteria criteria = new JobRunsListCriteria()
				.startAt(startAt)
				.maxResults(maxRecords)
				.scheduledJobId(scheduledJobId);
		return response(jobSvc.getJobRuns(getRequest(criteria)));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ScheduledJobRunDetail getJobRun(@PathVariable("id") Long id) {
		return response(jobSvc.getJobRun(getRequest(id)));
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}/output-file")
	@ResponseStatus(HttpStatus.OK)
	public void downloadExportDataFile(
			@PathVariable("id") Long id,
			HttpServletResponse response) {
		
		JobExportDetail detail = response(jobSvc.getExportDataFile(getRequest(id)));

		File file = detail.getFile();
		ScheduledJobRunDetail jobRun = detail.getJobRun();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm");
		String fileName = jobRun.getScheduledJob().getName() + "_" + dateFormat.format(jobRun.getStartedAt());
		
		response.setContentType("text/csv;");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			
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
