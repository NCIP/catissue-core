/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowStorageGridViewAction extends Action
{

    /**
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        String systemIdentifier = request.getParameter(Constants.IDENTIFIER);

        AbstractBizLogic bizLogic = BizLogicFactory
                .getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        List list = bizLogic.retrieve(StorageContainer.class.getName(),
                "systemIdentifier", systemIdentifier);
        StorageContainerGridObject storageContainerGridObject 	
        		= new StorageContainerGridObject();
        boolean [][]fullStatus = null;

        if (list != null)
        {
            StorageContainer storageContainer = (StorageContainer) list.get(0);
            storageContainerGridObject.setSystemIdentifier(storageContainer.getSystemIdentifier().longValue());
            
            storageContainerGridObject.setType(storageContainer.getStorageType().getType());
            
            Integer oneDimensionCapacity = storageContainer
            								.getStorageContainerCapacity().getOneDimensionCapacity();
            Integer twoDimensionCapacity = storageContainer
            								.getStorageContainerCapacity().getTwoDimensionCapacity();
            
            System.out.println(storageContainer.getStorageType().getType()+storageContainer.getName());
            storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
            storageContainerGridObject.setTwoDimensionCapacity(storageContainer
                    .getStorageContainerCapacity().getTwoDimensionCapacity());
            
            fullStatus = new boolean[oneDimensionCapacity.intValue()][twoDimensionCapacity.intValue()];
            
            storageContainer.getChildrenContainerCollection();
            
            if (storageContainer.getChildrenContainerCollection() != null)
            {
                Iterator iterator = storageContainer.getChildrenContainerCollection().iterator();
                while(iterator.hasNext())
                {
                    StorageContainer childStorageContainer = (StorageContainer)iterator.next();
                    Integer positionDimensionOne = childStorageContainer.getPositionDimensionOne();
                    Integer positionDimensionTwo = childStorageContainer.getPositionDimensionTwo();
                    fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = true;
                }
            }            
        }

        request.setAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS,fullStatus);
        request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT,
                storageContainerGridObject);
        return mapping.findForward(Constants.SUCCESS);
    }

}