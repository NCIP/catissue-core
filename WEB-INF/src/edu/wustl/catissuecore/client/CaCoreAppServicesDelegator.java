/**
 * <p>Title: CaCoreAppServicesDelegator Class>
 * <p>Description:	This class contains the basic methods that are required
 * for HTTP APIs. It just passes on the request at proper place.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jan 10, 2006
 */

package edu.wustl.catissuecore.client;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sql.CLOB;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReviewEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;
import edu.wustl.catissuecore.namegenerator.DefaultSCGLabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.ClinPortalCaTissueIntegrationUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.lookup.LookupLogic;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.query.security.QueryCsmCache;
import edu.wustl.query.security.QueryCsmCacheManager;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;

// TODO: Auto-generated Javadoc
/**
 * This class contains the basic methods that are required for HTTP APIs.
 * It just passes on the request at proper place.
 *
 * @author aniruddha_phadnis
 */
public class CaCoreAppServicesDelegator
{

	/** logger. Looger */
	private static transient final Logger LOGGER = Logger
			.getCommonLogger(CaCoreAppServicesDelegator.class);

	/** Static classList which has PHI attributes. */
	private static List<Class> classList = new ArrayList<Class>();
	static
	{
		classList.add(Participant.class);
		classList.add(SpecimenCollectionGroup.class);
		classList.add(Specimen.class);
		classList.add(CollectionProtocolRegistration.class);
		classList.add(IdentifiedSurgicalPathologyReport.class);
		classList.add(DeidentifiedSurgicalPathologyReport.class);
		classList.add(ParticipantMedicalIdentifier.class);
		classList.add(SpecimenEventParameters.class);
		classList.add(SpecimenArrayContent.class);
	}

	/**
	 * Passes User credentials to CaTissueHTTPClient to connect User with caTISSUE Core Application.
	 *
	 * @param userName userName of the User to connect to caTISSUE Core Application
	 * @param password password of the User to connect to caTISSUE Core Application
	 *
	 * @return the sessionID of user if he/she has successfullyy logged in else null
	 *
	 * @throws Exception exception
	 */
	public Boolean delegateLogin(final String userName, String password) throws Exception
	{
		final User validUser = AppUtility.getUser(userName);
		Boolean authenticated = false;
		if (validUser != null)
		{
			password = PasswordManager.encrypt(password);
			final boolean loginOK = SecurityManagerFactory.getSecurityManager().login(userName,
					password);
			authenticated = loginOK;
		}
		return authenticated;
	}

	/**
	 * Disconnects User from caTISSUE Core Application.
	 *
	 * @param sessionKey sessionKey
	 *
	 * @return returns the status of logout to caTISSUE Core Application
	 */
	public boolean delegateLogout(String sessionKey)
	{
		return false;
	}

