package com.krishagni.catissueplus.core.importer.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.importer.domain.ImportJob;
import com.krishagni.catissueplus.core.importer.domain.ImportJob.Status;
import com.krishagni.catissueplus.core.importer.domain.ImportJob.Type;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.events.ImportDetail;
import com.krishagni.catissueplus.core.importer.events.ImportJobDetail;
import com.krishagni.catissueplus.core.importer.repository.ImportJobDao;
import com.krishagni.catissueplus.core.importer.services.ImportService;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;
import com.krishagni.catissueplus.core.importer.services.ObjectImporterFactory;
import com.krishagni.catissueplus.core.importer.services.ObjectReader;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;

public class ImportServiceImpl implements ImportService {
	private ConfigurationService cfgSvc;
	
	private ImportJobDao importJobDao;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private ObjectSchemaFactory schemaFactory;
	
	private ObjectImporterFactory importerFactory;
	
	private PlatformTransactionManager transactionManager;
	
	private TransactionTemplate txTmpl;
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public void setImportJobDao(ImportJobDao importJobDao) {
		this.importJobDao = importJobDao;
	}
	
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setSchemaFactory(ObjectSchemaFactory schemaFactory) {
		this.schemaFactory = schemaFactory;
	}

	public void setImporterFactory(ObjectImporterFactory importerFactory) {
		this.importerFactory = importerFactory;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		this.txTmpl = new TransactionTemplate(this.transactionManager);
		this.txTmpl.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	}

	@Override
	public ResponseEvent<String> uploadImportJobFile(RequestEvent<InputStream> req) {
		OutputStream out = null;
		
		try {
			//
			// 1. Ensure import directory is present
			//
			String importDir = getImportDir();			
			new File(importDir).mkdirs();
			
			//
			// 2. Generate unique file ID
			//
			String fileId = UUID.randomUUID().toString();
			
			//
			// 3. Copy uploaded file to import directory
			//
			InputStream in = req.getPayload();			
			out = new FileOutputStream(importDir + File.separator + fileId);
			IOUtils.copy(in, out);
			
			return ResponseEvent.response(fileId);			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}	
	
	@Override
	@PlusTransactional
	public ResponseEvent<ImportJobDetail> importObjects(RequestEvent<ImportDetail> req) {
		try {
			ImportDetail detail = req.getPayload();
			ImportJob job = getImportJob(detail);						
			importJobDao.saveOrUpdate(job, true);
			
			//
			// Set up file in job's directory
			//
			String inputFile = getFilePath(detail.getInputFileId());
			createJobDir(job.getId());
			moveToJobDir(inputFile, job.getId());
			
			taskExecutor.submit(new ImporterTask(detail, job));
			return ResponseEvent.response(ImportJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);			
		}		
	}
	
	
	private String getDataDir() {
		return cfgSvc.getStrSetting("common", "data_dir", ".");
	}
	
	private String getImportDir() {
		return getDataDir() + File.separator + "bulk-import";
	}
	
	private String getFilePath(String fileId) { 
		 return getImportDir() + File.separator + fileId;
	}
	
	private String getJobsDir() {
		return getDataDir() + File.separator + "bulk-import" + File.separator + "jobs";
	}
	
	private String getJobDir(Long jobId) {
		return getJobsDir() + File.separator + jobId;
	}
	
	private boolean createJobDir(Long jobId) {
		return new File(getJobDir(jobId)).mkdirs();
	}
	
	private boolean moveToJobDir(String file, Long jobId) {
		File src = new File(file);
		File dest = new File(getJobDir(jobId) + File.separator + "input.csv");
		return src.renameTo(dest);		
	}
	
	private ImportJob getImportJob(ImportDetail detail) { // TODO: ensure checks are done
		ImportJob job = new ImportJob();
		job.setCreatedBy(AuthUtil.getCurrentUser());
		job.setCreationTime(Calendar.getInstance().getTime());
		job.setName(detail.getObjectType());
		job.setStatus(Status.IN_PROGRESS);
		
		String importType = detail.getImportType();
		job.setType(StringUtils.isBlank(importType) ? Type.CREATE : Type.valueOf(importType));		
		return job;		
	}
	
	private class ImporterTask implements Runnable {
		private ImportDetail detail;
		
		private ImportJob job;
		
		public ImporterTask(ImportDetail detail, ImportJob job) {
			this.detail = detail;
			this.job = job;
		}

		@Override
		public void run() {
			ObjectSchema   schema   = schemaFactory.getSchema(job.getName());
			ObjectImporter importer = importerFactory.getImporter(job.getName());
			
			ObjectReader reader = null;			
			try {
				String filePath = getJobDir(job.getId()) + File.separator + "input.csv";
				reader = new ObjectReader(filePath, schema);
				
				Object object = null;
				while ((object = reader.next()) != null) {
					importObject(importer, object);
				}
			} catch (Exception e) {
				
			}			
		}		
		
		private void importObject(final ObjectImporter importer, Object object) {
			try {
				final RequestEvent<Object> req = new RequestEvent<Object>(object);
				ResponseEvent<Object> resp = txTmpl.execute(
						new TransactionCallback<ResponseEvent<Object>>() {
							@Override
							public ResponseEvent<Object> doInTransaction(TransactionStatus status) {
								return importer.importObject(req);
							}
						});
				
				if (resp.isSuccessful()) {
					System.err.println("Success");
				} else {
					System.err.println("Unsuccessful");
				}				
			} catch (Exception e) {
				// TODO:
			}
		}
	}

}
