package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class SOPForm extends AbstractActionForm
{
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SOPForm.class);
	/**
	 * A string containing the name of the institute.
	 */
	private String name;

	private String barcode;

	private FormFile xmlFileName;
	/**
	 * @return the xmlFileName
	 */
	public FormFile getXmlFileName()
	{
		return xmlFileName;
	}

	/**
	 * @param xmlFileName the xmlFileName to set
	 */
	public void setXmlFileName(FormFile xmlFileName)
	{
		this.xmlFileName = xmlFileName;
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
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			String spChars = "!@#$%^&*()=+\\|{[]}\'\";:/?.>,<`~-";
			if(Validator.isEmpty(this.name) && xmlFileName.getFileName().isEmpty())
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("spp.error.fields"));
			}
			else
			{
				if (Validator.isEmpty(this.name))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("spp.error.name.missing"));
				}
				else if(validator.containsSpecialCharacters(this.name, spChars))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("spp.error.spcl.char"));
				}
				if(xmlFileName.getFileName().isEmpty())
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("spp.error.file"));
				}
				else if(!this.xmlFileName.getFileName().endsWith(".xml"))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("spp.error.valid.file"));
				}
			}
		}
		catch (final Exception excp)
		{
			SOPForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return Constants.SPP_ID;
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	/**
	 * @return the barcode
	 */
	public String getBarcode()
	{
		return barcode;
	}

	@Override
	public void setAddNewObjectIdentifier(String addNewFor,
			Long addObjectIdentifier)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final SpecimenProcessingProcedure spp = (SpecimenProcessingProcedure)abstractDomain;
		this.setId(spp.getId());
		this.setName(spp.getName());
		this.setBarcode(spp.getBarcode());
	}
}
