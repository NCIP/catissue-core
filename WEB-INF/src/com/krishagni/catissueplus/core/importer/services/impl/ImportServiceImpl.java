package com.krishagni.catissueplus.core.importer.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.MergedObject;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;
import com.krishagni.catissueplus.core.importer.domain.ImportJob;
import com.krishagni.catissueplus.core.importer.domain.ImportJob.CsvType;
import com.krishagni.catissueplus.core.importer.domain.ImportJob.Status;
import com.krishagni.catissueplus.core.importer.domain.ImportJob.Type;
import com.krishagni.catissueplus.core.importer.domain.ImportJobErrorCode;
import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;
import com.krishagni.catissueplus.core.importer.events.FileRecordsDetail;
import com.krishagni.catissueplus.core.importer.events.ImportDetail;
import com.krishagni.catissueplus.core.importer.events.ImportJobDetail;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.events.ObjectSchemaCriteria;
import com.krishagni.catissueplus.core.importer.repository.ImportJobDao;
import com.krishagni.catissueplus.core.importer.repository.ListImportJobsCriteria;
import com.krishagni.catissueplus.core.importer.services.ImportListener;
import com.krishagni.catissueplus.core.importer.services.ImportService;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;
import com.krishagni.catissueplus.core.importer.services.ObjectImporterFactory;
import com.krishagni.catissueplus.core.importer.services.ObjectReader;
import com.krishagni.catissueplus.core.importer.services.ObjectSchemaFactory;

import edu.common.dynamicextensions.query.cachestore.LinkedEhCacheMap;

public class ImportServiceImpl implements ImportService {
	private static final Log logger = LogFactory.getLog(ImportServiceImpl.class);

	private static final int MAX_RECS_PER_TXN = 10000;

	private static final String CFG_MAX_TXN_SIZE = "import_max_records_per_txn";

	private static Map<Long, ImportJob> runningJobs = new HashMap<>();

	private ConfigurationService cfgSvc;

	private ImportJobDao importJobDao;

	private ThreadPoolTaskExecutor taskExecutor;
	
	private ObjectSchemaFactory schemaFactory;
	
	private ObjectImporterFactory importerFactory;
	
	private PlatformTransactionManager transactionManager;

	private TransactionTemplate txTmpl;

	private TransactionTemplate newTxTmpl;

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

