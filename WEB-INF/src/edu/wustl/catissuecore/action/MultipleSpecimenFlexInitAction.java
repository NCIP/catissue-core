
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;

//import edu.wustl.common.action.SecureAction;

/**
 * @author renuka_bajpai
 *
 */
public class MultipleSpecimenFlexInitAction extends SecureAction
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
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */

	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		String mode = Constants.ADD;
		if (operation != null && operation.equalsIgnoreCase(Constants.EDIT))
		{
			mode = Constants.EDIT;
		}

		String showParentSelection = "false";
		String showLabel = "true";
		String showBarcode = "true";

		String parentType = request.getParameter("parentType");
		if (parentType == null)
		{
			parentType = Constants.NEW_SPECIMEN_TYPE;
			showParentSelection = "true";

		}
		final String numberOfSpecimens = this.getNumberOfSpecimens(request);
		final String parentName = this.getParentName(request, parentType);

		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl)
		{
			showLabel = "false";
		}
		if (edu.wustl.catissuecore.util.global.Variables.isSpecimenBarcodeGeneratorAvl)
		{
			showBarcode = "false";
		}
		this.setMSPRequestParame(request, mode, parentType, parentName, numberOfSpecimens,
				showParentSelection, showBarcode, CommonServiceLocator.getInstance()
						.getDatePattern());

		final String pageOf = request.getParameter("pageOf");
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

	/**
	 *
	 * @param request : request
	 * @param mode : mode
	 * @param parentType : parentType
	 * @param parentName : parentName
	 * @param numberOfSpecimens : numberOfSpecimens
	 * @param showParentSelection : showParentSelection
	 * @param showLabel : showLabel
	 * @param showBarcode : showBarcode
	 * @param dateFormat : dateFormat
	 */
	private void setMSPRequestParame(HttpServletRequest request, String mode, String parentType,
			String parentName, String numberOfSpecimens, String showParentSelection,
			String showBarcode, String dateFormat)
	{

		request.setAttribute("MODE", mode);
		request.setAttribute("PARENT_TYPE", parentType);
		request.setAttribute("PARENT_NAME", parentName);
		request.setAttribute("SP_COUNT", numberOfSpecimens);
		request.setAttribute("SHOW_PARENT_SELECTION", showParentSelection);
		request.setAttribute("SHOW_BARCODE", showBarcode);
		request.setAttribute("DATE_FORMAT", dateFormat.toUpperCase());

	}

	/**
	 *
	 * @param request : request
	 * @param parentType : parentType
	 * @return String : String
	 * @throws BizLogicException
	 */
	private String getParentName(HttpServletRequest request, String parentType) throws BizLogicException
	{
		final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null)
		{
			if (Constants.NEW_SPECIMEN_TYPE.equals(parentType))
			{
				String specimenCollectionGroupName = "";
				final Object obj = forwardToHashMap.get("specimenCollectionGroupName");
				if (obj != null)
				{
					specimenCollectionGroupName = (String) obj;
				}

				String  scgName = specimenCollectionGroupName.replace("'", "''");
				String generateLabel = Boolean.toString(false);
				String hql = "select scg.collectionProtocolRegistration.collectionProtocol.specimenLabelFormat, "
						+ "scg.collectionProtocolRegistration.collectionProtocol.derivativeLabelFormat, "
						+ "scg.collectionProtocolRegistration.collectionProtocol.aliquotLabelFormat "
						+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg where scg.name='"
						+ scgName + "'";

				setRequestAttribute(request, generateLabel, hql,Constants.NEW_SPECIMEN);
				return specimenCollectionGroupName;
			}
			else if (Constants.DERIVED_SPECIMEN_TYPE.equals(parentType))
			{
				String parentSpecimenLabel = "";
				final Object obj = forwardToHashMap.get("specimenLabel");
				if (obj != null)
				{
					parentSpecimenLabel = (String) obj;
				}
				else
				{
					if (forwardToHashMap.get(Constants.SPECIMEN_LABEL) != null)
					{
						parentSpecimenLabel = forwardToHashMap.get(Constants.SPECIMEN_LABEL)
								.toString();
					}
				}
				String generateLabel = Boolean.toString(false);
				if (forwardToHashMap.get("generateLabel") != null)
				{
					generateLabel = Boolean.toString(!(Boolean) forwardToHashMap
							.get("generateLabel"));
				}
				String parentName = parentSpecimenLabel.replace("'", "''");
				String hql = "select specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.specimenLabelFormat, "
						+ "specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.derivativeLabelFormat, "
						+ "specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.aliquotLabelFormat "
						+ "from edu.wustl.catissuecore.domain.Specimen as specimen where specimen.label ='"
						+ parentName + "'";

				//				try
				//				{
				//					List list = AppUtility.executeQuery(hql);
				//					String parentLabelFormat = null;
				//					String deriveLabelFormat = null;
				//					String aliquotLabelFormat = null;
				//					Object[] obje = (Object[])list.get(0);
				//					if(obje[0] != null)
				//					parentLabelFormat = obje[0].toString();
				//					if(obje[1] != null)
				//					deriveLabelFormat = obje[1].toString();
				//					if(obje[2] != null)
				//					aliquotLabelFormat = obje[2].toString();
				//					generateLabel = Boolean.toString(!SpecimenUtil.isLblGenOnForCP(parentLabelFormat, deriveLabelFormat, aliquotLabelFormat, "Derived"));
				//				}
				//				catch (ApplicationException e)
				//				{
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}
				//				request.setAttribute("SHOW_LABEL", generateLabel);
				setRequestAttribute(request, generateLabel, hql,Constants.DERIVED_SPECIMEN);
				return parentSpecimenLabel;
			}
		}
		else
		{
			request.setAttribute("SHOW_LABEL", Boolean.toString(!Variables.isSpecimenLabelGeneratorAvl));
		}
		return "";
	}

	/**
	 * @param request
	 * @param generateLabel
	 * @param hql
	 * @param derivedSpecimen
	 * @throws BizLogicException
	 */
	private void setRequestAttribute(HttpServletRequest request, String generateLabel, String hql, String lineage) throws BizLogicException
	{
		try
		{

			List list = AppUtility.executeQuery(hql);
			String parentLabelFormat = null;
			String aliquotLabelFormat = null;
			String deriveLabelFormat = null;
			Object[] obje = (Object[]) list.get(0);
			if (obje[0] != null)
			{
				parentLabelFormat = obje[0].toString();
			}
			if (obje[1] != null)
			{
				deriveLabelFormat = obje[1].toString();
			}
			if (obje[2] != null)
			{
				aliquotLabelFormat = obje[2].toString();
			}
//			generateLabel = Boolean.toString(!SpecimenUtil.isLblGenOnForCP(parentLabelFormat,
//					deriveLabelFormat, aliquotLabelFormat, lineage));
		}
		catch (ApplicationException e)
		{
//			LOGGER.error(e.getMessage(), e);
			throw new BizLogicException(e.getErrorKey(), e, e.getMessage());
		}
//		if(Variables.isSpecimenLabelGeneratorAvl){
			request.setAttribute("SHOW_LABEL", Boolean.toString(!Variables.isSpecimenLabelGeneratorAvl));
//		}else{
//			request.setAttribute("SHOW_LABEL", generateLabel);
//		}
	}

	/**
	 *
	 * @param request : request
	 * @return String : String
	 */
	private String getNumberOfSpecimens(HttpServletRequest request)
	{
		String numberOfSpecimens = request.getParameter(Constants.NUMBER_OF_SPECIMENS);
		if (numberOfSpecimens == null || numberOfSpecimens.equals(""))
		{
			numberOfSpecimens = "1";
		}
		return numberOfSpecimens;
	}

	/**
	 *
	 * @param form : form
	 * @return String : String
	 */
	/*

		protected String getObjectId(AbstractActionForm form)
		{
			final CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
			SpecimenCollectionGroup specimenCollectionGroup = null;
			if (createSpecimenForm.getParentSpecimenId() != null
					&& createSpecimenForm.getParentSpecimenId() != "")
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

		}*/
}