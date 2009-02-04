/**
 * <p>Title: ThawEventParameters Class>
 * <p>Description:  Attributes associated with a thawing event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.ThawEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a thawing event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_THAW_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Aniruddha Phadnis
 */
public class ThawEventParameters extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public ThawEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public ThawEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an ThawEventParameters object.
     * @param ThawEventParametersForm An ThawEventParametersForm object containing the information about the ThawEventParameters.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	ThawEventParametersForm form = (ThawEventParametersForm) abstractForm;

        	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
    
}