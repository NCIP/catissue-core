package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.ExecuteSavedQueryOp;
import com.krishagni.catissueplus.core.de.events.FacetDetail;
import com.krishagni.catissueplus.core.de.events.GetFacetValuesOp;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.events.QueryExecResult;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.common.dynamicextensions.nutility.IoUtil;

@Controller
@RequestMapping("/query")
public class QueryController {
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private QueryService querySvc;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody	
	public QueryExecResult executeQuery(@RequestBody ExecuteQueryEventOp opDetail) {
		return response(querySvc.executeQuery(request(opDetail)));
	}

	@RequestMapping(method = RequestMethod.POST, value="/{queryId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryExecResult executeQuery(
			@PathVariable("queryId")
			long queryId,

			@RequestBody
			ExecuteSavedQueryOp opDetail) {

		opDetail.setSavedQueryId(queryId);
		return response(querySvc.executeSavedQuery(request(opDetail)));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/export")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public QueryDataExportResult exportQueryData(@RequestBody ExecuteQueryEventOp opDetail) {
		return response(querySvc.exportQueryData(request(opDetail)));
	}	
	
	@RequestMapping(method = RequestMethod.GET, value="/export")
	@ResponseStatus(HttpStatus.OK)
	public void downloadExportDataFile(
			@RequestParam(value = "fileId", required = true) 
			String fileId,
			
			@RequestParam(value = "filename", required = false, defaultValue = "QueryResults.csv")
			String filename,
			
			HttpServletResponse response) {
		
		File file = response(querySvc.getExportDataFile(request(fileId)));

		response.setContentType("text/csv;");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

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

	@RequestMapping(method = RequestMethod.POST, value="/facet-values")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<FacetDetail> getFacetValues(@RequestBody GetFacetValuesOp op) {
		return response(querySvc.getFacetValues(request(op)));
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<T>(payload);				
	}
}
