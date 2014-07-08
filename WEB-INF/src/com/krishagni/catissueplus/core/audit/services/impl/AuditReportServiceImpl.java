
package com.krishagni.catissueplus.core.audit.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.krishagni.catissueplus.core.audit.events.AuditReportCreatedEvent;
import com.krishagni.catissueplus.core.audit.events.AuditReportDetail;
import com.krishagni.catissueplus.core.audit.events.AuditReportExportEvent;
import com.krishagni.catissueplus.core.audit.events.CreateAuditReportEvent;
import com.krishagni.catissueplus.core.audit.events.GetObjectNameEvent;
import com.krishagni.catissueplus.core.audit.events.GetOperationEvent;
import com.krishagni.catissueplus.core.audit.events.GetUsersInfoEvent;
import com.krishagni.catissueplus.core.audit.events.ReportDetail;
import com.krishagni.catissueplus.core.audit.events.UserInfo;
import com.krishagni.catissueplus.core.audit.services.AuditReportService;
import com.krishagni.catissueplus.core.audit.util.AuditUtil;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.VelocityClassLoaderManager;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;

public class AuditReportServiceImpl implements AuditReportService {

	private static final String USER_REPORT_VELOCITY_TEMPLETE_FILE = "../resources/ng-file-templates/auditReport.vm";

	public enum Operation {
		INSERT("INSERT"), UPDATE("UPDATE"), DELETE("DELETE");

		private String operation;

		private Operation(String operation) {
			this.operation = operation;
		}

		public String getOperation() {
			return operation;
		}

	}

	private static final String SELECT_CLAUSE = "SELECT usr.identifier,dataLogEvent.object_name,auditEvent.event_type,usr.first_name,usr.last_name,usr.login_name,";

	private static final String COUNT_MYSQL = " COUNT(dataLogEvent.identifier) TotalCount";

	private static final String COUNT_ORACLE = " COUNT(dataLogEvent.identifier) AS \"TOTALCOUNT\"";

	private static final String FROM = " FROM catissue_user usr, catissue_data_audit_event_log dataLogEvent, catissue_audit_event_log auditEventLog, "
			+ "catissue_audit_event auditEvent ";

	private static final String WHERE_CLAUSE = ("WHERE  dataLogEvent.identifier = auditEventLog.identifier AND auditEventLog.audit_event_id = auditEvent.identifier "
			+ "AND usr.identifier = auditEvent.user_id  AND dataLogEvent.PARENT_LOG_ID is null ");

	private static final String GROUP_BY_CLAUSE = "GROUP BY dataLogEvent.object_name, auditEvent.event_type, usr.first_name, usr.last_name, usr.login_name ";

	private static final String GROUP_BY_CLAUSE_ORCL = "GROUP BY usr.identifier,dataLogEvent.object_name,auditEvent.event_type,usr.first_name,usr.last_name,usr.login_name ";

	private static final String ORDER_BY_CLAUSE = "ORDER BY usr.identifier";

	private DaoFactory daoFactory;

	private Logger logger = Logger.getCommonLogger(AuditReportServiceImpl.class);

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@PlusTransactional
	@Override
	public AuditReportCreatedEvent getAuditReport(CreateAuditReportEvent auditReportEvent) {

		SessionDataBean sessionBean = auditReportEvent.getSessionDataBean();

		try {

			Map<String, Object> queryParameters = new HashMap<String, Object>();

			StringBuffer auditQuery = createAuditQuery(auditReportEvent.getAuditReportDetail(), queryParameters);
			List<ReportDetail> reportDetailsList = daoFactory.getAuditReportDao()
					.getAuditDetails(auditQuery, queryParameters);

			String reportData = generateAuditReport(reportDetailsList, auditReportEvent.getAuditReportDetail(),
					sessionBean.getFirstName() + " " + sessionBean.getLastName());

			return AuditReportCreatedEvent.ok(reportData);
		}
		catch (Exception exception) {
			logger.error(exception);
			return AuditReportCreatedEvent.serverError(exception);
		}
	}

	/**
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 * create query as per user requirement.
	 * @throws  
	 */

