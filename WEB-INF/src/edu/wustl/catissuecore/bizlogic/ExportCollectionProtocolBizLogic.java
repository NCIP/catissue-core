/**
 * <p>Title: InstitutionBizLogic Class>
 * <p>Description:	InstitutionBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Sep 19, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.CollectionProtocolEventComparator;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.SpecimenRequirementComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author
 *
 */
public class ExportCollectionProtocolBizLogic extends CatissueDefaultBizLogic
{

	DAO dao = null;
	private transient final Logger logger = Logger
			.getCommonLogger(ExportCollectionProtocolBizLogic.class);

	public StringBuffer getCPXMLFile(String title) throws BizLogicException
	{
		StringBuffer csvFile = null;
		final IDAOFactory daofactory = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName());
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			csvFile = getCSV(getCollectionProtocol(title));

		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		finally
		{
			try
			{
				if (dao != null)
					dao.closeSession();
			}
			catch (final DAOException e)
			{
				logger.error(e.getMessage(), e);
				throw getBizLogicException(e, e.getErrorKeyName(),
						e.getMsgValues());
			}
		}
		return csvFile;
	}

	/**
	 * @param collectionProtocol
	 * @return
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	private CollectionProtocol getCollectionProtocol(String title)
			throws DAOException
	{
		CollectionProtocol collectionProtocol;
		ColumnValueBean columnValueBean = new ColumnValueBean("title", title);
		List<CollectionProtocol> collectionProtocols = (List<CollectionProtocol>) dao
				.retrieve(CollectionProtocol.class.getName(), columnValueBean);
		collectionProtocol = collectionProtocols.get(0);

		return collectionProtocol;
	}

	private StringBuffer getCSV(CollectionProtocol collectionProtocol)
			throws BizLogicException
	{
		StringBuffer headerString = new StringBuffer();
		StringBuffer valueString = new StringBuffer();

		headerString.append("Principal Investigator").append(",");
		valueString
				.append("\"")
				.append(collectionProtocol.getPrincipalInvestigator()
						.getLoginName()).append("\"").append(",");
		//headerDataMap.put("Principal Investigator", collectionProtocol.getPrincipalInvestigator().getLoginName());
		Collection<User> coordinatorUsers = collectionProtocol
				.getCoordinatorCollection();
		int coordinatorCount = 1;
		for (User user : coordinatorUsers)
		{
			headerString.append("Principal cordinator#")
					.append(coordinatorCount++).append(",");
			valueString.append("\"").append(user.getLoginName()).append("\"")
					.append(",");
		}

		//headerDataMap.put("Title",collectionProtocol.getTitle());
		headerString.append("Title").append(",");
		valueString.append("\"").append(collectionProtocol.getTitle())
				.append("\"").append(",");

		//headerDataMap.put("STitle",collectionProtocol.getShortTitle());
		headerString.append("Short Title").append(",");
		valueString.append("\"").append(collectionProtocol.getShortTitle())
				.append("\"").append(",");

		if (collectionProtocol.getIrbIdentifier() != null)
		{
			//headerDataMap.put("IRB",collectionProtocol.getIrbIdentifier());
			headerString.append("IRB").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getIrbIdentifier()).append("\"")
					.append(",");
		}
		//headerDataMap.put("Date",getDateAsString(collectionProtocol.getStartDate()));
		headerString.append("Date").append(",");
		valueString.append("\"")
				.append(getDateAsString(collectionProtocol.getStartDate()))
				.append("\"").append(",");

		//headerDataMap.put("URL",collectionProtocol.getDescriptionURL());
		if (collectionProtocol.getDescriptionURL() != null
				&& !"".equals(collectionProtocol.getDescriptionURL()))
		{
			headerString.append("URL").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getDescriptionURL())
					.append("\"").append(",");
		}

		//headerDataMap.put("Activity Status",collectionProtocol.getActivityStatus());
		headerString.append("Activity Status").append(",");
		valueString.append("\"").append(collectionProtocol.getActivityStatus())
				.append("\"").append(",");
		if (collectionProtocol.getConsentsWaived() != null)
		{
			//headerDataMap.put("Waived",collectionProtocol.getConsentsWaived().toString());
			headerString.append("Waived").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getConsentsWaived().toString())
					.append("\"").append(",");
		}
		//headerDataMap.put("Aliquot In Same Container",collectionProtocol.getAliquotInSameContainer().toString());
		headerString.append("Aliquot In Same Container").append(",");
		valueString
				.append("\"")
				.append(collectionProtocol.getAliquotInSameContainer()
						.toString()).append("\"").append(",");

		if (collectionProtocol.getEnrollment() != null)
		{
			//headerDataMap.put("Enrollment",collectionProtocol.getEnrollment().toString());
			headerString.append("Enrollment").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getEnrollment().toString())
					.append("\"").append(",");
		}

		Collection<ConsentTier> consentTiers = collectionProtocol
				.getConsentTierCollection();
		int consentTiersCount = 1;
		Comparator consentTierComparator = new IdComparator();
		List<ConsentTier> consentTiersAsList = new ArrayList(consentTiers);
		Collections.sort(consentTiersAsList, consentTierComparator);

		for (ConsentTier consentTier : consentTiersAsList)
		{
			//headerDataMap.put("Statements#"+consentTiersCount++,consentTier.getStatement());
			headerString.append("Statements#").append(consentTiersCount++)
					.append(",");
			valueString.append("\"").append(consentTier.getStatement())
					.append("\"").append(",");

		}
		Collection<ClinicalDiagnosis> clinicalDiagnosis = collectionProtocol
				.getClinicalDiagnosisCollection();
		int clinicalDiagnosisCount = 1;
		for (ClinicalDiagnosis cd : clinicalDiagnosis)
		{
			//headerDataMap.put("Clinical Diagnosis#"+clinicalDiagnosisCount++,cd.getName());
			headerString.append("Clinical Diagnosis#")
					.append(clinicalDiagnosisCount++).append(",");
			valueString.append("\"").append(cd.getName()).append("\"")
					.append(",");
		}

		if (collectionProtocol.getSpecimenLabelFormat() != null)
		{
			headerString.append("Parent Specimen Label Format").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getSpecimenLabelFormat())
					.append("\"").append(",");
		}

		if (collectionProtocol.getDerivativeLabelFormat() != null)
		{
			headerString.append("Derivative Specimen Label Format").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getDerivativeLabelFormat())
					.append("\"").append(",");
		}

		if (collectionProtocol.getAliquotLabelFormat() != null)
		{
			headerString.append("Aliquot Specimen Label Format").append(",");
			valueString.append("\"")
					.append(collectionProtocol.getAliquotLabelFormat())
					.append("\"").append(",");
		}

		Collection<CollectionProtocolEvent> events = collectionProtocol
				.getCollectionProtocolEventCollection();
		List<CollectionProtocolEvent> eventsList = new ArrayList(events);
		CollectionProtocolUtil.getSortedCPEventList(eventsList);
		int eventsCount = 1;
		for (CollectionProtocolEvent collectionProtocolEvent : eventsList)
		{
			if (!Constants.DISABLE.equalsIgnoreCase(collectionProtocolEvent
					.getActivityStatus()))
			{
				//headerDataMap.put("Study Calender Event Point#"+eventsCount,collectionProtocolEvent.getStudyCalendarEventPoint().toString());
				headerString.append("Study Calender Event Point#")
						.append(eventsCount).append(",");
				valueString
						.append("\"")
						.append(collectionProtocolEvent
								.getStudyCalendarEventPoint().toString())
						.append("\"").append(",");

				//headerDataMap.put("CPL#"+eventsCount,collectionProtocolEvent.getCollectionPointLabel());
				headerString.append("Collection Point Label#")
						.append(eventsCount).append(",");
				valueString
						.append("\"")
						.append(collectionProtocolEvent
								.getCollectionPointLabel()).append("\"")
						.append(",");

				//headerDataMap.put("CS#"+eventsCount,collectionProtocolEvent.getClinicalStatus());
				headerString.append("Clinical Status#").append(eventsCount)
						.append(",");
				valueString.append("\"")
						.append(collectionProtocolEvent.getClinicalStatus())
						.append("\"").append(",");

				headerString.append("Clinical Diagnosis For Event#")
						.append(eventsCount).append(",");
				valueString.append("\"")
						.append(collectionProtocolEvent.getClinicalDiagnosis())
						.append("\"").append(",");

				if (collectionProtocolEvent.getDefaultSite() != null)
				{
					headerString.append("Site#").append(eventsCount)
							.append(",");
					valueString
							.append("\"")
							.append(collectionProtocolEvent.getDefaultSite()
									.getName()).append("\"").append(",");
				}

				//headerDataMap.put("ActivityStatus#"+eventsCount,collectionProtocolEvent.getActivityStatus());
				headerString.append("ActivityStatus#").append(eventsCount)
						.append(",");
				valueString.append("\"")
						.append(collectionProtocolEvent.getActivityStatus())
						.append("\"").append(",");
				Collection<SpecimenRequirement> requirements = collectionProtocolEvent
						.getSpecimenRequirementCollection();

				final SpecimenRequirementComparator sprComparator = new SpecimenRequirementComparator();
				List<SpecimenRequirement> requirementsList = new ArrayList(
						requirements);
				Collections.sort(requirementsList, sprComparator);

				int specimenRequirementCount = 1;
				for (SpecimenRequirement specimenRequirement : requirementsList)
				{
					if (specimenRequirement.getParentSpecimen() == null
							&& !Constants.DISABLED
									.equalsIgnoreCase(specimenRequirement
											.getActivityStatus()))
					{
						if (specimenRequirement.getSpecimenRequirementLabel() != null)
						{
							headerString.append("Specimen Requirement Title#")
									.append(eventsCount).append("#")
									.append(specimenRequirementCount)
									.append(",");
							valueString
									.append("\"")
									.append(specimenRequirement
											.getSpecimenRequirementLabel())
									.append("\"").append(",");
						}
						//headerDataMap.put("Specimen Class"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenClass());
						headerString.append("Specimen Class#")
								.append(eventsCount).append("#")
								.append(specimenRequirementCount).append(",");
						valueString.append("\"")
								.append(specimenRequirement.getSpecimenClass())
								.append("\"").append(",");

						//headerDataMap.put("Specimen Type"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenType());
						headerString.append("Specimen Type#")
								.append(eventsCount).append("#")
								.append(specimenRequirementCount).append(",");
						valueString.append("\"")
								.append(specimenRequirement.getSpecimenType())
								.append("\"").append(",");

						//headerDataMap.put("Storage Location"+postfix+"#"+childSpecimenCount,((SpecimenRequirement)abstractSpecimen).getStorageType());
						headerString.append("Storage Location#")
								.append(eventsCount).append("#")
								.append(specimenRequirementCount).append(",");
						valueString.append("\"")
								.append(specimenRequirement.getStorageType())
								.append("\"").append(",");

						//headerDataMap.put("Pathological Status"+postfix+"#"+childSpecimenCount,abstractSpecimen.getPathologicalStatus());
						headerString.append("Pathological Status#")
								.append(eventsCount).append("#")
								.append(specimenRequirementCount).append(",");
						valueString
								.append("\"")
								.append(specimenRequirement
										.getPathologicalStatus()).append("\"")
								.append(",");

						//headerDataMap.put("Initial Quantity"+postfix+"#"+childSpecimenCount,abstractSpecimen.getInitialQuantity().toString());
						headerString.append("Initial Quantity#")
								.append(eventsCount).append("#")
								.append(specimenRequirementCount).append(",");
						valueString
								.append("\"")
								.append(specimenRequirement
										.getInitialQuantity().toString())
								.append("\"").append(",");

						//headerDataMap.put("lineage"+postfix+"#"+childSpecimenCount,abstractSpecimen.getLineage());
						headerString.append("Lineage#").append(eventsCount)
								.append("#").append(specimenRequirementCount)
								.append(",");
						valueString.append("\"")
								.append(specimenRequirement.getLineage())
								.append("\"").append(",");

						//headerDataMap.put("Tissue Site"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSite());
						headerString.append("Tissue Site#").append(eventsCount)
								.append("#").append(specimenRequirementCount)
								.append(",");
						valueString
								.append("\"")
								.append(specimenRequirement
										.getSpecimenCharacteristics()
										.getTissueSite()).append("\"")
								.append(",");

						//headerDataMap.put("Tissue Side"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSide());
						headerString.append("Tissue Side#").append(eventsCount)
								.append("#").append(specimenRequirementCount)
								.append(",");
						valueString
								.append("\"")
								.append(specimenRequirement
										.getSpecimenCharacteristics()
										.getTissueSide()).append("\"")
								.append(",");

						if (specimenRequirement.getLabelFormat() != null)
						{
							headerString.append("Label Format#")
									.append(eventsCount).append("#")
									.append(specimenRequirementCount)
									.append(",");
							valueString
									.append("\"")
									.append(specimenRequirement
											.getLabelFormat()).append("\"")
									.append(",");
						}
						appendSpecimenEventData(
								specimenRequirement
										.getSpecimenEventCollection(),
								"#" + eventsCount + "#"
										+ specimenRequirementCount,
								headerString, valueString);
						updateMapForChildSpecimen(headerString, valueString,
								specimenRequirement
										.getChildSpecimenCollection(), "#"
										+ eventsCount + "#"
										+ specimenRequirementCount);
						specimenRequirementCount++;
					}
				}
			}
			eventsCount++;
		}
		headerString = headerString.replace(headerString.lastIndexOf(","),
				headerString.lastIndexOf(",") + 1, "");
		valueString = valueString.replace(valueString.lastIndexOf(","),
				valueString.lastIndexOf(",") + 1, "");
		return headerString.append("\n").append(valueString);//writeCSVFile(collectionProtocol.getShortTitle(),headerString,valueString);
	}

	private String getDateAsString(Date date)
	{
		DateFormat formatter = new SimpleDateFormat(CommonServiceLocator
				.getInstance().getDatePattern());
		String dateString = formatter.format(date);
		return dateString;
	}

	private void updateMapForChildSpecimen(StringBuffer headerString,
			StringBuffer valueString, Collection specimenCollection,
			String postfix)
	{
		final SpecimenRequirementComparator sprComparator = new SpecimenRequirementComparator();
		List<SpecimenRequirement> requirementsList = new ArrayList(
				specimenCollection);
		Collections.sort(requirementsList, sprComparator);

		int childSpecimenCount = 1;
		for (Object abstractSpecimen : requirementsList)
		{
			if (!Constants.DISABLE
					.equalsIgnoreCase(((SpecimenRequirement) abstractSpecimen)
							.getActivityStatus()))
			{
				if (((SpecimenRequirement) abstractSpecimen)
						.getSpecimenRequirementLabel() != null)
				{
					headerString.append("Specimen Requirement Title")
							.append(postfix).append("#")
							.append(childSpecimenCount).append(",");
					valueString
							.append("\"")
							.append(((SpecimenRequirement) abstractSpecimen)
									.getSpecimenRequirementLabel())
							.append("\"").append(",");
				}
				//headerDataMap.put("Specimen Class"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenClass());
				headerString.append("Specimen Class").append(postfix)
						.append("#").append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getSpecimenClass()).append("\"").append(",");

				//headerDataMap.put("Specimen Type"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenType());
				headerString.append("Specimen Type").append(postfix)
						.append("#").append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getSpecimenType()).append("\"").append(",");

				//headerDataMap.put("Storage Location"+postfix+"#"+childSpecimenCount,((SpecimenRequirement)abstractSpecimen).getStorageType());
				headerString.append("Storage Location").append(postfix)
						.append("#").append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getStorageType()).append("\"").append(",");

				//headerDataMap.put("Pathological Status"+postfix+"#"+childSpecimenCount,abstractSpecimen.getPathologicalStatus());
				headerString.append("Pathological Status").append(postfix)
						.append("#").append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getPathologicalStatus()).append("\"")
						.append(",");

				//headerDataMap.put("Initial Quantity"+postfix+"#"+childSpecimenCount,abstractSpecimen.getInitialQuantity().toString());
				headerString.append("Initial Quantity").append(postfix)
						.append("#").append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getInitialQuantity().toString()).append("\"")
						.append(",");

				//headerDataMap.put("lineage"+postfix+"#"+childSpecimenCount,abstractSpecimen.getLineage());
				headerString.append("Lineage").append(postfix).append("#")
						.append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getLineage()).append("\"").append(",");

				//headerDataMap.put("Tissue Site"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSite());
				headerString.append("Tissue Site").append(postfix).append("#")
						.append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getSpecimenCharacteristics().getTissueSite())
						.append("\"").append(",");

				//headerDataMap.put("Tissue Side"+postfix+"#"+childSpecimenCount,abstractSpecimen.getSpecimenCharacteristics().getTissueSide());
				headerString.append("Tissue Side").append(postfix).append("#")
						.append(childSpecimenCount).append(",");
				valueString
						.append("\"")
						.append(((SpecimenRequirement) abstractSpecimen)
								.getSpecimenCharacteristics().getTissueSide())
						.append("\"").append(",");

				if (((SpecimenRequirement) abstractSpecimen).getLabelFormat() != null)
				{
					headerString.append("Label Format").append(postfix)
							.append("#").append(childSpecimenCount).append(",");
					valueString
							.append("\"")
							.append(((SpecimenRequirement) abstractSpecimen)
									.getLabelFormat()).append("\"").append(",");
				}
				appendSpecimenEventData(
						((SpecimenRequirement) abstractSpecimen)
								.getSpecimenEventCollection(),
						postfix + "#" + childSpecimenCount, headerString,
						valueString);
				updateMapForChildSpecimen(headerString, valueString,
						((SpecimenRequirement) abstractSpecimen)
								.getChildSpecimenCollection(), postfix + "#"
								+ childSpecimenCount);
			}
			childSpecimenCount++;
		}
	}

	private void appendSpecimenEventData(
			Collection<SpecimenEventParameters> specimenEventParameters,
			String prefixString, StringBuffer headerString,
			StringBuffer valueString)
	{
		for (SpecimenEventParameters specimenEventParameter : specimenEventParameters)
		{
			if (specimenEventParameter instanceof CollectionEventParameters)
			{
				CollectionEventParameters collectionEventParameters = ((CollectionEventParameters) specimenEventParameter);

				headerString.append("Collector").append(prefixString)
						.append("#1,");
				valueString.append("\"")
						.append(collectionEventParameters.getUser().getId())
						.append("\"").append(",");

				headerString.append("Collection Procedure")
						.append(prefixString).append("#1,");
				valueString
						.append("\"")
						.append(collectionEventParameters
								.getCollectionProcedure()).append("\"")
						.append(",");

				headerString.append("Collection Container")
						.append(prefixString).append("#1,");
				valueString.append("\"")
						.append(collectionEventParameters.getContainer())
						.append("\"").append(",");
			}
			else if (specimenEventParameter instanceof ReceivedEventParameters)
			{
				ReceivedEventParameters receivedEventParameters = ((ReceivedEventParameters) specimenEventParameter);

				headerString.append("Receiver").append(prefixString)
						.append("#1,");
				valueString.append("\"")
						.append(receivedEventParameters.getUser().getId())
						.append("\"").append(",");

				headerString.append("Received Quality").append(prefixString)
						.append("#1,");
				valueString.append("\"")
						.append(receivedEventParameters.getReceivedQuality())
						.append("\"").append(",");
			}
		}
	}
}