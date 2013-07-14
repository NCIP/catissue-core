/**
 * <p>
 * Title: CreateSpecimenAction Class>
 * <p>
 * Description: CreateSpecimenAction initializes the fields in the Create
 * Specimen page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.dto.DerivedDTO;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bean.ExternalIdentifierBean;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenBizlogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.dao.SpecimenDAO;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.tag.ScriptGenerator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 *
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends CatissueBaseAction
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(CreateSpecimenAction.class);

	/** The Constant TRUE_STRING. */
	private static final String TRUE_STRING = "true";

	/** The Constant PAGE_OF_STRING. */
	private static final String PAGE_OF_STRING = "pageOf";

	/** The Constant INITVALUES_STRING. */
	private final static String INITVALUES_STRING = "initValues";

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
	public ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		HibernateDAO hibernateDao = null;
		try
		{
			hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionDataBean);
			Long parentId = 0l; 
			DerivedDTO derivedDTO = new DerivedDTO();
			if (!Validator.isEmpty(request.getParameter("parentSpecimenId")))
			{
				derivedDTO.setParentSpecimenId(parentId);
				derivedDTO.setParentSpecimenLabel(request.getParameter("parentLabel"));
				derivedDTO.setCreatedOn(new Date());
				parentId = Long.valueOf(request.getParameter("parentSpecimenId"));
				request.setAttribute("cpId", SpecimenDAO.getcpId(parentId,  hibernateDao));
				request.setAttribute("isSpecimenLabelGeneratorAvl",
						new SpecimenBizlogic().isSpecimenLabelGeneratorAvl(parentId, hibernateDao));
			}
			request.setAttribute("deriveDTO", derivedDTO);
			setSpecimenCharsInRequest(request);
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDao);
		}
		return mapping.findForward("success");
	}

	private void setSpecimenCharsInRequest(HttpServletRequest request) throws BizLogicException,
			DAOException, DynamicExtensionsSystemException
	{
		Gson gson = new Gson();
		request.setAttribute(Constants.TISSUE_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.TISSUE)));

		request.setAttribute(Constants.FLUID_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.FLUID)));

		request.setAttribute(Constants.CELL_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.CELL)));

		request.setAttribute(Constants.MOLECULAR_TYPE_LIST_JSON,
				gson.toJson(AppUtility.getSpecimenTypes(Constants.MOLECULAR)));

		request.setAttribute("isSpecimenBarcodeGeneratorAvl",
				Variables.isSpecimenBarcodeGeneratorAvl);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, AppUtility.getSpecimenClassList());
	}
}