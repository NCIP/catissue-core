/**
 * <p>Title: Order Class>
 * <p>Description:   Class for NewSpecimenOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */
package edu.wustl.catissuecore.domain;



/**
 * This class indicates the new specimens associated with the request order. 
 * @author ashish_gupta
 */
public class NewSpecimenOrderItem extends SpecimenOrderItem 
{
	private static final long serialVersionUID = -6534761806407210380L;

	/**
	 * String containing the specimen type of the new/derived specimens
	 */
	protected String specimenType;
	
	/**
	 * String containing the specimen class of the new/derived specimens
	 */
	protected String specimenClass;

	
}
