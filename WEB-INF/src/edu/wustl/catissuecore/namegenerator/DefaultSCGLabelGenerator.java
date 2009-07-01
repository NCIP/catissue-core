package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * This  class contains the default implementation for SpecimenCollectionGroup Label generation.
 * @author Abhijit_Naik
 */
public class DefaultSCGLabelGenerator implements LabelGenerator
{
	/**
	 * logger Generic logger.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(DefaultSCGLabelGenerator.class);
	/**
	 * Current label.
	 */
	protected Long currentLabel;
	/**
	 * Datasource Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor.
	 */
	public DefaultSCGLabelGenerator()
	{
		super();
		init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		currentLabel = getLastAvailableSCGLabel(null);
	}
	/**
	 * @param databaseConstant databaseConstant
	 * @return noOfRecords
	 */
	private Long getLastAvailableSCGLabel(String databaseConstant)
	{
		Long noOfRecords = new Long("0");
		String sql = "Select max(IDENTIFIER) as MAX_IDENTIFIER from CATISSUE_ABS_SPECI_COLL_GROUP";
		noOfRecords=AppUtility.getLastAvailableValue(sql);
		return noOfRecords;
	}
	/**
	 * Set label.
	 * @param obj SCG barcode
	 */
	public void setLabel(Object obj)
	{
		SpecimenCollectionGroup scg = (SpecimenCollectionGroup)obj;
		CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
		CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
		long participantId = cpr.getParticipant().getId();
		String cpTitle=collectionProtocol.getTitle();
		String maxCollTitle = cpTitle;
		if(cpTitle.length()>Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
		{
			maxCollTitle = cpTitle.substring(0,Constants.COLLECTION_PROTOCOL_TITLE_LENGTH-1);
		}
		currentLabel++;
		scg.setName(maxCollTitle+"_"+participantId+"_"+currentLabel);
	}

	/**
	 * Setting label.
	 * @param objSpecimenList scg obj list
	 */
	public void setLabel(List objSpecimenList)
	{
		//Not required in case of SCG -As only individual SCG will be labelled.
	}
	/**
	 * Returns label for the given domain object.
	 * @return scg name
	 * @param obj SCG Object
	 */
	public String getLabel(Object obj)
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)obj;
		setLabel(specimenCollectionGroup);
		return (specimenCollectionGroup.getName());
	}
}
