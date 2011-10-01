///**
// * <p>Title: CollectionEventParametersForm Class</p>
// * <p>Description:  This Class handles the Collection Event Parameters.
// * <p> It extends the EventParametersForm class.
// * Copyright:    Copyright (c) 2005
// * Company: Washington University, School of Medicine, St. Louis.
// * @author Mandar Deshmukh
// * @version 1.00
// * Created on Aug 05, 2005
// */
//
//package edu.wustl.catissuecore.actionForm;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionMapping;
//
//import edu.wustl.catissuecore.domain.CollectionEventParameters;
//import edu.wustl.catissuecore.util.global.Constants;
//import edu.wustl.catissuecore.util.global.DefaultValueManager;
//import edu.wustl.common.domain.AbstractDomainObject;
//import edu.wustl.common.util.global.ApplicationProperties;
//import edu.wustl.common.util.global.CommonUtilities;
//import edu.wustl.common.util.global.Validator;
//import edu.wustl.common.util.logger.Logger;
//
///**
// * @author mandar_deshmukh
// *
// * This Class handles the Collection Event Parameters.
// */
//public class CollectionEventParametersForm extends SpecimenEventParametersForm
//{
//
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * logger Logger - Generic logger.
//	 */
//	private static Logger logger = Logger.getCommonLogger(CollectionEventParametersForm.class);
//
//	/**
//	 * Name : Virender Mehta
//	 * Reviewer: Sachin Lale
//	 * Bug ID: defaultValueConfiguration_BugID
//	 * Patch ID:defaultValueConfiguration_BugID_12
//	 * Description: Configuration for default value for CollectionProcedure
//	 */
//
//	/**
//	 *	Method of specimen collection from participant (e.g. needle biopsy, central venous line, bone marrow aspiration)
//	 */
//	protected String collectionProcedure = (String) DefaultValueManager
//			.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE);
//
//	/**
//	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
//	 */
//	protected String container = (String) DefaultValueManager
//			.getDefaultValue(Constants.DEFAULT_CONTAINER);
//
//	/**
//	 * Returns the procedure of collection.
//	 * @return procedure of collection.
//	 */
//	public String getCollectionProcedure()
//	{
//		return this.collectionProcedure;
//	}
//
//	/**
//	 * Sets the procedure.
//	 * @param collectionProcedure procedure of collection.
//	 */
//	public void setCollectionProcedure(String collectionProcedure)
//	{
//		this.collectionProcedure = collectionProcedure;
//	}
//
//	/**
//	 * Returns the container type used for collecting the specimen.
//	 * @return container type used for collecting the specimen.
//	 */
//	public String getContainer()
//	{
//		return this.container;
//	}
//
//	/**
//	 * Sets the container
//	 * @param container container type used for collecting the specimen.
//	 */
//	public void setContainer(String container)
//	{
//		this.container = container;
//	}
//
//	//	 ---- super class methods
//	// ----- SUPERCLASS METHODS
//	/**
//	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
//	 * @return COLLECTION_EVENT_PARAMETERS_FORM_ID
//	 */
//	@Override
//	public int getFormId()
//	{
//		return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
//	}
//
//	/**
//	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
//	 * @param abstractDomain An object of AbstractDomainObject
//	 */
//	@Override
//	public void setAllValues(AbstractDomainObject abstractDomain)
//	{
//		super.setAllValues(abstractDomain);
//		final CollectionEventParameters collectionEventParameterObject = (CollectionEventParameters) abstractDomain;
//		this.collectionProcedure = CommonUtilities.toString(collectionEventParameterObject
//				.getCollectionProcedure());
//		this.container = CommonUtilities.toString(collectionEventParameterObject.getContainer());
//	}
//
//	/**
//	 * Overrides the validate method of ActionForm.
//	 * @return error ActionErrors instance
//	 * @param mapping Actionmapping instance
//	 * @param request HttpServletRequest instance
//	 */
//	@Override
//	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
//	{
//
//		final ActionErrors errors = super.validate(mapping, request);
//		final Validator validator = new Validator();
//
//		try
//		{
//
//			// checks the collectionProcedure
//			if (!validator.isValidOption(this.collectionProcedure))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
//						ApplicationProperties
//								.getValue("collectioneventparameters.collectionprocedure")));
//			}
//
//		}
//		catch (final Exception excp)
//		{
//			CollectionEventParametersForm.logger.error(excp.getMessage(),excp);
//			excp.printStackTrace();
//
//		}
//		return errors;
//	}
//
//	/**
//	 * Method to set class Attributes
//	 */
//	@Override
//	protected void reset()
//	{
//		//     	super.reset();
//		//        this.collectionProcedure = null;
//		//        this.container = null;
//	}
//
//	@Override
//	public void setAddNewObjectIdentifier(String arg0, Long arg1)
//	{
//		// TODO Auto-generated method stub
//
//	}
//
//}
