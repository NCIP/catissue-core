
package edu.wustl.catissuecore.reportloader;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

/**
 * @author sandeep_ranade
 * Represents a data loader which loads the report data into datastore.
 */

public class ReportLoader
{

	/**
	 * Logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ReportLoader.class);
	/**
	 * Participant of the report.
	 */
	private Participant participant;

	/**
	 *  Source of the report.
	 */
	private final Site site;

	/**
	 * Identified pathology report.
	 */
	private final IdentifiedSurgicalPathologyReport identifiedReport;

	/**
	 * Specimen Collection Group.
	 */
	private SpecimenCollectionGroup scg;

	/**
	 * Surgical Pathology Number for Specimen Collection Group.
	 */
	private String surgicalPathologyNumber;

	/**
	 * Specify default Site Name.
	 */
	//private static final String DEFAULT_SITE_NAME = SiteInfoHandler.getDefaultSiteName();

	/**
	 * Constructor.
	 * @param p participant
	 * @param report identified report
	 * @param s site
	 */
	public ReportLoader(Participant p, IdentifiedSurgicalPathologyReport report, Site s)
	{
		this.participant = p;
		this.identifiedReport = report;
		this.site = s;
	}

	/**
	 * Constructor.
	 * @param participant participant
	 * @param report identified report
	 * @param s site
	 * @param scg specimenCollectionGroup
	 * @param surgicalPathologyNumber surgicalPathologyNumber of report
	 * @throws DAOException DAO Exception
	 */
	public ReportLoader(Participant participant, IdentifiedSurgicalPathologyReport report, Site s,
			SpecimenCollectionGroup scg, String surgicalPathologyNumber) throws DAOException
	{
		this.participant = participant;
		this.participant = (Participant) CaCoreAPIService.getObject(Participant.class,
				Constants.SYSTEM_IDENTIFIER, this.participant.getId());
		this.identifiedReport = report;
		this.site = s;
		this.scg = scg;
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	/**
	 * processes the report data. Validates for participant,site and and report
	 * existance. It also stores data to the data store.
	 * @throws Exception generic exception
	 */
	public void process() throws Exception
	{

		this.logger.debug("Processing Report ");
		this.identifiedReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
		this.identifiedReport.setReportSource(this.site);
		try
		{
			// check for existing specimen collection group(scg)
			if (this.scg == null || this.scg.getId() == 0)
			{
				//check for exactly matching SCG
				/*this.logger.info("Checking for Matching SCG. Default site name:"
						+ DEFAULT_SITE_NAME + " :" + this.site.getName());
				if (this.site.getName().equalsIgnoreCase(DEFAULT_SITE_NAME))
				{
					this.logger.info("Inside if");
					this.scg = (SpecimenCollectionGroup) CaCoreAPIService.getObject(
							SpecimenCollectionGroup.class, "surgicalPathologyNumber",
							this.surgicalPathologyNumber);
				}*/
				if (this.scg == null || this.scg.getId() == 0)
				{
					// if scg is null create new scg
					this.logger.debug("Null SCG found in ReportLoader, Creating New SCG");
					this.scg = this.createNewSpecimenCollectionGroup();
					this.scg = (SpecimenCollectionGroup) CaCoreAPIService.createObject(this.scg);
					this.identifiedReport.setSpecimenCollectionGroup(this.scg);
					CaCoreAPIService.createObject(this.identifiedReport);
				}
				else
				{
					// use existing scg
					this.retrieveAndSetSCG();
				}
			}
			else
			{
				// use existing scg
				this.retrieveAndSetSCG();
			}
			this.logger.info("Processing finished for Report ");
		}
		catch (final Exception ex)
		{
			this.logger.error("Failed to process report "
					+ex.getMessage(),ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Method to create new SCG.
	 * @return scg object of SpecimenColectionGroup
	 * @throws Exception generic exception
	 */
	private SpecimenCollectionGroup createNewSpecimenCollectionGroup() throws Exception
	{
		// set default values for scg
		this.logger.info("Creating New Specimen Collection Group");
		final SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		scg.setCollectionStatus(Constants.COMPLETE);
		scg.setClinicalDiagnosis(Constants.NOT_SPECIFIED);
		scg.setClinicalStatus(Constants.NOT_SPECIFIED);
		scg.setSpecimenCollectionSite(this.site);
		scg.setSurgicalPathologyNumber(this.surgicalPathologyNumber);
		scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
		scg.setName(Constants.REPORT_LOADER_SCG);
		this.identifiedReport.setSpecimenCollectionGroup(scg);

		// Retrieve collection generic protocol
		final CollectionProtocol collectionProtocol = (CollectionProtocol) CaCoreAPIService
				.getObject(CollectionProtocol.class, Constants.COLUMN_NAME_TITLE, CaTIESProperties
						.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
		if (collectionProtocol == null)
		{
			throw new Exception(CaTIESConstants.CP_NOT_FOUND_ERROR_MSG);
		}

		InstanceFactory<CollectionProtocol> cpFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
		CollectionProtocol cp = cpFact.createObject();
		cp.setId(collectionProtocol.getId());
		// retrieve collection protocol event list
		final Collection collProtocolEventList = CaCoreAPIService.getList(
				CollectionProtocolEvent.class, "collectionProtocol", cp);
		final Iterator cpEventIterator = collProtocolEventList.iterator();

		if (cpEventIterator.hasNext())
		{
			final CollectionProtocolEvent collProtocolEvent = (CollectionProtocolEvent) cpEventIterator
					.next();

			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg = this.getExistingCPR(this.participant,
					collectionProtocol);
			if (collProtocolReg == null)
			{
				// otherwise create new CollectionProtocolRegistration
				this.logger.info("Creating New CollectionProtocolRegistration object");
				InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolRegistration.class);
				collProtocolReg = cprInstFact.createObject();
				collProtocolReg.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
				collProtocolReg.setRegistrationDate(new Date());

				InstanceFactory<Participant> instFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
				final Participant transientParticipant = instFact.createObject(); //new Participant();
				transientParticipant.setId(this.participant.getId());

				InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
				final CollectionProtocol transientCP = cpInstFact.createObject();
				transientCP.setId(collectionProtocol.getId());
				transientCP.setTitle(collectionProtocol.getTitle());

				collProtocolReg.setParticipant(transientParticipant);
				collProtocolReg.setCollectionProtocol(transientCP);
				collProtocolReg
						.setProtocolParticipantIdentifier(Constants.REGISTRATION_FOR_REPORT_LOADER);
				try
				{
					collProtocolReg = (CollectionProtocolRegistration) CaCoreAPIService
							.createObject(collProtocolReg);
				}
				catch (final Exception ex)
				{
					this.logger.error("Error: Could not save object"
							+ " of CollectionProtocolRegistration"
							+ex.getMessage(), ex);
					ex.printStackTrace();
					throw new Exception("Could not save object of"
							+ " CollectionProtocolRegistration :" + ex.getMessage());
				}
			}
			scg.setCollectionProtocolEvent(collProtocolEvent);
			scg.setCollectionProtocolRegistration(collProtocolReg);
			scg.setBarcode(Constants.REPORT_LOADER_SCG);
		}
		else
		{
			this.logger.error("Associated Collection Protocol Event not found for "
					+ CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
			throw new Exception("Associated Collection Protocol Event not found for "
					+ CaTIESProperties.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
		}

		return scg;
	}

	/**
	 * Method to check for existing collectionProtocolRegistration.
	 * @param participant object of Participant
	 * @param cp CollectionProtocol.
	 * @return object of CollectionProtocolRegistration
	 * @throws Exception generic exception
	 */
	private CollectionProtocolRegistration getExistingCPR(Participant participant,
			CollectionProtocol cp) throws Exception
	{
		List<CollectionProtocolRegistration> cprList = null;

		// retrive CollectionProtocolRegistration with current participant and collectionProtocol
		String hqlQuery = "from CollectionProtocolRegistration obj where participant.id = "+participant.getId()+" and collectionProtocol.id="+cp.getId();
		final HQLCriteria hqlCriteria = new HQLCriteria(hqlQuery);

		try
		{
			cprList = CaCoreAPIService.executeQuery(hqlCriteria.getHqlString(),
					CollectionProtocolRegistration.class);
			// check for existence
			if (cprList != null && cprList.size() > 0)
			{
				// cpr exist then return existing cpr
				this.logger.info("Existing CPR found for participant id=" + participant.getId()
						+ " collectionProtocol id=" + cp.getId());
				final CollectionProtocolRegistration collProtocolReg = (CollectionProtocolRegistration) cprList
						.get(0);
				collProtocolReg.setParticipant(participant);
				collProtocolReg.setCollectionProtocol(cp);
				return collProtocolReg;
			}
		}
		catch (final ApplicationException appEx)
		{
			this.logger.error("Error while retrieving List for "
					+ CollectionProtocolRegistration.class.getName()
					+ appEx.getMessage(),appEx);
			appEx.printStackTrace();
		}
		this.logger.info("No Existing CPR found for participant id=" + participant.getId()
				+ " collectionProtocol id=" + cp.getId());
		return null;
	}


	/**
	 * This method retrieve And Set SCG.
	 * @throws Exception Exception
	 */
	private void retrieveAndSetSCG() throws Exception
	{

		this.scg = (SpecimenCollectionGroup) CaCoreAPIService.getObject(
				SpecimenCollectionGroup.class, Constants.SYSTEM_IDENTIFIER, this.scg.getId());
		if (this.scg.getSpecimenCollectionSite() == null)
		{
			this.scg.setSpecimenCollectionSite(this.site);
		}
		final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) CaCoreAPIService
				.getObject(CollectionProtocolRegistration.class, Constants.SYSTEM_IDENTIFIER,
						this.scg.getCollectionProtocolRegistration().getId());
		this.scg.setCollectionProtocolRegistration(collectionProtocolRegistration);
		// set identified report
		if (this.scg.getIdentifiedSurgicalPathologyReport() != null)
		{

			this.logger.info("inside" + this.scg.getIdentifiedSurgicalPathologyReport().getId());

			IdentifiedSurgicalPathologyReport existingReport = (IdentifiedSurgicalPathologyReport) CaCoreAPIService
					.getObject(IdentifiedSurgicalPathologyReport.class,
							Constants.SYSTEM_IDENTIFIER, this.scg
									.getIdentifiedSurgicalPathologyReport().getId());
			DeidentifiedSurgicalPathologyReport existingDeidReport = null;
			if (this.scg.getDeIdentifiedSurgicalPathologyReport() != null)
			{
				existingDeidReport = (DeidentifiedSurgicalPathologyReport) CaCoreAPIService
						.getObject(DeidentifiedSurgicalPathologyReport.class,
								Constants.SYSTEM_IDENTIFIER, this.scg
										.getDeIdentifiedSurgicalPathologyReport().getId());
				existingReport.setDeIdentifiedSurgicalPathologyReport(null);
			}
			existingReport.getTextContent().setData(
					this.identifiedReport.getTextContent().getData());

			try
			{
				existingReport = (IdentifiedSurgicalPathologyReport) CaCoreAPIService
						.updateObject(existingReport);
				this.logger.info("existingReport updated:" + existingReport.getId());
				if (existingDeidReport != null)
				{
					existingDeidReport.setSpecimenCollectionGroup(null);
					final DeidentifiedSurgicalPathologyReport deidreport = (DeidentifiedSurgicalPathologyReport) CaCoreAPIService
							.updateObject(existingDeidReport);
					this.logger.info("deid report updated: " + deidreport.getId());
				}
			}
			catch (final ApplicationException ex)
			{
				this.logger.error("Error while updating old report!"
						+ex.getMessage(), ex);
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
		}
		else
		{
			this.logger.info("No associated report with SCG");
			this.scg.setIdentifiedSurgicalPathologyReport(this.identifiedReport);
			this.identifiedReport.setSpecimenCollectionGroup(this.scg);
			CaCoreAPIService.createObject(this.identifiedReport);
		}
		this.scg.setSurgicalPathologyNumber(this.surgicalPathologyNumber);
		CaCoreAPIService.updateObject(this.scg);
	}
}