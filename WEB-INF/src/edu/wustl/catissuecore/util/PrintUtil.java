
package edu.wustl.catissuecore.util;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
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
			HttpServletRequest request, Long specimenId)
	{
		String strIpAddress = request.getRemoteAddr();
		SessionDataBean dataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);

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
		return printStauts;
	}
}
