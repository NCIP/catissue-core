/**
 * <p>Title: MolecularSpecimenReviewParameters Class
 * <p>Description:  Attributes associated with a review event of a molecular specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a review event of a molecular specimen.
 * @hibernate.joined-subclass table="CATISSUE_MOL_SPE_REVIEW_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class MolecularSpecimenReviewParameters extends ReviewEventParameters implements java.io.Serializable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(MolecularSpecimenReviewParameters.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

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
	protected Integer gelNumber;

	/**
	 * Absorbance of the specimen at 260 nanometers.
	 */
	protected Double absorbanceAt260;

	/**
	 * Absorbance of the specimen at 280 nanometers.
	 */
	protected Double absorbanceAt280;

	/**
	 * A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected in a mammalian
	 * cellular RNA sample.
	 */
	protected Double ratio28STo18S;

	/**
	 * Returns a reference to the location of an electrophoretic gel image of the specimen.
	 * @return A reference to the location of an electrophoretic gel image of the specimen.
	 * @see #setGelImageURL(String)
	 * @hibernate.property name="gelImageURL" type="string"
	 * column="GEL_IMAGE_URL" length="255"
	 */
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
	 * @hibernate.property name="qualityIndex" type="string"
	 * column="QUALITY_INDEX" length="50"
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
	 * @hibernate.property name="laneNumber" type="string"
	 * column="LANE_NUMBER" length="50"
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
	 * @hibernate.property name="gelNumber" type="int"
	 * column="GEL_NUMBER" length="30"
	 */
	public Integer getGelNumber()
	{
		return gelNumber;
	}

	/**
	 * Sets a number corresponding to the gel on which the specimen was analyzed for QA purposes.
	 * @param gelNumber a number corresponding to the gel on which the specimen was analyzed for QA purposes.
	 * @see #getGelNumber()
	 */
	public void setGelNumber(Integer gelNumber)
	{
		this.gelNumber = gelNumber;
	}

	/**
	 * Returns absorbance of the specimen at 260 nanometers.
	 * @return Absorbance of the specimen at 260 nanometers.
	 * @see #setAbsorbanceAt260(Double)
	 * @hibernate.property name="absorbanceAt260" type="double"
	 * column="ABSORBANCE_AT_260" length="30"
	 */
	public Double getAbsorbanceAt260()
	{
		return absorbanceAt260;
	}

	/**
	 * Sets absorbance of the specimen at 260 nanometers.
	 * @param absorbanceAt260 absorbance of the specimen at 260 nanometers.
	 * @see #getAbsorbanceAt260()
	 */
	public void setAbsorbanceAt260(Double absorbanceAt260)
	{
		this.absorbanceAt260 = absorbanceAt260;
	}

	/**
	 * Returns absorbance of the specimen at 280 nanometers.
	 * @return Absorbance of the specimen at 280 nanometers.
	 * @see #setAbsorbanceAt280(Double)
	 * @hibernate.property name="absorbanceAt280" type="double"
	 * column="ABSORBANCE_AT_280" length="30"
	 */
	public Double getAbsorbanceAt280()
	{
		return absorbanceAt280;
	}

	/**
	 * Sets absorbance of the specimen at 280 nanometers.
	 * @param absorbanceAt280 absorbance of the specimen at 280 nanometers.
	 * @see #getAbsorbanceAt280()
	 */
	public void setAbsorbanceAt280(Double absorbanceAt280)
	{
		this.absorbanceAt280 = absorbanceAt280;
	}

	/**
	 * Returns  A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected in a
	 * mammalian cellular RNA sample.
	 * @return A unitless ratio of the full-length 28S to 18S ribosomal RNA mass that is detected
	 * in a mammalian cellular RNA sample.
	 * @see #setRatio28STo18S(Double)
	 * @hibernate.property name="ratio28STo18S" type="double"
	 * column="RATIO_28S_TO_18S" length="30"
	 */
	public Double getRatio28STo18S()
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
	public void setRatio28STo18S(Double ratio28STo18S)
	{
		this.ratio28STo18S = ratio28STo18S;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernate uses this by reflection API.
	 */
	public MolecularSpecimenReviewParameters()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 */
	public MolecularSpecimenReviewParameters(AbstractActionForm abstractForm)
	{
		super();
		setAllValues((IValueObject) abstractForm);
	}

	/**
	 * This function Copies the data from an MolecularSpecimenReviewParametersForm
	 * object to a MolecularSpecimenReviewParameters object.
	 * @param abstractForm - MolecularSpecimenReviewParametersForm object
	 * containing the information about the MolecularSpecimenReviewParameters.
	 */
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			MolecularSpecimenReviewParametersForm form =
				(MolecularSpecimenReviewParametersForm) abstractForm;
			this.gelImageURL = Utility.toString(form.getGelImageURL());
			this.qualityIndex = Utility.toString(form.getQualityIndex());
			this.laneNumber = Utility.toString(form.getLaneNumber());
			if (Utility.toString(form.getGelNumber()).trim().length() > 0)
			{
				this.gelNumber = Integer.valueOf(form.getGelNumber());
			}
			if (Utility.toString(form.getAbsorbanceAt260()).trim().length() > 0)
			{
				this.absorbanceAt260 = new Double(form.getAbsorbanceAt260());
			}
			if (Utility.toString(form.getAbsorbanceAt280()).trim().length() > 0)
			{
				this.absorbanceAt280 = new Double(form.getAbsorbanceAt280());
			}
			if (Utility.toString(form.getRatio28STo18S()).trim().length() > 0)
			{
				this.ratio28STo18S = new Double(form.getRatio28STo18S());
			}
			super.setAllValues(form);
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
	}
}