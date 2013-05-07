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
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.dao.exception.DAOException;

/**
 * AddSpecimenAction gets the Specimen Id from Label or Barcode.
 * @author santosh_chandak
 */
public class DerivedMultipleSpecimenAddAction extends SecureAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @throws DAOException : DAOException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException, DAOException
	{
		final CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
		request.setAttribute(Constants.DERIVED_FORM, createSpecimenForm);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 *
	 * @param form : form
	 * @return String : String
	 */

	protected String getObjectId(AbstractActionForm form)
	{
		final CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (createSpecimenForm.getParentSpecimenId() != null
				|| createSpecimenForm.getParentSpecimenId() != "")
		{
			final Specimen specimen = AppUtility.getSpecimen(createSpecimenForm
					.getParentSpecimenId());
			specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
			final CollectionProtocolRegistration cpr = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			if (cpr != null)
			{
				final CollectionProtocol cp = cpr.getCollectionProtocol();
				return Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
			}
		}
		return null;

	}

}
