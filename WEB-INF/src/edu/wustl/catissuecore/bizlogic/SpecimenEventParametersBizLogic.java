/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh</p>
 * This class contains the Business Logic for all EventParameters Classes.
 * This will be the class which will be used for data transactions of the EventParameters. 
 */
public class SpecimenEventParametersBizLogic extends DefaultBizLogic
{
	/**
     * Saves the FrozenEventParameters object in the database.
	 * @param obj The FrozenEventParameters object to be saved.
	 * @param session The session in which the object is saved.
	 * @throws DAOException 
     */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean) throws DAOException, UserNotAuthorizedException 
	{
//	    Set protectionObjects = new HashSet();
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;

		List list = dao.retrieve(User.class.getName(), "systemIdentifier", specimenEventParametersObject.getUser().getSystemIdentifier()  );
		if (!list.isEmpty())
		{
		    User user = (User) list.get(0);
		    specimenEventParametersObject.setUser(user);
		}
		Specimen specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getSystemIdentifier());
		
		if (specimen != null)
		{
		    specimenEventParametersObject.setSpecimen(specimen);
		    if (specimenEventParametersObject instanceof TransferEventParameters)
		    {
		        TransferEventParameters transferEventParameters = (TransferEventParameters)specimenEventParametersObject; 
			    specimen.setPositionDimensionOne(transferEventParameters.getToPositionDimensionOne());
			    specimen.setPositionDimensionTwo(transferEventParameters.getToPositionDimensionTwo());
			    
			    StorageContainer storageContainer = (StorageContainer)dao.retrieve(StorageContainer.class.getName(), transferEventParameters.getToStorageContainer().getSystemIdentifier());
			    if (storageContainer != null)
			    {
			        specimen.setStorageContainer(storageContainer);
			    }
			    dao.update(specimen, sessionDataBean, true, true, false);
		    }
		}
		
		dao.insert(specimenEventParametersObject,sessionDataBean, true, true);
//		protectionObjects.add(specimenEventParametersObject);
		
//		Inserting data for Authorization
//		try
//        {
//            SecurityManager.getInstance(this.getClass()).insertAuthorizationData(null,protectionObjects,getDynamicGroups(specimenEventParametersObject));
//        }
//        catch (SMException e)
//        {
//            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
//        }
	}
	
	 public String[] getDynamicGroups(AbstractDomainObject obj)
	    {
	        String[] dynamicGroups=null;
	        SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;
	        dynamicGroups = new String[1];
	        
	        try
	        {
	            dynamicGroups[0] = SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(specimenEventParametersObject.getSpecimen(),Constants.getCollectionProtocolPGName(null));
	        }
	        catch (SMException e)
	        {
	            Logger.out.error("Exception in Authorization: "+e.getMessage(),e);
	        }
	        Logger.out.debug("Dynamic Group name: "+dynamicGroups[0]);
	        return dynamicGroups;
	        
	    }
}