	/**
	 * Passes caCore Like domain object to  caTissue Core biz logic to perform Add operation.
	 *
	 * @param domainObject the caCore Like object to add using HTTP API
	 * @param userName user name
	 *
	 * @return returns the Added caCore Like
	 * object/Exception object if exception occurs performing Add operation
	 *
	 * @throws Exception exception
	 */
	public Object delegateAdd(String userName, Object domainObject) throws Exception
	{
		try
		{
			this.checkNullObject(domainObject, "Domain Object");
			final IBizLogic bizLogic = this.getBizLogic(domainObject.getClass().getName());
			bizLogic.insert(domainObject, this.getSessionDataBean(userName), 0);
			this.LOGGER.info(" Domain Object has been successfully inserted " + domainObject);
		}
		catch (final Exception e)
		{
			this.LOGGER.error("Delegate Add-->" + e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		return domainObject;
	}

	/**
	 * Passes caCore Like domain object to caTissue Core biz logic to perform Edit operation.
	 *
	 * @param domainObject the caCore Like object to edit using HTTP API
	 * @param userName  user name
	 *
	 * @return returns the Edited caCore Like object/Exception
	 * object if exception occurs performing Edit operation
	 *
	 * @throws Exception exception
	 */
	public Object delegateEdit(String userName, Object domainObject) throws Exception
	{
		DAO dao = null;
		try
		{
			this.checkNullObject(domainObject, "Domain Object");
			final String objectName = domainObject.getClass().getName();
			final IBizLogic bizLogic = this.getBizLogic(objectName);
			final AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
			// not null check for Id
			this.checkNullObject(abstractDomainObject.getId(), "Identifier");
			final Object object = bizLogic.retrieve(objectName, abstractDomainObject.getId());

			if (object == null)
			{
				throw new Exception("No such domain object found for update !! "
						+ "Please enter valid domain object for edit");
			}
			AbstractDomainObject abstractDomainOld = (AbstractDomainObject) object;

			dao = AppUtility.openDAOSession(null);

			abstractDomainOld = (AbstractDomainObject) dao.retrieveById(Class.forName(objectName)
					.getName(), Long.valueOf(abstractDomainObject.getId()));
			bizLogic.update(abstractDomainObject, abstractDomainOld, 0, this
					.getSessionDataBean(userName));

			this.LOGGER.info(" Domain Object has been successfully updated " + domainObject);
		}
		catch (final Exception e)
		{
			this.LOGGER.error("Delegate Edit" + e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		return domainObject;
	}

	/**
	 * Method is modified to allow to delete object of ReportLoaderQueue.
	 * Returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 *
	 * @param domainObject the caCore Like object to delete using HTTP API
	 * @param userName user name
	 *
	 * @return returns Exception object as Delete operation is not supported by CaTissue Core Application.
	 *
	 * @throws Exception exception
	 */
	public Object delegateDelete(String userName, Object domainObject) throws Exception
	{
		if (domainObject instanceof ReportLoaderQueue)
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(domainObject.getClass().getName());
			bizLogic.delete(domainObject, 0);
			return null;
		}
		else
		{
			throw new Exception("caTissue does not support delete");
		}
	}

	/**
	 * Delegate get objects.
	 *
	 * @param userName user name.
	 * @param domainObject domain object
	 *
	 * @return list of objects
	 *
	 * @throws Exception exception
	 */
	public List delegateGetObjects(String userName, Object domainObject) throws Exception
	{
		List searchObjects = new ArrayList();
		this.checkNullObject(domainObject, "Domain Object");
		final String objectName = domainObject.getClass().getName();
		final IBizLogic bizLogic = this.getBizLogic(objectName);
		final AbstractDomainObject abstractDomainObject = (AbstractDomainObject) domainObject;
		// not null check for Id
		this.checkNullObject(abstractDomainObject.getId(), "Identifier");
		searchObjects = bizLogic.retrieve(objectName, Constants.SYSTEM_IDENTIFIER,
				abstractDomainObject.getId());

		if (searchObjects.isEmpty())
		{
			throw new Exception("Please enter valid domain object for search operation!!");
		}
		return this.delegateSearchFilter(userName, searchObjects);
	}

	/**
	 * Check if user if super admin user.
	 *
	 * @param userName userName
	 *
	 * @return isUserisAdmin boolean
	 *
	 * @throws Exception exception
	 */
	private boolean checkIfUserIsAdmin(String userName) throws Exception
	{
		boolean isUserisAdmin = false;
		Logger.out.debug("User Name : " + userName);
		final User validUser = AppUtility.getUser(userName);
		final ISecurityManager securityManager = SecurityManagerFactory.getSecurityManager();
		try
		{
			securityManager.getUserRole(validUser.getCsmUserId());
			if (validUser.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				isUserisAdmin = true;
			}
			// reviewerRole=role.getName();
		}
		catch (final SMException ex)
		{
			this.LOGGER.error("Review Role not found!"+ex.getMessage(),ex);
			ex.printStackTrace();
		}
		return isUserisAdmin;
	}

	/**
	 * check for Maximum records and delegate call to disable and filter.
	 *
	 * @param userName userName
	 * @param list list
	 *
	 * @return filteredObjectList filtered Object
	 *
	 * @throws Exception exception
	 */
	public List delegateSearchFilter(String userName, List list) throws Exception
	{
		final int maxRecordsPerQuery = Integer.parseInt(XMLPropertyHandler
				.getValue(Constants.MAX_RECORDS_PER_CACORE_QUERY_ALLOWED));
		if (list.size() > maxRecordsPerQuery)
		{
			throw new Exception("The result size is greater than allowed maximum recors per query.");
		}
		final boolean isUserisAdmin = this.checkIfUserIsAdmin(userName);
		final List filteredObjectList = this.filterDisabledObjects(list, userName, isUserisAdmin);

		return filteredObjectList;
	}

	/**
	 * Method to filter out AbstractDomain objects of which activityStatus is disabled.
	 *
	 * @param list list
	 * @param userName username
	 * @param isUserisAdmin is admin user
	 *
	 * @return filteredObjects filtered objects
	 *
	 * @throws Exception exception
	 */
	private List filterDisabledObjects(List list, String userName, boolean isUserisAdmin)
			throws Exception
	{
		final long startTime = System.currentTimeMillis();
		final List result = new ArrayList();
		final int resultCount = list.size();

		for (int i = 0; i < resultCount; i++)
		{
			final Object object = list.get(i);
			/**
			 * Chech if object is of type AbstractDomainObject if not add it to final list
			 * else check call getActivityStatus of object and check if value is Disabled.
			 */
			if (object instanceof AbstractDomainObject)
			{
				final AbstractDomainObject abstractDomainObject = (AbstractDomainObject) object;
				try
				{
					final Method activityStatusMethod = abstractDomainObject.getClass().getMethod(
							"getActivityStatus", null);
					final String activityStatus = (String) activityStatusMethod.invoke(
							abstractDomainObject, null);
					if (!Constants.DISABLED.equalsIgnoreCase(activityStatus))
					{
						result.add(abstractDomainObject);
					}
				}
				catch (final Exception ex)
				{
					// Exception occured if Domain Oject is not having ActivityStatus field
					this.LOGGER.error("in CaCoreAppServicesDelegator" + ex.getMessage());
					ex.printStackTrace();
					result.add(abstractDomainObject);
				}
			}
			else if (!isUserisAdmin)
			{
				throw new Exception("caTissue doesnot support queries which returns result "
						+ "list containing instances of classes other than "
						+ "caTissue domain model");
			}
			else
			{
				// Add object to list as it is nit caTissue domain objectFor
				//e.g Admin user queries on get id from domain object
				result.add(object);
			}
		}
		final long endTime = System.currentTimeMillis();
		final long msec = 1000;
		Logger.out.info("Time to filer disable objects:" + (endTime - startTime) / msec);

		List filteredObjects = new ArrayList();
		if (isUserisAdmin)
		{
			filteredObjects = result;
		}
		else
		{
			filteredObjects = this.filterObjects(userName, result);
		}
		return filteredObjects;
	}

	/**
	 * Filters the list of objects returned by the search depending
	 * on the privilege of the user on the objects.
	 * Also sets the identified data to null if the user doesn'r has privilege to see the identified data.
	 *
	 * @param userName The name of the user whose privilege are to be checked.
	 * @param objectList The list of the objects which are to be filtered.
	 *
	 * @return The filtered list of objects according to the privilege of the user.
	 *
	 * @throws Exception exception
	 */
	private List filterObjects(String userName, List objectList) throws Exception
	{
		// boolean that indicates whether user has READ_DENIED privilege on the main object.
		boolean isReadDenied = false;
		// boolean that indicates whether user has privilege on identified data.
		boolean hasPHIAccess = false;
		final List filteredObjects = new ArrayList();
		JDBCDAO jdbcDAO = null;
		try
		{

			jdbcDAO = AppUtility.openJDBCSession();
			Logger.out.debug("In Filter Objects ......");
			final SessionDataBean sessionDataBean = this.getSessionDataBean(userName);
			final QueryCsmCacheManager cacheManager = new QueryCsmCacheManager(jdbcDAO);
			final QueryCsmCache cache = cacheManager.getNewCsmCacheObject();
			Map<String, Boolean> accessPrivilegeMap = new HashMap<String, Boolean>();

			Logger.out.debug("Total Objects>>>>>>>>>>>>>>>>>>>>>" + objectList.size());
			final Iterator iterator = objectList.iterator();
			while (iterator.hasNext())
			{
				final Object abstractDomainObject = iterator.next();
				Class classObject = abstractDomainObject.getClass();
				String objectName = classObject.getName();
				Long identifier = (Long) this.getFieldObject(abstractDomainObject, "id");

				if (classObject.getSuperclass().equals(SpecimenEventParameters.class)
						|| classObject.getSuperclass().equals(ReviewEventParameters.class))
				{
					classObject = SpecimenEventParameters.class;
				}
				else if (classObject.getSuperclass().equals(Specimen.class))
				{
					classObject = Specimen.class;
				}

				/** Check the permission of the user on the main object.
				 *  Call to SecurityManager.checkPermission bypassed &
				 *  instead, call redirected to privilegeCache.hasPrivilege
				 *  Check readDenied permission on particpant,SCG,CPR,Specimen,IdentifiedSPR
				 *  If the user has READ_DENIED privilege on the object,
				 *   remove that object from the list.
				 */

				if (classList.contains(classObject))
				{
					if (classObject.equals(Specimen.class))
					{
						objectName = Specimen.class.getName();
					}
					else if (classObject.equals(SpecimenEventParameters.class))
					{
						final SpecimenEventParameters spe = (SpecimenEventParameters) abstractDomainObject;
						if (spe.getSpecimenCollectionGroup() != null)
						{
							objectName = SpecimenCollectionGroup.class.getName();
							identifier = spe.getSpecimenCollectionGroup().getId();
						}
						else
						{
							objectName = Specimen.class.getName();
							identifier = spe.getSpecimen().getId();
						}
					}
					else if (classObject.equals(ParticipantMedicalIdentifier.class))
					{
						objectName = Participant.class.getName();
						final ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) abstractDomainObject;
						identifier = pmi.getParticipant().getId();
					}
					else if (classObject.equals(SpecimenArrayContent.class))
					{
						objectName = Specimen.class.getName();
						final SpecimenArrayContent sac = (SpecimenArrayContent) abstractDomainObject;
						identifier = sac.getSpecimen().getId();
					}
				}
				accessPrivilegeMap = cacheManager.getAccessPrivilegeMap(objectName, identifier,
						sessionDataBean, cache);
				isReadDenied = accessPrivilegeMap
						.get(edu.wustl.query.util.global.AQConstants.IS_READ_DENIED);
				/**
				 *  In case of no READ_DENIED privilege, check for privilege on identified data.
				 */
				if (!isReadDenied)
				{
					/**Check the permission of the user on the identified data of the object.
					 * Call to SecurityManager.checkPermission bypassed &
					 * instead, call redirected to privilegeCache.hasPrivilege
					 * call remove identified data
					 * If has no read privilege on identified data,
					 *  set the identified attributes as NULL.
					**/
					hasPHIAccess = accessPrivilegeMap
							.get(edu.wustl.query.util.global.AQConstants.HAS_PHI_ACCESS);
					if (!hasPHIAccess)
					{
						this.removeIdentifiedDataFromObject(abstractDomainObject, objectName,
								identifier, sessionDataBean);
					}
					filteredObjects.add(abstractDomainObject);
					Logger.out.debug("Intermediate Size of filteredObjects .............."
							+ filteredObjects.size());
				}

			}
			Logger.out.debug("Before Final Objects >>>>>>>>>>>>>>>>>>>>>>>>>"
					+ filteredObjects.size());
		}
		catch (final Exception exp)
		{
			this.LOGGER.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw exp;
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDAO);
		}
		return filteredObjects;
	}

	/**
	 * Remove identified data from object.
	 *
	 * @param abstractDomainObject abstract domain object
	 * @param objectName object name
	 * @param identifier id
	 * @param sessionDataBean session data bean
	 *
	 * @throws BizLogicException exception
	 */
	private void removeIdentifiedDataFromObject(Object abstractDomainObject, String objectName,
			Long identifier, SessionDataBean sessionDataBean) throws BizLogicException
	{
		Class classObject = abstractDomainObject.getClass();
		if (classObject.getSuperclass().equals(SpecimenEventParameters.class)
				|| classObject.getSuperclass().equals(ReviewEventParameters.class))
		{
			classObject = SpecimenEventParameters.class;
		}
		else if (classObject.getSuperclass().equals(Specimen.class))
		{
			classObject = Specimen.class;
		}
		if (classObject.equals(Participant.class))
		{
			this.removeParticipantIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(ParticipantMedicalIdentifier.class))
		{
			this.removePMIIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(SpecimenCollectionGroup.class))
		{
			this.removeSpecimenCollectionGroupIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(Specimen.class))
		{
			this.removeSpecimenIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(CollectionProtocolRegistration.class))
		{
			this.removeCollectionProtocolRegistrationIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(DeidentifiedSurgicalPathologyReport.class))
		{
			this.removeDeIdentifiedReportIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(IdentifiedSurgicalPathologyReport.class))
		{
			this.removeIdentifiedReportIdentifiedData(abstractDomainObject);
		}
		else if (classObject.equals(SpecimenEventParameters.class))
		{
			this.removeSpecimenEventParameters(abstractDomainObject);
		}
		else if (classObject.equals(SpecimenArrayContent.class))
		{
			this.removeSpecimenArrayContentIdentifiedData(abstractDomainObject);
		}
	}

	/**
	 * Removes the identified data from Participant object.
	 *
	 * @param object The Particpant object.
	 */
	private void removeParticipantIdentifiedData(Object object)
	{
		final Participant participant = (Participant) object;
		participant.setFirstName(null);
		participant.setLastName(null);
		participant.setMiddleName(null);
		participant.setBirthDate(null);
		participant.setDeathDate(null);
		participant.setSocialSecurityNumber(null);

		final Collection<ParticipantMedicalIdentifier> pmiCollection = participant
				.getParticipantMedicalIdentifierCollection();
		if (pmiCollection != null)
		{
			for (final ParticipantMedicalIdentifier participantMedicalIdentifier : pmiCollection)
			{
				final ParticipantMedicalIdentifier participantMedId = participantMedicalIdentifier;
				this.removePMIIdentifiedData(participantMedId);
			}
		}

		final Collection<CollectionProtocolRegistration> cpCollection = participant
				.getCollectionProtocolRegistrationCollection();
		if (cpCollection != null)
		{
			for (final CollectionProtocolRegistration collectionProtocolRegistration : cpCollection)
			{
				final CollectionProtocolRegistration collectionProtReg = collectionProtocolRegistration;
				this.removeCollectionProtocolRegistrationIdentifiedData(collectionProtReg);
			}
		}
	}

	/**
	 * Removes the identified data from SpecimenCollectionGroup object.
	 *
	 * @param object The SpecimenCollectionGroup object.
	 *
	 * @throws DAOException 	 */
	private void removeSpecimenCollectionGroupIdentifiedData(Object object)
	{
		final SpecimenCollectionGroup specimenCollGrp = (SpecimenCollectionGroup) object;
		specimenCollGrp.setSurgicalPathologyNumber(null);
		final CollectionProtocolRegistration cpr = specimenCollGrp
				.getCollectionProtocolRegistration();
		if (cpr != null)
		{
			this.removeCollectionProtocolRegistrationIdentifiedData(cpr);
		}
		final Collection<SpecimenEventParameters> spEvent = specimenCollGrp
				.getSpecimenEventParametersCollection();
		if (spEvent != null)
		{
			final Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
			while (eveItr.hasNext())
			{
				final SpecimenEventParameters spEventParam = eveItr.next();
				this.removeSpecimenEventParameters(spEventParam);
			}
		}
	}

	/**
	 * Removes the identified data from Specimen object.
	 *
	 * @param object The Specimen object.
	 *
	 * @throws DAOException 	 */
	private void removeSpecimenIdentifiedData(Object object)
	{
		final Specimen specimen = (Specimen) object;
		specimen.setCreatedOn(null);
		final SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
		if (scg != null)
		{
			this.removeSpecimenCollectionGroupIdentifiedData(scg);
		}
		final Collection<SpecimenEventParameters> spEvent = specimen.getSpecimenEventCollection();
		if (spEvent != null)
		{
			final Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
			while (eveItr.hasNext())
			{
				final SpecimenEventParameters spEventParam = eveItr.next();
				this.removeSpecimenEventParameters(spEventParam);
			}
		}
	}

	/**
	 * Removes the identified data from CollectionProtocolRegistration object.
	 *
	 * @param object The CollectionProtocolRegistration object.
	 *
	 * @throws DAOException 	 */
	private void removeCollectionProtocolRegistrationIdentifiedData(Object object)
	{
		final CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) object;
		cpr.setRegistrationDate(null);
		cpr.setConsentSignatureDate(null);
		cpr.setSignedConsentDocumentURL(null);
		cpr.setParticipant(null);
	}

