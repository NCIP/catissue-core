package com.krishagni.catissueplus.core.init;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import krishagni.catissueplus.beans.FormContextBean;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.service.TemplateService;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public abstract class ImportForms implements InitializingBean {
	private PlatformTransactionManager txnMgr;
	
	private UserDao userDao;
	
	private DaoFactory daoFactory;
	
	private TemplateService templateService;
	
	//
	// This is to ensure DE is initialized before importing forms 
	//
	private DeInitializer deInitializer;
	
	private boolean createTable = false;
	
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
	
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public void setDeInitializer(DeInitializer deInitializer) {
		this.deInitializer = deInitializer;
	}

	public boolean isCreateTable() {
		return createTable;
	}

	public void setCreateTable(boolean createTable) {
		this.createTable = createTable;
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
	
	protected Map<String, Object> getTemplateProps() {
		return new HashMap<String, Object>();
	}
		
	private void importForms(Collection<String> formFiles) 
	throws Exception {
		User sysUser = userDao.getSystemUser();
		UserContext userCtx = getUserContext(sysUser);
		
		for (String formFile : formFiles) {
			InputStream in = null;
			try {			
				in = preprocessForms(formFile);
				String existingDigest = daoFactory.getFormDao().getFormChangeLogDigest(formFile);
				String newDigest = Utility.getInputStreamDigest(in);
				if (existingDigest != null && existingDigest.equals(newDigest)) {
					continue;
				}
				
				in.reset();
				Long formId = Container.createContainer(userCtx, in, isCreateTable());				
				saveOrUpdateFormCtx(formFile, formId);
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
	
	private void saveOrUpdateFormCtx(String formFile, Long formId) {
		daoFactory.getFormDao().saveOrUpdate(getFormContext(formFile, formId));
	}
		
	private InputStream preprocessForms(String formFile) {
		String template = templateService.render(formFile, getTemplateProps()); 
		return new ByteArrayInputStream( template.getBytes() );
	}
}
