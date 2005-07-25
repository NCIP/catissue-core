/**
 * <p>Title: AbstractDomain Class>
 * <p>Description:  AbstractDomain class is the superclass of all the domain classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * AbstractDomain class is the superclass of all the domain classes.
 * @author gautam_shetty
 */
public abstract class AbstractDomainObject
{
    /**
     * Returns the fully qualified name of the class according to the form bean type.
     * @param FORM_TYPE Form bean Id.
     * @return the fully qualified name of the class according to the form bean type.
     */
    public static String getDomainObjectName(int FORM_TYPE)
    {
        String className = null;
        switch(FORM_TYPE)
        {
            case Constants.PARTICIPANT_FORM_ID:
                className = Participant.class.getName();
            	break;
            case Constants.INSTITUTION_FORM_ID:
                className = Institution.class.getName();
            	break;
            case Constants.REPORTEDPROBLEM_FORM_ID:
                className = ReportedProblem.class.getName();
            	break;
            case Constants.USER_FORM_ID:
                className = User.class.getName();
            	break;
        }
        return className;
    }
    
    /**
     * Copies all values from the AbstractForm object
     * @param abstractForm The AbstractForm object
     */
    public abstract void setAllValues(AbstractActionForm abstractForm);
    
    /**
	 * Returns the unique systemIdentifier assigned to the domain object.
     * @return returns a unique systemIdentifier assigned to the domain object.
     * @see #setIdentifier(Long)
	 * */
    public abstract Long getSystemIdentifier();

    /**
	 * Sets an systemIdentifier for the domain object.
	 * @param systemIdentifier systemIdentifier for the domain object.
	 * @see #getIdentifier()
	 * */
    public abstract void setSystemIdentifier(Long systemIdentifier);
}
