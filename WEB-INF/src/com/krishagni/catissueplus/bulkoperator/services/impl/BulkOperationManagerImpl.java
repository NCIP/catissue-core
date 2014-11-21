package com.krishagni.catissueplus.bulkoperator.services.impl;

import java.io.File;
import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.xml.sax.InputSource;

import com.krishagni.catissueplus.bulkoperator.BulkOperator;
import com.krishagni.catissueplus.bulkoperator.appservice.AppServiceInformationObject;
import com.krishagni.catissueplus.bulkoperator.common.ObjectImporter;
import com.krishagni.catissueplus.bulkoperator.common.JobStatusConstants;
import com.krishagni.catissueplus.bulkoperator.common.JobUtility;
import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.csv.CsvWriter;
import com.krishagni.catissueplus.bulkoperator.csv.impl.CsvFileReader;
import com.krishagni.catissueplus.bulkoperator.csv.impl.CsvFileWriter;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperation;
import com.krishagni.catissueplus.bulkoperator.domain.BulkOperationJob;
import com.krishagni.catissueplus.bulkoperator.events.ImportObjectEvent;
import com.krishagni.catissueplus.bulkoperator.events.ObjectImportedEvent;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetadata;
import com.krishagni.catissueplus.bulkoperator.processor.BulkObjectBuilder;
import com.krishagni.catissueplus.bulkoperator.processor.DynamicExtensionBulkObjectBuilder;
import com.krishagni.catissueplus.bulkoperator.processor.StaticBulkObjectBuilder;
import com.krishagni.catissueplus.bulkoperator.repository.BulkOperationJobDao;
import com.krishagni.catissueplus.bulkoperator.services.ObjectImporterFactory;
import com.krishagni.catissueplus.bulkoperator.services.BulkOperationManager;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import com.krishagni.catissueplus.bulkoperator.validator.TemplateValidator;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;

public class BulkOperationManagerImpl implements BulkOperationManager {
	private UserDao userDao;
	
	private BulkOperationJobDao jobDao;
	
	private ObjectImporterFactory importerFactory;
		
	private static ExecutorService threadPool = Executors.newFixedThreadPool(5);
	
	public void setJobDao(BulkOperationJobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setImporterFactory(ObjectImporterFactory importerFactory) {
		this.importerFactory = importerFactory;
	}

	
	@Override
	public Long importRecords(SessionDataBean session, BulkOperation bulkOperation, File fileIn) 
	throws BulkOperationException {		
		String opName = bulkOperation.getOperationName();
		BulkOperationMetadata boMetadata = getBulkOperationClass(bulkOperation.getXmlTemplate());		
		validateBulkOperation(opName, fileIn, boMetadata.getBulkOperationClassInstance());		
		
		BulkOperationJob job = JobUtility.createJob(opName, session.getUserId(), jobDao, userDao);
		
		BulkImporterTask task = new BulkImporterTask();
		task.boMetadata = boMetadata;
		task.fileIn = fileIn;
		task.job = job;
		task.operationName = opName;
		task.sdb = session;
		
		threadPool.execute(task);
		return job.getId();
	}
	
	private void validateBulkOperation(String operationName, File fileIn, BulkOperationClass bulkOperationClass) 
			throws BulkOperationException
	{
		TemplateValidator templateValidator = new TemplateValidator();
		CsvReader csvReader = CsvFileReader.createCsvFileReader(fileIn.getAbsolutePath(), true);
		
		try {
			Set<String> errorList = templateValidator.validateXmlAndCsv(bulkOperationClass,	operationName, csvReader);
			if (!errorList.isEmpty())
			{
				StringBuffer strBuffer = new StringBuffer();
				Iterator<String> errorIterator = errorList.iterator();
				while(errorIterator.hasNext())
				{
					strBuffer.append(errorIterator.next());
				}
				ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
				throw new BulkOperationException(errorkey, null, strBuffer.toString());
			}			
		} finally {
			csvReader.close();
		}
	}
	
	private BulkOperator parseXMLStringAndGetBulkOperatorInstance(InputSource templateInputSource) throws BulkOperationException
	{
		BulkOperator bulkOperator = null;
		try {
			String mappingFilePath = CommonServiceLocator.getInstance().getPropDirPath()
					+ File.separator + "bulkOperatorXMLTemplateRules.xml";
			bulkOperator = new BulkOperator(templateInputSource, mappingFilePath);
		} catch (BulkOperationException bulkExp) {
			ErrorKey errorKey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorKey, bulkExp, bulkExp.getMessage());
		}
		return bulkOperator;
	}
	
