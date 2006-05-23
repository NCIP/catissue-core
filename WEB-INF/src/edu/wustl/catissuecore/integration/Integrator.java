/**
 * <p>Title: IntegrationClass>
 * <p>Description:	This Interface is used to be implemented by BizLogic(s) related to integration of caTissue Core with caTies and CAE</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 22, 2006
 */
 

package edu.wustl.catissuecore.integration;

import java.util.List;

/**
 * This Interface is used to be implemented by BizLogic(s) related to integration of caTissue Core with caTies and CAE.
 * @author Krunal Thakkar
 */
public interface Integrator
{
    
    public List getLinkedAppData(Long systemIdentifier, String applicationName);
    
    public List getMatchingObjects();
    
    public String getPageToShow();

}
