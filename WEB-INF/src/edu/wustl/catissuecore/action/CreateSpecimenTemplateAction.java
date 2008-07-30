package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.emory.mathcs.backport.java.util.LinkedList;
import edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;


public class CreateSpecimenTemplateAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		CreateSpecimenTemplateForm createSpecimenTemplateForm = (CreateSpecimenTemplateForm)form;
		HttpSession session = request.getSession();
		//Gets the value of the operation parameter.
		String operation = (String) request.getParameter(Constants.OPERATION);
		String pageOf = (String)request.getParameter("pageOf");
		String selectedNode=(String)session.getAttribute(Constants.TREE_NODE_ID);
		//This will give node id when flow is from Specimen Tree View.
		String mapkey = (String)request.getParameter("key");
		String nodeId = (String)request.getParameter(Constants.TREE_NODE_ID);
		if(mapkey!=null && selectedNode!=null && !selectedNode.contains(mapkey))
		{
			session.setAttribute(Constants.TREE_NODE_ID, nodeId);
		}
		if(pageOf!=null&&pageOf.equalsIgnoreCase("error"))
		{
			mapkey=null;
		}
		request.setAttribute("pageOf", pageOf);
		//Sets the operation attribute to be used in the Edit/View Specimen Page in Advance Search Object View. 
		request.setAttribute(Constants.OPERATION, operation);
		
		//This will give Event key, Under that event Specimens are collected when flow is from Define event Page.
		
		String key = (String)session.getAttribute("listKey");
		
		List specimenTypeList = Utility.getListFromCDE(Constants.CDE_NAME_SPECIMEN_TYPE);
		List tissueSiteList = new ArrayList();
		List tissueSideList = new ArrayList();
		List pathologicalStatusList = new ArrayList();
		List specimenClassList = null;
		
		tissueSiteList.addAll(Utility.tissueSiteList());
    	
		//Getting tissue side list
		tissueSideList.addAll(Utility.getListFromCDE(Constants.CDE_NAME_TISSUE_SIDE));
		
		//Getting pathological status list
		pathologicalStatusList.addAll(Utility.getListFromCDE(Constants.CDE_NAME_PATHOLOGICAL_STATUS));
		Map subTypeMap = Utility.getSpecimenTypeMap();
		specimenClassList = Utility.getSpecimenClassList();
		if(operation.equals("add")&&pageOf!=null&&!pageOf.equals("error")&&!pageOf.equals("delete"))
		{
			// Setting the default values
			clearFormOnAddSpecimenRequirement(createSpecimenTemplateForm);
			if (createSpecimenTemplateForm.getClassName() == null)
			{
				createSpecimenTemplateForm.setClassName((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_SPECIMEN));
			}
			if (createSpecimenTemplateForm.getType() == null)
			{
				createSpecimenTemplateForm.setType((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_SPECIMEN_TYPE));
			}
			if (createSpecimenTemplateForm.getTissueSite() == null)
			{
				createSpecimenTemplateForm.setTissueSite((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_TISSUE_SITE));
			}
			if (createSpecimenTemplateForm.getTissueSide() == null)
			{
				createSpecimenTemplateForm.setTissueSide((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_TISSUE_SIDE));
			}
			if (createSpecimenTemplateForm.getPathologicalStatus() == null)
			{
				createSpecimenTemplateForm.setPathologicalStatus((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_PATHOLOGICAL_STATUS));
			}

			if (createSpecimenTemplateForm.getCollectionEventCollectionProcedure() == null)
			{
				createSpecimenTemplateForm.setCollectionEventCollectionProcedure((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE));
			}			
			if (createSpecimenTemplateForm.getCollectionEventContainer() == null)
			{
				createSpecimenTemplateForm.setCollectionEventContainer((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CONTAINER));
			}
			if (createSpecimenTemplateForm.getReceivedEventReceivedQuality() == null)
			{
				createSpecimenTemplateForm.setReceivedEventReceivedQuality((String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY));
			}
		}
		List storageContainerList = new LinkedList();
		storageContainerList.add(new NameValueBean("Virtual","Virtual"));
		storageContainerList.add(new NameValueBean("Auto","Auto"));
		storageContainerList.add(new NameValueBean("Manual","Manual"));
		 
    	//setting the procedure
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute(Constants.PROCEDURE_LIST, procedureList);
		//set the container lists
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute(Constants.CONTAINER_LIST, containerList);	

		//setting the quality for received events
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute(Constants.RECEIVED_QUALITY_LIST, qualityList);
   	
    	request.setAttribute("createSpecimenTemplateForm", createSpecimenTemplateForm);
    	
    	// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
    	
		// sets the Specimen Type list
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
		// sets the Tissue Site list
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);
		// sets the PathologicalStatus list
		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
		// sets the Side list
		request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
		
		//Storage Container List
		request.setAttribute("storageContainerList", storageContainerList);
		
		//Event Key
		request.setAttribute("key",key);
		//Node Key
		request.setAttribute("mapkey",mapkey);
		if(nodeId!=null)
		{
			session.setAttribute(Constants.TREE_NODE_ID,nodeId);
		}
		setUserInForm(request,operation,createSpecimenTemplateForm);
		if(operation.equals(Constants.EDIT)&&!pageOf.equals("error"))
		{
			if(!pageOf.equals("delete"))
			{
				initCreateSpecimenTemplateForm(createSpecimenTemplateForm, mapkey, request);
			}
		}
		

		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean)session.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		request.setAttribute("isParticipantReg", collectionProtocolBean.isParticiapantReg());
		request.setAttribute("opr", collectionProtocolBean.getOperation());

		if(pageOf!=null&&pageOf.equals("error"))
		{
			return (mapping.findForward(Constants.SUCCESS));
		}
		
		String redirectTo = request.getParameter("redirectTo");
		
		if(redirectTo!=null && redirectTo.equals("defineEvents"))
		{
			return (mapping.findForward("defineEvents"));
		}
		else
		{
			return (mapping.findForward(Constants.SUCCESS));
		}
			
	}
	
	/**
	 * @param request
	 * @param operation
	 * @param specimenCollectionGroupForm
	 * @throws DAOException
	 */
	private void setUserInForm(HttpServletRequest request,String operation,CreateSpecimenTemplateForm createSpecimenTemplateForm) 
	{
		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = null;
		try
		{
			userCollection = userBizLogic.getUsers(operation);
		}
		catch (DAOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute(Constants.USERLIST, userCollection);

		SessionDataBean sessionData = getSessionData(request);
		if (sessionData != null)
		{
			String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
			long collectionEventUserId = EventsUtil.getIdFromCollection(userCollection, user);

			if(createSpecimenTemplateForm.getCollectionEventUserId() == 0)
			{
				createSpecimenTemplateForm.setCollectionEventUserId(collectionEventUserId);
			}
			if(createSpecimenTemplateForm.getReceivedEventUserId() == 0)
			{
				createSpecimenTemplateForm.setReceivedEventUserId(collectionEventUserId);
			}
		}
	}
	
	private SpecimenRequirementBean getSpecimenBeanFromMap(StringTokenizer keyToken, SpecimenRequirementBean specimenRequirementBean, String parentKey)
	{
		while (keyToken.hasMoreTokens()) 
		{
			String specimenKey = keyToken.nextToken();
			String currentKey = parentKey +"_"+specimenKey;
			if(specimenKey.startsWith("A"))
			{
				Map aliqutCollectionMap = specimenRequirementBean.getAliquotSpecimenCollection();
				SpecimenRequirementBean childSpecimenRequirementBean = (SpecimenRequirementBean)aliqutCollectionMap.get(currentKey);
				SpecimenRequirementBean specimenRequirementBean1 = getSpecimenBeanFromMap(keyToken, childSpecimenRequirementBean, currentKey);
				return specimenRequirementBean1;
			}
			else
			{
				Map deriveCollectionMap = specimenRequirementBean.getDeriveSpecimenCollection();
				SpecimenRequirementBean childSpecimenRequirementBean = (SpecimenRequirementBean)deriveCollectionMap.get(currentKey);
				SpecimenRequirementBean specimenRequirementBean1 = getSpecimenBeanFromMap(keyToken, childSpecimenRequirementBean, currentKey);
				return specimenRequirementBean1;
			}
			
		}
		return specimenRequirementBean;
	}
	
	private void clearFormOnAddSpecimenRequirement(CreateSpecimenTemplateForm createSpecimenTemplateForm)
	{
		createSpecimenTemplateForm.setClassName(null);
		createSpecimenTemplateForm.setType(null);
		createSpecimenTemplateForm.setTissueSide(null);
		createSpecimenTemplateForm.setTissueSite(null);
		createSpecimenTemplateForm.setPathologicalStatus(null);
		createSpecimenTemplateForm.setConcentration(null);
		createSpecimenTemplateForm.setQuantity(null);
		createSpecimenTemplateForm.setStorageLocationForSpecimen(null);
		createSpecimenTemplateForm.setCollectionEventCollectionProcedure(null);
		createSpecimenTemplateForm.setReceivedEventReceivedQuality(null);
		createSpecimenTemplateForm.setCollectionEventContainer(null);
		
		createSpecimenTemplateForm.setQuantity("0");
		createSpecimenTemplateForm.setConcentration("0");
		createSpecimenTemplateForm.setNoOfAliquots(null);
		createSpecimenTemplateForm.setQuantityPerAliquot(null);
		createSpecimenTemplateForm.setStorageLocationForAliquotSpecimen(null);
		createSpecimenTemplateForm.setNoOfDeriveSpecimen(0);
		createSpecimenTemplateForm.setAliquotSpecimenCollection(null);
		createSpecimenTemplateForm.setDeriveSpecimenCollection(null);
		createSpecimenTemplateForm.setDeriveSpecimenValues(null);
	}
	private void initCreateSpecimenTemplateForm(CreateSpecimenTemplateForm createSpecimenTemplateForm, String mapkey, HttpServletRequest request)
	{
		
		String eventKey=null;
		String specimenKey=null;
		HttpSession session = request.getSession();
		StringTokenizer stringToken = new StringTokenizer(mapkey,"_");
		
		if(stringToken!=null&&stringToken.hasMoreTokens())
		{
			eventKey = stringToken.nextToken();
			specimenKey = eventKey+"_"+stringToken.nextToken();
		}
		Map collectionProtocolEventMap = (Map)session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean)collectionProtocolEventMap.get(eventKey);
		Map specimenRequirementmaps = collectionProtocolEventBean.getSpecimenRequirementbeanMap();
		SpecimenRequirementBean parentSpecimenRequirementBean = (SpecimenRequirementBean)specimenRequirementmaps.get(specimenKey);
		SpecimenRequirementBean specimenRequirementBean = getSpecimenBeanFromMap(stringToken,parentSpecimenRequirementBean,specimenKey);
		session.setAttribute(Constants.EDIT_SPECIMEN_REQUIREMENT_BEAN, specimenRequirementBean);
		createSpecimenTemplateForm.setDisplayName(specimenRequirementBean.getDisplayName());
		createSpecimenTemplateForm.setLineage(specimenRequirementBean.getLineage());
		createSpecimenTemplateForm.setClassName(specimenRequirementBean.getClassName());
		createSpecimenTemplateForm.setType(specimenRequirementBean.getType());
		createSpecimenTemplateForm.setTissueSide(specimenRequirementBean.getTissueSide());
		createSpecimenTemplateForm.setTissueSite(specimenRequirementBean.getTissueSite());
		createSpecimenTemplateForm.setPathologicalStatus(specimenRequirementBean.getPathologicalStatus());
		createSpecimenTemplateForm.setConcentration(specimenRequirementBean.getConcentration());
		createSpecimenTemplateForm.setQuantity(specimenRequirementBean.getQuantity());
		createSpecimenTemplateForm.setStorageLocationForSpecimen(specimenRequirementBean.getStorageContainerForSpecimen());
		createSpecimenTemplateForm.setCollectionEventUserId(specimenRequirementBean.getCollectionEventUserId());
		createSpecimenTemplateForm.setReceivedEventUserId(specimenRequirementBean.getReceivedEventUserId());
		
		//Collected and received events
		createSpecimenTemplateForm.setCollectionEventContainer(specimenRequirementBean.getCollectionEventContainer());
		createSpecimenTemplateForm.setReceivedEventReceivedQuality(specimenRequirementBean.getReceivedEventReceivedQuality());
		createSpecimenTemplateForm.setCollectionEventCollectionProcedure(specimenRequirementBean.getCollectionEventCollectionProcedure());
		
		//Aliquot
		createSpecimenTemplateForm.setNoOfAliquots(specimenRequirementBean.getNoOfAliquots());
		createSpecimenTemplateForm.setQuantityPerAliquot(specimenRequirementBean.getQuantityPerAliquot());
		createSpecimenTemplateForm.setStorageLocationForAliquotSpecimen(specimenRequirementBean.getStorageContainerForAliquotSpecimem());
		
		
		//Derive
		LinkedHashMap deriveSpecimenLinkedHashMap = null;
		
		if(specimenRequirementBean.getDeriveSpecimenCollection()!=null&&!specimenRequirementBean.getDeriveSpecimenCollection().isEmpty())
		{
			deriveSpecimenLinkedHashMap = CollectionProtocolUtil.getDerviredObjectMap(specimenRequirementBean.getDeriveSpecimenCollection().values());
			createSpecimenTemplateForm.setNoOfDeriveSpecimen(specimenRequirementBean.getDeriveSpecimenCollection().size());
		}
		else
		{
			createSpecimenTemplateForm.setNoOfDeriveSpecimen(0);
		}
		createSpecimenTemplateForm.setDeriveSpecimenValues(deriveSpecimenLinkedHashMap);
		
	}
}
