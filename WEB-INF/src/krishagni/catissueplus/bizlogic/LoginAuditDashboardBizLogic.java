package krishagni.catissueplus.bizlogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.beans.LoginAuditBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

public class LoginAuditDashboardBizLogic 
{
//	private String GET_QUERY_LOG_DETAILS ="select user.Last_Name, user.First_Name, audit.LOGIN_TIMESTAMP, audit.LOGIN_IP_ADDRESS, " +
//			"audit.IS_LOGIN_SUCCESSFUL from catissue_login_audit_event_log audit join catissue_user user on user.identifier=audit.USER_LOGIN_ID"; 
	
	private String GET_QUERY_LOG_DETAILS="select case when catUser.Last_Name is null then '' else catUser.Last_Name end, " +
																			 "case when catUser.First_Name is null then '' else catUser.First_Name end, catAudit.LOGIN_TIMESTAMP, " +
																			 "case when catAudit.LOGIN_IP_ADDRESS is null then '' else catAudit.LOGIN_IP_ADDRESS end, "+ 
																			 " case when catAudit.IS_LOGIN_SUCCESSFUL = 0 then '"+FAILED+"' else '"+SUCCESS+"' end "+ 
																			 " from catissue_login_audit_event_log catAudit join catissue_user catUser on catUser.identifier=catAudit.USER_LOGIN_ID";
			
			
			/*"select case when user.Last_Name is null then '' else user.Last_Name end, " +
			" case when user.First_Name is null then '' else user.First_Name end, " +
			" audit.LOGIN_TIMESTAMP, case when audit.LOGIN_IP_ADDRESS is null then '' else audit.LOGIN_IP_ADDRESS end, " +
			" case when audit.IS_LOGIN_SUCCESSFUL = 0 then 'Failed' else 'Success' end " +
			" from catissue_login_audit_event_log audit join catissue_user user on user.identifier=audit.USER_LOGIN_ID";*/
	
	private static final String SUCCESS = "Success";
	private static final String FAILED = "Failed";
	private static final String appName = CommonServiceLocator.getInstance().getAppName(); 
	
	private IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(appName);

	public List<LoginAuditBean> getQueryLogResults(int startIndex, int noOfRecords, Map<String, String> filterValueMap) 
			throws BizLogicException, DAOException, SQLException {
		String modifiedSql = addFilters(filterValueMap);
//		String sqlToBeExecuted = Utility.insertPageLimits(daoFactory.getDataBaseType(), modifiedSql, startIndex, noOfRecords);
		return getResultSet(modifiedSql);
	}
	
	public List<LoginAuditBean> getResultSet(String sqlToBeExecuted) throws BizLogicException,SQLException, DAOException {
		
		JDBCDAO jdbcDAO = null;
		ResultSet resultSet = null;
		try {
			jdbcDAO = daoFactory.getJDBCDAO(); 
			jdbcDAO.openSession(null); 
			resultSet = jdbcDAO.getResultSet(sqlToBeExecuted, null, null);
			return getLoginAudits(resultSet) ;
		} catch (DAOException e) {
			throw new BizLogicException(null,e,"DAOException: while Retrieving query log repots");
		} finally {
			if (resultSet != null) {
				try	{ jdbcDAO.closeStatement(resultSet); resultSet.close(); } catch (DAOException e) {} 
				jdbcDAO.closeSession();
			}	 
		}
	}
	
	private String addFilters(Map<String,String> columnVsValueMap) {
		
		StringBuilder modifiedSql = new StringBuilder(GET_QUERY_LOG_DETAILS);
		if (columnVsValueMap == null || columnVsValueMap.isEmpty()) {
			return modifiedSql.append(" ORDER BY catAudit.LOGIN_TIMESTAMP DESC ").toString();	
		}
			
		modifiedSql.append(" WHERE ");
		for(Map.Entry<String, String> entry : columnVsValueMap.entrySet()) {
			
			if (entry.getKey().equals("Query Id")) { 	
				modifiedSql.append("caeql.query_id =" + entry.getValue()).append(" AND ");
			} else if (entry.getKey().equals("IP Address")) {	
				modifiedSql.append("UPPER(").append(" catAudit.LOGIN_IP_ADDRESS ").append(") LIKE '%")
				.append(entry.getValue().toUpperCase()).append("%'").append(" AND ");
			} else if (entry.getKey().equals("User Name")) {
				if(entry.getValue().contains(","))
				{
					String[] names = (entry.getValue()).split("\\,");
					modifiedSql.append("UPPER(").append("catUser.last_name").append(") LIKE '%") 
						.append(names[0].toUpperCase()).append("%'").append(" OR UPPER(").append(" catUser.first_name").append(") LIKE '%")
						.append(names[1].toUpperCase()).append("%'").append(" AND ");
				}
				else
				{
					modifiedSql.append("UPPER(").append("catUser.last_name").append(") LIKE '%") 
					.append(entry.getValue().toUpperCase()).append("%'").append(" OR UPPER(").append(" catUser.first_name").append(") LIKE '%")
					.append(entry.getValue().toUpperCase()).append("%'").append(" AND ");
				}
			}
			else if (entry.getKey().equals("Login Attempt")) 
			{
				modifiedSql.append(" catAudit.IS_LOGIN_SUCCESSFUL = ");
				if(SUCCESS.equalsIgnoreCase(entry.getValue()))
				{
					modifiedSql.append(1);
				}
				else
				{
					modifiedSql.append(0);
				}
				modifiedSql.append(" AND ");
			}
		}	 
		
		 modifiedSql.delete(modifiedSql.length() - 5, modifiedSql.length()); //removing last AND
	     return modifiedSql.append(" ORDER BY catAudit.LOGIN_TIMESTAMP DESC ").toString();    
	}

	
	private List<LoginAuditBean> getLoginAudits(ResultSet rs) throws SQLException {
	
		List<LoginAuditBean> queryRunAudits = new ArrayList<LoginAuditBean>();		 
		if (rs == null) {
			return queryRunAudits;
		}
		while (rs.next()) {
			LoginAuditBean loginAuditBean = new LoginAuditBean();
			loginAuditBean.setUserName(getExecutedByValue(rs.getString(1), rs.getString(2)));
			if (rs.getString(3) != null) {
				loginAuditBean.setLoginTimeStamp(rs.getTimestamp((3)));	
			}			
			loginAuditBean.setIpAddress(rs.getString(4));
			loginAuditBean.setLastLoginState(rs.getString(5));
			queryRunAudits.add(loginAuditBean);
		}
		
		return queryRunAudits;
	}
	
	
	private String getExecutedByValue(String fName, String lName) { 
		return lName.replace(",", "") + ", " + fName.replace(",", "");
	}
}
