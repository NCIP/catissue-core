/**
 * <p>Title: MolecularSpecimenReviewParametersForm Class</p>
 * <p>Description:  This Class handles the Molecular Specimen Review event parameters.
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 * This Class handles the Molecular Specimen Review event parameters.
 * Attributes associated with a review event of a molecular specimen.
 */
public class MolecularSpecimenReviewParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(MolecularSpecimenReviewParametersForm.class);
	/**
     * A reference to the location of an electrophoretic gel image of the specimen.
     */
	protected String gelImageURL;
	
	/**
     * A normalized quality score that indicates the integrity of the specimen.
     */
	protected String qualityIndex;
	
	/**
     * The lane number within the electrophoretic gel image that corresponds to the specimen.
     */
	protected String laneNumber;
	
	/**
     * A number corresponding to the gel on which the specimen was analyzed for QA purposes.
     */
	protected String gelNumber;
	
	/**
     * Absorbance of the specimen at 260 nanometers.
     */
	protected String absorbanceAt260;
	
	/**
     * Absorbance of the specimen at 280 nanometers.
     */
	protected String absorbanceAt280;
	
	protected String isRNA = "false";
	
	/**
     * A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected in a mammalian 
     * cellular RNA sample.
     */
	protected String ratio28STo18S;
	
	/**
     * Returns a reference to the location of an electrophoretic gel image of the specimen. 
     * @return A reference to the location of an electrophoretic gel image of the specimen.
     * @see #setGelImageURL(String)
     */
	protected String checkRNA;
	
	public String getGelImageURL()
	{
		return gelImageURL;
	}

	/**
     * Sets a reference to the location of an electrophoretic gel image of the specimen.
     * @param gelImageURL a reference to the location of an electrophoretic gel image of the specimen.
     * @see #getGelImageURL()
     */
	public void setGelImageURL(String gelImageURL)
	{
		this.gelImageURL = gelImageURL;
	}

	/**
     * Returns a normalized quality score that indicates the integrity of the specimen. 
     * @return A normalized quality score that indicates the integrity of the specimen.
     * @see #setQualityIndex(String)
     */
	public String getQualityIndex()
	{
		return qualityIndex;
	}

	/**
     * Sets a normalized quality score that indicates the integrity of the specimen.
     * @param qualityIndex a normalized quality score that indicates the integrity of the specimen.
     * @see #getQualityIndex()
     */
	public void setQualityIndex(String qualityIndex)
	{
		this.qualityIndex = qualityIndex;
	}

	/**
     * Returns the lane number within the electrophoretic gel image that corresponds to the specimen. 
     * @return The lane number within the electrophoretic gel image that corresponds to the specimen.
     * @see #setLaneNumber(String)
     */
	public String getLaneNumber()
	{
		return laneNumber;
	}

	/**
     * Sets the lane number within the electrophoretic gel image that corresponds to the specimen.
     * @param laneNumber the lane number within the electrophoretic gel image that corresponds to the specimen.
     * @see #getLaneNumber()
     */
	public void setLaneNumber(String laneNumber)
	{
		this.laneNumber = laneNumber;
	}

	/**
     * Returns a number corresponding to the gel on which the specimen was analyzed for QA purposes. 
     * @return A number corresponding to the gel on which the specimen was analyzed for QA purposes.
     * @see #setGelNumber(Integer)
     */
	public String getGelNumber()
	{
		return gelNumber;
	}

	/**
     * Sets a number corresponding to the gel on which the specimen was analyzed for QA purposes.
     * @param gelNumber a number corresponding to the gel on which the specimen was analyzed for QA purposes.
     * @see #getGelNumber()
     */
	public void setGelNumber(String gelNumber)
	{
		this.gelNumber = gelNumber;
	}

	/**
     * Returns absorbance of the specimen at 260 nanometers. 
     * @return Absorbance of the specimen at 260 nanometers.
     * @see #setAbsorbanceAt260(double)
     */
	public String getAbsorbanceAt260()
	{
		return absorbanceAt260;
	}

	/**
     * Sets absorbance of the specimen at 260 nanometers.
     * @param absorbanceAt260 absorbance of the specimen at 260 nanometers.
     * @see #getAbsorbanceAt260()
     */
	public void setAbsorbanceAt260(String absorbanceAt260)
	{
		this.absorbanceAt260 = absorbanceAt260;
	}

	/**
     * Returns absorbance of the specimen at 280 nanometers. 
     * @return Absorbance of the specimen at 280 nanometers.
     * @see #setAbsorbanceAt280(double)
     */
	public String getAbsorbanceAt280()
	{
		return absorbanceAt280;
	}

	/**
     * Sets absorbance of the specimen at 280 nanometers.
     * @param absorbanceAt280 absorbance of the specimen at 280 nanometers.
     * @see #getAbsorbanceAt280()
     */
	public void setAbsorbanceAt280(String absorbanceAt280)
	{
		this.absorbanceAt280 = absorbanceAt280;
	}

	/**
     * Returns  A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected in a 
     * mammalian cellular RNA sample. 
     * @return A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected 
     * in a mammalian cellular RNA sample.
     * @see #setRatio28STo18S(String)
     */
	public String getRatio28STo18S()
	{
		return ratio28STo18S;
	}

	/**
     * Sets  A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected in a 
     * mammalian cellular RNA sample.
     * @param ratio28STo18S  A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is 
     * detected in a mammalian cellular RNA sample.
     * @see #getRatio28STo18S()
     */
	public void setRatio28STo18S(String ratio28STo18S)
	{
		this.ratio28STo18S = ratio28STo18S;
	}
	
