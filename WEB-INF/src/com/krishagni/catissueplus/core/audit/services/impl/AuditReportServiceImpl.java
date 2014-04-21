
package com.krishagni.catissueplus.core.audit.services.impl;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.krishagni.catissueplus.core.audit.events.AuditReportCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.AuditReportDetail;
import com.krishagni.catissueplus.core.audit.events.CreateAuditReportEvent;
import com.krishagni.catissueplus.core.audit.events.GetUsersInfoEvent;
import com.krishagni.catissueplus.core.audit.events.GetOperationEvent;
import com.krishagni.catissueplus.core.audit.events.GetObjectNameEvent;
import com.krishagni.catissueplus.core.audit.events.ReportDetail;
import com.krishagni.catissueplus.core.audit.events.UserInfo;
import com.krishagni.catissueplus.core.audit.services.AuditReportService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.VelocityClassLoaderManager;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

public class AuditReportServiceImpl implements AuditReportService {

	private static final String USER_REPORT_VELOCITY_TEMPLETE_FILE = "../resources/ng-file-templates/auditReport.vm";

	private static final String AUDIT_REPORT_PROPERTY_FILE = "/AuditReportProperties.properties";

	private Map<String, String> objectNameMap = new TreeMap<String, String>();

	public enum Operation {
		INSERT("Insert"), UPDATE("Update"), DELETE("Delete");

		private String operation;

		private Operation(String operation) {
			this.operation = operation;
		}

		public String getOperation() {
			return operation;
		}

	}

	private static final String SELECT_CLAUSE = "SELECT user.identifier,user.first_name,user.last_name,user.login_name,auditEvent.event_type,"
			+ " COUNT(dataLogEvent.identifier) count, dataLogEvent.object_name "
			+ " FROM catissue_user user, catissue_data_audit_event_log dataLogEvent, catissue_audit_event_log auditEventLog, "
			+ "catissue_audit_event auditEvent ";

	private static final String WHERE_CLAUSE = ("WHERE  dataLogEvent.identifier = auditEventLog.identifier AND auditEventLog.audit_event_id = auditEvent.identifier "
			+ "AND user.identifier = auditEvent.user_id ");

	private static final String GROUP_BY_CLAUSE = "GROUP BY dataLogEvent.object_name, auditEvent.event_type, user.first_name,"
			+ "user.last_name, user.login_name ";

	private static final String ORDER_BY_CLAUSE = "ORDER BY user.identifier";

	private DaoFactory daoFactory;

	private Logger logger = Logger.getCommonLogger(AuditReportServiceImpl.class);

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@PlusTransactional
	@Override
	public AuditReportCreatedEvent getAuditReport(CreateAuditReportEvent auditReportEvent) {

		try {

			Map<String, Object> queryParameters = new HashMap<String, Object>();

			StringBuffer auditQuery = createAuditQuery(auditReportEvent.getAuditReportDetail(), queryParameters);
			List<ReportDetail> reportDetailsList = daoFactory.getAuditReportDao()
					.getAuditDetails(auditQuery, queryParameters);

			String reportData = generateAuditReport(reportDetailsList);

			return AuditReportCreatedEvent.ok(reportData);
		}
		catch (Exception exception) {
			logger.error(exception);
			return AuditReportCreatedEvent.serverError(exception);
		}
	}

	/**
	 * @throws ParseException 
	 * create query as per user requirement.
	 * @throws  
	 */