	private StringBuffer createAuditQuery(AuditReportDetail reportDetailEvent, Map<String, Object> queryParameters)
			throws ParseException, IOException {

		String appName = CommonServiceLocator.getInstance().getAppName();
		String databaseName = DAOConfigFactory.getInstance().getDAOFactory(appName).getDataBaseType();

		// get User Ids from reportDetailEvent and set '?' into IN() clause as per number of values
		if (databaseName.equalsIgnoreCase("mysql")) {
			StringBuffer conditions = new StringBuffer();
			List<Long> userIds = reportDetailEvent.getUserIds();
			if (!(userIds.isEmpty())) {

				conditions.append("AND usr.identifier IN(");
				addNamedParameters(conditions, userIds, queryParameters, "user");
			}

			// get Object Names from reportDetailEvent and set '?' into IN() clause as per number of values
			List<String> objectNames = reportDetailEvent.getObjectTypes();
			if (!(objectNames.isEmpty())) {
				conditions.append("AND dataLogEvent.object_name IN(");
				addNamedParameters(conditions, objectNames, queryParameters, "object");
			}
			else {

				objectNames = AuditUtil.getObjectList();
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
			auditQuery.append(COUNT_MYSQL);
			auditQuery.append(FROM);
			auditQuery.append(WHERE_CLAUSE);
			auditQuery.append(conditions);
			auditQuery.append(GROUP_BY_CLAUSE);
			auditQuery.append(ORDER_BY_CLAUSE);
			return auditQuery;
		}
		else {
			StringBuffer conditions = new StringBuffer();
			List<Long> userIds = reportDetailEvent.getUserIds();
			if (!(userIds.isEmpty())) {

				conditions.append("AND usr.identifier IN(");
				addNamedParameters(conditions, userIds, queryParameters, "user");
			}

			// get Object Names from reportDetailEvent and set '?' into IN() clause as per number of values
			List<String> objectNames = reportDetailEvent.getObjectTypes();
			if (!(objectNames.isEmpty())) {
				conditions.append("AND dataLogEvent.object_name IN(");
				addNamedParameters(conditions, objectNames, queryParameters, "object");
			}
			else {

				objectNames = AuditUtil.getObjectList();
				conditions.append("AND dataLogEvent.object_name IN(");
				addNamedParameters(conditions, objectNames, queryParameters, "object");
			}

			// get Event Types from reportDetailEvent
			List<String> eventTypes = reportDetailEvent.getOperations();
			if (!(eventTypes.isEmpty())) {

				conditions.append("AND auditEvent.event_type IN(");
				addNamedParameters(conditions, eventTypes, queryParameters, "event");
			}

			if (reportDetailEvent.getStartDate() != null && reportDetailEvent.getEndDate() != null) {
				conditions.append("AND EVENT_TIMESTAMP  >= to_date('"
						+ new java.sql.Date(reportDetailEvent.getStartDate().getTime()) + "',\'yyyy-mm-dd\') ");

				conditions.append(" AND EVENT_TIMESTAMP  <= to_date('"
						+ new java.sql.Date(reportDetailEvent.getEndDate().getTime()) + "',\'yyyy-mm-dd\') + INTERVAL '1' DAY ");

			}

			else if (reportDetailEvent.getStartDate() == null && reportDetailEvent.getEndDate() != null) {
				conditions.append("AND EVENT_TIMESTAMP <=  to_date('"
						+ new java.sql.Date(reportDetailEvent.getEndDate().getTime()) + "',\'yyyy-mm-dd\')");

			}
			else if (reportDetailEvent.getEndDate() == null && reportDetailEvent.getStartDate() != null) {
				conditions.append("AND EVENT_TIMESTAMP >=  to_date('"
						+ new java.sql.Date(reportDetailEvent.getStartDate().getTime()) + "',\'yyyy-mm-dd\')");

			}

			StringBuffer auditQuery = new StringBuffer();
			auditQuery.append(SELECT_CLAUSE);
			auditQuery.append(COUNT_ORACLE);
			auditQuery.append(FROM);
			auditQuery.append(WHERE_CLAUSE);
			auditQuery.append(conditions);
			auditQuery.append(GROUP_BY_CLAUSE_ORCL);
			auditQuery.append(ORDER_BY_CLAUSE);
			return auditQuery;
		}
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
	 * @param userName 
	 * @param auditReportDetail 
	 * @throws Exception 
	 * this method use to generate report data using velocity manager
	 * @throws  
	 */

	private String generateAuditReport(List<ReportDetail> reportDetailsList, AuditReportDetail auditReportDetail,
			String userName) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern"));

		AuditReportExportEvent exportEvent = new AuditReportExportEvent();
		Date fromDate = auditReportDetail.getStartDate();

		if (fromDate != null) {
			String from = format.format(fromDate);
			exportEvent.setDateFrom(from);
		}
		else {
			exportEvent.setDateFrom("");
		}

		Date toDate = auditReportDetail.getEndDate();
		if (toDate != null) {
			String endDate = format.format(toDate);
			exportEvent.setDateTo(endDate);
		}
		else {
			exportEvent.setDateTo("");
		}

		exportEvent.setExportBy(userName);

		exportEvent.setReportDetailsList(reportDetailsList);

		List<String> users = getUserList(auditReportDetail.getUserIds());
		String user = users.toString();
		String usersString = user.substring(1, user.length() - 1);

		exportEvent.setUserList(usersString);

		String objects = auditReportDetail.getObjectTypes().toString();
		String objetTypes = objects.substring(1, objects.length() - 1);
		exportEvent.setObjectTypes(objetTypes);

		String operation = auditReportDetail.getOperations().toString();
		String operations = operation.substring(1, operation.length() - 1);
		exportEvent.setOperations(operations);

		Date exportDate = new Date();
		String strDate = format.format(exportDate);

		exportEvent.setExportOn(strDate);
		Map<String, Object> contextMap = new HashMap<String, Object>();

		contextMap.put("exportEvent", exportEvent);
		String auditReport = VelocityClassLoaderManager.getInstance().evaluate(contextMap,
				USER_REPORT_VELOCITY_TEMPLETE_FILE);

		return auditReport;

	}

	private List<String> getUserList(List<Long> list) {
		List<String> fullNames = new ArrayList<String>();
		StringBuffer query = new StringBuffer();
		if (!list.isEmpty()) {
			Map<String, Object> queryParameters = new HashMap<String, Object>();
			query.append("select distinct usr.FIRST_NAME,usr.LAST_NAME from catissue_user usr where usr.IDENTIFIER IN(");
			addNamedParameters(query, list, queryParameters, "user");
			List<UserInfo> userDetails = daoFactory.getAuditReportDao().getSelectedUserList(query, queryParameters);

			for (UserInfo user : userDetails) {
				fullNames.add(user.getLastName() + " " + user.getFirstName());

			}
		}
		return fullNames;
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

			objectDetailsEvent.setObjectNameMap(AuditUtil.getAuditObjectDataMap());
			return GetObjectNameEvent.ok(objectDetailsEvent.getObjectNameMap());
		}
		catch (Exception e) {
			return GetObjectNameEvent.serverError(e);
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