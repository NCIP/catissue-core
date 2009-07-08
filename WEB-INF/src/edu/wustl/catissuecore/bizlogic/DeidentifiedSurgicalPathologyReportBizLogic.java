
package edu.wustl.catissuecore.bizlogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.security.global.Permissions;

/**
 * Used to store deidentified pathology report to the database
 */
public class DeidentifiedSurgicalPathologyReportBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger
			.getCommonLogger(DeidentifiedSurgicalPathologyReportBizLogic.class);

	/**
	 * Saves the Deidentified pathology reportobject in the database.
	 * @param dao : dao
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final DeidentifiedSurgicalPathologyReport deidentifiedReport = (DeidentifiedSurgicalPathologyReport) obj;
			dao.insert(deidentifiedReport);

			final IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) this
					.retrieveAttribute(dao, SpecimenCollectionGroup.class, deidentifiedReport
							.getSpecimenCollectionGroup().getId(),
							"identifiedSurgicalPathologyReport");
			identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.DEIDENTIFIED);
			identifiedSurgicalPathologyReport
					.setDeIdentifiedSurgicalPathologyReport(deidentifiedReport);
			dao.update(identifiedSurgicalPathologyReport);

			final Set protectionObjects = new HashSet();
			protectionObjects.add(deidentifiedReport);

		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * Method to get dynamicGroup for given Report
	 * 
	 * @param obj
	 *            DeidentifiedSurgicalPathologyReport object
	 * @return Array of dynamicGroup
	 * @throws SMException
	 *             Security manager exception
	 * @throws BizLogicException
	 */
	/*
	 * private String[] getDynamicGroups(DAO dao, AbstractDomainObject obj)
	 * throws BizLogicException { DeidentifiedSurgicalPathologyReport
	 * deIdentifiedSurgicalPathologyReport=
	 * (DeidentifiedSurgicalPathologyReport)obj; CollectionProtocolRegistration
	 * collectionProtocolRegistration
	 * =(CollectionProtocolRegistration)dao.retrieveAttribute
	 * (SpecimenCollectionGroup
	 * .class.getName(),deIdentifiedSurgicalPathologyReport
	 * .getSpecimenCollectionGroup().getId(),Constants.COLUMN_NAME_CPR);
	 * String[] dynamicGroups = new String[1]; dynamicGroups[0] =
	 * SecurityManager.getInstance(this.getClass()).getProtectionGroupByName(
	 * collectionProtocolRegistration,
	 * Constants.getCollectionProtocolPGName(null));
	 * Logger.out.debug("Dynamic Group name: " + dynamicGroups[0]); return
	 * dynamicGroups; }
	 */
	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param obj
	 *            The object to be updated.
	 * @param oldObj : oldObj
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) obj;
			if (report.getTextContent().getId() == null)
			{
				dao.insert(report.getTextContent());
			}
			dao.update(report);

		}
		catch (final Exception ex)
		{
			this.logger
					.error("Error occured while updating DeidentifiedSurgicalPathologyReport domain object"
							+ ex);
		}
	}

	/**
	 * @return a collection of all deidentified reports
	 * @throws Exception : Exception
	 */
	public Map getAllIdentifiedReports() throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the Deidentified reports from the database
		final List listOfReports = bizLogic.retrieve(sourceObjectName);
		final Map mapOfReports = new HashMap();
		for (int i = 0; i < listOfReports.size(); i++)
		{
			final DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) listOfReports
					.get(i);
			mapOfReports.put(report.getId(), report);
		}
		return mapOfReports;
	}

	/**
	 * This function takes identifier as parameter and returns corresponding
	 * DeidentifiedSurgicalPathologyReport
	 * 
	 * @param identifier
	 *            system generated unique id for report
	 * @return Deidentified pathology report of given identifier
	 * @throws Exception : Exception
	 */
	public DeidentifiedSurgicalPathologyReport getReportById(Long identifier) throws Exception
	{
		// Initialising instance of IBizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		final String sourceObjectName = DeidentifiedSurgicalPathologyReport.class.getName();

		// getting all the deidentified reports from the database
		final Object object = bizLogic.retrieve(sourceObjectName, identifier);
		final DeidentifiedSurgicalPathologyReport report = (DeidentifiedSurgicalPathologyReport) object;
		return report;

	}

	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return true;
	}

	@Override
	public String getReadDeniedPrivilegeName()
	{
		return Permissions.READ_DENIED;
	}
}
