
package com.krishagni.catissueplus.core.audit.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.audit.events.ReportDetail;
import com.krishagni.catissueplus.core.audit.events.UserInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

@SuppressWarnings("rawtypes")
public interface AuditReportDao extends Dao {

	List<ReportDetail> getAuditDetails(StringBuffer auditQuery, Map<String, Object> queryParameters);

	List<UserInfo> getUserDetails();

	List<UserInfo> getSelectedUserList(StringBuffer query, Map<String, Object> queryParameters);

}
