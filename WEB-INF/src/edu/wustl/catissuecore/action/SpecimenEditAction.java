
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.dto.BiohazardDTO;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;

public class SpecimenEditAction extends CatissueBaseAction
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Gson gson = new Gson();
		SpecimenDTO specimenDTO = new SpecimenDTO();

		String obj = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		if (!Validator.isEmpty(obj))
		{
			Long identifier = Long.valueOf(Utility.toLong(obj));
			NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
			specimenDTO = bizLogic.getDTO(identifier);
			request.setAttribute("specimenDTO", specimenDTO);
			DAO dao = AppUtility.openDAOSession(null);
			try{
			Long reportId = bizLogic.getAssociatedIdentifiedReportId(specimenDTO.getId(), AppUtility.openDAOSession(null));
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
			}finally{
				AppUtility.closeDAOSession(dao);
			}
		}
		List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,
				Constants.SELECT_OPTION_VALUE));
		if (!specimenDTO.getClassName().equals(Constants.SELECT_OPTION))
		{
			specimenClassList.clear();
			specimenClassList.addAll(AppUtility.getSpecimenTypes(specimenDTO.getClassName()));
		}
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

		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, AppUtility.getSpecimenClassList());

		List<NameValueBean> collectionStatusList = new ArrayList<NameValueBean>();
		for (String status : Constants.SPECIMEN_COLLECTION_STATUS_VALUES)
		{
			collectionStatusList.add(new NameValueBean(status, status));
		}
		request.setAttribute(Constants.COLLECTIONSTATUSLIST, collectionStatusList);

		DAO dao = AppUtility.openDAOSession(null);
		List<Biohazard> biohazardList = dao.retrieve(Biohazard.class.getName());

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
		
		Long specimenEntityId = null;
		
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
		{
			specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
		}
		else
		{
			specimenEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
		}
		request.setAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
		request.setAttribute("entityName",AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);

		return mapping.findForward(Constants.PAGE_OF_NEW_SPECIMEN);
	}

}
