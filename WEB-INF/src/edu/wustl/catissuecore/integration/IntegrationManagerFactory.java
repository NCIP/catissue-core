/**
 * <p>Title: IntegratoinManagerFactory Class>
 * <p>Description:	This class is used to get application specific integration implementation instance.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 23, 2006
 */

package edu.wustl.catissuecore.integration;

/**
 * This class is used to get application specific integration implementation instance.
 * @author Krunal Thakkar
 */
public class IntegrationManagerFactory
{
    /**
     * static method to get instance of IntegrationManager class for required Application
     */
    public static IntegrationManager getIntegrationManager(String ApplicationID)
    {
        IntegrationManager integrationManager=null;
        if(ApplicationID.equals("caTies"))
        {
            integrationManager= new CaTiesIntegrationMgrImpl();
        }
        
        return integrationManager;
    }
}
