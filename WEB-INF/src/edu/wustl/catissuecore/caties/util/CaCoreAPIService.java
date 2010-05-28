
package edu.wustl.catissuecore.caties.util;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

/**
 * @author
 *
 */
public class CaCoreAPIService
{
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(CaCoreAPIService.class);
	/**
	 * appService.
	 */
	private static ApplicationService appService;
	/**
	 * cs.
	 */
	private static ClientSession clientSession;

	/**
	 * Default constructor declared as private to make is SingleTone.
	 */
	private CaCoreAPIService()
	{
		if (appService == null)
		{
			appService = ApplicationServiceProvider.getRemoteInstance();
		}
	}

	/**
	 * Method to initialize the CaCoreAPIService.
	 * @throws Exception : Exception
	 */
	public static void initialize() throws Exception
	{
		try
		{
			HostnameVerifier hostVerifier = new HostnameVerifier()
			{
				public boolean verify(String urlHostName, SSLSession session)
				{
					return true;
	            }
			};
			appService = ApplicationServiceProvider.getRemoteInstance();
			clientSession = ClientSession.getInstance();
			try
			{
				clientSession.startSession(CaTIESProperties.getValue(CaTIESConstants.USER_NAME),
						CaTIESProperties.getValue(CaTIESConstants.PASSWORD));
			}
			catch (final Exception ex)
			{
				//System.out.println("Please check your login information!");
				CaCoreAPIService.logger.error("Error in initializing CaCoreAPIService "
						+ ex.getMessage(),ex);
				ex.printStackTrace() ;
				throw ex;
			}
		}
		catch (final Exception ex)
		{
			CaCoreAPIService.logger.error("Test client throws Exception = " + ex.getMessage(),ex);
			ex.printStackTrace() ;
			throw ex;
		}
	}

	/**
	 * Method to retrieve object.
	 * @param targertClass class name of object to retrieve
	 * @param columnName column name
	 * @param columnValue column value
	 * @return required object
	 */
	public static Object getObject(Class targertClass, String columnName, Object columnValue)
	{
		try
		{
			final List resultList = getList(targertClass, columnName, columnValue);
			if (resultList != null && !resultList.isEmpty())
			{
				return resultList.get(0);
			}
		}
		catch (final Exception e)
		{
			CaCoreAPIService.logger.error("Error while retrieving object "
					+ targertClass + e.getMessage(),e);
			e.printStackTrace() ;
		}
		return null;
	}

