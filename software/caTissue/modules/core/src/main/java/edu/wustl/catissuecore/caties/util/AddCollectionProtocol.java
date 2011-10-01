package edu.wustl.catissuecore.caties.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * Program to add default Collection Protocol for caTIES.
 */
public class AddCollectionProtocol {
	static {
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * logger.
	 */
	private static Logger logger = Logger
			.getCommonLogger(AddCollectionProtocol.class);

	/**
	 * Default entry point of program.
	 *
	 * @param args
	 *            command line arguments list
	 */
	public static void main(String[] args) {
		try {
			//ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider();
			Utility.init();
			CaCoreAPIService.initialize();

			Site site = (Site) CaCoreAPIService
					.getObject(
							Site.class,
							"name",
							CaTIESProperties
									.getValue(CaTIESConstants.SITE_NAME_FROM_PROPERTIES));
			if (site == null) {
				logger.info("*** Add Site...");
				site = initSite();
				site = (Site) CaCoreAPIService.createObject(site);
				logger.info("*** Site " + site.getName()
						+ " added to system...");
			} else {
				logger.info("*** Using existing site " + site.getName()
						+ " ...");
			}

			logger.info("*** Add Collection protocol...");
			final CollectionProtocol collectionProtocol = initCollectionProtocol(site);
			CaCoreAPIService.createObject(collectionProtocol);
			logger.info("*** Default Collection protocol added to system...");
		} catch (final Exception ex) {
			AddCollectionProtocol.logger.error(
					"Exception in AddCollectionProtocol = " + ex.getMessage(),
					ex);
		}
	}

	/**
	 * Method to instantiate and initialize values for collection protocol.
	 *
	 * @return collectionProtocol object of CollectionProtocol
	 * @param site
	 *            : site
	 * @throws Exception
	 *             Generic exception occured
	 */
	public static CollectionProtocol initCollectionProtocol(Site site)
			throws Exception {
		InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory
				.getInstanceFactory(CollectionProtocol.class);
		final CollectionProtocol collectionProtocol = cpInstFact.createObject();

		collectionProtocol.setAliquotInSameContainer(new Boolean(false));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
				.toString());

		collectionProtocol.setTitle(CaTIESProperties
				.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));
		collectionProtocol.setShortTitle(CaTIESProperties
				.getValue(CaTIESConstants.COLLECTION_PROTOCOL_TITLE));

		collectionProtocol.setStartDate(CommonUtilities.parseDate("08/15/2003",
				CommonUtilities.datePattern("08/15/1975")));