//	 ----- SUPERCLASS METHODS
	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID;
	}
	/**
     * Populates all the fields from the domain object to the form bean.
     * @param abstractDomain An AbstractDomain Object  
     */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		MolecularSpecimenReviewParameters molecularSpecimenReviewParametersObject = (MolecularSpecimenReviewParameters)abstractDomain ;
		this.gelImageURL = Utility.toString(molecularSpecimenReviewParametersObject.getGelImageURL()) ;
		this.qualityIndex = Utility.toString(molecularSpecimenReviewParametersObject.getQualityIndex());
		this.laneNumber = Utility.toString(molecularSpecimenReviewParametersObject.getLaneNumber()) ;
		this.gelNumber = Utility.toString(molecularSpecimenReviewParametersObject.getGelNumber()) ;
		this.absorbanceAt260 = Utility.toString(molecularSpecimenReviewParametersObject.getAbsorbanceAt260());
		this.absorbanceAt280 = Utility.toString(molecularSpecimenReviewParametersObject.getAbsorbanceAt280());  
		this.ratio28STo18S = Utility.toString(molecularSpecimenReviewParametersObject.getRatio28STo18S()) ; 
		if(this.ratio28STo18S.trim().length()>0)
		{
			this.isRNA = "true"; 
		}
		else
		{
			this.isRNA = "false";
		}
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	ActionErrors errors = super.validate(mapping, request);
         Validator validator = new Validator();
         
         try
         {

         	logger.info("GelImageUrl: "+ gelImageURL);
//         //	 checks the gelImageURL
//          	if (validator.isEmpty(gelImageURL))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("molecularspecimenreviewparameters.gelimageurl")));
//            }

          	logger.info("qualityIndex: "+ qualityIndex);
            //	 checks the qualityIndex
//         	if (validator.isEmpty(qualityIndex ))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("molecularspecimenreviewparameters.qualityindex")));
//            }

          	logger.info("laneNumber: "+ laneNumber);
//            //	 checks the laneNumber
//         	if (validator.isEmpty(laneNumber))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("molecularspecimenreviewparameters.lanenumber")));
//            }

          	logger.info("gelNumber: "+ gelNumber);
            //	 checks the gelNumber
         	if (!validator.isEmpty(gelNumber) && !validator.isNumeric(gelNumber,0))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("molecularspecimenreviewparameters.gelnumber")));
            }

          	logger.info("absorbanceAt260: "+ absorbanceAt260);
            //	 checks the absorbanceAt260
         	if (!validator.isEmpty(absorbanceAt260) && !validator.isDouble(absorbanceAt260,false))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("molecularspecimenreviewparameters.absorbanceat260")));
            }

          	logger.info("absorbanceAt280: "+ absorbanceAt280);
            //	 checks the absorbanceAt280
         	if (!validator.isEmpty(absorbanceAt280) && !validator.isDouble(absorbanceAt280,false))
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("molecularspecimenreviewparameters.absorbanceat280")));
            }

         	logger.info("ratio28STo18S : "+ ratio28STo18S);
//            //	 checks the ratio28STo18S 
//         	if (!validator.isDouble(""+ratio28STo18S ))
//            {
//           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("molecularspecimenreviewparameters.ratio28STo18S")));
//            }
         }
         catch(Exception excp)
         {
             logger.error(excp.getMessage());
         }
         return errors;
      }
	

     /**
      * Resets the values of all the fields.
      * This method defined in ActionForm is overridden in this class.
      */
      protected void reset()
     {
//         super.reset();
//         this.gelImageURL = null;
//         this.qualityIndex = null;
//         this.laneNumber = null;
//         this.gelNumber = 0;
//         this.absorbanceAt260 = 0.0;
//         this.absorbanceAt280 = 0.0;
//         this.ratio28STo18S = null;
//         this.isRNA = "false";
     }
       
    
	
	/**
	 * @return Returns the isRNA.
	 */
	public String getIsRNA()
	{
		return isRNA;
	}
	/**
	 * @param isRNA The isRNA to set.
	 */
	public void setIsRNA(String isRNA) 
	{
		this.isRNA = isRNA;
	}

	public String getCheckRNA() {
		return checkRNA;
	}

	public void setCheckRNA(String checkRNA) {
		this.checkRNA = checkRNA;
	}
}
