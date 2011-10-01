package edu.wustl.catissuecore.caties.util;

import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

/**
 * @author
 *
 */
public class CaCoreAPIService {
	/**
	 * logger.
	 */
	private static Logger logger = Logger
			.getCommonLogger(CaCoreAPIService.class);

	/**
	 * appService.
	 */
	private static ApplicationService appService;

	/**
	 * Default constructor declared as private to make is SingleTone.
	 */
	private CaCoreAPIService() {

	}

	/**
	 * Method to initialize the CaCoreAPIService.
	 *
	 * @throws Exception
	 *             : Exception
	 */
	public static void initialize() throws Exception {
		try {
			HostnameVerifier hostVerifier = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier);
			String keyStoreFilePath = CaTIESProperties.getValue(
					CaTIESConstants.KEYSTORE_FILE_PATH).trim();
			if (keyStoreFilePath != null && !"".equals(keyStoreFilePath)) {
				System.setProperty("javax.net.ssl.trustStore", CaTIESProperties
						.getValue(CaTIESConstants.KEYSTORE_FILE_PATH));
			}
			appService = ApplicationServiceProvider.getApplicationService(
					CaTIESProperties.getValue(CaTIESConstants.USER_NAME),
					CaTIESProperties.getValue(CaTIESConstants.PASSWORD));
		} catch (final Exception ex) {
			CaCoreAPIService.logger.error("Test client throws Exception = "
					+ ex.getMessage(), ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Method to retrieve object.
	 *
	 * @param targertClass
	 *            class name of object to retrieve
	 * @param columnName
	 *            column name
	 * @param columnValue
	 *            column value
	 * @return required object
	 */
	public static <T> T getObject(Class<T> targertClass, String columnName,
			Object columnValue) {
		T result = null;
		try {
			List<T> resultList = getList(targertClass, columnName, columnValue);
			if (resultList != null && !resultList.isEmpty()) {
				result = resultList.get(0);
			}
		} catch (final Exception e) {
			CaCoreAPIService.logger.error("Error while retrieving object "
					+ targertClass + e.getMessage(), e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Method to retrieve list of objects.
	 *
	 * @param targertClass
	 *            class name of object to retrieve
	 * @param columnName
	 *            column value
	 * @param columnValue
	 *            column value
	 * @return List resultList
	 */
	public static <T> List<T> getList(Class<T> targertClass, String columnName,
			Object columnValue) {
		final DetachedCriteria criteria = DetachedCriteria
				.forClass(targertClass);
		criteria.add(Restrictions.eq(columnName, columnValue));

		List<T> result = null;
		try {
			result = appService.query(criteria);
		} catch (final Exception e) {
			CaCoreAPIService.logger.error("Error while retrieving List for "
					+ targertClass + e.getMessage(), e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Method to execute hqlQuery.
	 *
	 * @param hqlQuery
	 *            hqlQuery string
	 * @param targetClassName
	 *            class name of object to retrieve
	 * @return result List
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static <T> List<T> executeQuery(String hqlQuery, Class<T> klass)
			throws Exception {
		List<T> resultList = null;
		final HQLCriteria hqlCriteria = new HQLCriteria(hqlQuery);

		try {
			resultList = appService.query(hqlCriteria);
		} catch (final ApplicationException ex) {
			String errorMessage = "Error while executing query ";
			CaCoreAPIService.logger.error(errorMessage + hqlQuery
					+ ex.getMessage(), ex);
			ex.printStackTrace();
			throw new Exception(errorMessage + ex.getMessage());
		}
		return resultList;
	}

	/**
	 * Method to execute query on given criteria.
	 *
	 * @param criteria
	 *            object of DetachedCriteria
	 * @param targetClassName
	 *            class name of object to retrieve
	 * @return result List
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static Collection<Object> executeQuery(DetachedCriteria criteria,
			String targetClassName) throws Exception {
		List<Object> resultList = null;
		try {
			resultList = appService.query(criteria);
		} catch (final ApplicationException ex) {
			CaCoreAPIService.logger.error("Error while executing query "
					+ ex.getMessage(), ex);
			ex.printStackTrace();
			throw new Exception("Error while executing query "
					+ ex.getMessage());
		}
		return resultList;
	}

	/**
	 * Method to save object.
	 *
	 * @param object
	 *            object to be saved
	 * @return saved object
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static Object createObject(Object object) throws Exception {
		SDKQuery query = new InsertExampleQuery(object);
		try {
			SDKQueryResult result = ((CaTissueWritableAppService) appService)
					.executeQuery(query);
			object = result.getObjectResult();
		} catch (final ApplicationException e) {
			CaCoreAPIService.logger.error("Error occured while adding object"
					+ "using CaCoreAPI for object:" + object.getClass()
					+ e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error occured while adding object using CaCoreAPI "
							+ e.getMessage());
		}
		return object;
	}

	/**
	 * Method to update object.
	 *
	 * @param object
	 *            object to update
	 * @return updated object
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static Object updateObject(Object object) throws Exception {
		SDKQuery query = new UpdateExampleQuery(object);
		try {
			SDKQueryResult result = ((CaTissueWritableAppService) appService)
					.executeQuery(query);
			object = result.getObjectResult();
		} catch (final ApplicationException e) {
			CaCoreAPIService.logger.error("Error occured while updating object"
					+ "using CaCoreAPI for object:" + object.getClass()
					+ e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error occured while updating object using CaCoreAPI "
							+ e.getMessage());
		}
		return object;
	}

	/**
	 * Method to delete object.
	 *
	 * @param object
	 *            object to delete
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static void removeObject(Object object) throws Exception {
		SDKQuery query = new DeleteExampleQuery(object);
		try {
			SDKQueryResult result = ((CaTissueWritableAppService) appService)
					.executeQuery(query);
			object = result.getObjectResult();
		} catch (final ApplicationException e) {
			CaCoreAPIService.logger.error("Error occured while updating object"
					+ "using CaCoreAPI for object:" + object.getClass()
					+ e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error occured while updating object using CaCoreAPI "
							+ e.getMessage());
		}
	}

	/**
	 * Method to retrieve default value for given key.
	 *
	 * @param key
	 *            key
	 * @return default value for given key
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static String getCaTissueServerProperty(String key) throws Exception {
		String defaultValue = null;
		try {
			defaultValue = ((CaTissueWritableAppService) appService)
					.getCaTissueServerProperty(key);
		} catch (final ApplicationException e) {
			CaCoreAPIService.logger.error("Error occured while retrieving"
					+ " default value for " + key + e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error occured while retrieving default value for " + key
							+ e.getMessage());
		}
		return defaultValue;
	}

	/**
	 * Method to get list of matching participant for given participant.
	 *
	 * @param participant
	 *            object of Participant
	 * @return list of matching participant objects
	 * @throws Exception
	 *             generic exception occurred
	 */
	public static List<Object> getParticipantMatchingObects(
			Participant participant) throws Exception {
		List<Object> returnList = null;
		try {
			logger
					.info("::::::::::::::::::In getParticipantMatchingObects:::::::::");
			returnList = ((CaTissueWritableAppService) appService)
					.getParticipantMatchingObects(participant);
		} catch (final ApplicationException e) {
			CaCoreAPIService.logger.error("Error while retrieving"
					+ " matching participant list" + e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error while retrieving matching participant list"
							+ e.getMessage());
		}
		return returnList;
	}

	public static List<Object> getCaTissueLocalParticipantMatchingObects(
			Participant participant) throws Exception {
		List<Object> returnList = null;
		try {
			logger
					.info("::::::::::::::::::In getCaTissueLocalParticipantMatchingObects:::::::::");
			returnList = ((CaTissueWritableAppService) appService)
					.getCaTissueLocalParticipantMatchingObects(participant,
							null);
		} catch (final ApplicationException e) {
			logger.info("Exception In getParticipantMatchingObects");
			CaCoreAPIService.logger.error("Error while retrieving"
					+ " matching participant list" + e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error while retrieving matching participant list"
							+ e.getMessage());
		}
		return returnList;
	}

	public static String getSpecimenCollectionGroupLabel(
			SpecimenCollectionGroup scg) throws Exception {
		String scgLabel = null;
		try {
			logger
					.info("::::::::::::::::::In getCaTissueLocalParticipantMatchingObects:::::::::");
			scgLabel = ((CaTissueWritableAppService) appService)
					.getSpecimenCollectionGroupLabel(scg);
		} catch (final ApplicationException e) {
			logger
					.info("::::::::::::::::::Exception In getParticipantMatchingObects:::::::::");
			CaCoreAPIService.logger.error("Error while retrieving"
					+ " matching participant list" + e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(
					"Error while retrieving matching participant list"
							+ e.getMessage());
		}
		return scgLabel;
	}
}
