
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.tree.QueryTreeNodeData;

/**
 * This action is for getting the Tree For Events and Specimens collected Under that Event for a Collection Protocol
 * @author Virender Mehta
 *
 */
public class ShowCollectionProtocolTreeAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		HttpSession session = request.getSession();
		Map collectionProtocolEventMap = (Map)session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		String pageOf = request.getParameter(Constants.PAGE_OF);
		String operationType = request.getParameter("operationType");
		request.setAttribute("operationType", operationType);
		String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION, operation);
		Vector treeData=null;
		if(collectionProtocolEventMap!=null)
		{
			treeData = new Vector(); 
			Integer iEventCount = new Integer(1);
			Collection collectionProtocolEventBeanCollection = (Collection)collectionProtocolEventMap.values();
			Iterator collectionProtocolEventBeanCollectionItr = collectionProtocolEventBeanCollection.iterator();
			while(collectionProtocolEventBeanCollectionItr.hasNext())
			{
				CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean)collectionProtocolEventBeanCollectionItr.next();
				String objectName = collectionProtocolEventBean.getCollectionPointLabel();
				if(operation!=null && operation.equals(Constants.VIEW_SUMMARY))
				{
					objectName=Constants.VIEW_SUMMARY;
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
		request.setAttribute(Constants.TREE_DATA, treeData);
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**
	 * This is a recursive method for getting node data.
	 * @param parentObjectname
	 * @param parentIdentifier
	 * @param specimenRequirementBean
	 * @param treeData
	 * @param operation
	 */
	private void createSpecimenNode(String parentObjectname,String parentIdentifier,SpecimenRequirementBean specimenRequirementBean,Vector treeData, String operation)
	{
		String objectName = Constants.NEW_SPECIMEN;
		if(operation!=null && operation.equals(Constants.VIEW_SUMMARY))
		{
			objectName=Constants.VIEW_SUMMARY;
		}
		String identifier = specimenRequirementBean.getUniqueIdentifier();
			
		String displayName= Constants.SPECIMEN+"_"+specimenRequirementBean.getUniqueIdentifier();
		
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
	
	/**
	 * Description : This is a common method is for adding node.
	 * 																							
	 * @param objectName
	 * @param displayName
	 * @param parentIdentifier
	 * @param identifier
	 * @param parentObjectname
	 * @param treeData
	 */
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
