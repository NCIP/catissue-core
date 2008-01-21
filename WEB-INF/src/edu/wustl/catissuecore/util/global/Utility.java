/**
 * <p>Title: Utility Class>
 * <p>Description:  Utility Class contain general methods used through out the application. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.util.global;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility Class contain general methods used through out the application.
 * 
 * @author kapil_kaveeshwar
 */
public class Utility extends edu.wustl.common.util.Utility {

	public static Map getSpecimenTypeMap() {
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassList = CDEManager.getCDEManager()
				.getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,
						null);
		Map subTypeMap = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext()) {
			List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

			while (itr1.hasNext()) {
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				// Setting Specimen Type
				String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}

			subTypeMap.put(pv.getValue(), innerList);
		}

		return subTypeMap;
	}

	public static List getSpecimenTypes(String specimenClass) {
		Map specimenTypeMap = getSpecimenTypeMap();
		List typeList = (List) specimenTypeMap.get(specimenClass);

		return typeList;
	}

	public static String getSpecimenClassName(Specimen specimen) {
		if (specimen instanceof CellSpecimen) {
			return Constants.CELL;
		} else if (specimen instanceof MolecularSpecimen) {
			return Constants.MOLECULAR;
		} else if (specimen instanceof FluidSpecimen) {
			return Constants.FLUID;
		} else if (specimen instanceof TissueSpecimen) {
			return Constants.TISSUE;
		}

		return null;
	}

	public static int getEventParametersFormId(
			SpecimenEventParameters eventParameter) {
		if (eventParameter instanceof CheckInCheckOutEventParameter) {
			return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof CollectionEventParameters) {
			return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof EmbeddedEventParameters) {
			return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof FixedEventParameters) {
			return Constants.FIXED_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof FrozenEventParameters) {
			return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof ReceivedEventParameters) {
			return Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof TissueSpecimenReviewEventParameters) {
			return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
		} else if (eventParameter instanceof TransferEventParameters) {
			return Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID;
		}

		return -1;
	}

	// Aniruddha : Added for enhancement - Specimen Aliquoting [Bug Id : 560]
	/**
	 * Returns true if qunatity is of type double else false.
	 * 
	 * @param className
	 *            Name of specimen class
	 * @param type
	 *            Type of specimen
	 * @return true if qunatity is of type double else false.
	 */
	public static boolean isQuantityDouble(String className, String type) {
		if (Constants.CELL.equals(className)) {
			return false;
		} else if (Constants.TISSUE.equals(className)) {
			if (Constants.MICRODISSECTED.equals(type)
					|| Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type)) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	// Aniruddha : Added for enhancement - Specimen Aliquoting
	/**
	 * Returns the unit of specimen quantity.
	 * 
	 * @param className
	 *            Name of specimen class
	 * @param type
	 *            Type of specimen
	 * @return the unit of specimen quantity.
	 */
	public static String getUnit(String className, String type) {
		if (className == null || type == null || className.equals("-1")) {
			return "";
		}

		if (Constants.CELL.equals(className)) {
			return Constants.UNIT_CC;
		} else if (Constants.FLUID.equals(className)) {
			return Constants.UNIT_ML;
		} else if (Constants.MOLECULAR.equals(className)) {
			return Constants.UNIT_MG;
		} else if (Constants.TISSUE.equals(className)) {
			if (Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type)) {
				return Constants.UNIT_CN;
			} else if (Constants.MICRODISSECTED.equals(type)) {
				return Constants.UNIT_CL;
			} else {
				return Constants.UNIT_GM;
			}
		}
		return "";
	}

	// Aniruddha : Added for enhancement - Specimen Aliquoting
	/**
	 * Returns the particular specimen object as per the specimen class.
	 * 
	 * @param className
	 *            Name of specimen class
	 * @return the particular specimen object as per the specimen class.
	 */
	public static Specimen getSpecimen(Specimen specimen) {
		if (specimen instanceof CellSpecimen) {
			return new CellSpecimen();
		} else if (specimen instanceof FluidSpecimen) {
			return new FluidSpecimen();
		} else if (specimen instanceof MolecularSpecimen) {
			return new MolecularSpecimen();
		} else if (specimen instanceof TissueSpecimen) {
			return new TissueSpecimen();
		}

		return new Specimen();
	}

	public static List getSpecimenClassTypes() {
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassTypeList = new ArrayList();

		while (itr.hasNext()) {

			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassTypeList.add(tmpStr);

		} // class and values set

		return specimenClassTypeList;

	}

	/*
	 * this Function gets the list of all storage types as argument and create a
	 * list in which nameValueBean is stored with Type and Identifier of storage
	 * type. and returns this list
	 */
	public static List getStorageTypeList(List list) {
		NameValueBean typeAny = null;
		List storageTypeList = new ArrayList();
		Iterator typeItr = list.iterator();

		while (typeItr.hasNext()) {
			StorageType type = (StorageType) typeItr.next();
			if (type.getId().longValue() == 1) {
				typeAny = new NameValueBean(Constants.HOLDS_ANY, type.getId());
			} else {
				storageTypeList.add(new NameValueBean(type.getName(), type
						.getId()));
			}
		}
		Collections.sort(storageTypeList);
		if (typeAny != null) {
			storageTypeList.add(0, typeAny);
		}
		return storageTypeList;

	}

	/*
	 * this Function gets the list of all storage types as argument and create a
	 * list in which nameValueBean is stored with Type and Identifier of storage
	 * type. and returns this list
	 */
	public static List getStorageTypeList(List list, boolean includeAny) {
		NameValueBean typeAny = null;
		List storageTypeList = new ArrayList();
		Iterator typeItr = list.iterator();
		while (typeItr.hasNext()) {
			StorageType type = (StorageType) typeItr.next();
			if (type.getId().longValue() == 1) {
				typeAny = new NameValueBean(Constants.HOLDS_ANY, type.getId());
			} else {
				storageTypeList.add(new NameValueBean(type.getName(), type
						.getId()));
			}
		}
		Collections.sort(storageTypeList);
		if (includeAny) {
			if (typeAny != null) {
				storageTypeList.add(0, typeAny);
			}
		} else {
			storageTypeList.add(0, new NameValueBean(Constants.SELECT_OPTION,
					"-1"));
		}
		return storageTypeList;

	}

	/*
	 * this Function gets the list of all Specimen Class Types as argument and
	 * create a list in which nameValueBean is stored with Name and Identifier
	 * of specimen Class Type. and returns this list
	 */

	public static List getSpecimenClassTypeListWithAny() {
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassTypeList = new ArrayList();
		specimenClassTypeList.add(new NameValueBean("--All--", "-1"));

		while (itr.hasNext()) {
			// List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			Logger.out.info("specimen class:" + tmpStr);
			specimenClassTypeList.add(new NameValueBean(tmpStr, tmpStr));

		} // class and values set

		return specimenClassTypeList;

	}

	/*
	 * This function gets the list of all collection protocols as argument and
	 * create a list in which nameValueBean is stored with Title and Identifier
	 * of Collection Protocol. and returns this list
	 */
	public static List getCollectionProtocolList(List list) {
		List collectionProtocolList = new ArrayList();

		Iterator cpItr = list.iterator();
		while (cpItr.hasNext()) {
			CollectionProtocol cp = (CollectionProtocol) cpItr.next();
			collectionProtocolList.add(new NameValueBean(cp.getTitle(), cp
					.getId()));
		}
		Collections.sort(collectionProtocolList);
		collectionProtocolList.add(0, new NameValueBean(
				Constants.SELECT_OPTION, "-1"));
		return collectionProtocolList;
	}

	/*
	 * this Function gets the list of all specimen Array types as argument and
	 * create a list in which nameValueBean is stored with specimen array and
	 * Identifier of specimen Array type. and returns this list
	 */
	public static List getSpecimenArrayTypeList(List list) {
		NameValueBean typeAny = null;
		List spArrayTypeList = new ArrayList();
		Iterator typeItr = list.iterator();

		while (typeItr.hasNext()) {
			SpecimenArrayType type = (SpecimenArrayType) typeItr.next();
			if (type.getId().longValue() == 2) {
				typeAny = new NameValueBean(Constants.HOLDS_ANY, type.getId());
			} else {
				spArrayTypeList.add(new NameValueBean(type.getName(), type
						.getId()));
			}
		}
		Collections.sort(spArrayTypeList);
		if (typeAny != null) {
			spArrayTypeList.add(0, typeAny);
		}
		return spArrayTypeList;

	}

	private static String pattern = "MM-dd-yyyy";

	/**
	 * @param date
	 *            String representation of date.
	 * @param pattern
	 *            Date pattern to be used.
	 * @return Month of the given date.
	 * @author mandar_deshmukh
	 */
	public static int getMonth(String date, String pattern) {
		int month = 0;
		month = getCalendar(date, pattern).get(Calendar.MONTH);
		month = month + 1;
		return month;
	}

	public static int getMonth(String date) {
		int month = 0;

		month = getCalendar(date, pattern).get(Calendar.MONTH);
		month = month + 1;
		return month;
	}

	/**
	 * @param date
	 *            String representation of date.
	 * @param pattern
	 *            Date pattern to be used.
	 * @return Day of the given date.
	 * @author mandar_deshmukh
	 */
	public static int getDay(String date, String pattern) {
		int day = 0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static int getDay(String date) {
		int day = 0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * @param date
	 *            String representation of date.
	 * @param pattern
	 *            Date pattern to be used.
	 * @return Year of the given date.
	 * @author mandar_deshmukh
	 */
	public static int getYear(String date, String pattern) {
		int year = 0;
		year = getCalendar(date, pattern).get(Calendar.YEAR);
		return year;
	}

	public static int getYear(String date) {
		int year = 0;
		year = getCalendar(date, pattern).get(Calendar.YEAR);
		return year;
	}

	/*
	 * Method to validate the date given by the user and return a calendar
	 * object for the date instance. It returns a calendar object based on the
	 * date provided. If invalid date is provided it returns the current
	 * calendar instance.
	 */
	private static Calendar getCalendar(String date, String pattern) {
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
			Date givenDate = dateformat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(givenDate);
			return calendar;
		} catch (Exception e) {
			Logger.out.error(e);
			Calendar calendar = Calendar.getInstance();
			return calendar;
		}
	}

	public static void main(String[] args) {
		/*String s = "ds fsbsdjf hsfsdfdsh f,sd fsdfjbhsdj sdf,sdf s,sd fds,sd ffs,sd f\"sd fs \"sd fsdF \"sf";
		Object s1 = toNewGridFormat(s);
		System.out.println("Original String : " + s);
		System.out.println("Updated String : " + s1);*/

		// // Utility u = new Utility();
		// String dt = "18-10-06";
		// String pt = "dd-MM-yy";
		//
		// System.out.println(Utility.getMonth(dt, pt) + "/" +
		// Utility.getDay(dt, pt) + "/"
		// + Utility.getYear(dt, pt));
		//
		// dt = "28-11-06";
		// pt = "dd-MM-yy";
		// System.out.println(Utility.getMonth(dt, pt) + "/" +
		// Utility.getDay(dt, pt) + "/"
		// + Utility.getYear(dt, pt));
		//
		// dt = "18-21-06";
		// pt = "MM-dd-yy";
		// System.out.println(Utility.getMonth(dt, pt) + "/" +
		// Utility.getDay(dt, pt) + "/"
		// + Utility.getYear(dt, pt));
		//
		// dt = "18-asa-06";
		// pt = "dd-MM-yy";
		// System.out.println(Utility.getMonth(dt, pt) + "/" +
		// Utility.getDay(dt, pt) + "/"
		// + Utility.getYear(dt, pt));
		ArrayList<String> attributeValuesInProperOrder = 
			getAttributeValuesInProperOrder("date","13-02-2006","12-02-2006");
		System.out.println(attributeValuesInProperOrder);
	}

	/**
	 * This method returns a list of string values for a given CDE.
	 * 
	 * @param cdeName
	 * @return
	 */
	public static List getListForCDE(String cdeName) {
		CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
		List valueList = new ArrayList();

		if (cde != null) {
			Iterator iterator = cde.getPermissibleValues().iterator();
			while (iterator.hasNext()) {
				PermissibleValue permissibleValue = (PermissibleValue) iterator
						.next();

				valueList.addAll(loadPermissibleValue(permissibleValue));
			}
		}

		Collections.sort(valueList);
		valueList.add(0, Constants.SELECT_OPTION);
		return valueList;
	}

	/**
	 * returns list of all subPVs under this PV, recursively.
	 * 
	 * @param permissibleValue
	 * @return
	 */
	private static List loadPermissibleValue(PermissibleValue permissibleValue) {
		List pvList = new ArrayList();
		String value = permissibleValue.getValue();
		pvList.add(value);

		Iterator iterator = permissibleValue.getSubPermissibleValues()
				.iterator();
		while (iterator.hasNext()) {
			PermissibleValue subPermissibleValue = (PermissibleValue) iterator
					.next();
			List subPVList = loadPermissibleValue(subPermissibleValue);
			pvList.addAll(subPVList);
		}
		return pvList;
	}

	// Mandar : 29Nov06
	/**
	 * Changes the format of the string compatible to New Grid Format, removing
	 * escape characters and special characters from the string Also replaces
	 * comma with space as comma is used as a delimiter.
	 * 
	 * @param obj -
	 *            Unformatted obj to be printed in Grid Format
	 * @return obj - Foratted obj to print in Grid Format
	 */
	public static Object toNewGridFormat(Object obj) {
		obj = toGridFormat(obj);
		if (obj instanceof String) {
			String objString = (String) obj;
			StringBuffer tokenedString = new StringBuffer();

			StringTokenizer tokenString = new StringTokenizer(objString, ",");

			while (tokenString.hasMoreTokens()) {
				tokenedString.append(tokenString.nextToken() + " ");
			}
			String gridFormattedStr = new String(tokenedString);
			obj = gridFormattedStr;
		}

		return obj;
	}

	// Consent tracking(Virender Mehta)
	/**
	 * Prepare Respopnse List
	 * 
	 * @param opr
	 *            If Operation = Edit then "Withdraw" is added in the List
	 * @return listOfResponces
	 */
	public static List responceList(String addeditOperation) {
		List listOfResponces = new ArrayList();
		listOfResponces.add(new NameValueBean(Constants.NOT_SPECIFIED,
				Constants.NOT_SPECIFIED));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_YES,
				Constants.BOOLEAN_YES));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_NO,
				Constants.BOOLEAN_NO));
		if (addeditOperation.equalsIgnoreCase(Constants.EDIT)) {
			listOfResponces.add(new NameValueBean(Constants.WITHDRAWN,
					Constants.WITHDRAWN));
		}
		return listOfResponces;
	}

	public static Long toLong(String string) throws NumberFormatException {
		if ((string != null) && (string.trim() != "")) {
			return new Long(string);
		}
		return null;
	}

	/**
	 * Changes the format of the string compatible to New Grid Format. Also
	 * create hyperlink for the columns that are to be shown as hyperlink.
	 * 
	 * @param row
	 *            The List representing row of that is to be shown in the Grid.
	 * @param hyperlinkColumnMap
	 *            Map containing information about which column to be marked as
	 *            Hyperlink. It is map of the column index that are to be shown
	 *            as hyperlink verses the QueryResultObjectData, which contain
	 *            information of the aliasName of the Object to which this
	 *            column belongs & the index of the associated Id column.
	 * @param index
	 *            The index of the attribute in List whose format is to be
	 *            changed.
	 * @return The Formated object, Hypelink format if the Column is tobe marked
	 *         as hyperlink.
	 * @see edu.wustl.catissuecore.util.global.Utility#toNewGridFormat(java.lang.Object)
	 *      Patch ID: SimpleSearchEdit_6
	 */
	public static Object toNewGridFormatWithHref(List<String> row,
			Map<Integer, QueryResultObjectData> hyperlinkColumnMap, int index) {
		Object obj = row.get(index);

		if (obj instanceof String) {
			obj = toNewGridFormat(obj);

			QueryResultObjectData queryResultObjectData = hyperlinkColumnMap
					.get(index);

			if (queryResultObjectData != null)// This column is to be shown as
												// hyperlink.
			{
				if (obj == null || obj.equals(""))
					obj = "NA";

				/**
				 * Name : Prafull Bug ID: 4223 Patch ID: 4223_1 Description:
				 * Edit User: password fields empty & error on submitting new
				 * password Added PageOf Attribute as request parameter in the
				 * link.
				 */
				String aliasName = queryResultObjectData.getAliasName();
				String link = "SimpleSearchEdit.do?"
						+ edu.wustl.common.util.global.Constants.TABLE_ALIAS_NAME
						+ "="
						+ aliasName
						+ "&"
						+ Constants.SYSTEM_IDENTIFIER
						+ "="
						+ row
								.get(queryResultObjectData
										.getIdentifierColumnId()) + "&"
						+ Constants.PAGE_OF + "="
						+ Variables.aliasAndPageOfMap.get(aliasName);
				/**
				 * bug ID: 4225 Patch id: 4225_1 Description : Passing a
				 * different name to the popup window
				 */
				String onclickStr = " onclick=javascript:NewWindow('" + link
						+ "','simpleSearch','800','600','yes') ";
				String hrefTag = "<a class='normalLink' href='#'" + onclickStr
						+ ">" + obj + "</a>";
				// String hrefTag = "<a href='"+ link+ "'>"+obj+"</a>";
				obj = hrefTag;
			}
		}
		return obj;
	}

	/**
	 * This method creates a comma separated string of numbers representing
	 * column width.
	 * 
	 */
	public static String getColumnWidth(List columnNames) {
		String colWidth = getColumnWidth((String) columnNames.get(0));

		for (int col = 1; col < columnNames.size(); col++) {
			String columnName = (String) columnNames.get(col);
			colWidth = colWidth + "," + getColumnWidth(columnName);
		}
		return colWidth;
	}

	private static String getColumnWidth(String columnName) {
		/*
		 * Patch ID: Bug#3090_31 Description: The first column which is just a
		 * checkbox, used to select the rows, was always given a width of 100.
		 * Now width of 20 is set for the first column. Also, width of 100 was
		 * being applied to each column of the grid, which increasing the total
		 * width of the grid. Now the width of each column is set to 80.
		 */
		String columnWidth = null;
		if (columnName.trim().equals("ID")) {
			columnWidth = "0";
		} else if (columnName.trim().equals("")) {
			columnWidth = "20";
		} else {
			columnWidth = "80";
		}
		return columnWidth;
	}

	/**
	 * This method set TissueList with only Leaf node.
	 * 
	 * @return tissueList
	 */
	public static List tissueSiteList() {
		CDE cde = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_TISSUE_SITE);
		CDEBizLogic cdeBizLogic = (CDEBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Constants.CDE_FORM_ID);
		List tissueList = new ArrayList();
		// set first index as --select-- option to display in combo.
		tissueList.add(new NameValueBean(Constants.SELECT_OPTION, ""
				+ Constants.SELECT_OPTION_VALUE));
		// get the filtered tissue list which is a leaf node
		cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(), tissueList);
		return tissueList;
	}

	/**
	 * Name : Vijay Pande Bug ID: 4119 Patch ID: 4119_1 See also: 1-2
	 * Description: Signature of method is changed to pass only required
	 * parameters instread of whole object, so that the method become more
	 * maintainable
	 */
	/**
	 * Method to check type and class compatibility of specimen as a part of
	 * validation process.
	 * 
	 * @param specimenClass
	 *            specimen class
	 * @param specimenType
	 *            type of specimen
	 * @return boolean returns (true/false) depending on type and class are
	 *         compatible or not
	 * @throws DAOException
	 */
	public static boolean validateSpecimenTypeClass(String specimenClass,
			String specimenType) throws DAOException {
		List specimenClassList = CDEManager.getCDEManager()
				.getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,
						null);
		if (specimenClass == null
				|| !Validator.isEnumeratedValue(specimenClassList,
						specimenClass)) {
			throw new DAOException(ApplicationProperties
					.getValue("protocol.class.errMsg"));
		}
		if (!Validator.isEnumeratedValue(Utility
				.getSpecimenTypes(specimenClass), specimenType)) {
			throw new DAOException(ApplicationProperties
					.getValue("protocol.type.errMsg"));
		}
		/* Patch ends here */
		return true;
	}

	/**
	 * This method will return Permissible Value List from CDE depending on the
	 * listType
	 * 
	 * @param listType
	 * @return
	 */
	public static List getListFromCDE(String listType) {
		List CDEList = CDEManager.getCDEManager().getPermissibleValueList(
				listType, null);
		return CDEList;
	}

	/**
	 * Patch ID: Entered_Events_Need_To_Be_Visible_16 See also: 1-5 Description:
	 * getter setter for the eventsToolTipText and eventsToolTipMap
	 */
	/**
	 * This method generated the toolTip for events in HTML format for the given
	 * NewSpecimen form
	 * 
	 * @param specimenForm
	 *            for which toolTip has to generate for events
	 * @return toolTipText in required format
	 * @throws Exception
	 *             generic exception
	 */
	public static String getToolTipText(NewSpecimenForm specimenForm)
			throws Exception {
		StringBuffer toolTipText = new StringBuffer("");

		if (specimenForm != null) {
			/**
			 * Patch ID: 4176_2 See also: 1-3 Description: Field name and field
			 * value was displayed in different style. Italics style for value
			 * is removed.
			 */
			toolTipText
					.append("<HTML><table border='0'><tr><td colspan='2'><b>CollectedEvents</b></td></tr><tr><td>");
			toolTipText.append("Collector: ");
			toolTipText.append(Utility.getUserNameById(specimenForm
					.getCollectionEventUserId()));
			toolTipText.append("</td><td>Date: ");
			toolTipText.append(specimenForm.getCollectionEventdateOfEvent());
			toolTipText.append("</td></tr><tr><td>Procedure: ");
			toolTipText.append(specimenForm
					.getCollectionEventCollectionProcedure());
			toolTipText.append("</td><td>Container: ");
			toolTipText.append(specimenForm.getCollectionEventContainer());

			toolTipText
					.append("</td></tr><tr><td colspan='2'><b>Received Events</b></td></tr>");
			toolTipText.append("<tr><td>Reciever: ");
			toolTipText.append(Utility.getUserNameById(specimenForm
					.getReceivedEventUserId()));
			toolTipText.append("</td><td>Date: ");
			toolTipText.append(specimenForm.getReceivedEventDateOfEvent());
			toolTipText.append("</td></tr><tr><td colspan='2'>Quality: ");
			toolTipText.append(specimenForm.getReceivedEventReceivedQuality());
			toolTipText.append("</td></tr></HTML>");
		}
		return toolTipText.toString();
	}

	/**
	 * This method returns the user name of user of given id (format of name:
	 * LastName,FirstName)
	 * 
	 * @param userId
	 *            user id of which user name has to return
	 * @return userName in the given format
	 * @throws Exception
	 *             generic exception
	 */
	public static String getUserNameById(Long userId) throws Exception {
		String className = User.class.getName();
		String colName = Constants.SYSTEM_IDENTIFIER;
		String colValue = userId.toString();
		String userName = "";
		UserBizLogic bizLogic = (UserBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(User.class.getName());
		List userList = bizLogic.retrieve(className, colName, colValue);
		if (userList != null && userList.size() > 0) {
			User user = (User) userList.get(0);
			userName = user.getLastName();
			userName += ", ";
			userName += user.getFirstName();
		}
		return userName;
	}

	/**
	 * @param form
	 * @param specimenCollectionGroupForm
	 */
	public static void setEventsFromScg(ActionForm form,
			SpecimenCollectionGroupForm specimenCollectionGroupForm) {
		NewSpecimenForm newSpecimenForm = (NewSpecimenForm) form;

		newSpecimenForm.setCollectionEventId(specimenCollectionGroupForm
				.getCollectionEventId());
		newSpecimenForm
				.setCollectionEventSpecimenId(specimenCollectionGroupForm
						.getCollectionEventSpecimenId());
		newSpecimenForm.setCollectionEventUserId(specimenCollectionGroupForm
				.getCollectionEventUserId());
		newSpecimenForm
				.setCollectionEventdateOfEvent(specimenCollectionGroupForm
						.getCollectionEventdateOfEvent());
		newSpecimenForm
				.setCollectionEventTimeInHours(specimenCollectionGroupForm
						.getCollectionEventTimeInHours());
		newSpecimenForm
				.setCollectionEventTimeInMinutes(specimenCollectionGroupForm
						.getCollectionEventTimeInMinutes());
		newSpecimenForm
				.setCollectionEventCollectionProcedure(specimenCollectionGroupForm
						.getCollectionEventCollectionProcedure());
		newSpecimenForm.setCollectionEventContainer(specimenCollectionGroupForm
				.getCollectionEventContainer());
		newSpecimenForm.setCollectionEventComments(specimenCollectionGroupForm
				.getCollectionEventComments());

		newSpecimenForm.setReceivedEventId(specimenCollectionGroupForm
				.getReceivedEventId());
		newSpecimenForm.setReceivedEventSpecimenId(specimenCollectionGroupForm
				.getReceivedEventSpecimenId());
		newSpecimenForm.setReceivedEventUserId(specimenCollectionGroupForm
				.getReceivedEventUserId());
		newSpecimenForm.setReceivedEventDateOfEvent(specimenCollectionGroupForm
				.getReceivedEventDateOfEvent());
		newSpecimenForm.setReceivedEventTimeInHours(specimenCollectionGroupForm
				.getReceivedEventTimeInHours());
		newSpecimenForm
				.setReceivedEventTimeInMinutes(specimenCollectionGroupForm
						.getReceivedEventTimeInMinutes());
		newSpecimenForm
				.setReceivedEventReceivedQuality(specimenCollectionGroupForm
						.getReceivedEventReceivedQuality());
		newSpecimenForm.setReceivedEventComments(specimenCollectionGroupForm
				.getReceivedEventComments());
	}

	/**
	 * This method returns the SpecimenCollectionGroup given a
	 * SpecimenCollectionGroup identifier.
	 * 
	 * @param specimenCollectionGroupId
	 *            SpecimenCollectionGroup identifier.
	 * @return SpecimenCollectionGroup
	 * @throws DAOException
	 *             on failure to fetch SpecimenCollectionGroup
	 */
	public static SpecimenCollectionGroup getSpecimenCollectionGroup(
			String specimenCollectionGroupId) throws DAOException {
		String sourceObjectName = SpecimenCollectionGroup.class.getName();
		String valueField = Constants.SYSTEM_IDENTIFIER;

		SpecimenCollectionGroupBizLogic scgbizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory
				.getInstance().getBizLogic(
						Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		List specimenCollectionObjectGroupList = scgbizLogic.retrieve(
				sourceObjectName, valueField, specimenCollectionGroupId);
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (specimenCollectionObjectGroupList.size() > 0) {
			specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionObjectGroupList
					.get(0);
		}

		return specimenCollectionGroup;
	}

	public static String getSCGId(String scgName) throws Exception {
		String scgId = Utility.toString(null);
		String sourceObjectName = SpecimenCollectionGroup.class.getName();
		String[] selectColumnName = new String[] { "id" };
		String[] whereColumnName = new String[] { "name" };
		String[] whereColumnCondition = new String[] { "=" };
		Object[] whereColumnValue = new String[] { scgName };
		String joinCondition = null;
		BizLogicFactory bizLogic = BizLogicFactory.getInstance();
		SpecimenCollectionGroupBizLogic scgBizLogic = (SpecimenCollectionGroupBizLogic) bizLogic
				.getBizLogic(SpecimenCollectionGroup.class.getName());
		List scgList = scgBizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue,
				joinCondition);
		if (scgList.size() > 0) {
			scgId = Utility.toString((Long) scgList.get(0));
		}
		return scgId;
	}

	/**
	 * Generates key for ParticipantMedicalIdentifierMap
	 * 
	 * @param i
	 *            serial number
	 * @param keyFor
	 *            atribute based on which rspective key is to generate
	 * @return key for map attribute
	 */
	public static String getParticipantMedicalIdentifierKeyFor(int i,
			String keyFor) {
		return (Constants.PARTICIPANT_MEDICAL_IDENTIFIER + i + keyFor);
	}

	/**
	 * To get the array of ids from the given DomainObject collection.
	 * 
	 * @param domainObjectCollection
	 *            The collectio of domain objects.
	 * @return The array of ids from the given DomainObject collection.
	 */
	public static long[] getobjectIds(Collection domainObjectCollection) {
		long ids[] = new long[domainObjectCollection.size()];
		int i = 0;
		Iterator it = domainObjectCollection.iterator();
		while (it.hasNext()) {
			AbstractDomainObject domainObject = (AbstractDomainObject) it
					.next();
			ids[i] = domainObject.getId().longValue();
			i++;
		}
		return ids;
	}

	/**
	 * This method returns a list of CP ids and CP Long titles
	 * 
	 * @return cpid and cpTitle Map
	 * @throws DAOException
	 */
	public static Map<Long, String> getCPIDTitleMap() throws DAOException {
		// This method returns a list of CP ids and CP titles from the
		// participantRegistrationInfoList
		Map<Long, String> cpIDTitleMap = new HashMap<Long, String>();
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		List collectionProtocolList = bizLogic
				.retrieve(CollectionProtocol.class.getName());
		Iterator itr = collectionProtocolList.iterator();
		while (itr.hasNext()) {
			CollectionProtocol collectionProtocol = (CollectionProtocol) itr
					.next();
			// NameValueBean cpDetails = new
			// NameValueBean(participantRegInfo.getCpShortTitle(),
			// participantRegInfo.getCpId());
			if (!collectionProtocol.getActivityStatus().equalsIgnoreCase(
					Constants.ACTIVITY_STATUS_DISABLED)) {
				cpIDTitleMap.put(collectionProtocol.getId(), collectionProtocol
						.getTitle());
			}
		}
		return cpIDTitleMap;
	}

	/**
	 * @param request
	 * @param recordsPerPage
	 * @param pageNum
	 * @param querySessionData
	 * @return
	 * @throws DAOException
	 */
	public static List getPaginationDataList(HttpServletRequest request,
			SessionDataBean sessionData, int recordsPerPage, int pageNum,
			QuerySessionData querySessionData) throws DAOException {
		List paginationDataList;
		querySessionData.setRecordsPerPage(recordsPerPage);
		int startIndex = recordsPerPage * (pageNum - 1);
		QueryBizLogic qBizLogic = new QueryBizLogic();
		PagenatedResultData pagenatedResultData = qBizLogic.execute(
				sessionData, querySessionData, startIndex);
		paginationDataList = pagenatedResultData.getResult();
		return paginationDataList;
	}

	/**
	 * Executes hql Query and returns the results.
	 * 
	 * @param hql
	 *            String hql
	 * @throws DAOException
	 *             DAOException
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	public static List executeQuery(String hql) throws DAOException,
			ClassNotFoundException {
		HibernateDAO dao = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		dao.openSession(null);
		List list = dao.executeQuery(hql, null, false, null);
		dao.closeSession();
		return list;
	}

	// for conflictResolver pagination:kalpana
	public static PagenatedResultData executeForPagination(String sql,
			SessionDataBean sessionDataBean, boolean isSecureExecute,
			Map queryResultObjectDataMap,
			boolean hasConditionOnIdentifiedField, int startIndex,
			int totoalRecords) throws DAOException, SQLException {
		try {
			JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(
					Constants.JDBC_DAO);
			dao.openSession(null);
			Logger.out.debug("SQL************" + sql);
			PagenatedResultData pagenatedResultData = dao.executeQuery(sql,
					sessionDataBean, isSecureExecute,
					hasConditionOnIdentifiedField, queryResultObjectDataMap,
					startIndex, totoalRecords);
			dao.closeSession();
			return pagenatedResultData;
		} catch (DAOException daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		} catch (ClassNotFoundException classExp) {
			throw new DAOException(classExp.getMessage(), classExp);
		}
	}

	/**
	 * limits the title of the saved query to 125 characters to avoid horizontal
	 * scrollbar
	 * 
	 * @param title -
	 *            title of the saved query (may be greater than 125 characters)
	 * @return - query title upto 125 characters
	 */
	public static String getQueryTitle(String title) {
		String multilineTitle = "";
		if (title.length() <= Constants.CHARACTERS_IN_ONE_LINE) {
			multilineTitle = title;
		} else {
			multilineTitle = title.substring(0,
					Constants.CHARACTERS_IN_ONE_LINE)
					+ ".....";
		}
		return multilineTitle;
	}

	/**
	 * returns the entire title to display it in tooltip .
	 * 
	 * @param title -
	 *            title of the saved query
	 * @return tooltip string
	 */
	public static String getTooltip(String title) {
		String tooltip = title.replaceAll("'",
				Constants.SINGLE_QUOTE_ESCAPE_SEQUENCE); // escape sequence
															// for '
		return tooltip;
	}

	/**
	 * Method to check the associated deidentified report is quarantined or not
	 * 
	 * @param reportId
	 *            id of identified report
	 * @return boolean value for is quarantine
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public static boolean isQuarantined(Long reportId) throws DAOException,
			ClassNotFoundException {
		String hqlString = "select ispr.deIdentifiedSurgicalPathologyReport.id "
				+ " from edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport as ispr, "
				+ " edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport as deidReport"
				+ " where ispr.id = "
				+ reportId
				+ " and ispr.deIdentifiedSurgicalPathologyReport.id=deidReport.id"
				+ " and ispr.deIdentifiedSurgicalPathologyReport.isQuanrantined='"
				+ Constants.QUARANTINE_REQUEST + "'";

		List reportIDList = Utility.executeQuery(hqlString);
		if (reportIDList != null && reportIDList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Adds the attribute values in the list in sorted order and returns the
	 * list containing the attribute values in proper order
	 * 
	 * @param dataType -
	 *            data type of the attribute value
	 * @param value1 -
	 *            first attribute value
	 * @param value2 -
	 *            second attribute value
	 * @return List containing value1 and valu2 in sorted order
	 */
	public static ArrayList<String> getAttributeValuesInProperOrder(
			String dataType, String value1, String value2) {
		String v1 = value1;
		String v2 = value2;
		ArrayList<String> attributeValues = new ArrayList<String>();
		if (dataType.equalsIgnoreCase(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE)) 
		{
			SimpleDateFormat df =new SimpleDateFormat(pattern);
			try {
				Date date1 = df.parse(value1);
				Date date2 = df.parse(value2);
				if(date1.after(date2))
				{
					v1 = value2;
					v2 = value1;
				}
			} catch (ParseException e) {
				Logger.out.error("Can not parse the given date in getAttributeValuesInProperOrder() method :"+e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			if (dataType
					.equalsIgnoreCase(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE)
					|| dataType
					.equalsIgnoreCase(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE)) {
				if (Long.parseLong(value1) > Long.parseLong(value2)) {
					v1 = value2;
					v2 = value1;
				}

			} else {
				if (dataType
						.equalsIgnoreCase(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE)) {
					if (Double.parseDouble(value1) > Double.parseDouble(value2)) {
						v1 = value2;
						v2 = value1;
					}

				}
			}
		}
		attributeValues.add(v1);
		attributeValues.add(v2);
		return attributeValues;
	}

	/**
	 * This method returns the new Date by adding the days as specified in the
	 * Date which user passes.
	 * 
	 * @param date
	 *            Date in which days are to be added
	 * @param daysToBeAdded
	 *            Number of days to be added
	 * @return
	 */
	public static Date getNewDateByAdditionOfDays(Date date, int daysToBeAdded) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DAY_OF_MONTH, daysToBeAdded);
		return calendar.getTime();
	}
}