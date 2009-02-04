/**
 * <p>Title: IntegratoinManager Class>
 * <p>Description:	This abstract class is used to be implemented by application specific Integraton classes related to integration of caTissue Core with caTies and CAE</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 23, 2006
 */

package edu.wustl.catissuecore.integration;

import java.util.List;

/**
 * This abstract class is used to be implemented by application specific Integraton classes related to integration of caTissue Core with caTies and CAE.
 * @author Krunal Thakkar
 */
public abstract class IntegrationManager
{
    /**
     * Abstract method to fetch linked data from integrated applications 
     */
    public abstract List getLinkedAppData(Object linkedObj, Long id);
    
    /**
     * Abstract method to fetch mathich objects from integrated application 
     */
    public abstract List getMatchingObjects(Object objToMatch);
    
}
