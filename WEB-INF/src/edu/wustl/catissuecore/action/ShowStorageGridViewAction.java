/**
 * <p>Title: ShowStorageGridViewAction Class>
 * <p>Description:	ShowStorageGridViewAction shows the grid view of the map 
 * according to the storage container selected from the tree view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
/**
 * ShowStorageGridViewAction shows the grid view of the map according to the
 * storage container selected from the tree view.
 * 
 * @author gautam_shetty
 */
public class ShowStorageGridViewAction  extends BaseAction
{

    /**
	 * Overrides the execute method of Action class.
	 */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	String target=Constants.SUCCESS;
        String id = request.getParameter(Constants.SYSTEM_IDENTIFIER);
       	 if(id == null)
        {
        	id=(String)request.getAttribute(Constants.SYSTEM_IDENTIFIER);
        	if (id==null)
        	{
        		id="0";
        	}
        }
        request.setAttribute("storageContainerIdentifier",id);
        
        String contentOfContainer = null;
        // To get privilegeCache through 
		// Singleton instance of PrivilegeManager, requires User LoginName
        PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(getUserLoginName(request));
		
        // Aarti: Check whether user has use permission on the storage container
		// or not
//        if(!SecurityManager.getInstance(this.getClass()).isAuthorized(getUserLoginName(request)
//        		,StorageContainer.class.getName()+"_"+id,Permissions.USE))
		
		// Call to SecurityManager.isAuthorized bypassed &
		// instead, call redirected to privilegeCache.hasPrivilege		
	//Commented by Ravindra and vishvesh because this is not how it will work in MSR
	
//		if(!privilegeCache.hasPrivilege(StorageContainer.class.getName()+"_"+id,Permissions.USE))
//		{
//        	ActionErrors errors = new ActionErrors();
//         	ActionError error = new ActionError("access.use.object.denied");
//         	errors.add(ActionErrors.GLOBAL_ERROR,error);
//         	saveErrors(request,errors);
//        	return mapping.findForward(Constants.FAILURE);
//		}
        
        String pageOf = request.getParameter(Constants.PAGE_OF);
        if(pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
        {
        	target=Constants.PAGE_OF_STORAGE_CONTAINER;
        }

        //Sri: Added to get the position of the storage container map
        String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
        if((null != position) && ("" != position))
        {
        	Long positionOne;
        	Long positionTwo;
        	try
			{
	        	//The two positions are separated by :
	        	StringTokenizer strToken = new StringTokenizer(position,":");
	        	positionOne = Long.valueOf(strToken.nextToken());
	        	positionTwo = Long.valueOf(strToken.nextToken());
			}
        	catch (Exception ex)
			{
			    //Will not select anything
        		positionOne = null;
        		positionTwo = null;
			}
        	request.setAttribute(Constants.POS_ONE,positionOne);
        	request.setAttribute(Constants.POS_TWO,positionTwo);

        }
        
        StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory
        .getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        
        Object containerObject = bizLogic.retrieve(StorageContainer.class.getName(), new Long(id));
        StorageContainerGridObject storageContainerGridObject = null;
        int [][]fullStatus = null;
        int [][] childContainerIds = null;
        String [][] childContainerType = null;
        String [][] childContainerName=null;
        Object a=request.getAttribute("tree");
        
        if (containerObject != null)
        {
        	storageContainerGridObject = new StorageContainerGridObject();
            StorageContainer storageContainer = (StorageContainer) containerObject;

            setEnablePageAttributeIfRequired(request, storageContainer, bizLogic);
            
            Site site = (Site)bizLogic.retrieveAttribute(StorageContainer.class.getName(), storageContainer.getId(), "site");//container.getSite();
            request.setAttribute("siteName",site.getName());
            
            StorageType storageType = (StorageType) bizLogic.retrieveAttribute(StorageContainer.class.getName(), storageContainer.getId(), "storageType");//storageContainer.getStorageType();
			request.setAttribute("storageTypeName",storageType.getName());
            Object tree=request.getAttribute("tree");
            
            //Mandar : Labels for Dimensions  
            String oneDimLabel = storageType.getOneDimensionLabel();
            String twoDimLabel = storageType.getTwoDimensionLabel();
            
            if(oneDimLabel == null )oneDimLabel = " ";
            if(twoDimLabel == null )twoDimLabel = " ";
            
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL ,oneDimLabel );
            request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL ,twoDimLabel );
            
            storageContainerGridObject.setId(storageContainer.getId().longValue());
            storageContainerGridObject.setType(storageType.getName());
            storageContainerGridObject.setName(storageContainer.getName());
            
            Integer oneDimensionCapacity = storageContainer
            				.getCapacity().getOneDimensionCapacity();
            Integer twoDimensionCapacity = storageContainer
 							.getCapacity().getTwoDimensionCapacity();
            