	private BulkObjectBuilder getObjectBuilder(BulkOperationClass bulkOperationClass, AppServiceInformationObject serviceInformationObject) {
		BulkObjectBuilder bulkOperationProcessor;
		if (BulkOperationConstants.ENTITY_TYPE.equalsIgnoreCase(bulkOperationClass.getType())) {
		   bulkOperationProcessor = new StaticBulkObjectBuilder(bulkOperationClass);
		} else {
			bulkOperationProcessor = new DynamicExtensionBulkObjectBuilder(bulkOperationClass);
		}
		return bulkOperationProcessor;
	}
	
	private int getBatchSize(BulkOperationClass bulkOperationClass) {
		int batchSize = bulkOperationClass.getBatchSize();
		if (batchSize == 0) {
			batchSize = 100;
		}
		return batchSize;
	}
	
	
	private void addRecordToWrite(CsvReader csvReader, String[] columnNames,
			CsvWriter csvWriter,String status,String message, String objectId) {
		csvWriter.nextRow();
		for (String column : columnNames) {
			csvWriter.setColumnValue(column, csvReader.getColumn(column));
		}
		addStatusOfRow(csvWriter,status, message, objectId);
	}
	
	private void addStatusOfRow(CsvWriter csvWriter, String status,
			String meessage, String mainObject) {
		csvWriter.setColumnValue(BulkOperationConstants.STATUS, status);
		csvWriter.setColumnValue(BulkOperationConstants.MESSAGE, meessage);
		csvWriter.setColumnValue(BulkOperationConstants.MAIN_OBJECT_ID, mainObject);
	}
	
