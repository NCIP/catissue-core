/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.catissuecore.util.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Variables
{

	private static Logger logger = Logger.getCommonLogger(Variables.class);
	public static Vector databaseDefinitions = new Vector();
	public static String databaseDriver = new String();
	public static String[] databasenames;
	public static String applicationCvsTag = new String();
	public static String applicationAdditionInfo = new String();
	public static int maximumTreeNodeLimit;
	public static int maximumTreeNodeLimitForChildNode;
	public static boolean isSpecimenLabelGeneratorAvl = false;
	public static boolean isTemplateBasedLblGeneratorAvl = false;
	public static boolean isStorageContainerLabelGeneratorAvl = false;
	public static boolean isSpecimenBarcodeGeneratorAvl = false;
	public static boolean isStorageContainerBarcodeGeneratorAvl = false;
	public static boolean isSpecimenCollGroupLabelGeneratorAvl = false;
	public static boolean isSpecimenCollGroupBarcodeGeneratorAvl = false;
	public static boolean isCollectionProtocolRegistrationBarcodeGeneratorAvl = false;
	public static boolean isProtocolParticipantIdentifierLabelGeneratorAvl = false;
	public static boolean isCasAvl = false;

	public static boolean isPhoneNumberToBeValidated = true;
	public static List<NameValueBean> printerLocationList = null;
	public static List<NameValueBean> printerTypeList = null;

	//Added by Geeta
	public static boolean isStateRequired = true;

	public static boolean isCPTitleChange = false;

	public static boolean isSSNRemove = false;

	public static boolean isSexGenoTypeRemove = false;

	public static boolean isRaceRemove = false;

	public static boolean isEthnicityRemove = false;

	public static int sessionTimeOut;

	//  public static String dateFormat ="";

	// Added by Geeta
	public static boolean isLastNameNull = true;

	// Patch ID: SimpleSearchEdit_7
	public static Map<String, String> aliasAndPageOfMap = new HashMap<String, String>();

	public static List<String> queryReadDeniedObjectList = new ArrayList<String>();

	public static Map<String, List<String>> roleEntityMap = new HashMap<String, List<String>>();

	public static Map<String, SessionDataBean> sessionDataMap = new HashMap<String, SessionDataBean>();

	public static String prepareColTypes(List dataColl)
	{
		return prepareColTypes(dataColl, false);
	}

	public static String prepareColTypes(List dataColl, boolean createCheckBoxColumn)
	{
		String colType = "";
		if (dataColl != null && !dataColl.isEmpty())
		{
			final List rowDataColl = (List) dataColl.get(0);

			final Iterator it = rowDataColl.iterator();
			if (createCheckBoxColumn == true)
			{
				colType = "ch,";
			}
			while (it.hasNext())
			{
				final Object obj = it.next();
				if (obj != null && obj instanceof Number)
				{
					colType = colType + "int,";
				}
				else if (obj != null && obj instanceof Date)
				{
					colType = colType + "date,";
				}
				else
				{
					colType = colType + "str,";
				}
			}
		}
		if (colType.length() > 0)
		{
			colType = colType.substring(0, colType.length() - 1);
		}
		return colType;
	}

	public static void setPrinterInfo(String absolutePath)
	{
		printerTypeList = new ArrayList<NameValueBean>();
		printerLocationList = new ArrayList<NameValueBean>();

		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(new File(absolutePath));
			final Properties printerProp = new Properties();
			printerProp.load(inputStream);
			final String printerTypes = printerProp.getProperty(Constants.PRINTER_TYPE);
			if (printerTypes.length() != 0)
			{
				final String[] printerTypesArr = printerTypes.split(",");
				for (final String type : printerTypesArr)
				{
					printerTypeList.add(new NameValueBean(type, type));
				}
			}
			final String printerLocationsStr = printerProp.getProperty(Constants.PRINTER_LOCATION);
			if (printerTypes.length() != 0)
			{
				final String[] printerLocationArr = printerLocationsStr.split(",");
				for (final String location : printerLocationArr)
				{
					printerLocationList.add(new NameValueBean(location, location));
				}
			}
		}
		catch (final FileNotFoundException e)
		{
			Variables.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			Variables.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		final List list = new ArrayList();

		final List a = new ArrayList();
		a.add("As");
		a.add("1");
		a.add("1.5");
		a.add("true");
		a.add("BB");

		list.add(a);

		final String str = prepareColTypes(list);
		System.out.println(str);
	}
}