	/**
	 * Returns the field object from the class object and field name passed.
	 *
	 * @param object domain object
	 * @param fieldName field name
	 *
	 * @return childObject domain object
	 */
	private Object getFieldObject(Object object, String fieldName)
	{
		Object childObject = null;
		fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		this.LOGGER.debug("Method Name***********************" + fieldName);

		try
		{
			childObject = object.getClass().getMethod(fieldName, null).invoke(object, null);
		}
		catch (final NoSuchMethodException noMetExp)
		{
			this.LOGGER.error(noMetExp.getMessage(), noMetExp);
			noMetExp.printStackTrace();
		}
		catch (final IllegalAccessException illAccExp)
		{
			this.LOGGER.error(illAccExp.getMessage(), illAccExp);
			illAccExp.printStackTrace();
		}
		catch (final InvocationTargetException invoTarExp)
		{
			this.LOGGER.error(invoTarExp.getMessage(), invoTarExp);
			invoTarExp.printStackTrace();
		}

		return childObject;
	}

	/**
	 * Get Biz Logic based on domai object class name.
	 *
	 * @param domainObjectName name of domain object
	 *
	 * @return bizLogic biz logic
	 *
	 * @throws BizLogicException exception
	 */
	private IBizLogic getBizLogic(String domainObjectName) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(domainObjectName);
		return bizLogic;
	}

	/**
	 * Get seesion data bean.
	 *
	 * @param userName user name
	 *
	 * @return session data bean
	 *
	 * @throws Exception exception
	 */
	private SessionDataBean getSessionDataBean(String userName) throws Exception
	{
		// Map<String, SessionDataBean> sessionDataMap = Variables.sessionDataMap;
		SessionDataBean sessionDataBean = null;

		if (Variables.sessionDataMap.containsKey(userName))
		{
			sessionDataBean = Variables.sessionDataMap.get(userName);
			sessionDataBean.setIpAddress("caCore IP issue");
		}
		else
		{
			User user = null;
			sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(userName);

			sessionDataBean.setAdmin(false);
			user = AppUtility.getUser(userName);
			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				sessionDataBean.setAdmin(true);
			}

			sessionDataBean.setUserId(user.getId());
			sessionDataBean.setIpAddress("caCore IP issue");
			Variables.sessionDataMap.put(userName, sessionDataBean);
		}

		return sessionDataBean;
	}

	/**
	 * check whether the object is null or not.
	 *
	 * @param domainObject domain object.
	 * @param messageToken  message token
	 *
	 * @throws Exception exception
	 */
	private void checkNullObject(Object domainObject, String messageToken) throws Exception
	{
		if (domainObject == null)
		{
			throw new Exception("Please enter valid " + messageToken + "!! " + messageToken
					+ " should not be NULL");
		}
	}

	/**
	 * Find out the matching participant list based on the participant object provided.
	 *
	 * @param userName user name
	 * @param domainObject domain object
	 *
	 * @return filteredMatchingObjects matching participant list
	 *
	 * @throws Exception exception
	 */
	public List delegateGetParticipantMatchingObects(String userName, Object domainObject)
			throws Exception
	{
		List matchingObjects = new ArrayList();
		this.checkNullObject(domainObject, "Domain Object");
		final String className = domainObject.getClass().getName();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ParticipantBizLogic bizLogic = (ParticipantBizLogic) factory.getBizLogic(className);
		// not null check for Id
		//checkNullObject(abstractDomainObject.getId(),"Identifier");
		final LookupLogic participantLookupLogicForSPR = (LookupLogic) CommonUtilities
				.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO_FOR_SPR));
		matchingObjects = bizLogic.getListOfMatchingParticipants((Participant) domainObject,
				participantLookupLogicForSPR);
		/*bug 7561*/
		List filteredMatchingObjects = null;
		if (matchingObjects != null)
		{
			filteredMatchingObjects = this.delegateSearchFilter(userName, matchingObjects);
		}
		else
		{
			filteredMatchingObjects = new ArrayList();
		}
		return filteredMatchingObjects;
		//return matchingObjects;
	}

	/**
	 * Method to get next Specimen Collection Group Number.
	 *
	 * @param userName user name
	 * @param obj object
	 *
	 * @return label label
	 *
	 * @throws Exception exception
	 */
	public String delegateGetSpecimenCollectionGroupLabel(String userName, Object obj)
			throws Exception
	{
		LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
				.getInstance(Constants.SPECIMEN_COLL_GROUP_LABEL_GENERATOR_PROPERTY_NAME);
		if(specimenCollectionGroupLableGenerator == null)
		{
			DefaultSCGLabelGenerator defaultSCGLabelGenerator = new DefaultSCGLabelGenerator();
			defaultSCGLabelGenerator.getLabel(obj);
			specimenCollectionGroupLableGenerator = (LabelGenerator)defaultSCGLabelGenerator;
		}
		return specimenCollectionGroupLableGenerator.getLabel(obj);
	}

	/**
	 * Method to get default value for given key using default value manager.
	 *
	 * @param userName username
	 * @param obj object
	 *
	 * @return String string
	 *
	 * @throws Exception exception
	 */
	public String delegateGetDefaultValue(String userName, Object obj) throws Exception
	{
		return ((String) DefaultValueManager.getDefaultValue((String) obj));
	}

	/**
	 * Removes the identified data from identified Report object.
	 *
	 * @param object object of IdentifiedSurgicalPathologyReport
	 */
	private void removeIdentifiedReportIdentifiedData(Object object)
	{
		final IdentifiedSurgicalPathologyReport identiPathologyReport = (IdentifiedSurgicalPathologyReport) object;
		identiPathologyReport.setCollectionDateTime(null);
		identiPathologyReport.setId(null);
		identiPathologyReport.setActivityStatus(null);
		identiPathologyReport.setIsFlagForReview(null);
		if (identiPathologyReport.getTextContent() != null)
		{
			identiPathologyReport.getTextContent().setData(null);
		}
	}

	/**
	 * Removes the identified data from de-identified Report object.
	 *
	 * @param object object of DeIdentifiedSurgicalPathologyReport
	 */
	private void removeDeIdentifiedReportIdentifiedData(Object object)
	{
		final DeidentifiedSurgicalPathologyReport deIdentiPathologyReport = (DeidentifiedSurgicalPathologyReport) object;
		deIdentiPathologyReport.setCollectionDateTime(null);
		deIdentiPathologyReport.setId(null);
		deIdentiPathologyReport.setActivityStatus(null);
		deIdentiPathologyReport.setIsFlagForReview(null);
	}

	/**
	 * Removes the identified data from SpecimenEvent parameters object.
	 *
	 * @param object object of child classes of SpecimenEventParameters
	 */
	private void removeSpecimenEventParameters(Object object)
	{
		final SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) object;
		specimenEventParameters.setTimestamp(null);
	}

	/**
	 * Removes the identified data from PMI object.
	 *
	 * @param object PMI Object
	 */
	private void removePMIIdentifiedData(Object object)
	{
		final ParticipantMedicalIdentifier pmi = (ParticipantMedicalIdentifier) object;
		pmi.setMedicalRecordNumber(null);
	}

	/**
	 * Removes the identified data from SpecimenArrayContent object.
	 *
	 * @param object SpecimenArrayContent object to filter
	 *
	 * @throws DAOException 	 */
	private void removeSpecimenArrayContentIdentifiedData(Object object)
	{
		final SpecimenArrayContent specimenArrayContent = (SpecimenArrayContent) object;
		final Specimen specimen = specimenArrayContent.getSpecimen();
		if (specimen != null)
		{
			this.removeSpecimenIdentifiedData(specimen);
		}
	}

	/**
	 * Audit api query.
	 *
	 * @param queryObject Query object to audit
	 * @param userName User Name
	 *
	 * @throws Exception Exception
	 */
	public void auditAPIQuery(String queryObject, String userName) throws Exception
	{
		final SessionDataBean sessionDataBean = this.getSessionDataBean(userName);
		this.insertQuery(queryObject, sessionDataBean);
	}

	/**
	 * *
	 * Copy paste from Query Bizlogic class to insert API query Log.
	 *
	 * @param sqlQuery sqlQuery
	 * @param sessionData sessiondata
	 *
	 * @throws Exception exception
	 */
	private void insertQuery(String sqlQuery, SessionDataBean sessionData) throws Exception
	{

		JDBCDAO jdbcDAO = null;
		try
		{

			final String sqlQuery1 = sqlQuery.replaceAll("'", "''");
			long number = 1;
			jdbcDAO = AppUtility.openJDBCSession();
			final SimpleDateFormat fSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String timeStamp = fSDateFormat.format(new Date());

			final String ipAddr = sessionData.getIpAddress();

			final String userId = sessionData.getUserId().toString();
			final String comments = "APIQueryLog";
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			final String databaseType = DAOConfigFactory.getInstance().getDAOFactory(
					applicationName).getDataBaseType();
			if (databaseType.equals(Constants.ORACLE_DATABASE))
			{
				String sql = "select CATISSUE_AUDIT_EVENT_PARAM_SEQ.nextVal from dual";

				List list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{

					final List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						final String str = (String) columnList.get(0);
						if (!"".equals(str))
						{
							number = Long.parseLong(str);

						}
					}
				}
				final String sqlForAudiEvent = "insert into catissue_audit_event"
						+ "(IDENTIFIER,IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) "
						+ "values ('" + number + "','" + ipAddr + "',to_date('" + timeStamp
						+ "','yyyy-mm-dd HH24:MI:SS'),'" + userId + "','" + comments + "')";
				this.LOGGER.info("sqlForAuditLog:" + sqlForAudiEvent);
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				long queryNo = 1;
				sql = "select CATISSUE_AUDIT_EVENT_QUERY_SEQ.nextVal from dual";

				list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{

					final List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						final String str = (String) columnList.get(0);
						if (!"".equals(str))
						{
							queryNo = Long.parseLong(str);

						}
					}
				}
				final String sqlForQueryLog = "insert into " + "catissue_audit_event_query_log"
						+ "(IDENTIFIER,QUERY_DETAILS,AUDIT_EVENT_ID) " + "values (" + queryNo
						+ ",EMPTY_CLOB(),'" + number + "')";
				jdbcDAO.executeUpdate(sqlForQueryLog);
				final String sql1 = "select QUERY_DETAILS from "
						+ "catissue_audit_event_query_log where IDENTIFIER=" + queryNo
						+ " for update";
				list = jdbcDAO.executeQuery(sql1);

				CLOB clob = null;

				if (!list.isEmpty())
				{

					final List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						clob = (CLOB) columnList.get(0);
					}
				}
				//get output stream from the CLOB object
				final OutputStream outputStream = clob.getAsciiOutputStream();
				final OutputStreamWriter osw = new OutputStreamWriter(outputStream);

				//use that output stream to write character data
				//to the Oracle data store
				osw.write(sqlQuery1.toCharArray());
				//write data and commit
				osw.flush();
				osw.close();
				outputStream.close();
				jdbcDAO.commit();
				this.LOGGER.info("sqlForQueryLog:" + sqlForQueryLog);
			}
			else
			{
				final String sqlForAudiEvent = "insert into catissue_audit_event"
						+ "(IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('" + ipAddr
						+ "','" + timeStamp + "','" + userId + "','" + comments + "')";
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				final String sql = "select max(identifier) from catissue_audit_event "
						+ "where USER_ID='" + userId + "'";

				final List list = jdbcDAO.executeQuery(sql);

				if (!list.isEmpty())
				{
					final List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						final String str = (String) columnList.get(0);
						if (!"".equals(str))
						{
							number = Long.parseLong(str);

						}
					}
				}
				final String sqlForQueryLog = "insert into catissue_audit_event_query_log"
						+ "(QUERY_DETAILS,AUDIT_EVENT_ID) values ('" + sqlQuery1 + "','" + number
						+ "')";
				this.LOGGER.debug("sqlForQueryLog:" + sqlForQueryLog);
				jdbcDAO.executeUpdate(sqlForQueryLog);
				jdbcDAO.commit();
			}
		}
		catch (final Exception e)
		{
			this.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcDAO);
		}
	}

	/**
	 * Iterate over the object array.
	 * Crate the target object and set appropriate attributes value
	 *
	 * @param targetClassName target class
	 * @param fields fields name
	 * @param rows records
	 *
	 * @return returnList return list
	 *
	 * @throws Exception exception
	 */
	public List createTargetObjectList(String targetClassName, List<Field> fields, List rows)
			throws Exception
	{
		List returnList = new ArrayList();
		try
		{
			boolean isParticipantMedicalIdentifier = false;
			boolean isSpecimenArrayContent = false;
			boolean isSpecimenEventParameters = false;
			final Class targetClass = Class.forName(targetClassName);

			/**
			 * Special cases to set parent associated object
			 */
			if (targetClassName.equals(ParticipantMedicalIdentifier.class.getName()))
			{
				isParticipantMedicalIdentifier = true;
			}
			else if (targetClassName.equals(SpecimenArrayContent.class.getName()))
			{
				isSpecimenArrayContent = true;
			}
			else
			{
				if (targetClass.getSuperclass().equals(SpecimenEventParameters.class)
						|| targetClass.getSuperclass().equals(ReviewEventParameters.class))
				{
					isSpecimenEventParameters = true;
				}

			}
			for (int i = 0; i < rows.size(); i++)
			{
				final Object[] row = (Object[]) rows.get(i);
				final Object temp = targetClass.newInstance();
				int counter_j = 0;
				for (counter_j = 0; counter_j < fields.size(); counter_j++)
				{
					final Field field = fields.get(counter_j);
					field.setAccessible(true);
					field.set(temp, row[counter_j]);
				}
				if (isParticipantMedicalIdentifier)
				{
					final Long participantIdentifier = (Long) row[counter_j];
					final Participant participant = new Participant();
					participant.setId(participantIdentifier);
					((ParticipantMedicalIdentifier) temp).setParticipant(participant);
				}
				else if (isSpecimenArrayContent)
				{
					final Long specimenIdentifier = (Long) row[counter_j];
					final Specimen specimen = new Specimen();
					specimen.setId(specimenIdentifier);
					((SpecimenArrayContent) temp).setSpecimen(specimen);
				}
				else if (isSpecimenEventParameters)
				{
					final Long scjIdentifier = (Long) row[counter_j++];
					if (scjIdentifier != null)
					{
						final SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
						scg.setId(scjIdentifier);
						((SpecimenEventParameters) temp).setSpecimenCollectionGroup(scg);
					}
					final Long specimenIdentifier = (Long) row[counter_j];
					if (specimenIdentifier != null)
					{
						final Specimen specimen = new Specimen();
						specimen.setId(specimenIdentifier);
						((SpecimenEventParameters) temp).setSpecimen(specimen);
					}
				}

				returnList.add(temp);
			}

		}
		catch (final ClassCastException e)
		{
			this.LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			returnList = rows;
		}
		return returnList;
	}

	/**
	 * Get Max records allowed per query from caTissueCore_properties.xml.
	 *
	 * @return Integer
	 */
	public Integer getAllowedMaxRecordsPerQuery()
	{
		return Integer.valueOf(XMLPropertyHandler
				.getValue(Constants.MAX_RECORDS_PER_CACORE_QUERY_ALLOWED));
	}

	/**
	 * register participant.
	 *
	 * @param domainObject the domain object
	 * @param cpid the cpid
	 * @param userName the user name
	 *
	 * @return the object
	 *
	 * @throws Exception the exception
	 */
	public Object delegateRegisterParticipant(Object domainObject, Long cpid, String userName)
			throws Exception
	{
		try
		{
			this.checkNullObject(domainObject, "Domain Object");
			final ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
			participantBizLogic.registerParticipant(domainObject, cpid, userName);
			this.LOGGER.info(" Domain Object has been successfully registered " + domainObject);
		}
		catch (final Exception e)
		{
			this.LOGGER.error("Delegate Add-->" + e.getMessage(),e);
			e.printStackTrace();
			throw e;
		}
		return domainObject;
	}


	/**
	 * Delegate get ca tissue local participant matching obects.
	 *
	 * @param userName the user name
	 * @param domainObject the domain object
	 *
	 * @return the list
	 *
	 * @throws Exception the exception
	 */
	public List delegateGetCaTissueLocalParticipantMatchingObects(String userName,
			Object domainObject, Long cpId) throws Exception
	{
		List matchingObjects = new ArrayList();
		this.checkNullObject(domainObject, "Domain Object");
		final String className = domainObject.getClass().getName();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final ParticipantBizLogic bizLogic = (ParticipantBizLogic) factory.getBizLogic(className);
		// not null check for Id
		//checkNullObject(abstractDomainObject.getId(),"Identifier");
		final LookupLogic participantLookupLogic = (LookupLogic) CommonUtilities
				.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));
		matchingObjects = bizLogic.getListOfMatchingParticipants((Participant) domainObject,
				participantLookupLogic);
		/*bug 7561*/
		List filteredMatchingObjects = null;
		if (matchingObjects != null)
		{
			filteredMatchingObjects = this.delegateSearchFilter(userName, matchingObjects);
		}
		else
		{
			filteredMatchingObjects = new ArrayList();
		}

		if (cpId != null)
		{
			filteredMatchingObjects=processListForMatchWithinCP(filteredMatchingObjects, cpId);
		}
		return filteredMatchingObjects;
	}

	public List processListForMatchWithinCP(List<DefaultLookupResult> matchPartpantLst, Long cpId)
			throws ApplicationException
	{
		List idList = this.getPartcipantIdsList(cpId);
		matchPartpantLst = filerMatchedList(matchPartpantLst, idList);
		return matchPartpantLst;
	}

	public List<DefaultLookupResult> filerMatchedList(List<DefaultLookupResult> matchPartpantLst,
			List idList)
	{

		List<DefaultLookupResult> matchPartpantLstFiltred = new ArrayList<DefaultLookupResult>();
		Iterator<DefaultLookupResult> itr = matchPartpantLst.iterator();

		if (!idList.isEmpty() && idList.get(0) != null && String.valueOf(idList.get(0)) != "")
		{
			List participantIdList = (List) idList;
			while (itr.hasNext())
			{
				DefaultLookupResult result = (DefaultLookupResult) itr.next();
				Participant participant = (Participant) result.getObject();
				if ((participantIdList).contains(String.valueOf(participant.getId().longValue())))
				{
					matchPartpantLstFiltred.add(result);
				}
			}
		}
		return matchPartpantLstFiltred;
	}

	public static List getPartcipantIdsList(Long id) throws ApplicationException
	{
		List idListArray = null;
		List<String> idList = new ArrayList<String>();
		JDBCDAO jdbcDAO = null;
		String query = null;
		try
		{
			query = "SELECT PARTICIPANT_ID FROM CATISSUE_COLL_PROT_REG WHERE COLLECTION_PROTOCOL_ID='"
					+ id + "'";
			jdbcDAO = AppUtility.openJDBCSession();
			idListArray = jdbcDAO.executeQuery(query);
			if (!idListArray.isEmpty() && idListArray.get(0) != "")
			{
				for (Iterator<List> itr = idListArray.iterator(); itr.hasNext();)
				{
					idList.add(String.valueOf(((List) itr.next()).get(0)));
				}
			}
		}
		catch (DAOException exp)
		{
			// TODO Auto-generated catch block
			throw new DAOException(exp.getErrorKey(), exp, exp.getMsgValues());
		}
		finally
		{
			jdbcDAO.closeSession();
		}
		return idList;
	}


}