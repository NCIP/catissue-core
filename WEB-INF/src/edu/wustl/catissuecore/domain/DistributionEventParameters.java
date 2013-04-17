/**
 * <p>Title: DistributionEventParameters Class</p>
 * @author Pathik Sheth
 * @version 3.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with moving specimen from one storage location to another.
 * @hibernate.joined-subclass table="CATISSUE_TRANSFER_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class DistributionEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DistributionEventParameters.class);
	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

    private Double distributedQuantity=0D;
	
    private Distribution distributionDetails;
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public DistributionEventParameters()
	{
		super();
	}

    	
	public Double getDistributedQuantity() {
		return distributedQuantity;
	}


	public void setDistributedQuantity(Double distributedQuantity) {
		this.distributedQuantity = distributedQuantity;
	}

	public Distribution getDistributionDetails() {
		return distributionDetails;
	}


	public void setDistributionDetails(Distribution distributionDetails) {
		this.distributionDetails = distributionDetails;
	}


	/**
	 * Parameterised constructor.
	 * @param abstractForm of AbstractActionForm type.
	 * @throws AssignDataException : AssignDataException
	 */
	public DistributionEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an TransferEventParametersForm object to a
	 * TransferEventParameters object.
	 * @param abstractForm - TransferEventParametersForm An TransferEventParametersForm object
	 * containing the information about the TransferEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final TransferEventParametersForm form = (TransferEventParametersForm) abstractForm;
						super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			DistributionEventParameters.logger.error(excp.getMessage(),excp);
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "DistributionEventParameters.java :");
		}
	}
}