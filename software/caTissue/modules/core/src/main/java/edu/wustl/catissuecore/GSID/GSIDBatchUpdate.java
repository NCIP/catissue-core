package edu.wustl.catissuecore.GSID;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import gov.nih.nci.logging.api.util.StringUtils;

/****
 * This class helps assigning GSIDs for batch of identifiers. 
 * @author srikalyan
 */
public class GSIDBatchUpdate implements Runnable {
	private static volatile boolean lock = false;
	private static volatile String currentUser;
	private static volatile int unassignedSpecimenCount = 0;
	private static volatile int processedSpecimenCount = 0;
	private static volatile String lastProcessedLabel;
	private static volatile boolean error=false;
	private static volatile Exception lastException;
	private static final Log LOG = LogFactory.getLog(GSIDBatchUpdate.class);
	

	/***
	 * Determines if some one is running the process or not.
	 * @return
	 */
	public static boolean isLock() {
		return lock;
	}
	
	/*********
	 * returns the user id of the user who is running the process.
	 * @return
	 */
	public static String getCurrentUser() {
		return currentUser;
	}

	/***
	 * returns the unassigned specimen count.
	 * @return
	 */
	public static int getUnassignedSpecimenCount() {
		return unassignedSpecimenCount;
	}

	/***
	 * returns the processed specimen count.
	 * @return
	 */
	public static int getProcessedSpecimenCount() {
		return processedSpecimenCount;
	}	

	/*****
	 * returns the last processed specimen's label.
	 * @return
	 */
	public static String getLastProcessedLabel() {
		return lastProcessedLabel;
	}

	/******
	 * returns true if there is any error occurred during the batch update.
	 * @return
	 */
	public static boolean isError() {
		return error;
	}

	/**********
	 * returns the last exception occurred during the batch update process.
	 * @return
	 */
	public static Exception getLastException() {
		return lastException;
	}

	/*****
	 * Constructor
	 * @param currentUser is the id of the current user.
	 */
	public GSIDBatchUpdate(String currentUser) {
		startProcess(currentUser);
	}

	/*****
	 * thread which starts the process.
	 * @param userName
	 */
	public synchronized void startProcess(String userName) {
		if (!lock) {
			lock = true;
			currentUser = userName;
			unassignedSpecimenCount = 0;
			processedSpecimenCount = 0;
			lastProcessedLabel = "";
			lastException = null;
			error = false;
			Thread t = new Thread(this);
			t.start();
			try {
				t.sleep(500);
			} catch (InterruptedException e) {

			}
		}
	}

	/*****
	 * gets the percentage of completion.
	 * @return
	 */
	public static double getPercentage() {
		double percentage = 0;
		if (unassignedSpecimenCount != 0) {
			percentage = ((double) processedSpecimenCount / (double) unassignedSpecimenCount) * 100;
		}
		return percentage;
	}

