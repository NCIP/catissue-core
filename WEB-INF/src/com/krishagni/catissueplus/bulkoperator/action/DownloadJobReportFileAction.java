/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.krishagni.catissueplus.bulkoperator.util.AppUtility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.global.CommonServiceLocator;
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
	 * @see com.krishagni.catissueplus.core.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String fileId;
		fileId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		ResultSet dblist = null;
		JDBCDAO dao = null;
		try
		{
			dao = AppUtility.openJDBCSession();
			dblist = dao
					.getQueryResultSet("select LOG_FILE, LOG_FILE_NAME from JOB_DETAILS where IDENTIFIER = "
							+ fileId);

			if (dblist.next())
			{
				InputStream inputStream = dblist.getBinaryStream(1);

				String zipFileName = dblist.getString(2);

				String tempDirFilePath = CommonServiceLocator.getInstance().getAppHome();
				File file = new File(tempDirFilePath + "/" + zipFileName);
				OutputStream out = new FileOutputStream(file);
				byte buff[] = new byte[1024];
				int len;
				while ((len = inputStream.read(buff)) > 0)
				{
					out.write(buff, 0, len);
				}
				out.close();
				inputStream.close();

				if (file.exists())
				{
					response.setContentType("application/download");
					response.setHeader("Content-Disposition", "attachment;filename=\""
							+ zipFileName + "\";");
					response.setContentLength((int) file.length());

					OutputStream outputStream = response.getOutputStream();
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					int count;
					byte buf[] = new byte[4096];
					while ((count = bis.read(buf)) > -1)
					{
						outputStream.write(buf, 0, count);
					}
					outputStream.flush();
					bis.close();
				}
				else
				{
					throw new Exception("An Exception has occured....");
				}
				file.delete();
			}

		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
		}
		finally
		{
			try
			{
				if(dblist != null)
				{
					dblist.close();
				}
				if (dao != null)
				{
					dao.closeSession();

				}
			}
			catch (final DAOException daoExp)
			{
				logger.debug(daoExp);
			}
		}
		return null;
	}
}