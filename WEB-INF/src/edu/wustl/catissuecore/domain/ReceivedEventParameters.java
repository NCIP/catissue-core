/**
 * <p>Title: ReceivedEventParameters Class</p>
 * <p>Description: Attributes associated with the received event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.ReceivedEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with the received event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_RECEIVED_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ReceivedEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ReceivedEventParameters.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Grossly evaluated quality of the received specimen.
	 */
	protected String receivedQuality;

	/**
	 * Returns the receivedQuality of the specimen.
	 * @hibernate.property name="receivedQuality" type="string" column="RECEIVED_QUALITY" length=50"
	 * @return receivedQuality of the specimen.
	 */
	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	/**
	 * Sets the receivedQuality of the SPECIMEN.
	 * @param receivedQuality receivedQuality of the SPECIMEN.
	 */
	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	/**
	 * Default Constructor.
	 */
	public ReceivedEventParameters()
	{
		super();
	}
	/**
	 * Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 */
	public ReceivedEventParameters(AbstractActionForm abstractForm)
	{
		super();
		setAllValues((IValueObject)abstractForm);
	}

	/**
     * This function Copies the data from an ReceivedEventParametersForm object to a
     * ReceivedEventParameters object.
     * @param abstractForm - receivedEventParametersForm An ReceivedEventParametersForm
     * object containing the information about the ReceivedEventParameters.
     */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
            ReceivedEventParametersForm form = (ReceivedEventParametersForm) abstractForm;
            this.receivedQuality = form.getReceivedQuality();
            super.setAllValues(form);
        	//call to event parameters setallvalue method
//        	super.setAllValues(abstractForm);

            logger.debug("receivedQuality: "+receivedQuality);
        }
        catch (Exception excp)
        {
        	logger.error(excp.getMessage());
        }
    }

    /**
     * Parameterized Constructor.
     * @param obj of ReceivedEventParameters type.
     */
    public ReceivedEventParameters(ReceivedEventParameters obj)
    {
    	super();
    	this.receivedQuality = obj.receivedQuality;
    	this.comment = obj.comment;
    }
}