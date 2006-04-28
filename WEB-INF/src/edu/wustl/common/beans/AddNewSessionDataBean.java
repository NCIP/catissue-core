/**
 * <p>Title: AddNewAction Class>
 * <p>Description:	This Class is used to maintain FormBean for AddNew operation</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on Apr 12, 2006
 */

package edu.wustl.common.beans;

import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * This Class is used to maintain FormBean for AddNew operation.
 * @author Krunal Thakkar
 */
public class AddNewSessionDataBean 
{
    private AbstractActionForm abstractActionForm;
    private String forwardTo= new String();
    private String addNewFor = new String();
    
    /**
     * @return Returns abstractActionForm
     */
    public AbstractActionForm getAbstractActionForm()
    {
        return this.abstractActionForm;
    }
    
    /**
     * @param abstractActionForm The abstractActionForm to set
     */
    public void setAbstractActionForm(AbstractActionForm abstractActionForm)
    {
        this.abstractActionForm = abstractActionForm;
    }
   
    /**
     * @return Returns redirectToPath
     */
    public String getForwardTo()
    {
        return this.forwardTo;
    }
    
    /**
     * @param redirectToPath The redirectToPath to set
     */
    public void setForwardTo(String forwardTo)
    {
        this.forwardTo = forwardTo;
    }
    
    /**
     * @return Returns addNewFor
     */
    public String getAddNewFor()
    {
        return this.addNewFor;
    }

    /**
     * @param addNewFor The addNewFor to set
     */
    public void setAddNewFor(String addNewFor)
    {
        this.addNewFor = addNewFor;
    }
}