	/****
	 * overridden method of the interface. 
	 */
	public void run() {
		try {
			updateAllSpecimen();
		} catch (GSIDException e) {
			LOG.error(e, e);
		} finally {
			lock = false;
		}
	}

	
	private void updateParentGSID(Specimen parentSpecimen, DAO dao) throws GSIDException {
		GSIDClient gsidClient = new GSIDClient();
		gsidClient.syncClient();
		if (parentSpecimen != null
				&& StringUtils.isBlank(parentSpecimen
						.getGlobalSpecimenIdentifier())) {
			if (parentSpecimen.getParentSpecimen() != null
					&& parentSpecimen.getParentSpecimen() instanceof Specimen) {
				Specimen newParentSpecimen = (Specimen) parentSpecimen
						.getParentSpecimen();
				if (newParentSpecimen != null
						&& StringUtils.isBlank(newParentSpecimen
								.getGlobalSpecimenIdentifier())) {
					updateParentGSID(newParentSpecimen, dao);
				}
				String[] parentIdentifiers = null;
				if (newParentSpecimen != null
						&& !StringUtils.isBlank(newParentSpecimen
								.getGlobalSpecimenIdentifier())) {
					parentIdentifiers = new String[1];
					parentIdentifiers[0] = newParentSpecimen
							.getGlobalSpecimenIdentifier();
					LOG.debug(GSIDConstant.GSID_PARENT_IDENTIFIER_PRINT_MSG + parentIdentifiers[0]);
				} else {
					// parent is present but GSID is absent => service down :(
					// so don't go any further just return					
					error=true;					
					LOG.error(GSIDConstant.GSID_SERVICE_DOWN);
					return;
				}

				String identifier = gsidClient.getGSID(null, parentIdentifiers,false);
				LOG.debug(GSIDConstant.GSID_IDENTIFIER_PRINT_MSG + identifier);

				if (identifier != null) {
					parentSpecimen.setGlobalSpecimenIdentifier(identifier);
					try {
						dao.update(parentSpecimen);
						lastProcessedLabel=parentSpecimen.getLabel();
						processedSpecimenCount++;
					} catch (DAOException e) {
						LOG.error(GSIDConstant.GSID_PARENT_UPDATE_ERROR, e);
					}
				} else {
					// could not get an identifier => service might be down :(
					error=true;
					return;
				}

			} else {
				String identifier = gsidClient.getGSID(null, null,false);
				LOG.debug(GSIDConstant.GSID_IDENTIFIER_PRINT_MSG  + identifier);
				if (identifier != null) {
					parentSpecimen.setGlobalSpecimenIdentifier(identifier);
					try {
						dao.update(parentSpecimen);
						lastProcessedLabel=parentSpecimen.getLabel();
						processedSpecimenCount++;
					} catch (DAOException e) {
						LOG.error(GSIDConstant.GSID_PARENT_UPDATE_ERROR, e);
					}
				}
			}
		} else {
			if (parentSpecimen != null)
				LOG.debug(GSIDConstant.GSID_IDENTIFIER_PRINT_MSG
						+ parentSpecimen.getGlobalSpecimenIdentifier());
		}
	}

	private void updateAllSpecimen() throws GSIDException {
		final String applicationName = CommonServiceLocator.getInstance()
				.getAppName();
		DAO dao = null;
		try {
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
					.getDAO();
			//null session because user session should not be used as this is an asynchronous call.
			dao.openSession(null);
			final String sourceObjectName = Specimen.class.getName();
			List<Specimen> list = dao
					.executeQuery("select md from "
							+ sourceObjectName
							+ " md "
							+ "where md.globalSpecimenIdentifier is null order by md.id asc");
			unassignedSpecimenCount=list.size();
			for (Specimen obj : list) {
				try {
					dao.beginTransaction();
				} catch (Exception e) {
					LOG.debug(
							"Exception occured during creation of transaction. Probably because transaction has already been created",
							e);
				}
				updateParentGSID(obj, dao);				
				dao.commit();
				if(error){
					break;
				}
			}
			dao.closeSession();
		} catch (DAOException e) {
			LOG.error("Error occured during the batch update of GSID", e);
			try {
				dao.closeSession();
			} catch (DAOException e1) {
				LOG.error(GSIDConstant.GSID_CLOSING_SESSION_ERROR, e1);
			}
		}
		
	}
	
	/*****
	 * used to get the number of specimens without GSID.
	 * @param sessionDataBean
	 * @return
	 */
	public static long getUnassignedSpecimenCount(SessionDataBean sessionDataBean)
	{
		long count=0l;
		final String applicationName = CommonServiceLocator.getInstance()
		.getAppName();
		DAO dao = null;
		try {
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
					.getDAO();
			//null session because user session should not be used as this is an asynchronous call.
			dao.openSession(sessionDataBean);
			final String sourceObjectName = Specimen.class.getName();
			List list = dao
					.executeQuery("select count(*) from "
							+ sourceObjectName
							+ " md "
							+ "where md.globalSpecimenIdentifier is null");
			if(list.size()>0)
			{
				count=(Integer)(list.get(0));
			}
			dao.closeSession();
		} catch (DAOException e) {
			LOG.error("Error occured during the batch update of GSID", e);
			try {
				dao.closeSession();
			} catch (DAOException e1) {
				LOG.error(GSIDConstant.GSID_CLOSING_SESSION_ERROR, e1);
			}
		}
		return count;
	}

}
