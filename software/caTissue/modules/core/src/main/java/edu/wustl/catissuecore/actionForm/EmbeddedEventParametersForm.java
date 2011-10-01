///**
// * <p>Title: EmbeddedEventParametersForm Class</p>
// * <p>Description:  This Class handles the embedded event parameters..
// * <p> It extends the EventParametersForm class.
// * Copyright:    Copyright (c) 2005
// * Company: Washington University, School of Medicine, St. Louis.
// * @author Jyoti Singh
// * @version 1.00
// * Created on Aug 2, 2005
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
//import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
//import edu.wustl.catissuecore.util.global.Constants;
//import edu.wustl.catissuecore.util.global.DefaultValueManager;
//import edu.wustl.common.domain.AbstractDomainObject;
//import edu.wustl.common.util.global.ApplicationProperties;
//import edu.wustl.common.util.global.CommonUtilities;
//import edu.wustl.common.util.global.Validator;
//import edu.wustl.common.util.logger.Logger;
//
///**
//  *
// * Description:  This Class handles the embedded event parameters..
// */
//public class EmbeddedEventParametersForm extends SpecimenEventParametersForm
//{
//
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * logger Logger - Generic logger.
//	 */
//	private static Logger logger = Logger.getCommonLogger(EmbeddedEventParametersForm.class);
//	private String embeddingMedium = (String) DefaultValueManager
//			.getDefaultValue(Constants.DEFAULT_EMBEDDING_MEDIUM);
//
//	/**
//	 * @return embeddingMedium Getting embeddingMedium
//	 */
//	public String getEmbeddingMedium()
//	{
//		return this.embeddingMedium;
//	}
//
//	/**
//	 * @param embeddingMedium Setting embeddingMedium
//	 */
//	public void setEmbeddingMedium(String embeddingMedium)
//	{
//		this.embeddingMedium = embeddingMedium;
//	}
//
//	// ----- SUPERCLASS METHODS
//	/**
//	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
//	 * @return EMBEDDED_EVENT_PARAMETERS_FORM_ID
//	 */
//	@Override
//	public int getFormId()
//	{
//		return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
//	}
//
//	/**
//	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
//	 * @param abstractDomain  An AbstractDomainObject obj
//	 */
//	@Override
//	public void setAllValues(AbstractDomainObject abstractDomain)
//	{
//		super.setAllValues(abstractDomain);
//		final EmbeddedEventParameters embeddedEventParametersObject = (EmbeddedEventParameters) abstractDomain;
//		this.embeddingMedium = CommonUtilities.toString(embeddedEventParametersObject
//				.getEmbeddingMedium());
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
//		final ActionErrors errors = super.validate(mapping, request);
//		final Validator validator = new Validator();
//
//		try
//		{
//			if (!validator.isValidOption(this.embeddingMedium))
//			{
//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
//						ApplicationProperties.getValue("embeddedeventparameters.embeddingMedium")));
//			}
//		}
//		catch (final Exception excp)
//		{
//			EmbeddedEventParametersForm.logger.error(excp.getMessage(),excp);
//			excp.printStackTrace() ;
//		}
//		return errors;
//	}
//
//	/**
//	 * Resets the values of all the fields.
//	 * This method defined in ActionForm is overridden in this class.
//	 */
//	@Override
//	protected void reset()
//	{
//		//         super.reset();
//		//         this.embeddingMedium = null;
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