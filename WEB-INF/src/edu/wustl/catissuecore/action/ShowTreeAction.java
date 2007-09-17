
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.tree.QueryTreeNodeData;

/**
 * This action is for getting the collection protocol and 
 * participants registered for that collection protocol from cache
 * @author vaishali_khandelwal
 *
 */
public class ShowTreeAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		//CPSearchForm cpsearchForm = (CPSearchForm) form;
		HttpSession session = request.getSession();
		//Map collectionProtocolEventMap = (Map)session.getAttribute("collectionProtocolEventMap");
		Map collectionProtocolEventMap = (Map)session.getAttribute("collectionProtocolEventMap");
		Map newSpecimenMap = (Map)session.getAttribute("newSpecimenMap");
		String pageOf = request.getParameter("pageOf");
		String cpId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		String participantId = request.getParameter(Constants.CP_SEARCH_PARTICIPANT_ID);
		String operation = request.getParameter("operation");
		request.setAttribute("operation", operation);
		
		/**
		 * Name : Deepti Shelar
		 * Reviewer's Name : Sachin Lale
		 * Bug id : 4213
		 * Patch id  : 4213_1
		 * Description : getting parameters from request and keeping them in seesion to keep the node in tree selected. 
		 */
		String isParticipantChanged = request.getParameter("particiantChnaged");
		if(isParticipantChanged != null && isParticipantChanged.equalsIgnoreCase("true"))
		{
			request.getSession().setAttribute("nodeId",null);
		}
		if(request.getParameter("nodeId") != null)
		{
			String nodeId = request.getParameter("nodeId");
			if(!nodeId.equalsIgnoreCase("SpecimenCollectionGroup_0"))
				request.getSession().setAttribute("nodeId",nodeId);
		}
		Vector treeData = null;
		if (cpId != null && participantId != null && !cpId.equals("") && !participantId.equals(""))
		{
			SpecimenCollectionGroupBizLogic bizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance().getBizLogic(
					Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			/**
			 * Patch Id : FutureSCG_2
			 * Description : Calling method to create tree for future scgs
			 */
			//Commented out by Baljeet as not required after Flex realted changes
			//treeData = bizLogic.getSCGTreeForCPBasedView(new Long(cpId), new Long(participantId)); 
		}
		
		if(pageOf!=null && pageOf.equals("specimenEventsPage")&& collectionProtocolEventMap!=null)
		{
			treeData = new Vector();
			Integer iEventCount = new Integer(1);
			
			Collection collectionProtocolEventBeanCollection = (Collection)collectionProtocolEventMap.values();
			Iterator collectionProtocolEventBeanCollectionItr = collectionProtocolEventBeanCollection.iterator();
			while(collectionProtocolEventBeanCollectionItr.hasNext())
			{
				CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean)collectionProtocolEventBeanCollectionItr.next();
				String objectName = collectionProtocolEventBean.getCollectionPointLabel();
				if(operation!=null && operation.equals("ViewSummary"))
				{
					objectName="ViewSummary";
				}
				String displayName = collectionProtocolEventBean.getStudyCalenderEventPoint().toString()+" "+ collectionProtocolEventBean.getCollectionPointLabel();
				String parentIdentifier= Constants.ZERO_ID;
				String identifier = collectionProtocolEventBean.getUniqueIdentifier();
				String parentIdentifierForSpecimens = identifier; 
				addNode(objectName, displayName, parentIdentifier,identifier,"",treeData);
				
				Map SpecimenRequirementMap = collectionProtocolEventBean.getSpecimenRequirementbeanMap();
				
				if(SpecimenRequirementMap!=null)
				{
					Collection specimenRequirementBeanCollection = (Collection)SpecimenRequirementMap.values();
					Iterator specimenRequirementBeanCollectionItr = specimenRequirementBeanCollection.iterator();
					while(specimenRequirementBeanCollectionItr.hasNext())
					{ 
						SpecimenRequirementBean specimenRequirementBean = (SpecimenRequirementBean)specimenRequirementBeanCollectionItr.next();
						createSpecimenNode(objectName,identifier,specimenRequirementBean,treeData,operation);
					}	
				}
				iEventCount++;
			}
		}
		request.setAttribute("treeData", treeData);
		request.setAttribute(Constants.CP_SEARCH_CP_ID, cpId);
		request.setAttribute(Constants.CP_SEARCH_PARTICIPANT_ID, participantId);
		/**
		 * Name : Falguni Sachde
		 * Reviewer's Name : Sachin Lale
		 * 
		 * 
		 * Description : getting parameters Name of Collection Protocol Name from request and setting it as attribute
		 *  
		 */
		
		String cpTitle = request.getParameter("cpTitle");
		
		if(cpTitle!=null)
		{
			session.setAttribute("cpTitle",cpTitle);
		}
		return mapping.findForward("success");
	}
	
	private void createSpecimenNode(String parentObjectname,String parentIdentifier,SpecimenRequirementBean specimenRequirementBean,Vector treeData, String operation)
	{
		String objectName = Constants.NEW_SPECIMEN;
		if(operation!=null && operation.equals("ViewSummary"))
		{
			objectName="ViewSummary";
		}
		String identifier = specimenRequirementBean.getUniqueIdentifier();
			
		String displayName= Constants.SPECIMEN+specimenRequirementBean.getUniqueIdentifier();
		
		addNode(objectName, displayName, parentIdentifier, identifier, parentObjectname, treeData);
		
		if(specimenRequirementBean.getAliquotSpecimenCollection()!=null&&!specimenRequirementBean.getAliquotSpecimenCollection().isEmpty())
		{
			Map aliquotsCollection = specimenRequirementBean.getAliquotSpecimenCollection();
			Iterator aliquotsCollectionItr = aliquotsCollection.values().iterator();
			parentIdentifier = identifier;
			parentObjectname=objectName;
			while(aliquotsCollectionItr.hasNext())
			{
					SpecimenRequirementBean specimenRequirementBean1 = (SpecimenRequirementBean)aliquotsCollectionItr.next();
					
					displayName= Constants.ALIQUOT+specimenRequirementBean1.getUniqueIdentifier();
					createSpecimenNode(parentObjectname,parentIdentifier,specimenRequirementBean1,treeData,operation);
			}
		}
		if(specimenRequirementBean.getDeriveSpecimenCollection()!=null&&!specimenRequirementBean.getDeriveSpecimenCollection().isEmpty())
		{
			Map deriveSpecimenMap = specimenRequirementBean.getDeriveSpecimenCollection();
			Iterator deriveSpecimenCollectionItr = deriveSpecimenMap.values().iterator();
			parentIdentifier = identifier;
			parentObjectname=objectName;
			while(deriveSpecimenCollectionItr.hasNext())
			{
				SpecimenRequirementBean specimenRequirementBean1 = (SpecimenRequirementBean)deriveSpecimenCollectionItr.next();
				
				displayName= Constants.DERIVED_SPECIMEN+specimenRequirementBean1.getUniqueIdentifier();
				createSpecimenNode(parentObjectname,parentIdentifier,specimenRequirementBean1,treeData,operation);		
			
			}
		}
	}
	
	private void addNode(String objectName ,String displayName, String parentIdentifier, String identifier, String parentObjectname, Vector treeData)
	{
		QueryTreeNodeData treeNode = new QueryTreeNodeData();
		treeNode.setParentIdentifier(parentIdentifier);
		treeNode.setIdentifier(identifier);
		treeNode.setObjectName(objectName);
		treeNode.setDisplayName(displayName);
		treeNode.setParentObjectName(parentObjectname);
		treeNode.setToolTipText(displayName);
		treeData.add(treeNode);
	}
	
	
	
}
