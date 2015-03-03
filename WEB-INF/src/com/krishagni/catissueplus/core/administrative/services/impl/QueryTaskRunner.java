package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.events.SavedQueryDetail;
import com.krishagni.catissueplus.core.de.services.QueryService;

import edu.wustl.common.beans.SessionDataBean;

public class QueryTaskRunner implements ScheduledTask {

	private QueryService querySvc;
	
	@Override
	public void doJob(ScheduledJobDetail detail) throws Exception {
		querySvc = OpenSpecimenAppCtxProvider.getAppCtx().getBean(QueryService.class);
		SavedQueryDetail query = getSavedQuery(detail);
		exportQuery(detail, query);
	}
	
	private SavedQueryDetail getSavedQuery(ScheduledJobDetail detail) {
		Long queryid = getQueryId(detail.getCommand());
		ResponseEvent<SavedQueryDetail> resp = querySvc.getSavedQuery(getRequest(detail, queryid));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	private void exportQuery(ScheduledJobDetail detail, SavedQueryDetail query) {
		ExecuteQueryEventOp input = new ExecuteQueryEventOp();
		input.setCpId(-1L);
		input.setAql(query.getAql());
		input.setSavedQueryId(query.getId());
		input.setForceUseEmail(true);
		input.setAdditionalRecipients(detail.getRecipients());
		input.setRunType("data");
		
		ResponseEvent<QueryDataExportResult> resp = querySvc.exportQueryData(getRequest(detail, input));
		resp.throwErrorIfUnsuccessful();
	}

	private Long getQueryId(String command) {
			return Long.parseLong(command);
	}
	
	private <T> RequestEvent<T> getRequest(ScheduledJobDetail detail, T payload) {
		return new RequestEvent<T>(getSession(detail), payload);
	}
	
	public SessionDataBean getSession(ScheduledJobDetail detail) {
		UserSummary user = detail.getCreatedBy();
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setFirstName(user.getFirstName());
		sessionDataBean.setLastName(user.getLastName());
		sessionDataBean.setUserId(user.getId());
		sessionDataBean.setUserName(user.getLoginName());
		return sessionDataBean;
	}
}
