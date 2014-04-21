
package krishagni.catissueplus.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.bizlogic.AliquotBizLogic;
import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import krishagni.catissueplus.dao.SpecimenDAO;
import krishagni.catissueplus.dto.AliquotContainerDetailsDTO;
import krishagni.catissueplus.dto.AliquotDetailsDTO;
import krishagni.catissueplus.dto.ContainerInputDetailsDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CatissuePlusCommonUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.velocity.VelocityManager;
import edu.wustl.dao.HibernateDAO;

public class GetAliquotInfoAjaxAction extends BaseAction
{

	public static final String ALIQUOT_COUNT = "aliquotCount";
	public static final String PARENT_SPECIMEN_LABEL = "parentSpecimentLabel";
	public static final String SEARCH_BASED_ON = "searchBasedOn";
	public static final String QUANTITY_PER_ALIQUOT = "quantityPerAliquot";
	public static final String PARENT_SPECIMEN_BARCODE = "parentSpecimentBarcode";
	public static final String ALIQUOT_GRID_VM_TEMPLATE = "aliquotGridTemplate.vm";
	public static final String AVAILABEL_CONTAINER_NAME = "availabelContainerName";
	public static final String ALIQUOT_GRID_XML = "aliquotGridXml";
	public static final String ALIQUOTS_DETAILS_DTO = "aliquotDetailsDTO";

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 *
	 * @throws Exception generic exception
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HibernateDAO hibernateDAO = null;
		JSONObject jsonObject = new JSONObject();

		try
		{
			SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			hibernateDAO = (HibernateDAO) AppUtility.openDAOSession((SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA));
			final AliquotBizLogic aliquotBizLogic = new AliquotBizLogic();
			List<SpecimenDTO> specimenDTOList;
			if (request.getParameter("validated").equals("false"))
			{
				specimenDTOList = aliquotBizLogic.getAvailabelSpecimenList(
						request.getParameter(PARENT_SPECIMEN_LABEL), hibernateDAO);
				JSONArray jsonArr = new JSONArray();
				if (specimenDTOList.size() > 1)
				{
					for (int i = 0; i < specimenDTOList.size(); i++)
					{
						SpecimenDTO dtoObj = specimenDTOList.get(i);
						JSONObject specJsonObject = new JSONObject();
						specJsonObject.put("label", dtoObj.getLabel());
						specJsonObject.put("barcode", dtoObj.getBarcode());
						jsonArr.put(specJsonObject);
					}
					jsonObject.put("conflictingSpecimens", jsonArr);
					response.getWriter().write(jsonObject.toString());
					return null;
				}

			}

			if (request.getParameter(ALIQUOT_COUNT) == null
					|| request.getParameter(ALIQUOT_COUNT).equals(""))
			{
				jsonObject.put(Constants.SUCCESS, "false");
				jsonObject.put("message", Constants.VALID_ALIQUOT_COUNT_ERROR);
			}
			else
			{

				String searchBasedOn = request.getParameter(SEARCH_BASED_ON);

				int aliquotCount = Integer.parseInt(request.getParameter(ALIQUOT_COUNT));
				Double quantityPerAliquot = null;

				if (request.getParameter(QUANTITY_PER_ALIQUOT) != null
						&& request.getParameter(QUANTITY_PER_ALIQUOT) != "")
				{
					quantityPerAliquot = Double.parseDouble(request
							.getParameter(QUANTITY_PER_ALIQUOT));
				}
				//PARENT_SPECIMEN_LABEL will be either specimen label or its barcode.
				String parentSpecimenLabelOrBarcode = request.getParameter(PARENT_SPECIMEN_LABEL);
List<AliquotContainerDetailsDTO> containerDetailsDTOs = new ArrayList<AliquotContainerDetailsDTO>();
				AliquotDetailsDTO aliquotDetailsDTO = aliquotBizLogic.getAliquotDetailsDTO(
						parentSpecimenLabelOrBarcode, aliquotCount, quantityPerAliquot,
						hibernateDAO, sessionDataBean,containerDetailsDTOs);

				ContainerInputDetailsDTO containerInputDetails = new ContainerInputDetailsDTO();
				containerInputDetails.aliquotCount = aliquotCount;
				SpecimenDAO specimenDAO = new SpecimenDAO();
				containerInputDetails.cpId = specimenDAO.getCpId(
						aliquotDetailsDTO.getParentLabel(), hibernateDAO);
				containerInputDetails.isAdmin = sessionDataBean.isAdmin();
				containerInputDetails.userId = sessionDataBean.getUserId();
				containerInputDetails.specimenClass = aliquotDetailsDTO.getSpecimenClass();
				containerInputDetails.specimenType = aliquotDetailsDTO.getType();
				StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();
				List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = new ArrayList<AliquotContainerDetailsDTO>(); 
				aliquotContainerDetailsDTOList = storageContainerBizlogic
						.getStorageContainerList(containerInputDetails, null, hibernateDAO, 5,aliquotContainerDetailsDTOList);

				String aliquotsInfoXmlString = VelocityManager.getInstance().evaluate(
						aliquotDetailsDTO.getPerAliquotDetailsCollection(),
						ALIQUOT_GRID_VM_TEMPLATE);

				StringBuffer containerNameStr = new StringBuffer();
				for (int i = 0; i < aliquotContainerDetailsDTOList.size(); i++)
				{
					containerNameStr.append(aliquotContainerDetailsDTOList.get(i).containerName)
							.append(",");
				}
				containerNameStr.append(Constants.STORAGE_TYPE_POSITION_VIRTUAL);

				jsonObject.put(AVAILABEL_CONTAINER_NAME, containerNameStr.toString());
				jsonObject.put(ALIQUOT_GRID_XML, aliquotsInfoXmlString.trim().replaceAll("\n", ""));
				Gson gson = CatissuePlusCommonUtil.getGson();

				jsonObject.put(ALIQUOTS_DETAILS_DTO, gson.toJson(aliquotDetailsDTO));
				jsonObject.put(Constants.CP_ID, specimenDAO.getCpId(
						aliquotDetailsDTO.getParentId(), hibernateDAO));
				boolean isLabelGenerationOn = false;
				if ((Variables.isSpecimenLabelGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl))
				{
					isLabelGenerationOn = true;
				}
				jsonObject.put("isLabelGenerationOn", isLabelGenerationOn);
				boolean isBarGenerationOn = false;
				if ((Variables.isSpecimenBarcodeGeneratorAvl || Variables.isTemplateBasedLblGeneratorAvl))
				{
					isBarGenerationOn = true;
				}
				jsonObject.put("isBarGenerationOn", isBarGenerationOn);
				jsonObject.put(
						"unit",
						AppUtility.getUnit(aliquotDetailsDTO.getSpecimenClass(),
								aliquotDetailsDTO.getType()));
				jsonObject.put(Constants.SUCCESS, "true");

			}

		}
		catch (NumberFormatException ex)
		{
			jsonObject.put(Constants.SUCCESS, "false");
			jsonObject.put("message", Constants.VALID_ALIQUOT_COUNT_ERROR);

		}
		catch (ApplicationException ex)
		{
			jsonObject.put(Constants.SUCCESS, "false");
			jsonObject.put("message", ex.getMsgValues());
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDAO);
		}
		response.getWriter().write(jsonObject.toString());

		return null;
	}
}
