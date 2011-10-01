/**
 * <p>Title: CreateSpecimenForm Class>
 * <p>Description:  CreateSpecimenForm Class is used to encapsulate all the request parameters passed
 * from Create Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenForm Class is used to encapsulate all the request parameters passed
 * from Create Specimen webpage.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenForm extends SpecimenForm implements Cloneable, IPrinterTypeLocation
{

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(CreateSpecimenForm.class);
	/**
	 * Identifier of the Parent Speciemen.
	 * */
	private String parentSpecimenId;

	/**
	 * label of Parent Specimen
	 */
	private String parentSpecimenLabel = "";

	/**
	* barcode of Parent Specimen
	*/
	private String parentSpecimenBarcode = "";

	/**
	* Radio button to choose barcode/specimen identifier
	*/
	private String radioButton = "1";

	/**
	 *  Decides the calling of reset
	 */
	private boolean reset = true;
	/**
	 * Next forwardto in case of Print
	 */
	private String nextForwardTo;
	/**
	 * print checkbox
	 */
	private String printCheckbox;

	private String printerType;

	private String printerLocation;
	/**
	 * bug no. 4265
	 * indicates that the parent specimen is set for disposal or not
	 */
	private boolean disposeParentSpecimen = false;

	/**
	 * Returns an identifier of the Parent Speciemen.
	 * @return String an identifier of the Parent Speciemen.
	 * @see #setParentSpecimenId(String)
	 * */
	public String getParentSpecimenId()
	{
		return (this.parentSpecimenId);
	}

	/**
	 * Sets an identifier of the Parent Speciemen.
	 * @param parentSpecimenId an identifier of the Parent Speciemen.
	 * @see #getParentSpecimenId()
	 * */
	public void setParentSpecimenId(String parentSpecimenId)
	{
		this.parentSpecimenId = parentSpecimenId;
	}

	/**
	 * This method is to reset Class attributes
	 */
	@Override
	protected void reset()
	{
		if (this.reset == true)
		{
			super.reset();
			this.parentSpecimenId = null;
		}
	}

	/**
	 * @return CREATE_SPECIMEN_FORM_ID Returns the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return Constants.CREATE_SPECIMEN_FORM_ID;
	}

	/**
	 * This function Copies the data from an site object to a SiteForm object.
	 * @param abstractDomain An object containing the information about site.
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final Specimen specimen = (Specimen) abstractDomain;
		super.setAllValues(specimen);
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = super.validate(mapping, request);
		final Validator validator = new Validator();

		try
		{
			if (this.getOperation().equals(Constants.ADD)
					|| this.getOperation().equals(Constants.EDIT))
			{
				if (request.getParameter("retainForm") == null)
				{
					if (this.getRadioButton().equals("1"))
					{
						if (Validator.isEmpty(this.parentSpecimenLabel))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.required", ApplicationProperties
											.getValue("createSpecimen.parentLabel")));
						}
					}
					else
					{
						if (Validator.isEmpty(this.parentSpecimenBarcode))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.required", ApplicationProperties
											.getValue("createSpecimen.parentBarcode")));
						}
					}
				}
//				if (!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl
//				if(!this.generateLabel && Validator.isEmpty(this.label) && this.label.trim().equals(""))
//				{
//					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
//							ApplicationProperties.getValue("specimen.label")));
//
//				}
			}
		}
		catch (final Exception excp)
		{
			CreateSpecimenForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	/**
	 * @return Returns the checkedButton.
	 */
	public String getRadioButton()
	{
		return this.radioButton;
	}

	/**
	 * @param checkedButton The checkedButton to set.
	 */
	public void setRadioButton(String radioButton)
	{
		this.radioButton = radioButton;
	}

	/**
	 * @return Returns the parentSpecimenBarcode.
	 */
	public String getParentSpecimenBarcode()
	{
		return this.parentSpecimenBarcode;
	}

	/**
	 * @param parentSpecimenBarcode The parentSpecimenBarcode to set.
	 */
	public void setParentSpecimenBarcode(String parentSpecimenBarcode)
	{
		this.parentSpecimenBarcode = parentSpecimenBarcode;
	}

	/**
	 * @return Returns the parentSpecimenLabel.
	 */
	public String getParentSpecimenLabel()
	{
		return this.parentSpecimenLabel;
	}

	/**
	 * @param parentSpecimenLabel The parentSpecimenLabel to set.
	 */
	public void setParentSpecimenLabel(String parentSpecimenLabel)
	{
		this.parentSpecimenLabel = parentSpecimenLabel;
	}

	/**
	 * @return Returns the reset.
	 */
	public boolean getReset()
	{
		return this.reset;
	}

	/**
	 * @param reset The reset to set.
	 */
	public void setReset(boolean reset)
	{
		this.reset = reset;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			logger.info("Error in Clone method of CreateSpecimenForm:" + e,e);
		}
		return null;
	}

	public String getNextForwardTo()
	{
		return this.nextForwardTo;
	}

	public void setNextForwardTo(String nextForwardTo)
	{
		this.nextForwardTo = nextForwardTo;
	}

	public String getPrintCheckbox()
	{
		return this.printCheckbox;
	}

	public void setPrintCheckbox(String printCheckbox)
	{
		this.printCheckbox = printCheckbox;
	}

	public boolean getDisposeParentSpecimen()
	{
		return this.disposeParentSpecimen;
	}

	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	public String getPrinterLocation()
	{
		return this.printerLocation;
	}

	public void setPrinterLocation(String printerLocation)
	{
		this.printerLocation = printerLocation;
	}

	public String getPrinterType()
	{
		return this.printerType;
	}

	public void setPrinterType(String printerType)
	{
		this.printerType = printerType;
	}
}