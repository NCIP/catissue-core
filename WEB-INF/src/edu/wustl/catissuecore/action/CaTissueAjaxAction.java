
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.dao.exception.DAOException;

public class CaTissueAjaxAction extends DispatchAction
{

	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, BizLogicException,
			IOException, DAOException
	{
		HttpSession session = request.getSession();
		session.invalidate();
		return null;
	}

	/**
	 * Method to encrypt the URL and encode it
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws PasswordEncryptionException
	 * @throws IOException
	 */
	public ActionForward encryptData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws PasswordEncryptionException, IOException
	{
		String dataString = "";
		Map<String, String[]> parameterMap = request.getParameterMap();
		Iterator<String> keys = parameterMap.keySet().iterator();

		while (keys.hasNext())
		{
			String key = keys.next();
			String[] value = parameterMap.get(key);
			dataString = dataString + key + "=" + value[0] + "&";
		}

		dataString = dataString.substring(0, dataString.length() - 1);
		PrintWriter out = response.getWriter();
		dataString = PasswordManager.encrypt(dataString);
		dataString = java.net.URLEncoder.encode(dataString, "UTF-8");
		out.print(dataString);

		return null;
	}
}
