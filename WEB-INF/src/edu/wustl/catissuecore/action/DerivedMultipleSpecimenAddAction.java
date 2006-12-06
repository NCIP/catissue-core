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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * AddSpecimenAction gets the Specimen Id from Label or Barcode
 * @author santosh_chandak
 */
public class DerivedMultipleSpecimenAddAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, DAOException
	{
            CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;	
            request.setAttribute(Constants.DERIVED_FORM,createSpecimenForm);
			return mapping.findForward(Constants.SUCCESS);
	}

	}
