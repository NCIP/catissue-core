/**
 * <p>Title: IntegrationBizLogic Class>
 * <p>Description:	This Class is used to handle BizLogic related to integration of caTissue Core with caTies and CAE</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 22, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.integration.Integrator;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * This Class is used to handle BizLogic related to integration of caTissue Core with caTies and CAE.
 * @author Krunal Thakkar
 */
public class IntegrationBizLogic extends DefaultBizLogic implements Integrator
{
    public List getLinkedAppData(Long id, String applicationName)
    {
        return new ArrayList();
    }
    
    public List getMatchingObjects()
    {
        return new ArrayList();
    }
    
    public String getPageToShow()
    {
        return new String();
    }
}
