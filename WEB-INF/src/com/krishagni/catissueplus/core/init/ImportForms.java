package com.krishagni.catissueplus.core.init;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import krishagni.catissueplus.beans.FormContextBean;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	private MessageSource messageSource;
	
	//
	// This is to ensure DE is initialized before importing forms 
	//
	private DeInitializer deInitializer;
	
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

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setDeInitializer(DeInitializer deInitializer) {
		this.deInitializer = deInitializer;
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
				in = preProcessDefForms(formFile);
				String existingDigest = daoFactory.getFormDao().getFormChangeLogDigest(formFile);
				String newDigest = Utility.getInputStreamDigest(in);
				if (existingDigest != null && existingDigest.equals(newDigest)) {
					continue;
				}
				
				in.reset();
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
	
	private InputStream preProcessDefForms(String formFile) throws Exception{
		InputStream in = Utility.getResourceInputStream(formFile);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);
			
			Locale locale = Locale.getDefault();
			NodeList captionList = doc.getElementsByTagName("caption");
			for (int i = 0 ; i < captionList.getLength(); i++) {
				Node node = captionList.item(i);
				String val = node.getFirstChild().getNodeValue().trim();
				if (val.startsWith("@@") && val.endsWith("@@")) {
					String captionVal = messageSource.getMessage(val.replace("@", ""), null, locale);
					node.getFirstChild().setNodeValue(captionVal);
					System.out.println("Caption " + val + " is replaced by " + captionVal);
				}
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(outputStream);
			TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} finally {
			IOUtils.closeQuietly(in); 
		}
	}
}
