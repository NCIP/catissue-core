
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
/**
 * This  class contains the default implementation for SpecimenCollectionGroup Label generation.
 * @author Abhijit_Naik
 */
public class DefaultSCGLabelGenerator implements LabelGenerator
{

	/**
	 * Current label.
	 */
	protected Long currentLabel;

	/**
	 * Data source Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSCGLabelGenerator() throws ApplicationException
	{
		super();
		this.init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Database Name and then set function name that will convert
	 * label from integer to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		this.currentLabel = this.getLastAvailableSCGLabel(null);
	}

	/**
	 * @param databaseConstant databaseConstant
	 * @return noOfRecords
	 * @throws ApplicationException Application Exception
	 */
	private Long getLastAvailableSCGLabel(String databaseConstant) throws ApplicationException
	{
		Long noOfRecords = new Long("0");
		final String sql = "Select max(IDENTIFIER) as MAX_IDENTIFIER from CATISSUE_ABS_SPECI_COLL_GROUP";
		noOfRecords = AppUtility.getLastAvailableValue(sql);
		return noOfRecords;
	}

	/**
	 * Set label.
	 * @param obj SCG bar code
	 */
	public void setLabel(Object obj)
	{
		final SpecimenCollectionGroup scg = (SpecimenCollectionGroup) obj;
		final CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
		final CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
		final long participantId = cpr.getParticipant().getId();
		final String cpTitle = collectionProtocol.getTitle();
		String maxCollTitle = cpTitle;
		if (cpTitle.length() > Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
		{
			maxCollTitle = cpTitle.substring(0, Constants.COLLECTION_PROTOCOL_TITLE_LENGTH - 1);
		}
		this.currentLabel++;
		scg.setName(maxCollTitle + "_" + participantId + "_" + this.currentLabel);
	}

	/**
	 * Setting label.
	 * @param objSpecimenList SCG object list
	 */
	public void setLabel(List objSpecimenList)
	{
		//Not required in case of SCG -As only individual SCG will be labeled.
	}

	/**
	 * Returns label for the given domain object.
	 * @return SCG name
	 * @param obj SCG Object
	 */
	public String getLabel(Object obj)
	{
		final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) obj;
		this.setLabel(specimenCollectionGroup);
		return specimenCollectionGroup.getName();
	}
}