		this.newTxTmpl = new TransactionTemplate(this.transactionManager);
		this.newTxTmpl.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ImportJobDetail>> getImportJobs(RequestEvent<ListImportJobsCriteria> req) {
		try {
			ListImportJobsCriteria crit = req.getPayload();
			if (!AuthUtil.isAdmin()) {
				crit.userId(AuthUtil.getCurrentUser().getId());
			}
			
			List<ImportJob> jobs = importJobDao.getImportJobs(crit);			
			return ResponseEvent.response(ImportJobDetail.from(jobs));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ImportJobDetail> getImportJob(RequestEvent<Long> req) {
		try {
			ImportJob job = getImportJob(req.getPayload());
			return ResponseEvent.response(ImportJobDetail.from(job));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<String> getImportJobFile(RequestEvent<Long> req) {
		try {
			ImportJob job = getImportJob(req.getPayload());
			File file = new File(getJobOutputFilePath(job.getId()));
			if (!file.exists()) {
				return ResponseEvent.userError(ImportJobErrorCode.OUTPUT_FILE_NOT_CREATED);
			}
			
			return ResponseEvent.response(file.getAbsolutePath());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
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
		ImportJob job = null;
		try {
			ImportDetail detail = req.getPayload();
			String inputFile = getFilePath(detail.getInputFileId());

			//
			// Ensure transaction size is well within configured limits
			//
			int inputRecordsCnt = CsvFileReader.getRowsCount(inputFile, true);
			if (detail.isAtomic() && inputRecordsCnt > getMaxRecsPerTxn()) {
				return ResponseEvent.response(ImportJobDetail.txnSizeExceeded(inputRecordsCnt));
			}

			job = createImportJob(detail);
			importJobDao.saveOrUpdate(job, true);

			//
			// Set up file in job's directory
			//
			createJobDir(job.getId());
			moveToJobDir(inputFile, job.getId());

			runningJobs.put(job.getId(), job);
			taskExecutor.submit(new ImporterTask(AuthUtil.getAuth(), job, detail.getListener(), detail.isAtomic()));
			return ResponseEvent.response(ImportJobDetail.from(job));
		} catch (Exception e) {
			if (job != null && job.getId() != null) {
				runningJobs.remove(job.getId());
			}

			return ResponseEvent.serverError(e);			
		}
	}

	@Override
	public ResponseEvent<ImportJobDetail> stopJob(RequestEvent<Long> req) {
		try {
			ImportJob job = runningJobs.get(req.getPayload());
			if (job == null || job.isInProgress()) {
				return ResponseEvent.userError(ImportJobErrorCode.NOT_IN_PROGRESS, req.getPayload());
			}

			ensureAccess(job).stop();
			for (int i = 0; i < 5; ++i) {
				TimeUnit.SECONDS.sleep(1);
				if (!job.isInProgress()) {
					break;
				}
			}

			return ResponseEvent.response(ImportJobDetail.from(job));
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<String> getInputFileTemplate(RequestEvent<ObjectSchemaCriteria> req) {
		try {
			ObjectSchemaCriteria detail = req.getPayload();
			ObjectSchema schema = schemaFactory.getSchema(detail.getObjectType(), detail.getParams());
			if (schema == null) {
				return ResponseEvent.userError(ImportJobErrorCode.OBJ_SCHEMA_NOT_FOUND, detail.getObjectType());
			}

			return ResponseEvent.response(ObjectReader.getSchemaFields(schema));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<List<Map<String, Object>>> processFileRecords(RequestEvent<FileRecordsDetail> req) {
		ObjectReader reader = null;
		File file = null;

		try {
			FileRecordsDetail detail = req.getPayload();

			ObjectSchema.Record schemaRec = new ObjectSchema.Record();
			schemaRec.setFields(detail.getFields());

			ObjectSchema schema = new ObjectSchema();
			schema.setRecord(schemaRec);

			file = new File(getFilePath(detail.getFileId()));
			reader = new ObjectReader(
				file.getAbsolutePath(),
				schema,
				ConfigUtil.getInstance().getDeDateFmt(),
				ConfigUtil.getInstance().getTimeFmt());

			List<Map<String, Object>> records = new ArrayList<>();
			Map<String, Object> record = null;
			while ((record = (Map<String, Object>)reader.next()) != null) {
				records.add(record);
			}

			return ResponseEvent.response(records);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(reader);
			if (file != null) {
				file.delete();
			}
		}
	}

	private int getMaxRecsPerTxn() {
		return ConfigUtil.getInstance().getIntSetting("common", CFG_MAX_TXN_SIZE, MAX_RECS_PER_TXN);
	}

	private ImportJob getImportJob(Long jobId) {
		ImportJob job = importJobDao.getById(jobId);
		if (job == null) {
			throw OpenSpecimenException.userError(ImportJobErrorCode.NOT_FOUND, jobId);
		}

		return ensureAccess(job);
	}

	private ImportJob ensureAccess(ImportJob job) {
		User currentUser = AuthUtil.getCurrentUser();
		if (!currentUser.isAdmin() && !currentUser.equals(job.getCreatedBy())) {
			throw OpenSpecimenException.userError(ImportJobErrorCode.ACCESS_DENIED);
		}

		return job;
	}
	
	private String getDataDir() {
		return cfgSvc.getDataDir();
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
	
	private String getJobOutputFilePath(Long jobId) {
		return getJobDir(jobId) + File.separator + "output.csv";
	}
	
	private boolean createJobDir(Long jobId) {
		return new File(getJobDir(jobId)).mkdirs();
	}
	
	private boolean moveToJobDir(String file, Long jobId) {
		File src = new File(file);
		File dest = new File(getJobDir(jobId) + File.separator + "input.csv");
		return src.renameTo(dest);		
	}
	
	private ImportJob createImportJob(ImportDetail detail) { // TODO: ensure checks are done
		ImportJob job = new ImportJob();
		job.setCreatedBy(AuthUtil.getCurrentUser());
		job.setCreationTime(Calendar.getInstance().getTime());
		job.setName(detail.getObjectType());
		job.setStatus(Status.IN_PROGRESS);
		job.setParams(detail.getObjectParams());
		
		String importType = detail.getImportType();
		job.setType(StringUtils.isBlank(importType) ? Type.CREATE : Type.valueOf(importType));
		
		String csvType = detail.getCsvType();
		job.setCsvtype(StringUtils.isBlank(csvType) ? CsvType.SINGLE_ROW_PER_OBJ : CsvType.valueOf(csvType));
		return job;		
	}
	
	private class ImporterTask implements Runnable {
		private Authentication auth;
		
		private ImportJob job;

		private ImportListener callback;

		private boolean atomic;

		private long totalRecords = 0;

		private long failedRecords = 0;

		public ImporterTask(Authentication auth, ImportJob job, ImportListener callback, boolean atomic) {
			this.auth = auth;
			this.job = job;
			this.callback = callback;
			this.atomic = atomic;
		}

		@Override
		public void run() {
			SecurityContextHolder.getContext().setAuthentication(auth);

			ObjectReader objReader = null;
			CsvWriter csvWriter = null;
			try {
				ObjectSchema   schema   = schemaFactory.getSchema(job.getName(), job.getParams());
				String filePath = getJobDir(job.getId()) + File.separator + "input.csv";
				csvWriter = getOutputCsvWriter(job);
				objReader = new ObjectReader(
						filePath, schema,
						ConfigUtil.getInstance().getDeDateFmt(),
						ConfigUtil.getInstance().getTimeFmt());

				List<String> columnNames = objReader.getCsvColumnNames();
				columnNames.add("OS_IMPORT_STATUS");
				columnNames.add("OS_ERROR_MESSAGE");

				csvWriter.writeNext(columnNames.toArray(new String[0]));

				Status status = processRows(objReader, csvWriter);
				saveJob(totalRecords, failedRecords, status);

				if (status == Status.COMPLETED) {
					success();
				} else {
					failed(OpenSpecimenException.userError(ImportJobErrorCode.FAIL, failedRecords, totalRecords));
				}
			} catch (Exception e) {
				logger.error("Error running import records job", e);
				saveJob(totalRecords, failedRecords, Status.FAILED);
				failed(e);
			} finally {
				runningJobs.remove(job.getId());
				IOUtils.closeQuietly(objReader);
				closeQuietly(csvWriter);
			}
		}

		private Status processRows(ObjectReader objReader, CsvWriter csvWriter) {
			if (atomic) {
				return txTmpl.execute(new TransactionCallback<Status>() {
					@Override
					public Status doInTransaction(TransactionStatus txnStatus) {
						Status jobStatus = processRows0(objReader, csvWriter);
						if (jobStatus == Status.FAILED || jobStatus == Status.STOPPED) {
							txnStatus.setRollbackOnly();
						}

						return jobStatus;
					}
				});
			} else {
				return processRows0(objReader, csvWriter);
			}
		}

		private Status processRows0(ObjectReader objReader, CsvWriter csvWriter) {
			boolean stopped;
			ObjectImporter<Object, Object> importer = importerFactory.getImporter(job.getName());
			if (job.getCsvtype() == CsvType.MULTIPLE_ROWS_PER_OBJ) {
				stopped = processMultipleRowsPerObj(objReader, csvWriter, importer);
			} else {
				stopped = processSingleRowPerObj(objReader, csvWriter, importer);
			}

			return stopped ? Status.STOPPED : (failedRecords > 0) ? Status.FAILED : Status.COMPLETED;
		}

		private boolean processSingleRowPerObj(ObjectReader objReader, CsvWriter csvWriter, ObjectImporter<Object, Object> importer) {
			boolean stopped = false;
			while (!job.isAskedToStop() || !(stopped = true)) {
				String errMsg = null;
				try {
					Object object = objReader.next();
					if (object == null) {
						break;
					}
	
					errMsg = importObject(importer, object, job.getParams());
				} catch (OpenSpecimenException ose) {
					errMsg = ose.getMessage();
				}
				
				++totalRecords;
				
				List<String> row = objReader.getCsvRow();
				if (StringUtils.isNotBlank(errMsg)) {
					row.add("FAIL");
					row.add(errMsg);
					++failedRecords;
				} else {
					row.add("SUCCESS");
					row.add("");
				}
				
				csvWriter.writeNext(row.toArray(new String[0]));
				if (totalRecords % 25 == 0) {
					saveJob(totalRecords, failedRecords, Status.IN_PROGRESS);
				}
			}

			return stopped;
		}

		private boolean processMultipleRowsPerObj(ObjectReader objReader, CsvWriter csvWriter, ObjectImporter<Object, Object> importer) {
			LinkedEhCacheMap<String, MergedObject> objectsMap =  new LinkedEhCacheMap<String, MergedObject>();
			
			while (true) {
				String errMsg = null;
				Object parsedObj = null;
				
				try {
					parsedObj = objReader.next();
					if (parsedObj == null) {
						break;
					}
				} catch (Exception e) {
					errMsg = e.getMessage();
				}
				
				String key = objReader.getRowKey();
				MergedObject mergedObj = objectsMap.get(key);
				if (mergedObj == null) {
					mergedObj = new MergedObject();
					mergedObj.setKey(key);
					mergedObj.setObject(parsedObj);
				}
				
				mergedObj.addErrMsg(errMsg);
				mergedObj.addRow(objReader.getCsvRow());
				mergedObj.merge(parsedObj);

				objectsMap.put(key, mergedObj);
			}

			boolean stopped = false;
			Iterator<MergedObject> mergedObjIter = objectsMap.iterator();
			while (mergedObjIter.hasNext() && (!job.isAskedToStop() || !(stopped = true))) {
				MergedObject mergedObj = mergedObjIter.next();
				if (!mergedObj.isErrorneous()) {
					String errMsg = null;
					try {
						errMsg = importObject(importer, mergedObj.getObject(), job.getParams());
					} catch (OpenSpecimenException ose) {
						errMsg = ose.getMessage();
					}

					if (StringUtils.isNotBlank(errMsg)) {
						mergedObj.addErrMsg(errMsg);
					}

					mergedObj.setProcessed(true);
					objectsMap.put(mergedObj.getKey(), mergedObj);
				}
				
				++totalRecords;
				if (mergedObj.isErrorneous()) {
					++failedRecords;
				}
				
				if (totalRecords % 25 == 0) {
					saveJob(totalRecords, failedRecords, Status.IN_PROGRESS);
				}
			}
			
			mergedObjIter = objectsMap.iterator();
			while (mergedObjIter.hasNext()) {
				MergedObject mergedObj = mergedObjIter.next();
				csvWriter.writeAll(mergedObj.getRowsWithStatus());
			}

			objectsMap.clear();
			return stopped;
		}
		
		private String importObject(final ObjectImporter<Object, Object> importer, Object object, Map<String, String> params) {
			try {
				ImportObjectDetail<Object> detail = new ImportObjectDetail<Object>();
				detail.setCreate(job.getType() == Type.CREATE);
				detail.setObject(object);
				detail.setParams(params);
				
				final RequestEvent<ImportObjectDetail<Object>> req = new RequestEvent<ImportObjectDetail<Object>>(detail);
				ResponseEvent<Object> resp = txTmpl.execute(
						new TransactionCallback<ResponseEvent<Object>>() {
							@Override
							public ResponseEvent<Object> doInTransaction(TransactionStatus status) {
								ResponseEvent<Object> resp = importer.importObject(req);
								if (!resp.isSuccessful()) {
									status.setRollbackOnly();
								}
								
								return resp;
							}
						});
				
				if (resp.isSuccessful()) {
					return null;
				} else {
					return resp.getError().getMessage();
				}				
			} catch (Exception e) {
				if (StringUtils.isBlank(e.getMessage())) {
					return "Internal Server Error";
				} else {
					return e.getMessage();
				}
			}
		}
		
		private void saveJob(long totalRecords, long failedRecords, Status status) {
			job.setTotalRecords(totalRecords);
			job.setFailedRecords(failedRecords);
			job.setStatus(status);
			
			if (status != Status.IN_PROGRESS) {
				job.setEndTime(Calendar.getInstance().getTime());
			}

			newTxTmpl.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					importJobDao.saveOrUpdate(job);
					return null;
				}
			});
		}

		private CsvWriter getOutputCsvWriter(ImportJob job) 
		throws IOException {
			return CsvFileWriter.createCsvFileWriter(new FileWriter(getJobOutputFilePath(job.getId())));
		}
				
		private void closeQuietly(CsvWriter writer) {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
											
				}
			}
		}

		private void success() {
			if (callback != null) {
				callback.success();
			}
		}

		private void failed(Throwable t) {
			if (callback != null) {
				callback.fail(t);
			}
		}
	}
}
