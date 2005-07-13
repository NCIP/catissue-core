/**
 * <p>Title: DAOFactory Class>
 * <p>Description:	DAOFactory is a factory for JDBC DAO instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.dao;

import edu.wustl.catissuecore.util.global.Constants;




/**
 * DAOFactory is a factory for JDBC DAO instances of various domain objects.
 * @author gautam_shetty
 */
public class DAOFactory
{
    /**
     * Returns DAO instance according to the type.
     * @param type The DAO type.
     * @return An AbstractDAO object.
     */
    public static AbstractDAO getDAO(int daoType)
    {
        AbstractDAO dao = null;
        switch (daoType)
        {
            case Constants.HIBERNATE_DAO :
                dao = new HibernateDAO();
                break;

            case Constants.JDBC_DAO :
                dao = new JDBCDAO();
            default :
                break;
        }
        return dao;
    }

}
