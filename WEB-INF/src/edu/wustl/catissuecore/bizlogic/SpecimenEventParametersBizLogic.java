/*
 * Created on Jul 29, 2005
 *<p>SpecimenEventParametersBizLogic Class</p>
 * This class contains the Biz Logic for all EventParameters Classes.
 * This will be the class which will be used for datatransactions of the EventParameters. 
 */
package edu.wustl.catissuecore.bizlogic;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.dao.AbstractDAO;
import edu.wustl.catissuecore.dao.DAOFactory;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
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
     * @param session The session in which the object is saved.
     * @param obj The FrozenEventParameters object to be saved.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
	public void insert(Object obj) throws DAOException 
	{
		SpecimenEventParameters specimenEventParametersObject = (SpecimenEventParameters)obj;

        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();
		
//		List list = dao.retrieve(User.class.getName(), "systemIdentifier", specimenEventParametersObject.getUser().getSystemIdentifier()  );
//		if (list.size() != 0)
//		{
//		    User user = (User) list.get(0);
//		    specimenEventParametersObject.setUser(user);
//		}
		
		dao.insert(specimenEventParametersObject);
	    
	    dao.closeSession();
	}
	
	/**
     * Updates the persistent object in the database.
     * @param session The session in which the object is saved.
     * @param obj The object to be updated.
     * @throws HibernateException Exception thrown during hibernate operations.
     * @throws DAOException 
     */
    public void update(Object obj) throws DAOException
    {
    }
}
