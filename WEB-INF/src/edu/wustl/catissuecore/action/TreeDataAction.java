/**
 * <p>
 * Title: TreeDataAction Class>
 * <p>
 * Description: TreeDataAction creates a tree from the temporary query results
 * table and passes it to applet.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.tree.TreeDataInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

/**
 * TreeDataAction creates a tree from the temporary query results table and
 * passes it to applet.
 *
 * @author gautam_shetty
 */
public class TreeDataAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(TreeDataAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ObjectOutputStream out = null;
		// Map columnIdsMap = new HashMap();

		try
		{
			final String pageOf = URLDecoder.decode(request.getParameter(Constants.PAGE_OF));
			TreeDataInterface bizLogic = new StorageContainerBizLogic();
			Vector dataList = new Vector();

			// Map containerMap = new HashMap(), containerRelationMap = new
			// HashMap();
			/*if (pageOf.equals(Constants.PAGE_OF_TISSUE_SITE))
			{
				bizLogic = new CDEBizLogic();
				final CDEBizLogic cdeBizLogic = (CDEBizLogic) bizLogic;
				final String cdeName = request.getParameter(Constants.CDE_NAME);
				dataList = cdeBizLogic.getTreeViewData(cdeName);
			}
			else*/ if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION)
					|| pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_ALIQUOT))
			{
				dataList = bizLogic.getTreeViewData();
			}

			final String contentType = "application/x-java-serialized-object";
			response.setContentType(contentType);
			out = new ObjectOutputStream(response.getOutputStream());

			if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION)
					|| pageOf.equals(Constants.PAGE_OF_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_TISSUE_SITE)
					|| pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)
					|| pageOf.equals(Constants.PAGE_OF_SPECIMEN_TREE)
					|| pageOf.equals(Constants.PAGE_OF_ALIQUOT))
			{
				out.writeObject(dataList);
			}
			else
			{
				out.writeObject(dataList);
			}
		}
		catch (final Exception exp)
		{
			this.logger.error(exp.getMessage(), exp);
		}
		finally
		{
			if (out != null)
			{
				out.flush();
				out.close();
			}
		}

		return mapping.findForward(Constants.SUCCESS);
	}
}