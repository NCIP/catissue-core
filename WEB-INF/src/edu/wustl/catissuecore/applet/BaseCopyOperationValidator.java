package edu.wustl.catissuecore.applet;

import java.io.Serializable;


/**
 * <p>This class initializes the fields of BaseCopyOperationValidator.java</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public abstract class BaseCopyOperationValidator implements Serializable 
{
	
	/**
	 * Specify the serialVersionUID field 
	 */
	private static final long serialVersionUID = 366636337395262535L;

	/**
	 * Specify the validatorModel field 
	 */
	protected CopyOperationValidatorModel validatorModel;
	
	/**
	 * Perform the base level validations required for copy operation.  
	 */
	public String validate()
	{
		String message = "";
		message = preValidate();
		message = doValidate();
		message = postValidate();
		return message;
	}
	
	/**
	 * Perform the pre validations required for copy operation.  
	 */
	protected String preValidate()
	{
		String message = "";
		return message;
	}
	
	/**
	 * Perform the actual validations required for copy operation.
	 * That method must be overridden in subclasses.  
	 */
	protected abstract String doValidate();
	
	/**
	 * Perform the post validations required for copy operation.
	 */
	protected String postValidate()
	{
		String message = "";
		return message;
	}
}
