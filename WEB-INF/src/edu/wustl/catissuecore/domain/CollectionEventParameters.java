/**
 * <p>Title: CollectionEventParameters Class</p>
 * <p>Description: Attributes associated with the collection event of a specimen from participant. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with the collection event of a specimen from participant.
 * @hibernate.joined-subclass table="CATISSUE_COLL_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CollectionEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**	
	 *	Method of specimen collection from participant (e.g. needle biopsy, central venous line, bone marrow aspiration)
	 */
	protected String collectionProcedure;
	
	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	protected String container;

	/**
	 * Returns the procedure of collection.
	 * @hibernate.property name="collectionProcedure" type="string" column="COLLECTION_PROCEDURE" length="50"
	 * @return procedure of collection.
	 */
	public String getCollectionProcedure()
	{
		return collectionProcedure;
	}

	/**
	 * Sets the procedure. 
	 * @param procedure procedure of collection.
	 */
	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	/**
	 * Returns the container type used for collecting the specimen.
	 * @hibernate.property name="container" type="string" column="CONTAINER" length="50"
	 * @return container type used for collecting the specimen. 
	 */
	public String getContainer()
	{
		return container;
	}

	/**
	 * Sets the container 
	 * @param container container type used for collecting the specimen.
	 */
	public void setContainer(String container)
	{
		this.container = container;
	}
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public CollectionEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public CollectionEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an CollectionEventParameters object.
     * @param CollectionEventParametersForm An CollectionEventParametersForm object containing the information about the CollectionEventParameters.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	CollectionEventParametersForm form = (CollectionEventParametersForm) abstractForm;
        	Validator validator = new Validator();
        	
        	if(!validator.isValidOption(form.getContainer()))
        	{
        		this.container = null; //Purposefully set null for Edit Mode
        	}
        	else
        	{
        		this.container = form.getContainer();
        	}
        	
        	this.collectionProcedure = form.getCollectionProcedure() ;
        	
        	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
   
}