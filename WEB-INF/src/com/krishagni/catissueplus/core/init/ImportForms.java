package com.krishagni.catissueplus.core.init;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
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
import com.krishagni.catissueplus.core.common.util.AuthUtil;
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
					AuthUtil.setCurrentUser(getSystemUser());
					importForms(formFiles);
					return null;
				} catch (Exception e) {
					status.setRollbackOnly();
					throw new RuntimeException(e);
				} finally {
					AuthUtil.clearCurrentUser();
				}
			}
		});
	}
	
	protected abstract Collection<String> listFormFiles() throws IOException;
	
	protected abstract boolean isSysForm(String formFile);

	protected abstract FormContextBean getFormContext(String formFile, Long formId);

	protected void saveOrUpdateFormCtx(String formFile, Long formId) {
		daoFactory.getFormDao().saveOrUpdate(getFormContext(formFile, formId));
	}

	protected abstract void cleanup();

	protected Map<String, Object> getTemplateProps() {
		return new HashMap<>();
	}

	private void importForms(Collection<String> formFiles)
	throws Exception {
		UserContext userCtx = getUserContext(getSystemUser());

		for (String formFile : formFiles) {
			InputStream in = null;
			try {
				in = preprocessForms(formFile);

				Object[] changeLog = daoFactory.getFormDao().getLatestFormChangeLog(formFile);
				String existingDigest  = (changeLog != null) ? (String) changeLog[2] : null;
				String newDigest = Utility.getInputStreamDigest(in);
				if (existingDigest != null && existingDigest.equals(newDigest)) {
					continue; // form XML has not got modified since last import
				}

				Long formId     = (changeLog != null) ? (Long) changeLog[1] : null;
				Date importDate = (changeLog != null) ? (Date) changeLog[3] : null;
				if (!isSysForm(formFile) && formId != null && importDate != null) {
					//
					// Non system form. Need to check whether the form got modified since last import
					//
					Date updateDate = daoFactory.getFormDao().getUpdateTime(formId);
					if (updateDate != null && updateDate.after(importDate)) {
						continue;
					}
				}

				in.reset();
				formId = Container.createContainer(userCtx, in, isCreateTable());
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

	private User getSystemUser() {
		return userDao.getSystemUser();
	}

	private InputStream preprocessForms(String formFile) {
		String template = templateService.render(formFile, getTemplateProps());
		return new ByteArrayInputStream( template.getBytes() );
	}
}
