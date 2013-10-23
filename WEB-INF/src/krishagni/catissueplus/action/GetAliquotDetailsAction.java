
package krishagni.catissueplus.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;

public class GetAliquotDetailsAction extends BaseAction
{

	public static final String ALIQUOT_COUNT = "aliquotCount";
	public static final String PARENT_SPECIMEN_LABEL = "parentSpecimentLabel";
	public static final String SEARCH_BASED_ON = "searchBasedOn";
	public static final String QUANTITY_PER_ALIQUOT = "quantityPerAliquot";
	public static final String PARENT_SPECIMEN_BARCODE = "parentSpecimentBarcode";
	public static final String AVAILABEL_CONTAINER_NAME = "availabelContainerName";
	public static final String ALIQUOT_GRID_XML = "aliquotGridXml";
	public static final String ALIQUOTS_DETAILS_DTO = "aliquotDetailsDTO";

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
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		request.setAttribute("pageOf", request.getParameter("pageOf"));
		request.setAttribute(ALIQUOT_COUNT, request.getParameter(ALIQUOT_COUNT));
		request.setAttribute(QUANTITY_PER_ALIQUOT, request.getParameter(QUANTITY_PER_ALIQUOT));
		request.setAttribute(PARENT_SPECIMEN_LABEL, request.getParameter(PARENT_SPECIMEN_LABEL));
		request.setAttribute(PARENT_SPECIMEN_BARCODE, request.getParameter(PARENT_SPECIMEN_BARCODE));
		request.setAttribute(SEARCH_BASED_ON, request.getParameter(SEARCH_BASED_ON));
		request.setAttribute("creationDate", new Date());
		boolean isLabelGenerationOn = false;
		if ((Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl))
		{
			isLabelGenerationOn = true;
		}
		request.setAttribute("isLabelGenerationOn", isLabelGenerationOn);
		boolean isBarGenerationOn = false;
		if (Variables.isSpecimenBarcodeGeneratorAvl)
		{
			isBarGenerationOn = true;
		}
		request.setAttribute("isBarGenerationOn", isBarGenerationOn);
		request.setAttribute(Constants.SUCCESS, "true");

		return mapping.findForward(request.getParameter("pageOf"));
	}

}