            childContainerIds = new int[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
            storageContainerGridObject.setTwoDimensionCapacity(storageContainer
                    		.getCapacity().getTwoDimensionCapacity());
            
            fullStatus = new int[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            childContainerType = new String[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            childContainerName= new String[oneDimensionCapacity.intValue()+1][twoDimensionCapacity.intValue()+1];
            //Showing Containers in the Container map.
            //Collection children = (Collection)bizLogic.retrieveAttribute(StorageContainer.class.getName(), storageContainer.getId(), "elements(children)");//storageContainer.getChildren();
            Collection children = bizLogic.getContainerChildren(storageContainer.getId());
//			Collection<ContainerPosition> occPosColl = (Collection) bizLogic.retrieveAttribute(StorageContainer.class.getName(), storageContainer.getId(), "elements(occupiedPositions)");
//			if(occPosColl != null)
//			{
//				for(ContainerPosition occPos : occPosColl)
//				{
//					Collection occContainers = (Collection) bizLogic.retrieveAttribute(ContainerPosition.class.getName(), occPos.getId(), "occupiedContainer");
//					children.add(occContainers);
//				}
//			}
            
            if (children != null)
            {
                Iterator iterator = children.iterator();
                while(iterator.hasNext())
                {
                    Object object = iterator.next();
                    StorageContainer childStorageContainer = (StorageContainer)HibernateMetaData.getProxyObjectImpl(object);
                    if(childStorageContainer != null && childStorageContainer.getLocatedAtPosition() != null)
                    {
                    	Integer positionDimensionOne = childStorageContainer.getLocatedAtPosition().getPositionDimensionOne();
	                    Integer positionDimensionTwo = childStorageContainer.getLocatedAtPosition().getPositionDimensionTwo();
	                
	                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 1;
	                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
	                                                   = childStorageContainer.getId().intValue();
	                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
	                                                   = Constants.CONTAINER_LABEL_CONTAINER_MAP + childStorageContainer.getName();
	                    childContainerName[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]=childStorageContainer.getName();
	                }
                }
            }          
            
            IBizLogic specimenBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
            
            //Showing Specimens in the Container map.
            String sourceObjectName = Specimen.class.getName();
			String[] selectColumnName = {"id","specimenPosition.positionDimensionOne", "specimenPosition.positionDimensionTwo","label"};
			String[] whereColumnName = {"specimenPosition.storageContainer.id"};
            String[] whereColumnCondition = {"="};
			Object[] whereColumnValue = {Long.valueOf(id)};
            String joinCondition = Constants.AND_JOIN_CONDITION;
			
            List list = null;
            list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
                    whereColumnCondition, whereColumnValue, joinCondition);
            
           
			
            if (list != null)
            {
                Iterator iterator = list.iterator();
                while(iterator.hasNext())
                {
                    //Specimen specimen = (Specimen)iterator.next();
                	Object obj[] =  (Object[])iterator.next();
                	
                	Long specimenID = (Long)obj[0]; 
                    Integer positionDimensionOne = (Integer)obj[1];
                    Integer positionDimensionTwo = (Integer)obj[2];
                    String specimenLable = (String)obj[3];
                    
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = specimenID.intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                                        = Constants.SPECIMEN_LABEL_CONTAINER_MAP + specimenLable;
                    contentOfContainer=Constants.ALIAS_SPECIMEN;
                    
                }
            }
            
            // Showing Specimen Arrays in the Container map.
            sourceObjectName = SpecimenArray.class.getName();
           
            selectColumnName[1] = "locatedAtPosition.positionDimensionOne";
            selectColumnName[2] = "locatedAtPosition.positionDimensionTwo";
            selectColumnName[3] = "name";
            whereColumnName[0] = "locatedAtPosition.parentContainer.id";
            
            list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
                    whereColumnCondition, whereColumnValue, joinCondition);
			
            if (list != null)
            {
                Iterator iterator = list.iterator();
                while(iterator.hasNext())
                {
                	Object obj[] =  (Object[])iterator.next();
                	
                	Long specimenID = (Long)obj[0]; 
                    Integer positionDimensionOne = (Integer)obj[1];
                    Integer positionDimensionTwo = (Integer)obj[2];
                    String specimenArrayLable = obj[3].toString();
                    
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
                    childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                                   = specimenID.intValue();
                    childContainerType[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] 
                                                                        = Constants.SPECIMEN_ARRAY_LABEL_CONTAINER_MAP + specimenArrayLable;
                    contentOfContainer=Constants.ALIAS_SPECIMEN_ARRAY;
                    
                }
            }
        }
        
