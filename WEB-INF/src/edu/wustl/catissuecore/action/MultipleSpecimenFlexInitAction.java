
package edu.wustl.catissuecore.action;

import java.util.HashMap;

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
import edu.wustl.common.util.global.CommonServiceLocator;

//import edu.wustl.common.action.SecureAction;

/**
 * @author renuka_bajpai
 *
 */
public class MultipleSpecimenFlexInitAction extends SecureAction
{

	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		String mode = Constants.ADD;
		if (operation != null && operation.equalsIgnoreCase(Constants.EDIT))
			mode = Constants.EDIT;

		String showParentSelection = "false";
		String showLabel = "true";
		String showBarcode = "true";

		String parentType = request.getParameter("parentType");
		if (parentType == null)
		{
			parentType = Constants.NEW_SPECIMEN_TYPE;
			showParentSelection = "true";

		}
		String numberOfSpecimens = getNumberOfSpecimens(request);
		String parentName = getParentName(request, parentType);

		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
		{
			showLabel = "false";
		}
		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenBarcodeGeneratorAvl)
		{
			showBarcode = "false";
		}
		setMSPRequestParame(request, mode, parentType, parentName, numberOfSpecimens,
				showParentSelection, showLabel, showBarcode, CommonServiceLocator.getInstance()
						.getDatePattern());

		String pageOf = (String) request.getParameter("pageOf");
		if (pageOf != null)
		{
			request.setAttribute(Constants.PAGE_OF, pageOf);
			return mapping.findForward(pageOf);
		}
		else if (Constants.EDIT.equalsIgnoreCase(mode))
		{
			request.setAttribute(Constants.PAGE_OF, "pageOfMultipleSpWithMenu");
		}

		return mapping.findForward("success");
	}

	private void setMSPRequestParame(HttpServletRequest request, String mode, String parentType,
			String parentName, String numberOfSpecimens, String showParentSelection,
			String showLabel, String showBarcode, String dateFormat)
	{

		request.setAttribute("MODE", mode);
		request.setAttribute("PARENT_TYPE", parentType);
		request.setAttribute("PARENT_NAME", parentName);
		request.setAttribute("SP_COUNT", numberOfSpecimens);
		request.setAttribute("SHOW_PARENT_SELECTION", showParentSelection);
		request.setAttribute("SHOW_LABEL", showLabel);
		request.setAttribute("SHOW_BARCODE", showBarcode);
		request.setAttribute("DATE_FORMAT", dateFormat.toUpperCase());

	}

	private String getParentName(HttpServletRequest request, String parentType)
	{
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null)
		{
			if (Constants.NEW_SPECIMEN_TYPE.equals(parentType))
			{
				String specimenCollectionGroupName = "";
				Object obj = forwardToHashMap.get("specimenCollectionGroupName");
				if (obj != null)
				{
					specimenCollectionGroupName = (String) obj;
				}

				return specimenCollectionGroupName;
			}
			else if (Constants.DERIVED_SPECIMEN_TYPE.equals(parentType))
			{
				String parentSpecimenLabel = "";
				Object obj = forwardToHashMap.get("specimenLabel");
				if (obj != null)
				{
					parentSpecimenLabel = (String) obj;
				}

				return parentSpecimenLabel;
			}
		}
		return "";
	}

	private String getNumberOfSpecimens(HttpServletRequest request)
	{
		String numberOfSpecimens = request.getParameter(Constants.NUMBER_OF_SPECIMENS);
		System.out.println("numberOfSpecimens " + numberOfSpecimens);
		if (numberOfSpecimens == null || numberOfSpecimens.equals(""))
		{
			numberOfSpecimens = "1";
		}
		return numberOfSpecimens;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#getObjectId(edu.wustl.common.actionForm.AbstractActionForm)
	 */

	protected String getObjectId(AbstractActionForm form)
	{
		CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (createSpecimenForm.getParentSpecimenId() != null
				&& createSpecimenForm.getParentSpecimenId() != "")
		{
			Specimen specimen = AppUtility.getSpecimen(createSpecimenForm.getParentSpecimenId());
			specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
			CollectionProtocolRegistration cpr = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			if (cpr != null)
			{
				CollectionProtocol cp = cpr.getCollectionProtocol();
				return Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
			}
		}
		return null;

	}
}