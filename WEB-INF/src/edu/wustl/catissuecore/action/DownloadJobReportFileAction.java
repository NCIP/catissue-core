
package edu.wustl.catissuecore.action;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadBulkOperationLogAction.
 */
public class DownloadJobReportFileAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(DownloadJobReportFileAction.class);

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String fileId;
		fileId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		JDBCDAO dao = null;
		try
		{
			dao = AppUtility.openJDBCSession();
			ResultSet dblist = dao
					.getQueryResultSet("select LOG_FILE, LOG_FILE_NAME from JOB_DETAILS where IDENTIFIER = "
							+ fileId);
			if (dblist.next())
			{
				InputStream inputStream = dblist.getBinaryStream(1);
				String zipFileName = dblist.getString(2);
				int count, writeCount;
				long length = 0L;
				final byte[] buf = new byte[4096];
				count = inputStream.read(buf);
				writeCount = count;
				while (count > -1)
				{
					length += count;
					count = inputStream.read(buf);
				}
				response.setContentLength((int) length);
				response.setHeader("Content-Disposition", "attachment;filename=\"" + zipFileName
						+ "\";");
				final OutputStream outStream = response.getOutputStream();
				while (writeCount > -1)
				{
					outStream.write(buf, 0, writeCount);
					writeCount = inputStream.read(buf);
				}
				outStream.flush();
				inputStream.close();
			}
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
		}
		finally
		{
			try
			{
				if (dao != null)
				{
					dao.closeSession();
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