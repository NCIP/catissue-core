/**
 * <p>Title: AbstractForwardToProcessor Class>
 * <p>Description:	AbstractForwardToProcessor populates data required for ForwardTo activity</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 08, 2006
 */

package edu.wustl.common.util;

import java.util.HashMap;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * AbstractForwardToProcessor populates data required for ForwardTo activity
 * @author Krunal Thakkar
 */
public abstract class AbstractForwardToProcessor 
{
    public abstract HashMap populateForwardToData(AbstractActionForm actionForm, AbstractDomainObject domainObject);
}
