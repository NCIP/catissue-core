package com.krishagni.catissueplus.core.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

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

public abstract class ImportForms implements InitializingBean {
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
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void afterPropertiesSet() 
	throws Exception {
		try {
			importForms();
		} finally {
			cleanup();
		}
	}
	
	public void importForms()
	throws Exception {
		final Collection<String> formFiles = listFormFiles();
		
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
	
	protected abstract Collection<String> listFormFiles() throws IOException;
	
	protected abstract FormContextBean getFormContext(String formFile, Long formId);
	
	protected abstract void cleanup();
		
	private void importForms(Collection<String> formFiles) 
	throws Exception {
		User sysUser = userDao.getSystemUser();
		UserContext userCtx = getUserContext(sysUser);
		
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
					insertFormContext(formFile, formId);
				}
				
				daoFactory.getFormDao().insertFormChangeLog(formFile, newDigest, formId);				
			} finally {
				IOUtils.closeQuietly(in);
			}
		}		
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
	
	private void insertFormContext(String formFile, Long formId) {
		daoFactory.getFormDao().saveOrUpdate(getFormContext(formFile, formId));
	}
}