        request.setAttribute(Constants.CONTENT_OF_CONTAINNER, contentOfContainer);
        if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
        {
        	String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
        	Logger.out.info("Id-----------------"+id);
        	Logger.out.info("storageContainerType:"+storageContainerType);
            int startNumber = bizLogic.getNextContainerNumber(Long.parseLong(id),
                    Long.parseLong(storageContainerType),false);
            request.setAttribute(Constants.STORAGE_CONTAINER_TYPE,storageContainerType);
            request.setAttribute(Constants.START_NUMBER,new Integer(startNumber));
        }
         
        request.setAttribute(Constants.PAGE_OF, pageOf);
        request.setAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS, childContainerIds);
        request.setAttribute(Constants.CHILD_CONTAINER_TYPE, childContainerType);
        request.setAttribute(Constants.CHILD_CONTAINER_NAME, childContainerName);
        request.setAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS,fullStatus);
        request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT,
                storageContainerGridObject);
        
        //Mandar : 29aug06 : to set collectionprotocol titles
        List collectionProtocolList = bizLogic.getCollectionProtocolList(id);
        request.setAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST, collectionProtocolList);

        //Mandar : 29aug06 : to set specimenclass 
        List specimenClassList = bizLogic.getSpecimenClassList(id);
        request.setAttribute(Constants.MAP_SPECIMEN_CLASS_LIST, specimenClassList);

        return mapping.findForward(target);
    }

    /**
     * To enable or disable the Storage container links on the page depending on restriction criteria on Container.
	 * @param request Th HttpServletRequest object reference.
     * @param storageContainer The Storage container object reference.
     * @param bizLogic reference to bizLogic class. 
     * @throws DAOException 
     * @throws BizLogicException 
	 */
	private void setEnablePageAttributeIfRequired(HttpServletRequest request, StorageContainer storageContainer, StorageContainerBizLogic bizLogic) throws BizLogicException
	{
		boolean enablePage=true;
		String activityStatus = request.getParameter(Status.ACTIVITY_STATUS.toString());
		if(activityStatus == null)
		{
			activityStatus=(String)request.getAttribute(Status.ACTIVITY_STATUS.toString());
		}
			
		if (activityStatus!=null && activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
		{
			enablePage=false;
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.container.closed"));
			saveErrors(request, errors);
		}
		
		HttpSession session = request.getSession();
		// checking for container type.
		String holdContainerType = (String)session.getAttribute(Constants.CAN_HOLD_CONTAINER_TYPE);
		if (enablePage && holdContainerType!=null && !holdContainerType.equals(""))
		{
			int typeId = Integer.parseInt(holdContainerType);
			enablePage = bizLogic.canHoldContainerType(typeId, storageContainer);
		}
		
		//checking for collection Protocol. 
		String holdCollectionProtocol = (String)session.getAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
		if (enablePage && holdCollectionProtocol!=null)
		{
			if (!holdCollectionProtocol.equals(""))
			{
				int collectionProtocolId = Integer.parseInt(holdCollectionProtocol);
				enablePage = bizLogic.canHoldCollectionProtocol(collectionProtocolId,storageContainer);
			}
			else
				enablePage=false;
		}
		
		//checking for sepecimen class.
		String holdspecimenClass = (String)session.getAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS);
		if (enablePage && holdspecimenClass!=null)
		{
			if (!holdspecimenClass.equals(""))
				enablePage = bizLogic.canHoldSpecimenClass(holdspecimenClass,storageContainer);
			else
				enablePage = false;
		}
		
		//checking for specimen array type.
		String holdspecimenArrayType = (String)session.getAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);
		if (enablePage && holdspecimenArrayType!=null)
		{
			if (!holdspecimenArrayType.equals(""))
			{
				int specimenArrayTypeId = Integer.parseInt(holdspecimenArrayType);
				enablePage = bizLogic.canHoldSpecimenArrayType(specimenArrayTypeId,storageContainer);
			}
			else
				enablePage = false;
		}
		
		if (enablePage)
			request.setAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE,Constants.TRUE);
	}


	/**
	 * @param fullStatus
	 * @param childContainerIds
	 * @param storageContainer
	 */
    private void setStorageContainerStatus(boolean[][] fullStatus, int[][] childContainerIds, Collection collection)
    {
        Iterator iterator = collection.iterator();
        while(iterator.hasNext())
        {
            StorageContainer childStorageContainer = (StorageContainer)iterator.next();
            if(childStorageContainer != null && childStorageContainer.getLocatedAtPosition() != null)
            {
            	Integer positionDimensionOne = childStorageContainer.getLocatedAtPosition().getPositionDimensionOne();
	            Integer positionDimensionTwo = childStorageContainer.getLocatedAtPosition().getPositionDimensionTwo();
	        
	            fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = true;
	            childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo.intValue()]
                                           = childStorageContainer.getId().intValue();
            }
        }
    }

}