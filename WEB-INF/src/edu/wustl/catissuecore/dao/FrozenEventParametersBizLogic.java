/*
 * Created on Jul 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.dao;

import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FrozenEventParametersBizLogic extends DefaultBizLogic
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
		FrozenEventParameters frozenEventParametersObject = (FrozenEventParameters)obj;

        AbstractDAO dao = DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		dao.openSession();
		
		List list = dao.retrieve(User.class.getName(), "systemIdentifier", frozenEventParametersObject.getUser().getSystemIdentifier()  );
		if (list.size() != 0)
		{
		    User user = (User) list.get(0);
		    frozenEventParametersObject.setUser(user);
		}
		
		dao.insert(frozenEventParametersObject);
	    
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
