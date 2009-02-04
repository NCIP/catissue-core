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
    /**
     * Method to fetch linked data from integrated applications 
     */
    public List getLinkedAppData(Long id, String applicationName);
    
    /**
     * Method to fetch mathich objects from integrated application 
     */
    public List getMatchingObjects();
    
    /**
     * Mehtod to retrieve PageToShow on Edit Link
     */
    public String getPageToShow();

}