	private BulkOperationMetadata getBulkOperationClass(String inputFile) {
		try {
			return parseXMLStringAndGetBulkOperatorInstance(new InputSource(new StringReader(inputFile))).getMetadata();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void processRecord(
			SessionDataBean sdb,
			BulkOperationClass bulkOperationClass,
			Long currentCSVRowCount, 
			CsvReader csvReader,
			String[] columnNames, 
			CsvWriter csvWriter, 
			BulkObjectBuilder objectBuilder)
	throws BulkOperationException, Exception {
		
		Object processedObject = objectBuilder.process(csvReader, currentCSVRowCount.intValue());
		
		processedObject = execute(sdb, processedObject);
		
		String objectId=null;
		if (objectBuilder instanceof StaticBulkObjectBuilder) {
			objectId=String.valueOf(bulkOperationClass.invokeGetIdMethod(processedObject));
		} else {
			objectId=String.valueOf(processedObject);
		}
		
		addRecordToWrite(csvReader, columnNames, csvWriter, "Success", "", objectId);
	}
	
	private Object execute(SessionDataBean sdb, Object processedObject) {
		ObjectImporter controller = importerFactory.getImporter(processedObject.getClass().getSimpleName());
		if (controller == null) {
			throw new RuntimeException("Object of class " + processedObject.getClass().getName() + " is not registered!");
		}
		
		ObjectImportedEvent resp = controller.importObject(new ImportObjectEvent(processedObject, sdb));		
		if (resp != null && resp.getStatus() != EventStatus.OK) {
			String message = buildProcessingFailureMessage(resp);
			throw new RuntimeException(message);
		}
		
		return resp.getObject();
	}
	
	private String buildProcessingFailureMessage(ObjectImportedEvent res) {
		if (res.getErroneousFields() != null && res.getErroneousFields().length > 0) {
			String message = res.getMessage() + " ";
			for (ErroneousField error : res.getErroneousFields()) {
				message += "[detail: " +  error.getErrorMessage() + ", field: " + error.getFieldName() + "], ";
			}
			
			return message;
		} 
		return res.getMessage();
	}

	private void saveReportToDatabase(
			BulkOperationJob job, 
			String operationName, 
			Long recordsProcessed, Long failureCount,	
			String statusMessage, CsvWriter csvWriter,
			Long currentCSVRowCount, boolean finalexecute) {
		try {
			String commonFileName = operationName + job.getId();
			
			File file = new File(commonFileName + ".csv");
			String[] fileNames = file.getName().split(".csv");
			String zipFilePath = CommonServiceLocator.getInstance().getAppHome() + System.getProperty("file.separator")	+ fileNames[0];
			
			File zipFile = null;
			if (csvWriter != null) {
				csvWriter.flush();
				zipFile = BulkOperationUtility.createZip(file, zipFilePath);
			}
			
			Long localTimetaken = (new Date().getTime() - job.getStartTime().getTime())/1000;
			
			Transaction txn = startTxn();
			try {
	
				JobUtility.updateJob(job.getId(), statusMessage, recordsProcessed, failureCount, currentCSVRowCount, 
						localTimetaken, zipFile.getName(), zipFile, jobDao);
				txn.commit();
			} catch (Exception e) {
				if (txn != null) {
					txn.rollback();
				}
			}
			
			if (finalexecute && csvWriter != null) 
			{
				file.delete();
				zipFile.delete();
			}
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	private Transaction startTxn() {
		AbstractDao<?> dao = (AbstractDao<?>)userDao;
		Session session = dao.getSessionFactory().getCurrentSession();
		Transaction txn = session.getTransaction();
		if (txn == null || !txn.isActive()) {
			txn = session.beginTransaction();			
		}
		
		return txn;
	}
	
	private class BulkImporterTask implements Runnable {
		
		private BulkOperationMetadata boMetadata;
		
		private File fileIn;
		
		private boolean isReRun = false;
		
		private String operationName;
		
		private BulkOperationJob job;
		
		private SessionDataBean sdb;

		@Override
		public void run() {
			startBulkOperation();			
		}
		
		private void startBulkOperation() {
			Long failureCount = 0L, successCount = 0L, currentCSVRowCount = 0L;			
			CsvReader csvReader = null;
			
			try {
				BulkOperationClass boClass = boMetadata.getBulkOperationClassInstance();
				int batchSize = getBatchSize(boClass);
				csvReader = CsvFileReader.createCsvFileReader(fileIn.getAbsolutePath(), true);
				String[] columnNames = csvReader.getColumnNames();
				CsvWriter csvWriter = getCsvWriter(batchSize, columnNames);
				BulkObjectBuilder objectBuilder = getObjectBuilder(boClass, null);
				
				while (csvReader.next()) {
					try {
						if (isReRun && BulkOperationConstants.SUCCESS.equalsIgnoreCase(csvReader.getColumn(BulkOperationConstants.STATUS))) {
							addRecordToWrite(
									csvReader,
									columnNames,
									csvWriter,
									BulkOperationConstants.SUCCESS,
									csvReader.getColumn(BulkOperationConstants.MESSAGE),
									csvReader.getColumn(BulkOperationConstants.MAIN_OBJECT_ID));
						} else {
							processRecord(sdb, boClass, currentCSVRowCount, csvReader, columnNames, csvWriter, objectBuilder);
						}
						
						successCount++;
					} catch (Exception e) {
						addRecordToWrite(csvReader, columnNames, csvWriter,"Failure", e.getMessage(),"");
						failureCount++;
					} finally {
						if ((currentCSVRowCount % batchSize) == 0) {
							try {
								saveReportToDatabase(job, operationName, successCount, failureCount, JobStatusConstants.JOB_IN_PROGRESS_STATUS, 
										csvWriter, currentCSVRowCount, false);
							} catch (Exception e) {
								throw e;
							}
						}
					}
					
					currentCSVRowCount++;
				}
				
				saveReportToDatabase(job, operationName, successCount, failureCount, JobStatusConstants.JOB_COMPLETED_STATUS, csvWriter, currentCSVRowCount, true);			
			} catch (Exception e) {
				e.printStackTrace();
				saveReportToDatabase(job, operationName, successCount, failureCount, JobStatusConstants.JOB_FAILED_STATUS,	null, currentCSVRowCount, false);
			}
		}
		
		private CsvWriter getCsvWriter(int batchSize, String[] columnNames) {
			isReRun = ArrayUtils.contains(columnNames, BulkOperationConstants.STATUS);
			if (!isReRun) {	
				columnNames = BulkOperationUtility.concatArrays(columnNames, BulkOperationConstants.DEFAULT_COLUMNS); 
			}
			
			return CsvFileWriter.createCsvFileWriter(
					operationName + job.getId() + ".csv",
					columnNames,	
					batchSize);
		}		
	}	
}