	private StringBuffer createAuditQuery(AuditReportDetail reportDetailEvent, Map<String, Object> queryParameters)
			throws ParseException {

		// get User Ids from reportDetailEvent and set '?' into IN() clause as per number of values
		StringBuffer conditions = new StringBuffer();
		List<Long> userIds = reportDetailEvent.getUserIds();
		if (!(userIds.isEmpty())) {

			conditions.append("AND user.identifier IN(");
			addNamedParameters(conditions, userIds, queryParameters, "user");
		}

		// get Object Names from reportDetailEvent and set '?' into IN() clause as per number of values
		List<String> objectNames = reportDetailEvent.getObjectTypes();
		if (!(objectNames.isEmpty())) {
			conditions.append("AND dataLogEvent.object_name IN(");
			addNamedParameters(conditions, objectNames, queryParameters, "object");
		}

		// get Event Types from reportDetailEvent
		List<String> eventTypes = reportDetailEvent.getOperations();
		if (!(eventTypes.isEmpty())) {

			conditions.append("AND auditEvent.event_type IN(");
			addNamedParameters(conditions, eventTypes, queryParameters, "event");
		}

		if (reportDetailEvent.getStartDate() != null || reportDetailEvent.getEndDate() != null) {
			if (reportDetailEvent.getStartDate() != null) {
				queryParameters.put("startDate", new java.sql.Date(reportDetailEvent.getStartDate().getTime()));

			}
			else {
				Date startDate = new Date(Long.MIN_VALUE);
				queryParameters.put("startDate", new java.sql.Date(startDate.getTime()));
			}

			if (reportDetailEvent.getEndDate() != null) {
				queryParameters.put("endDate", new java.sql.Date(reportDetailEvent.getEndDate().getTime()));
			}
			else {
				Date endDate = new Date();
				queryParameters.put("endDate", new java.sql.Date(endDate.getTime()));
			}
			conditions.append("AND EVENT_TIMESTAMP  between (:startDate) ");
			conditions.append(" AND (:endDate) + INTERVAL 1 DAY ");
		}

		StringBuffer auditQuery = new StringBuffer();
		auditQuery.append(SELECT_CLAUSE);
		auditQuery.append(WHERE_CLAUSE);
		auditQuery.append(conditions);
		auditQuery.append(GROUP_BY_CLAUSE);
		auditQuery.append(ORDER_BY_CLAUSE);
		return auditQuery;
	}

	// generic method to set placeholder into IN() clause as per number of  values

	private void addNamedParameters(StringBuffer conditions, List<? extends Object> objects,
			Map<String, Object> queryParameters, String paramString) {
		int id = 0, key = 0;
		for (Object obj : objects) {
			conditions.append(":" + paramString + (++id) + ",");
			queryParameters.put(paramString + (++key), obj);
		}
		conditions.deleteCharAt(conditions.length() - 1);
		conditions.append(")");
	}

	/**
	 * @throws Exception 
	 * this method use to generate report data using velocity manager
	 * @throws  
	 */

	private String generateAuditReport(List<ReportDetail> reportDetailsList) throws Exception {

		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("reportDetailsList", reportDetailsList);
		String auditReport = VelocityClassLoaderManager.getInstance().evaluate(contextMap,
				USER_REPORT_VELOCITY_TEMPLETE_FILE);

		return auditReport;

	}

	@PlusTransactional
	@Override
	public GetUsersInfoEvent getUserDetails() {
		GetUsersInfoEvent createUserDetailsEvent = new GetUsersInfoEvent();
		List<UserInfo> userDetails = daoFactory.getAuditReportDao().getUserDetails();
		createUserDetailsEvent.setUserDetails(userDetails);
		return createUserDetailsEvent;
	}

	public GetObjectNameEvent getObjectTypes() {

		GetObjectNameEvent objectDetailsEvent = new GetObjectNameEvent();
		try {

			if (objectNameMap.size() == 0) {
				populateObjectsInMap();
			}
			
			objectDetailsEvent.setObjectNameMap(objectNameMap);
			return GetObjectNameEvent.ok(objectDetailsEvent.getObjectNameMap());
		}
		catch (Exception e) {
			return GetObjectNameEvent.serverError(e);
		}

	}

	private void populateObjectsInMap() throws FileNotFoundException, IOException {
		final String path = CommonServiceLocator.getInstance().getPropDirPath();

		Properties properties = new Properties();
		String actualPath = path + AUDIT_REPORT_PROPERTY_FILE;
		InputStream inputStream = new FileInputStream(new File(actualPath));
		properties.load(inputStream);
		inputStream.close();
		
		for (String key : properties.stringPropertyNames()) {
	    objectNameMap.put(key,properties.getProperty(key));
	}
	}

	/**
	 * this method provide list of Event types	
	 */

	public GetOperationEvent getEventsTypes() {

		Map<String, String> operationMap = new TreeMap<String, String>();
		operationMap.put(Operation.INSERT.toString(), Operation.INSERT.getOperation());
		operationMap.put(Operation.UPDATE.toString(), Operation.UPDATE.getOperation());
		operationMap.put(Operation.DELETE.toString(), Operation.DELETE.getOperation());

		GetOperationEvent eventTypeDetailsEvent = new GetOperationEvent();
		eventTypeDetailsEvent.setOperationMap(operationMap);
		return GetOperationEvent.ok(eventTypeDetailsEvent.getOperationMap());
	}
}