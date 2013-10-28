
package edu.wustl.catissuecore.util;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

/**
 * @author Nitesh
 *
 */
public class PrintUtil
{

	private static final Logger logger = Logger.getCommonLogger(PrintUtil.class);

	public static boolean printSpecimenLabel(String printerType, String printerLocation,
			SessionDataBean dataBean, Long specimenId)
	{
		String strIpAddress = dataBean.getIpAddress();
		boolean printStauts = false;
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(dataBean);
			final Specimen objSpecimen = (Specimen) dao.retrieveById(Specimen.class.getName(),
					specimenId);

			final LabelPrinter labelPrinter = LabelPrinterFactory.getInstance("specimen");

			printStauts = labelPrinter.printLabel(objSpecimen, strIpAddress, null, printerType,
					printerLocation);

		}
		catch (final DAOException exception)
		{
			logger.error(exception.getMessage(), exception);
		}
		catch (Exception exception)
		{
			logger.error(exception.getMessage(), exception);
		}
		finally
		{
			try {
				AppUtility.closeDAOSession(dao);
			} catch (ApplicationException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return printStauts;
	}
}
