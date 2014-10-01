package krishagni.catissueplus.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import krishagni.catissueplus.dto.BiohazardDTO;
import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.SpecimenUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;


public class SpecimenAddAction extends CatissueBaseAction
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String pageOf = "pageOfNewSpecimenCPQuery";
		Gson gson = new Gson();
		SpecimenDTO specimenDTO = new SpecimenDTO();
		String obj = null;
		HibernateDAO hibernateDao = null;

		if (request.getAttribute(Constants.SYSTEM_IDENTIFIER) != null)
		{
			obj = request.getAttribute(Constants.SYSTEM_IDENTIFIER).toString();
		}
		else
		{
			obj = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		}
		try
		{
			hibernateDao = (HibernateDAO)AppUtility.openDAOSession(null);
			if (!Validator.isEmpty(obj))
			{
				Long identifier = Long.valueOf(Utility.toLong(obj));
				SpecimenCollectionGroup scg = (SpecimenCollectionGroup) hibernateDao.retrieveById(SpecimenCollectionGroup.class.getName(),
						identifier);
				specimenDTO = new krishagni.catissueplus.dto.SpecimenDTO();
				specimenDTO.setSpecimenCollectionGroupId(scg.getId());
				specimenDTO.setSpecimenCollectionGroupName(scg.getName());
				specimenDTO.setLineage(Constants.NEW_SPECIMEN);
				specimenDTO.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
				specimenDTO.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				specimenDTO.setAvailable(Boolean.FALSE);
				specimenDTO.setIsVirtual(Boolean.TRUE);
				specimenDTO.setAvailableQuantity(0.0);
				specimenDTO.setQuantity(0.0);
				specimenDTO.setPathologicalStatus(Constants.NOT_SPECIFIED);
				specimenDTO.setTissueSide(Constants.NOT_SPECIFIED);
				specimenDTO.setTissueSite(Constants.NOT_SPECIFIED);
				
				specimenDTO.setUserId(((SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA)).getUserId());
				Calendar cal = Calendar.getInstance();
				specimenDTO.setDisposalDate(cal.getTime());
				specimenDTO.setDisposalHours(Integer.toString(cal
						.get(Calendar.HOUR_OF_DAY)));
				specimenDTO.setDisposalMins(Integer.toString(cal
						.get(Calendar.MINUTE)));
				
				request.setAttribute("specimenDTO", specimenDTO);
				SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
						.getAttribute(Constants.SESSION_DATA);
				NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
//				List<Object> list = bizLogic.getcpIdandPartId(sessionDataBean, obj);
//				Object[] objArr = (Object[]) list.get(0);
//				Long cpId = Long.valueOf(objArr[0].toString());
				request.setAttribute("cpId", scg.getCollectionProtocolRegistration().getCollectionProtocol().getId());

				Long reportId = bizLogic.getAssociatedIdentifiedReportId(specimenDTO.getId(), hibernateDao);
				if (reportId == null)
				{
					reportId = -1l;
				}
				else if (AppUtility.isQuarantined(reportId))
				{
					reportId = -2l;
				}
				final HttpSession session = request.getSession();
				session.setAttribute(Constants.IDENTIFIED_REPORT_ID, reportId);
				
				request.setAttribute("isSpecimenLabelGeneratorAvl",
						SpecimenUtil.isSpecimenLabelGeneratorAvl(identifier, hibernateDao));
				request.setAttribute("isSpecimenBarcodeGeneratorAvl",
						Variables.isSpecimenBarcodeGeneratorAvl);
			}
			List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();
			specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,
					Constants.SELECT_OPTION_VALUE));
			request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenClassList);

			request.setAttribute(Constants.TISSUE_TYPE_LIST_JSON,
					gson.toJson(AppUtility.getSpecimenTypes(Constants.TISSUE)));

			request.setAttribute(Constants.FLUID_TYPE_LIST_JSON,
					gson.toJson(AppUtility.getSpecimenTypes(Constants.FLUID)));

			request.setAttribute(Constants.CELL_TYPE_LIST_JSON,
					gson.toJson(AppUtility.getSpecimenTypes(Constants.CELL)));

			request.setAttribute(Constants.MOLECULAR_TYPE_LIST_JSON,
					gson.toJson(AppUtility.getSpecimenTypes(Constants.MOLECULAR)));

			request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST,
					AppUtility.getListFromCDE(Constants.CDE_NAME_PATHOLOGICAL_STATUS));
			request.setAttribute(Constants.TISSUE_SITE_LIST, AppUtility.tissueSiteList());
			request.setAttribute(Constants.TISSUE_SIDE_LIST,
					AppUtility.getListFromCDE(Constants.CDE_NAME_TISSUE_SIDE));
			request.setAttribute("isImageEnabled", Variables.isImagingConfigurred);

			request.setAttribute(Constants.SPECIMEN_CLASS_LIST, AppUtility.getSpecimenClassList());

			List<NameValueBean> collectionStatusList = new ArrayList<NameValueBean>();
			for (String status : Constants.SPECIMEN_COLLECTION_STATUS_VALUES)
			{
				collectionStatusList.add(new NameValueBean(status, status));
			}
			request.setAttribute(Constants.COLLECTIONSTATUSLIST, collectionStatusList);

			List<NameValueBean> activityStatusList = new ArrayList<NameValueBean>();
			for (String status : Constants.SPECIMEN_ACTIVITY_STATUS_VALUES)
			{
				activityStatusList.add(new NameValueBean(status, status));
			}

			request.setAttribute(Constants.ACTIVITYSTATUSLIST, activityStatusList);

			List<Biohazard> biohazardList = hibernateDao.retrieve(Biohazard.class.getName());

			ArrayList<BiohazardDTO> biohazardTypeNameList = new ArrayList<BiohazardDTO>();
			for (Biohazard biohazard : biohazardList)
			{
				BiohazardDTO biohazardDTO = new BiohazardDTO();
				biohazardDTO.setId(biohazard.getId());
				biohazardDTO.setName(biohazard.getName());
				biohazardDTO.setType(biohazard.getType());

				biohazardTypeNameList.add(biohazardDTO);
			}

			String biohazardTypeNameListJSON = gson.toJson(biohazardTypeNameList);
			request.setAttribute(Constants.BIOHAZARD_TYPE_NAME_LIST_JSON, biohazardTypeNameListJSON);
			request.setAttribute(Constants.OPERATION, Constants.ADD);
			UserBizLogic userBizLogic=new UserBizLogic();
			final List<NameValueBean> users=userBizLogic.getUsersNameValueList(null);
			users.add(new NameValueBean(Constants.SELECT_OPTION, String.valueOf(Constants.SELECT_OPTION_VALUE)));
			request.setAttribute(Constants.USERLIST, users);
			
		// Sets the hourList attribute to be used in the Add/Edit
			// FrozenEventParameters Page.
			request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);
			// Sets the minutesList attribute to be used in the Add/Edit
			// FrozenEventParameters Page.
			request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}

		return mapping.findForward(pageOf);
	}

}
