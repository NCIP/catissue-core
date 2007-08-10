/**
 * <p>Title: CreateSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * AddSpecimenAction gets the Specimen Id from Label or Barcode
 * @author santosh_chandak
 */
public class AddSpecimenAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, DAOException
	{

		CreateSpecimenForm createForm = (CreateSpecimenForm) form;
		String pageOf = request.getParameter(Constants.PAGEOF);
		
		String sourceObjectName = Specimen.class.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};

		String[] whereColumnName = new String[1];
		Object[] whereColumnValue = new Object[1];
	
		// checks whether label or barcode is selected
		if (createForm.getCheckedButton().equals("1"))
		{
			whereColumnName[0] = Constants.SYSTEM_LABEL;
			whereColumnValue[0] = createForm.getParentSpecimenLabel().trim();
		}
		else
		{
			whereColumnName[0] = Constants.SYSTEM_BARCODE;
			whereColumnValue[0] = createForm.getParentSpecimenBarcode().trim();
		}

		String[] whereColumnCondition = {"="};

		String joinCondition = null;
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

		List list = bizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);

		/**
		 *  If list is not empty, set the Parent specimen Id and forward to success. 
		 *  If list is null or empty, forward to failure.
		 */
		if (list != null && !list.isEmpty())
		{
			Object obj = list.get(0);
			Long specimen = (Long) obj;
			createForm.setParentSpecimenId("" + specimen.longValue());
			createForm.setReset(false);   // Will not reset the parameters   
			if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
			{
				return mapping.findForward(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY);
			}
			return mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			return mapping.findForward(Constants.FAILURE);
		}

	}
}