		final Collection<CollectionProtocolEvent> collectionProtocolEventList = new LinkedHashSet();
		final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) DomainInstanceFactory
				.getInstanceFactory(CollectionProtocolEvent.class)
				.createObject();
		setCollectionProtocolEvent(collectionProtocolEvent);

		collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
		collectionProtocolEventList.add(collectionProtocolEvent);

		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventList);
		InstanceFactory<User> instFact = DomainInstanceFactory
				.getInstanceFactory(User.class);
		final User principleInvestigator = instFact.createObject();
		principleInvestigator.setId(new Long(1));
		collectionProtocol.setPrincipalInvestigator(principleInvestigator);

		/** Set site to CP **/
		final Set<Site> siteCollection = new HashSet<Site>();
		siteCollection.add(site);

		final Collection<CollectionProtocol> cpCollection = site
				.getCollectionProtocolCollection();
		cpCollection.add(collectionProtocol);

		collectionProtocol.setSiteCollection(siteCollection);

		return collectionProtocol;
	}

	/**
	 * Method to create collection protocol event.
	 *
	 * @param collectionProtocolEvent
	 *            : collectionProtocolEvent
	 * @throws Exception
	 *             Generic exception occured
	 */
	private static void setCollectionProtocolEvent(
			CollectionProtocolEvent collectionProtocolEvent) throws Exception {
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));
		collectionProtocolEvent
				.setCollectionPointLabel("Collection Point Label 1");
		collectionProtocolEvent.setClinicalStatus(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_CLINICAL_STATUS));
		collectionProtocolEvent.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
				.toString());
		collectionProtocolEvent.setClinicalDiagnosis(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_CLINICAL_DIAGNOSIS));

		Collection specimenCollection = null;
		final CollectionProtocolEventBean cpEventBean = new CollectionProtocolEventBean();
		final SpecimenRequirementBean specimenRequirementBean = createSpecimenBean();

		cpEventBean.addSpecimenRequirementBean(specimenRequirementBean);
		final Map specimenMap = cpEventBean.getSpecimenRequirementbeanMap();
		if (specimenMap != null && !specimenMap.isEmpty()) {
			specimenCollection = edu.wustl.catissuecore.util.CollectionProtocolUtil
					.getReqSpecimens(specimenMap.values(), null,
							collectionProtocolEvent);
		}
		collectionProtocolEvent
				.setSpecimenRequirementCollection(specimenCollection);
	}

	/**
	 * Method to create specimen requirement bean.
	 *
	 * @return specimenRequirementBean object of SpecimenRequirementBean
	 * @throws Exception
	 *             Generic exception occured
	 */
	private static SpecimenRequirementBean createSpecimenBean()
			throws Exception {
		final SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
		specimenRequirementBean.setUniqueIdentifier("E1_S0");
		specimenRequirementBean.setDisplayName("Specimen_E1_S0");
		specimenRequirementBean.setLineage("New");
		specimenRequirementBean.setClassName(Constants.TISSUE);
		specimenRequirementBean.setType(Constants.FIXED_TISSUE);
		specimenRequirementBean.setTissueSide(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_TISSUE_SIDE));
		specimenRequirementBean.setTissueSite(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_TISSUE_SITE));
		specimenRequirementBean.setPathologicalStatus(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_PATHOLOGICAL_STATUS));
		specimenRequirementBean.setQuantity("0");
		specimenRequirementBean.setStorageContainerForSpecimen("Virtual");

		// Collected and received events
		specimenRequirementBean.setCollectionEventUserId(1);
		specimenRequirementBean.setReceivedEventUserId(1);
		specimenRequirementBean.setCollectionEventContainer(CaCoreAPIService
				.getCaTissueServerProperty(Constants.DEFAULT_CONTAINER));
		specimenRequirementBean
				.setReceivedEventReceivedQuality(CaCoreAPIService
						.getCaTissueServerProperty(Constants.DEFAULT_RECEIVED_QUALITY));
		specimenRequirementBean
				.setCollectionEventCollectionProcedure(CaCoreAPIService
						.getCaTissueServerProperty(Constants.DEFAULT_COLLECTION_PROCEDURE));

		specimenRequirementBean.setNoOfDeriveSpecimen(0);
		specimenRequirementBean.setDeriveSpecimen(null);
		return specimenRequirementBean;
	}

	/**
	 * Method to initialize site object.
	 *
	 * @return site Site object
	 * @throws Exception
	 *             : Exception
	 */
	private static Site initSite() throws Exception {
		/*
		 * String name = CaCoreAPIService.getObject(User.class, "emailAddress",
		 * CaTIESProperties
		 * .getValue(CaTIESConstants.USER_NAME)).getClass().getName(); Enhancer
		 * eg = new Enhancer();
		 */
		/*
		 * Proxy pxy = (Proxy)CaCoreAPIService.getObject(User.class,
		 * "emailAddress",
		 * CaTIESProperties.getValue(CaTIESConstants.USER_NAME));
		 */
		/*AopProxy pxy = (AopProxy) CaCoreAPIService.getObject(User.class,
				"emailAddress", CaTIESProperties
						.getValue(CaTIESConstants.USER_NAME));*/
		User user = (User) CaCoreAPIService.getObject(User.class,
				"emailAddress", CaTIESProperties
						.getValue(CaTIESConstants.USER_NAME));
		//User user1 = (User) HibernateMetaData.getProxyObjectImpl(user);
		// net.sf.cglib.proxy.Enhancer.

		InstanceFactory<Site> instFact = DomainInstanceFactory
				.getInstanceFactory(Site.class);
		final Site site = instFact.createObject();// new Site();

		site.setEmailAddress(user.getEmailAddress());
		site.setName(CaTIESProperties
				.getValue(CaTIESConstants.SITE_NAME_FROM_PROPERTIES));
		site.setType("Repository");
		site.setActivityStatus("Active");

		site.setCoordinator(user);

		// final Address address = new Address();
		final Address address = (Address) DomainInstanceFactory
				.getInstanceFactory(Address.class).createObject();
		if (address.getCity() == null) {
			address.setCity(Constants.NOT_SPECIFIED);
		}
		if (address.getStreet() == null) {
			address.setStreet(Constants.NOT_SPECIFIED);
		}
		if (address.getState() == null) {
			address.setState("Alabama");
		}
		if (address.getZipCode() == null) {
			address.setZipCode("00000");
		}
		if (address.getCountry() == null) {
			address.setCountry(CaCoreAPIService
					.getCaTissueServerProperty(Constants.DEFAULT_COUNTRY));
		}
		site.setAddress(address);

		return site;
	}
}
