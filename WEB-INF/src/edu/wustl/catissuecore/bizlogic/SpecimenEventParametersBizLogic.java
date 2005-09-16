/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */
package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.dao.DAO;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

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
			    dao.update(specimen, sessionDataBean, true, true);
		    }
		}
		
		dao.insert(specimenEventParametersObject,sessionDataBean, true, true);
	}
}
