package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Validator;

public class SpecimenEditAction extends CatissueBaseAction{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		
		SpecimenDTO specimenDTO = new SpecimenDTO();
		String obj = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		if(!Validator.isEmpty(obj))
		{
			Long identifier = Long.valueOf(Utility.toLong(obj));
			NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
			specimenDTO = bizLogic.getDTO(identifier);
			request.setAttribute("specimenDTO", specimenDTO);
		}
		List<NameValueBean> specimenClassList = new ArrayList<NameValueBean>();
		List<NameValueBean> collectionStatusList = new ArrayList<NameValueBean>();
		// sri: create 4 lists, and in jsp allow edit
			request.setAttribute(Constants.SPECIMEN_TYPE_LIST, AppUtility.getSpecimenTypes(specimenDTO.getClassName()));
			request.setAttribute(Constants.TISSUE_TYPE_LIST, AppUtility.getSpecimenTypes(Constants.TISSUE));
			request.setAttribute(Constants.FLUID_TYPE_LIST, AppUtility.getSpecimenTypes(Constants.FLUID));
			request.setAttribute(Constants.CELL_TYPE_LIST, AppUtility.getSpecimenTypes(Constants.CELL));
			request.setAttribute(Constants.MOLECULAR_TYPE_LIST, AppUtility.getSpecimenTypes(Constants.MOLECULAR));
			
			request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, AppUtility.getListFromCDE(Constants.CDE_NAME_PATHOLOGICAL_STATUS));
			request.setAttribute(Constants.TISSUE_SITE_LIST, AppUtility.tissueSiteList());
			request.setAttribute(Constants.TISSUE_SIDE_LIST, AppUtility.getListFromCDE(Constants.CDE_NAME_TISSUE_SIDE));
//		}
		
		specimenClassList.add(new NameValueBean(specimenDTO.getClassName(),specimenDTO.getClassName()));
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
		for (String status : Constants.SPECIMEN_COLLECTION_STATUS_VALUES) 
		{
			collectionStatusList.add(new NameValueBean(status,status));
		}
		request.setAttribute(Constants.COLLECTIONSTATUSLIST,collectionStatusList);

		List<NameValueBean> biohazardList = new ArrayList<NameValueBean>();
		biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		biohazardList.remove(0);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

		/* Dummy BioHazard list. To be replaced by Nitesh with actual List*/
		ArrayList<NameValueBean> biohazardNameList = new ArrayList<NameValueBean>();

		ArrayList<String> dummyTypeList = new ArrayList<String>();
		dummyTypeList.add("Carcinogen");
		dummyTypeList.add("Infectious");
		dummyTypeList.add("Mutagen");
		dummyTypeList.add("Radioactive");
		dummyTypeList.add("Toxic");
		dummyTypeList.add("Not Specified");

		for (String type : dummyTypeList)
		{
			/*name value pair where name is biohazard name and value is type*/
			biohazardNameList.add(new NameValueBean(type + 1, type));
			biohazardNameList.add(new NameValueBean(type + 2, type));
			biohazardNameList.add(new NameValueBean(type + 3, type));
		}

		request.setAttribute(Constants.BIOHAZARD_NAME_LIST, biohazardNameList);
		Gson gson = new Gson();
		String biohazardNameListJSON = gson.toJson(biohazardNameList);

		request.setAttribute("biohazardNameListJSON", biohazardNameListJSON);


		return mapping.findForward(Constants.PAGE_OF_NEW_SPECIMEN);
	}

}
