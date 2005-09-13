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
import edu.wustl.catissuecore.domain.User;
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
     * @param session The session in which the object is saved.
     * @param obj The FrozenEventParameters object to be saved.
     * @throws DAOException 
     */
	protected void insert(DAO dao, Object obj) throws DAOException 
	{
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;

		List list = dao.retrieve(User.class.getName(), "systemIdentifier", specimenEventParametersObject.getUser().getSystemIdentifier()  );
		if (!list.isEmpty())
		{
		    User user = (User) list.get(0);
		    specimenEventParametersObject.setUser(user);
		}
		Logger.out.debug("specimen ************************"+"specimenEventParametersObject.getSpecimen().getSystemIdentifier()**************************"+specimenEventParametersObject.getSpecimen().getSystemIdentifier());
		Specimen specimen = (Specimen) dao.retrieve(Specimen.class.getName(), specimenEventParametersObject.getSpecimen().getSystemIdentifier());
		
		if (specimen != null)
		{
		    specimenEventParametersObject.setSpecimen(specimen);
		}
		
		dao.insert(specimenEventParametersObject,true);
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws DAOException 
     */
//	protected void update(DAO dao, Object obj) throws DAOException
//    {
//    }
}
