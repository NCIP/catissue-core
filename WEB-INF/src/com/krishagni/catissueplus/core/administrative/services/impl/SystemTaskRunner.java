package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobInstanceDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.email.EmailHandler;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SystemTaskRunner implements ScheduledTask {

	private ScheduledJobService jobSvc;
	
	@Override
	public void doJob(ScheduledJobDetail jobDetail) throws Exception {
		jobSvc = OpenSpecimenAppCtxProvider.getAppCtx().getBean(ScheduledJobService.class);

		ScheduledJobInstanceDetail jobInstance = null;
		jobInstance = createJobInstance(jobDetail);
		
		Long jobInstanceId = jobInstance.getId();
		executeProcess(jobDetail, jobInstanceId);
		
		String dropFilePath = getDropFilePath(jobInstanceId);
		sendEmail(jobDetail, dropFilePath);
	}
	
	private void executeProcess(ScheduledJobDetail jobDetail, Long jobInstanceId) {
		String command = new String(jobDetail.getCommand());
		command += " " + jobInstanceId.toString();
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendEmail(ScheduledJobDetail detail, String dropFilePath) {
		DaoFactory daoFactory = OpenSpecimenAppCtxProvider.getAppCtx().getBean(DaoFactory.class);
		
		Transaction txn = startTxn(daoFactory);
		User user = daoFactory.getUserDao().getById(detail.getCreatedBy().getId());
		txn.commit();
		
		String filename = UUID.randomUUID().toString();
		String path = ScheduledTaskManager.EXPORT_DATA_DIR + filename;

		try {
			String subject = detail.getName();
			boolean isSuccessful = false;
			if (StringUtils.isBlank(dropFilePath)) {
				subject += " has failed!";
			} else {
				isSuccessful = true;
				subject += " has completed successfully";
				FileUtils.copyFile(new File(dropFilePath), new File(path));
			}
			
			EmailHandler.sendJobCompletedEmail(user, filename, detail.getRecipients(), detail.getName(), subject, isSuccessful);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private Transaction startTxn(DaoFactory daoFactory) {
		AbstractDao<?> dao = (AbstractDao<?>)daoFactory.getContainerDao();
		Session session = dao.getSessionFactory().getCurrentSession();
		Transaction txn = session.getTransaction();
		if (txn == null || !txn.isActive()) {
			txn = session.beginTransaction();			
		}
		
		return txn;
	}
	
	private String getDropFilePath(Long jobInstanceId) {
		RequestEvent<Long> req = new RequestEvent<Long>();
		req.setPayload(jobInstanceId);
		ResponseEvent<ScheduledJobInstanceDetail> resp = jobSvc.getJobInstance(req);
		resp.throwErrorIfUnsuccessful();
		
		ScheduledJobInstanceDetail detail = resp.getPayload();
		if (detail.getStatus().equals("SUCCEEDED")) {
			return detail.getLogFilePath();
		}
		
		return null;
	}
	
	private ScheduledJobInstanceDetail createJobInstance(ScheduledJobDetail jobDetail) {
		ScheduledJobInstanceDetail instance = new ScheduledJobInstanceDetail();
		instance.setScheduledJob(jobDetail);
		instance.setStatus("PENDING");
		
		ResponseEvent<ScheduledJobInstanceDetail> resp = jobSvc.createScheduledJobInstance(new RequestEvent<ScheduledJobInstanceDetail>(null, instance));
		resp.throwErrorIfUnsuccessful();
		instance = resp.getPayload();
		return instance;
	}
}
