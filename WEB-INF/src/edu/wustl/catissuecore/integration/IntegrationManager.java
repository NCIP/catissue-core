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
    public abstract List getLinkedAppData(Object linkedObj, Long systemIdentifier);
    
    public abstract List getMatchingObjects(Object objToMatch);
    
}
