
package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.audit.events.ReportDetail;
import com.krishagni.catissueplus.core.audit.events.UserInfo;
import com.krishagni.catissueplus.core.audit.repository.AuditReportDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

@SuppressWarnings("rawtypes")
public class AuditReportDaoImpl extends AbstractDao implements AuditReportDao {

	/**
	 * This method  get Summary related data from database as per user requirement
	 * @param parameters 
	 * @throws Exception 
	 * 
	 */
	private static String GET_USERS = "select distinct user.IDENTIFIER,user.FIRST_NAME,user.LAST_NAME from "
			+ "catissue_user user left join catissue_audit_event audit on user.IDENTIFIER = audit.USER_ID";

	@SuppressWarnings("unchecked")
	public List<ReportDetail> getAuditDetails(StringBuffer auditQuery, Map<String, Object> queryParameters) {

		Query query = sessionFactory.getCurrentSession().createSQLQuery(auditQuery.toString());
		for (String  element : queryParameters.keySet()) {
			query.setParameter(element, queryParameters.get(element));
		}
		
		List<Object[]> result = query.list();
		List<ReportDetail> reportDataList = new ArrayList<ReportDetail>();
		for (Object[] row : result) {

			ReportDetail reportDetail = new ReportDetail();
			reportDetail.setIdentifier((Long.parseLong(row[0].toString())));
			reportDetail.setFirstName((String) row[1]);
			reportDetail.setLastName((String) row[2]);
			reportDetail.setLoginName((String) row[3]);
			reportDetail.setEventType((String) row[4]);
			reportDetail.setCount((Integer.parseInt(row[5].toString())));
			reportDetail.setObjectName((String) row[6]);

			reportDataList.add(reportDetail);
		}
		return reportDataList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserInfo> getUserDetails() {
		List<UserInfo> usersDetails = new ArrayList<UserInfo>();

		Query query = sessionFactory.getCurrentSession().createSQLQuery(GET_USERS);
		List<Object[]> users = query.list();
		for (Object[] user : users) {
			UserInfo userDetailsEvent = new UserInfo();
			userDetailsEvent.setIdentifier(Long.parseLong(user[0].toString()));
			userDetailsEvent.setFirstName((String) user[1]);
			userDetailsEvent.setLastName((String) user[2]);
			usersDetails.add(userDetailsEvent);
		}
		return usersDetails;
	}

}
