
package edu.wustl.catissuecore.action;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc

/**
 * The Class BulkOperatorAjaxAction.
 * @author nitesh_marwaha
 *
 */
public class JobGridAjaxAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(JobGridAjaxAction.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String index = request.getParameter("index");
		String jobId = request.getParameter("jobId");
		List<ArrayList> list = null;
		JDBCDAO dao = null;
		try
		{
			dao = AppUtility.openJDBCSession();
			String query = "Select IDENTIFIER, JOB_NAME, JOB_STATUS, TOTAL_RECORDS_COUNT, "
					+ "CURRENT_RECORDS_PROCESSED, FAILED_RECORDS_COUNT, TIME_TAKEN,JOB_STARTED_BY from "
					+ "JOB_DETAILS where IDENTIFIER = " + jobId;
			dao = AppUtility.openJDBCSession();
			list = dao.executeQuery(query);
			JSONObject resultObject = new JSONObject();
			if (!list.isEmpty())
			{
				List innerList = list.get(0);
				if (innerList != null && !innerList.isEmpty())
				{
					resultObject.append("identifier", innerList.get(0));
					resultObject.append("jobName", innerList.get(1));
					resultObject.append("jobStatus", innerList.get(2));
					resultObject.append("totalRecords", innerList.get(3));
					resultObject.append("processedRecords", innerList.get(4));
					resultObject.append("failedRecords", innerList.get(5));
					resultObject.append("timeTaken", innerList.get(6));
					resultObject.append("startedBy", innerList.get(7));
					resultObject.append("index", index);
				}
			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			Writer writer = response.getWriter();
			writer.write(new JSONObject().put("resultObject", resultObject).toString());
		}
		catch (JSONException jsonExp)
		{
			logger.error(jsonExp.getMessage(), jsonExp);
		}
		finally
		{
			try
			{
				if (dao != null)
				{
					AppUtility.closeJDBCSession(dao);
				}
			}
			catch (final DAOException daoExp)
			{
				logger.error(daoExp);
			}
		}
		return null;
	}
}