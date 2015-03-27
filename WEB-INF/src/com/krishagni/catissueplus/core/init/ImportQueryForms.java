package com.krishagni.catissueplus.core.init;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import krishagni.catissueplus.beans.FormContextBean;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public class ImportQueryForms implements InitializingBean {
	private PlatformTransactionManager txnMgr;
	
	private UserDao userDao;
	
	private DaoFactory daoFactory;
	
	public PlatformTransactionManager getTxnMgr() {
		return txnMgr;
	}

	public void setTxnMgr(PlatformTransactionManager txnMgr) {
		this.txnMgr = txnMgr;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final String[] formFiles = listFormFiles();
		
		TransactionTemplate txnTmpl = new TransactionTemplate(txnMgr);
		txnTmpl.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		txnTmpl.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				try {
					importForms(formFiles);
					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					throw new RuntimeException(e);					
				}				
			}
		});
	}
		
	private void importForms(String[] formFiles) 
	throws Exception {
		User sysUser = userDao.getSystemUser();
		UserContext userCtx = getUserContext(sysUser);
		
		int order = 1;
		for (String formFile : formFiles) {
			InputStream in = null;
			try {				
				String existingDigest = daoFactory.getFormDao().getFormChangeLogDigest(formFile);
				String newDigest = Utility.getResourceDigest(formFile);
				if (existingDigest != null && existingDigest.equals(newDigest)) {
					continue;
				}
				
				in = Utility.getResourceInputStream(formFile);
				Long formId = Container.createContainer(userCtx, in, false);				
				if (existingDigest == null) {
					insertQueryFormContext(formId, order);
				}
				
				++order;
				daoFactory.getFormDao().insertFormChangeLog(formFile, newDigest, formId);				
			} finally {
				IOUtils.closeQuietly(in);
			}
		}		
	}
		
	private void insertQueryFormContext(Long formId, int sortOrder) {
		FormContextBean formCtx = new FormContextBean();
		formCtx.setContainerId(formId);
		formCtx.setCpId(-1L);
		formCtx.setEntityType("Query");
		formCtx.setMultiRecord(false);
		formCtx.setSortOrder(sortOrder);
		formCtx.setSysForm(true);
		daoFactory.getFormDao().saveOrUpdate(formCtx);		
	}
	
	private UserContext getUserContext(final User user) {
		return new UserContext() {			
			@Override
			public String getUserName() {				
				return user.getLoginName();
			}
			
			@Override
			public Long getUserId() {
				return user.getId();
			}
			
			@Override
			public String getIpAddress() {
				return null;
			}
		};
	}
	
	private String[] listFormFiles() 
	throws Exception {
		BufferedReader reader = null;
		InputStream in = null;
		try {
			in = Utility.getResourceInputStream("/query-forms/list.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			
			List<String> files = new ArrayList<String>();
			String file = null;
			while ((file = reader.readLine()) != null) {
				files.add("/query-forms/" + file);
			}
			
			return files.toArray(new String[0]);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}		
	}
}