	/**
	 * Method to retrieve list of objects.
	 * @param targertClass class name of object to retrieve
	 * @param columnName column value
	 * @param columnValue column value
	 * @return List resultList
	 */
	public static List getList(Class targertClass, String columnName, Object columnValue)
	{
		final DetachedCriteria criteria = DetachedCriteria.forClass(targertClass);
		criteria.add(Restrictions.eq(columnName, columnValue));
		try
		{
			return appService.query(criteria, targertClass.getName());
		}
		catch (final Exception e)
		{
			CaCoreAPIService.logger.error("Error while retrieving List for "
					+ targertClass+e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to execute hqlQuery.
	 * @param hqlQuery hqlQuery string
	 * @param targetClassName class name of object to retrieve
	 * @return result List
	 * @throws Exception generic exception occurred
	 */
	public static Collection executeQuery(String hqlQuery, String targetClassName) throws Exception
	{
		List resultList = null;
		final HQLCriteria hqlCriteria = new HQLCriteria(hqlQuery);

		try
		{
			resultList = appService.query(hqlCriteria, targetClassName);
			return resultList;
		}
		catch (final ApplicationException ex)
		{
			String errorMessage = "Error while executing query ";
			CaCoreAPIService.logger.error(errorMessage
					+ hqlQuery + ex.getMessage(),ex);
			ex.printStackTrace();
			throw new Exception(errorMessage + ex.getMessage());
		}
	}

	/**
	 * Method to execute query on given criteria.
	 * @param criteria object of DetachedCriteria
	 * @param targetClassName class name of object to retrieve
	 * @return result List
	 * @throws Exception generic exception occurred
	 */
	public static Collection executeQuery(DetachedCriteria criteria, String targetClassName)
			throws Exception
	{
		List resultList = null;
		try
		{
			resultList = appService.query(criteria, targetClassName);
			return resultList;
		}
		catch (final ApplicationException ex)
		{
			CaCoreAPIService.logger.error("Error while executing query " + ex.getMessage(),ex);
			ex.printStackTrace();
			throw new Exception("Error while executing query " + ex.getMessage());
		}
	}

	/**
	 * Method to save object.
	 * @param object object to be saved
	 * @return saved object
	 * @throws Exception generic exception occurred
	 */
	public static Object createObject(Object object) throws Exception
	{
		try
		{
			object = appService.createObject(object);
			return object;
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error occured while adding object" +
					"using CaCoreAPI for object:"
					+ object.getClass()+e.getMessage(),e);
			e.printStackTrace() ;
			throw new Exception("Error occured while adding object using CaCoreAPI "
					+ e.getMessage());
		}
	}

	/**
	 * Method to update object.
	 * @param object object to update
	 * @return updated object
	 * @throws Exception generic exception occurred
	 */
	public static Object updateObject(Object object) throws Exception
	{
		try
		{
			object = appService.updateObject(object);
			return object;
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error occured while updating object" +
					"using CaCoreAPI for object:"
					+ object.getClass()+e.getMessage(),e);
			e.printStackTrace() ;
			throw new Exception("Error occured while updating object using CaCoreAPI "
					+ e.getMessage());
		}
	}

	/**
	 * Method to delete object.
	 * @param object object to delete
	 * @throws Exception generic exception occurred
	 */
	public static void removeObject(Object object) throws Exception
	{
		try
		{
			appService.removeObject(object);
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error occured while updating object" +
					"using CaCoreAPI for object:"
					+ object.getClass()+e.getMessage(),e);
			e.printStackTrace();
			throw new Exception("Error occured while updating object using CaCoreAPI "
					+ e.getMessage());
		}
	}

	/**
	 * Method to get SCG label for given SCG.
	 * @param scg object of SpecimenCollectionGroup
	 * @return label of SCG
	 * @throws Exception generic exception occurred
	 */
	public static String getSpecimenCollectionGroupLabel(SpecimenCollectionGroup scg)
			throws Exception
	{
		try
		{
			return appService.getSpecimenCollectionGroupLabel(scg);
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error occured while retrieving SCG label"+e.getMessage(),e);
			e.printStackTrace();
			throw new Exception("Error occured while retrieving SCG label " + e.getMessage());
		}
	}

	/**
	 * Method to retrieve default value for given key.
	 * @param key key
	 * @return default value for given key
	 * @throws Exception generic exception occurred
	 */
	public static String getDefaultValue(String key) throws Exception
	{
		try
		{
			return appService.getDefaultValue(key);
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error occured while retrieving" +
					" default value for " + key+e.getMessage(),e);
			e.printStackTrace();
			throw new Exception("Error occured while retrieving default value for " + key
					+ e.getMessage());
		}
	}

	/**
	 * Method to get list of matching participant for given participant.
	 * @param participant object of Participant
	 * @return list of matching participant objects
	 * @throws Exception generic exception occurred
	 */
	public static List getParticipantMatchingObects(Participant participant) throws Exception
	{
		try
		{
			return appService.getParticipantMatchingObects(participant,null);
		}
		catch (final ApplicationException e)
		{
			CaCoreAPIService.logger.error("Error while retrieving" +
					" matching participant list"+e.getMessage(),e);
			e.printStackTrace() ;
			throw new Exception("Error while retrieving matching participant list" + e.getMessage());
		}
	}
}
