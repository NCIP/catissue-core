package edu.wustl.catissuecore.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import krishagni.catissueplus.util.DAOUtil;

import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.DAOUtility;

public class LoginAuditManager
{

    public LoginAuditManager()
    {
    }

    private List getLoginAuditEventDetails(Long userLoginId, int maxResults, Boolean isLoginSuccessful, String loginName) throws BizLogicException
        
    {
        StringBuffer queryName = new StringBuffer(500);
        queryName.append("select new edu.wustl.common.domain.LoginDetails(loginAudit.userLoginId, loginAudit.sourceId, loginAudit.ipAddress, loginAudit.isLoginSuccessful) from edu.wustl.common.domain.LoginEvent loginAudit");
        List columnValueBeans = new ArrayList();
        queryName.append(appendWhereClause(userLoginId, isLoginSuccessful, columnValueBeans));
        queryName.append(" order by loginAudit.timestamp desc");
        List loginDetails = null;
        HibernateDAO hibernateDao = null;
        try
        {
	        hibernateDao = DAOUtil.openDAOSession(null);
	        
	        if(maxResults > 0)
	            loginDetails = hibernateDao.executeQuery(queryName.toString(),1,maxResults, columnValueBeans);
	        else
	            loginDetails = hibernateDao.executeQuery(queryName.toString(), columnValueBeans);
        } catch (DAOException e) {
			throw new BizLogicException(e);
		}
        finally
        {
        	DAOUtil.closeDAOSession(hibernateDao);
        }
        
        return loginDetails;
    }

    private String appendWhereClause(Long userLoginId, Boolean isLoginSuccessful, List columnValueBeans)
    {
        StringBuffer appendWhereClause = new StringBuffer();
        if(userLoginId == null && isLoginSuccessful == null)
        {
            appendWhereClause.append("");
        } else
        {
            appendWhereClause.append(" where");
            appendWhereColumn(isLoginSuccessful, "loginAudit.isLoginSuccessful", columnValueBeans, appendWhereClause);
            appendWhereColumn(userLoginId, "loginAudit.userLoginId", columnValueBeans, appendWhereClause);
//            appendWhereColumn(sinceDate, "loginAudit.timestamp", columnValueBeans, appendWhereClause);
//            appendWhereColumn(loginName, "loginAudit.loginName", columnValueBeans, appendWhereClause);
        }
        return appendWhereClause.toString();
    }

    private void appendWhereColumn(Object columnValue, String columnName, List columnValueBeans, StringBuffer appendWhereClause)
    {
        if(columnValue != null)
        {
            if("where".equalsIgnoreCase(appendWhereClause.toString().trim()))
            {
                appendWhereClause.append(" ");
                appendWhereClause.append(columnName);
                appendEquals(columnName, appendWhereClause);
                appendWhereClause.append("?");
            } else
            {
                appendWhereClause.append(" and ");
                appendWhereClause.append(columnName);
                appendEquals(columnName, appendWhereClause);
                appendWhereClause.append("?");
            }
            columnValueBeans.add(columnValue);
        }
    }

    private void appendEquals(String columnName, StringBuffer appendWhereClause)
    {
        if("loginAudit.timestamp".equalsIgnoreCase(columnName))
            appendWhereClause.append(">=");
        else
            appendWhereClause.append("=");
    }

    public LoginDetails getLastSuccessfulLoginDetails(String userLoginName)
        throws BizLogicException
    {
        LoginDetails loginDetails = null;
        List loginAuditDetails = getLoginAuditEventDetails(null, 0, Boolean.valueOf(true), userLoginName);
        if(loginAuditDetails != null && !loginAuditDetails.isEmpty())
            loginDetails = (LoginDetails)loginAuditDetails.iterator().next();
        return loginDetails;
    }

    public LoginDetails getLastUnSuccessfulLoginDetails(String userLoginName)
        throws BizLogicException
    {
        LoginDetails loginDetails = null;
        List loginAuditDetails = getLoginAuditEventDetails(null, 0, Boolean.valueOf(false), userLoginName);
        if(loginAuditDetails != null && !loginAuditDetails.isEmpty())
            loginDetails = (LoginDetails)loginAuditDetails.iterator().next();
        return loginDetails;
    }

    public List getLastSuccessfulLoginDetails(String userLoginName, int count)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(null, count, Boolean.valueOf(true), userLoginName);
    }

    public List getLastUnSuccessfulLoginDetails(String userLoginName, int count)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(null, count, Boolean.valueOf(false), userLoginName);
    }

//    public List getLastSuccessfulLoginDetails(String userLoginName)
//        throws BizLogicException
//    {
//        return getLoginAuditEventDetails(null, 0, Boolean.valueOf(true), userLoginName);
//    }

//    public List getLastUnSuccessfulLoginDetails(String userLoginName, Date since)
//        throws BizLogicException
//    {
//        return getLoginAuditEventDetails(null, 0, since, Boolean.valueOf(false), userLoginName);
//    }

    public LoginDetails getLastSuccessfulLoginDetails(Long userLoginId)
        throws BizLogicException
    {
        LoginDetails loginDetails = null;
        List loginAuditDetails = getLoginAuditEventDetails(userLoginId, 0, Boolean.valueOf(true), null);
        if(loginAuditDetails != null && !loginAuditDetails.isEmpty())
            loginDetails = (LoginDetails)loginAuditDetails.iterator().next();
        return loginDetails;
    }

    public LoginDetails getLastUnSuccessfulLoginDetails(Long userLoginId)
        throws BizLogicException
    {
        LoginDetails loginDetails = null;
        List loginAuditDetails = getLoginAuditEventDetails(userLoginId, 0, Boolean.valueOf(false), null);
        if(loginAuditDetails != null && !loginAuditDetails.isEmpty())
            loginDetails = (LoginDetails)loginAuditDetails.iterator().next();
        return loginDetails;
    }

    public List getLastSuccessfulLoginDetails(Long userLoginId, int count)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(userLoginId, count, Boolean.valueOf(true), null);
    }

    public List getLastUnSuccessfulLoginDetails(Long userLoginId, int count)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(userLoginId, count, Boolean.valueOf(false), null);
    }

//    public List getLastSuccessfulLoginDetails(Long userLoginId)
//        throws BizLogicException
//    {
//        return getLoginAuditEventDetails(userLoginId, 0, since, Boolean.valueOf(true), null);
//    }

//    public List getLastUnSuccessfulLoginDetails(Long userLoginId)
//        throws BizLogicException
//    {
//        return getLoginAuditEventDetails(userLoginId, 0, since, Boolean.valueOf(false), null);
//    }

    public List getAllLoginDetails()
        throws BizLogicException
    {
        return getLoginAuditEventDetails(null, 0, null, null);
    }

    public List getAllLoginDetailsForUser(Long userLoginId, int maxResults)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(userLoginId, maxResults, null, null);
    }

    public List getAllLoginDetailsForUser(String loginName, int maxResults)
        throws BizLogicException
    {
        return getLoginAuditEventDetails(null, maxResults, null, loginName);
    }
}

