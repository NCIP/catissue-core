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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.CSMValidator;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.QueryResultObjectDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

/**
 * Utility Class contain general methods used through out the application.
 * 
 * @author kapil_kaveeshwar
 */
public class Utility extends edu.wustl.common.util.Utility {

	public static Set getSpecimenClassCDE()
	{
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		return setPV;
	}
	
	/* Method returns the storage position list */
	public static List getStoragePositionTypeList()
	{
		List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();
		
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_VIRTUAL, 
							Constants.STORAGE_TYPE_POSITION_VIRTUAL_VALUE));
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_AUTO, 
				Constants.STORAGE_TYPE_POSITION_AUTO_VALUE));
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_MANUAL, 
				Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE));
		
		return storagePositionTypeList;		
	}
	
	public static List getStoragePositionTypeListForTransferEvent()
	{
		List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();
		
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_AUTO, 
				Constants.STORAGE_TYPE_POSITION_AUTO_VALUE_FOR_TRANSFER_EVENT));
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_MANUAL, 
				Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE_FOR_TRANSFER_EVENT));
		
		return storagePositionTypeList;	
	}
	
	public static List getSpecimenClassList()
	{
		List specimenClassList= new ArrayList();
		Set setPV = getSpecimenClassCDE();
		Iterator itr = setPV.iterator();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		while (itr.hasNext()) 
		{
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));
		}	
		return specimenClassList;
		
	}
	public static Map getSpecimenTypeMap()
	{
		Set setPV = getSpecimenClassCDE();
		Iterator itr = setPV.iterator();
		Map subTypeMap = new HashMap();
		while (itr.hasNext()) 
		{
			List<NameValueBean> innerList = new ArrayList<NameValueBean>();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext()) 
			{
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
	
	public static List getSpecimenTypes(String specimenClass) 
	{

		Map specimenTypeMap = getSpecimenTypeMap();
		List typeList = (List) specimenTypeMap.get(specimenClass);
		return typeList;
	}

	public static String getSpecimenClassName(AbstractSpecimen specimen) {
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
	public static List getStorageTypeList(List list, boolean includeAny)
	{
		NameValueBean typeAny = null;
		List storageTypeList = new ArrayList();
		Iterator typeItr = list.iterator();
		while (typeItr.hasNext()) 
		{
			StorageType type = (StorageType) typeItr.next();
			if (type.getId().longValue() == 1) 
			{
				typeAny = new NameValueBean(Constants.HOLDS_ANY, type.getId());
			}
			else
			{
				storageTypeList.add(new NameValueBean(type.getName(), type.getId()));
			}
		}
		Collections.sort(storageTypeList);
		if (includeAny) 
		{
			if (typeAny != null) 
			{
				storageTypeList.add(0, typeAny);
			}
		}
		else 
		{
			storageTypeList.add(0, new NameValueBean(Constants.SELECT_OPTION,"-1"));
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
	public static List<NameValueBean> responceList(String addeditOperation) 
	{
		List<NameValueBean> listOfResponces = new ArrayList<NameValueBean>();
		listOfResponces.add(new NameValueBean(Constants.NOT_SPECIFIED,Constants.NOT_SPECIFIED));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_YES,Constants.BOOLEAN_YES));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_NO,Constants.BOOLEAN_NO));
		if (addeditOperation.equalsIgnoreCase(Constants.EDIT)) 
		{
			listOfResponces.add(new NameValueBean(Constants.WITHDRAWN,Constants.WITHDRAWN));
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
		if ("ID".equals(columnName.trim())) {
			columnWidth = "0";
		} else if ("".equals(columnName.trim())) {
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
		
		SpecimenCollectionGroupBizLogic scgbizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory
				.getInstance().getBizLogic(
						Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		Object object = scgbizLogic.retrieve(
				sourceObjectName, new Long(specimenCollectionGroupId));
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (object != null) {
			specimenCollectionGroup = (SpecimenCollectionGroup) object;
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
		String isSimpleSearch = (String)request.getSession().getAttribute(edu.wustl.common.util.global.Constants.IS_SIMPLE_SEARCH);
		if (isSimpleSearch == null || (!isSimpleSearch.equalsIgnoreCase(Constants.TRUE)))
		{
			Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap = querySessionData.getQueryResultObjectDataMap();
			if(queryResultObjectDataBeanMap!=null)
			{	
				for (Iterator<Long> beanMapIterator = queryResultObjectDataBeanMap.keySet().iterator() ; beanMapIterator.hasNext() ; )
				{
					Long id = beanMapIterator.next();
					QueryResultObjectDataBean bean = queryResultObjectDataBeanMap.get(id);
					if (bean.isClobeType())
					{
						List<String> columnsList = (List<String>)request.getSession().getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
						QueryOutputSpreadsheetBizLogic queryBizLogic = new QueryOutputSpreadsheetBizLogic();
						Map<Integer, Integer> fileTypeIndexMainEntityIndexMap = queryBizLogic.updateSpreadSheetColumnList(columnsList, queryResultObjectDataBeanMap);
						//	QueryOutputSpreadsheetBizLogic.updateDataList(paginationDataList, fileTypeIndexMainEntityIndexMap);
						Map exportMetataDataMap = QueryOutputSpreadsheetBizLogic.updateDataList(paginationDataList, fileTypeIndexMainEntityIndexMap);
						request.getSession().setAttribute(Constants.ENTITY_IDS_MAP,exportMetataDataMap.get(Constants.ENTITY_IDS_MAP));
						request.getSession().setAttribute(Constants.EXPORT_DATA_LIST,exportMetataDataMap.get(Constants.EXPORT_DATA_LIST));
						break;
					}
				}
			}
		}
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
				+ " and ispr.deIdentifiedSurgicalPathologyReport.isQuarantined='"
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

	/**
	 * Method to get userID, retriev userId using loginName in case of API call
	 * @param dao
	 * @param sessionDataBean
	 * @return
	 * @throws DAOException
	 */
	public static Long getUserID(DAO dao, SessionDataBean sessionDataBean) throws DAOException
	{
		Long userID = sessionDataBean.getUserId();
		if (userID == null)
		{
			String sourceObjectName = User.class.getName();
			String[] selectColumnName = new String[]{Constants.SYSTEM_IDENTIFIER};
			String[] whereColumnName = new String[]{Constants.LOGINNAME};
			String[] whereColumnCondition = new String[]{"="};
			String[] whereColumnValue = new String[]{sessionDataBean.getUserName()};
			String joinCondition = "";
			List userIDList = (List) dao.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue,
					joinCondition);
			if (userIDList != null && userIDList.size() > 0)
			{
				userID = (Long) userIDList.get(0);
				sessionDataBean.setUserId(userID);
			}

		}
		return userID;
	}

	/**
	 * This will return the comma separated ids string
	 * @param orderItemIds
	 * @return
	 */
	public static String getCommaSeparatedIds(Collection orderItemIds)
	{
		int counter = 1;
		Iterator orderItemIdsIterator = orderItemIds.iterator();
		String ids="";
		
		while(orderItemIdsIterator.hasNext())
		{
			ids = ids + orderItemIdsIterator.next();
			if(counter<orderItemIds.size())
				ids = ids +",";
			counter++;
		}
		return ids;
	}
	/**
	 * This function will return CollectionProtocolRegistration object 
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return collectionProtocolRegistration
	 */
	public static CollectionProtocolRegistration getcprObj(String cpr_id) throws DAOException
	{
		CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogic = (CollectionProtocolRegistrationBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
		
		Object object = collectionProtocolRegistrationBizLogic.retrieve(CollectionProtocolRegistration.class.getName(), new Long(cpr_id));		
		CollectionProtocolRegistration collectionProtocolRegistrationObject = (CollectionProtocolRegistration) object;
		return collectionProtocolRegistrationObject;
	}
	
	/**
	 * This function will return SpecimenCollectionGroup object 
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return specimenCollectionGroupObject
	 */
	public static SpecimenCollectionGroup getSCGObj(String scg_id) throws DAOException
	{
		SpecimenCollectionGroupBizLogic specimenCollectionBizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
			
		Object object = specimenCollectionBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), new Long(scg_id));
		SpecimenCollectionGroup specimenCollectionGroupObject = null;
		if(object != null)
		{
			specimenCollectionGroupObject = (SpecimenCollectionGroup) object;
		}
		return specimenCollectionGroupObject;
	}
	
	/**
	 * @param request
	 * @param operation
	 * @param specimenCollectionGroupForm
	 * @throws DAOException
	 */
	public static long setUserInForm(HttpServletRequest request, String operation)
			throws DAOException
	{
		long collectionEventUserId =  0 ;
		UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, userCollection);
		SessionDataBean sessionData = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		if (sessionData != null)
		{
			String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
			collectionEventUserId = EventsUtil.getIdFromCollection(userCollection, user);
		}
		return collectionEventUserId;
	}
	
	/**
	 * @param sourceObjectName
	 * @param selectColumnName
	 * @return
	 * @throws DAOException
	 */
	public static int getNextUniqueNo(String sourceObjectName, String[] selectColumnName)
			throws DAOException
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		dao.openSession(null);
		List list = dao.retrieve(sourceObjectName, selectColumnName);
		dao.closeSession();

		if (!list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!str.equals(""))
				{
					int no = Integer.parseInt(str);
					return no + 1;
				}
			}
		}
		return 1;
	}

	/**
	 * @return
	 */
	public static String getlLabel(String lastName,String firstName)
	{
		if (lastName!= null && !lastName.equals("") && firstName != null && !firstName.equals("")) 
		{
			return lastName + "," + firstName;
		} 
		else if(lastName!= null && !lastName.equals(""))
		{
			return lastName;
		}
		else if(firstName!= null && !firstName.equals(""))
		{
			return firstName;
		}		
		return null;
	}
	
	/**
	 * @param request
	 * @param responseString
	 * @return
	 */
	public static String getResponseString(HttpServletRequest request, String responseString)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		Logger.out.info("Errors:" + errors);
		if (errors != null || errors.size() != 0)
		{
			Iterator iterator = errors.get();
			while (iterator.hasNext())
			{
				ActionError next = (ActionError)iterator.next();
				Object[] values = next.getValues();
				for(int j=0;j<values.length;j++)
				{
					responseString = (String)values[j];
				}
			}
		}
		return responseString;
	}
	/**
	 * Checks the class of specimen and returns the object of specific type.
	 * @param classType
	 * @return
	 */
	public static Specimen getSpecimenObject(String classType)
	{
		Specimen specimen;
		if(Constants.CELL.equals(classType))
		{
			specimen = new CellSpecimen();
		} else if (Constants.MOLECULAR.equals(classType))
		{
			specimen = new MolecularSpecimen();
		} else if(Constants.FLUID.equals(classType))
		{
			specimen = new FluidSpecimen();
		} else
		{
			specimen = new TissueSpecimen();
		}
		 return specimen;
	}
	
	
	/**
	 *
	 */
	
	public static Specimen getSpecimen(String specimenId)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		Specimen specimen = null;
		try 
		{
			dao.openSession(null);
			NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			specimen = newSpecimenBizLogic.getSpecimenObj(specimenId, dao);
			dao.closeSession();
		} 
		catch (DAOException e) 
		{
			// TODO Auto-generated catch block
			Logger.out.debug(e.getMessage(), e);
			return null;
		}
		return specimen;
	}

	
	public static String getmyData(List dataList)
	{
		String myData =	"[";
		int i=0;
		if(dataList !=null && dataList.size()!=0)
		{
			for (i=0;i<(dataList.size()-1);i++)
			{
				List row = (List)dataList.get(i);
				int j;
				myData=myData+"\"";
				for (j=0;j < (row.size()-1);j++)
				{
					myData=myData+Utility.toNewGridFormat(row.get(j)).toString();
					myData=myData+",";
				}
				myData=myData+Utility.toNewGridFormat(row.get(j)).toString();
				myData=myData+"\"";
				myData=myData+",";
			}
		
			List row = (List)dataList.get(i);
			int j;
			myData=myData+"\"";
			for (j=0;j < (row.size()-1);j++)
			{
				myData=myData+Utility.toNewGridFormat(row.get(j)).toString();
				myData=myData+",";
			}
			myData=myData+Utility.toNewGridFormat(row.get(j)).toString();
			myData=myData+"\"";
		}
		myData=myData+"]";
		return myData;
	}
	public static String getcolumns(List columnList) 
	{
		String columns="\"";
		int col;
		if(columnList!=null)
		{
			for(col=0;col<(columnList.size()-1);col++)
			{
				columns=columns+columnList.get(col);
				columns=columns+",";
			}
			columns=columns+columnList.get(col);
		}
		columns=columns+"\"";
		return columns;
	}
	public static String getcolWidth(List columnList, boolean isWidthInPercent)
	{
	
		String colWidth="\"";
		int col;
		if(columnList!=null)
		{
			String fixedColWidth = null;
			if(isWidthInPercent)
			{
				fixedColWidth = String.valueOf(100/columnList.size());
			}
			else
			{
				fixedColWidth="100";
			}
			for(col=0;col<(columnList.size()-1);col++)
			{
				colWidth=colWidth+fixedColWidth;
				colWidth=colWidth+",";
			}
			colWidth=colWidth+fixedColWidth;
		}
		colWidth=colWidth+"\"";
		return colWidth;
	}
	public static String getcolTypes(List dataList)
	{
		StringBuffer colTypes= new StringBuffer(); 
		colTypes.append("\"");
		colTypes.append(Variables.prepareColTypes(dataList));
		colTypes.append("\"");
		return colTypes.toString();
	}
	
	public static void setGridData(List dataList, List columnList, HttpServletRequest request)
	{
		request.setAttribute("myData",getmyData(dataList));
		request.setAttribute("columns", getcolumns(columnList));
		boolean isWidthInPercent=false;
		if( columnList.size()<10)
		{
			isWidthInPercent=true;
		}
		request.setAttribute("colWidth",getcolWidth(columnList,isWidthInPercent));
		request.setAttribute("isWidthInPercent",isWidthInPercent);
		request.setAttribute("colTypes",getcolTypes(dataList));
		int heightOfGrid = 100;
		if(dataList!=null)
		{
			int noOfRows = dataList.size();
			heightOfGrid = (noOfRows + 2) * 20;
			if(heightOfGrid > 240)
			{
				heightOfGrid = 230;
			}
		}
		request.setAttribute("heightOfGrid", heightOfGrid);
		int col = 0;
		int i=0;
		String hiddenColumnNumbers="";
		if(columnList!=null)
		{
			for(col=0;col<columnList.size();col++)
			{
				if (columnList.get(col).toString().trim().equals("ID") || columnList.get(col).toString().trim().equals("Status")
						|| columnList.get(col).toString().trim().equals("Site Name")|| columnList.get(col).toString().trim().equals("Report Collection Date"))
				{
					hiddenColumnNumbers=hiddenColumnNumbers+"hiddenColumnNumbers["+i+"] = "+col+";";
					i++;
				}
			}
		}
		request.setAttribute("hiddenColumnNumbers", hiddenColumnNumbers);
	}
	
	 
	/**
     * Gets the user detail on the basis of login name
     * @param loginName login Name
     * @return User object
     * @throws DAOException
     */
    public static User getUser(String loginName) throws DAOException
    {
    	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(User.class.getName());
    	String[] whereColumnName = {"activityStatus","loginName"};
    	String[] whereColumnCondition = {"=","="};
    	String[] whereColumnValue = {Constants.ACTIVITY_STATUS_ACTIVE, loginName};
    	
    	List users = userBizLogic.retrieve(User.class.getName(), whereColumnName, 
    			whereColumnCondition, whereColumnValue,Constants.AND_JOIN_CONDITION);
    	
    	if (!users.isEmpty())
    	{
    	    User validUser = (User)users.get(0);
    	    return validUser;
    	}
        return null;
    }
    public static List getParentContainerTypeList()
	{
		List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();
		
		storagePositionTypeList.add(new NameValueBean(Constants.SITE, 
							Constants.SITE));
		storagePositionTypeList.add(new NameValueBean(Constants.CDE_NAME_CONTAINER+" ("+
				Constants.STORAGE_TYPE_POSITION_AUTO+")", Constants.STORAGE_TYPE_POSITION_AUTO));
		storagePositionTypeList.add(new NameValueBean(Constants.CDE_NAME_CONTAINER+" ("+
				Constants.STORAGE_TYPE_POSITION_MANUAL+")", Constants.STORAGE_TYPE_POSITION_MANUAL));		
		return storagePositionTypeList;		
	}
    
    public static boolean checkForAllCurrentAndFutureCPs(AbstractDAO dao, String privilegeName, SessionDataBean sessionDataBean, String cpId)
    {
    	boolean allowOperation = false;
    	
    	String privilegeNames[] = privilegeName.split(",");
        Collection<Site> siteCollection = null;
        if (cpId != null && cpId.trim().length()!=0)
        {
            siteCollection = new CollectionProtocolBizLogic().getRelatedSites(Long.valueOf(cpId));
        } else 
        {
            Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
            if (siteIds != null && !siteIds.isEmpty())
            {
                siteCollection = new ArrayList<Site>();
                for (Long siteId : siteIds)
                {   
                    Site site = new Site();
                    site.setId(siteId);
                    siteCollection.add(site);
                   
                }
            }
        }
    	Set<Long> idSet = new HashSet<Long>();
    	
    	if(siteCollection == null)
    	{
    		return false;
    	}
    	
    	for(Site site : siteCollection)
    	{
    		idSet.add(site.getId());
    	}
    	// Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
		if (dao instanceof HibernateDAO)
		{
			try 
			{
				((HibernateDAO)dao).openSession(null);
			} 
			catch (DAOException e) 
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		for(Long id : idSet)
		{
			if(privilegeNames.length > 1)
			{
				if((PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName()).hasPrivilege(Constants.getCurrentAndFuturePGAndPEName(id), privilegeNames[0])) ||
					(PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName()).hasPrivilege(Constants.getCurrentAndFuturePGAndPEName(id), privilegeNames[1])))
					{	
						allowOperation = true;
					}
			}
			else if(PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName()).hasPrivilege(Constants.getCurrentAndFuturePGAndPEName(id), privilegeName))
			{
				allowOperation = true;
			}
			
			if(allowOperation)
			{
				return true;
			}
		}
		return false;
    }
    
	/**
	 * To distribute bean data in case C & F checkbox is checked 
	 * into 2 beans - 1 for CP privileges, other for Site privileges
	 */
	public static Map splitBeanData(SiteUserRolePrivilegeBean siteUserRolePrivBean)
	{
		Map<String, SiteUserRolePrivilegeBean> rowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		
		SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = siteUserRolePrivBean;
		
		List<Site> siteList = siteUserRolePrivilegeBean.getSiteList();
		
	    NameValueBean role = siteUserRolePrivilegeBean.getRole();
	    List<NameValueBean> sitePrivileges = new ArrayList<NameValueBean>();
	    List<NameValueBean> cpPrivileges = new ArrayList<NameValueBean>();
	    Set<String> sitePriv = getSitePrivileges();
	    Set<String> cpPriv = getCPPrivileges();
	    
	    List<NameValueBean> allPrivileges = siteUserRolePrivilegeBean.getPrivileges();
	    
	    for(NameValueBean nmv : allPrivileges)
	    {
	    	if(sitePriv.contains(nmv.getName()))
	    	{
	    		sitePrivileges.add(nmv);
	    	}
	    	else
	    	{
	    		cpPrivileges.add(nmv);
	    	}
	    }
	    
	    SiteUserRolePrivilegeBean bean1 = new SiteUserRolePrivilegeBean();
	    SiteUserRolePrivilegeBean bean2 = new SiteUserRolePrivilegeBean();
	    
	    bean1.setSiteList(siteList);
	    bean1.setRole(role);
        bean1.setUser(siteUserRolePrivBean.getUser());
	    bean1.setPrivileges(sitePrivileges);
	    bean1.setRowDeleted(siteUserRolePrivBean.isRowDeleted());
        bean1.setRowEdited(siteUserRolePrivBean.isRowEdited());
	    bean2.setSiteList(siteList);
        bean2.setUser(siteUserRolePrivBean.getUser());
	    bean2.setRole(role);
	    bean2.setPrivileges(cpPrivileges);
	    bean2.setAllCPChecked(true);
        bean2.setRowEdited(siteUserRolePrivBean.isRowEdited());
        bean2.setRowDeleted(siteUserRolePrivBean.isRowDeleted());
	    rowIdMap.put("SITE", bean1);
	    rowIdMap.put("CP", bean2);
	    
	    return rowIdMap;
	}
	
	
	public static Set getSitePrivileges()
	{
		List<NameValueBean> list = edu.wustl.common.util.global.Variables.privilegeGroupingMap.get("SITE");
		Set<String> sitePrivileges = new HashSet<String>();
		for(NameValueBean nmv : list)
		{
			sitePrivileges.add(nmv.getName());
		}
		return sitePrivileges;
	}
	
	public static Set getCPPrivileges()
	{
		List<NameValueBean> list = edu.wustl.common.util.global.Variables.privilegeGroupingMap.get("CP");
		Set<String> cpPrivileges = new HashSet<String>();
		for(NameValueBean nmv : list)
		{
			cpPrivileges.add(nmv.getName());
		}
		return cpPrivileges;
	}
	
	public static void processDeletedPrivileges(SiteUserRolePrivilegeBean siteUserRolePrivilegeBean)
	{
		SiteUserRolePrivilegeBean bean = siteUserRolePrivilegeBean;
		String groupName = null;
		String pgName = null;
		Site site = bean.getSiteList().get(0);
		User user = bean.getUser();
		CollectionProtocol cp = bean.getCollectionProtocol();
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		
		List<Group> grpList = new ArrayList<Group>();
		List<ProtectionGroup> pgList = new ArrayList<ProtectionGroup>();
		
		if(bean.getCollectionProtocol()!=null)
		{
			groupName = Constants.getCPUserGroupName(cp.getId(), user.getCsmUserId());
			pgName = Constants.getCollectionProtocolPGName(cp.getId());
		}
		else
		{
			if(bean.isAllCPChecked())
			{
				pgName = Constants.getCurrentAndFuturePGAndPEName(site.getId());
			}
			else
			{
				pgName = Constants.getSitePGName(site.getId());
			}
			
			groupName = Constants.getSiteUserGroupName(site.getId(), user.getCsmUserId());		
		}
		
		Group group = new Group();
		group.setGroupName(groupName);
		GroupSearchCriteria groupSearchCriteria = new GroupSearchCriteria(group);
		try 
		{
			grpList = privilegeUtility.getUserProvisioningManager().getObjects(groupSearchCriteria);
			
			if (grpList != null && !grpList.isEmpty())
			{
				group = grpList.get(0);
			}
			
			ProtectionGroup pg = new ProtectionGroup();
			pg.setProtectionGroupName(pgName);
			ProtectionGroupSearchCriteria pgSearchCriteria = new ProtectionGroupSearchCriteria(pg);
			pgList = privilegeUtility.getUserProvisioningManager().getObjects(pgSearchCriteria);
			if (pgList != null && !pgList.isEmpty())
			{
				pg = pgList.get(0);
			}
			
			new PrivilegeUtility().getUserProvisioningManager().removeGroupFromProtectionGroup(pg.getProtectionGroupId().toString(), group.getGroupId().toString());
			PrivilegeManager.getInstance().removePrivilegeCache(user.getLoginName());
		} 
		catch (CSException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
	}
	
	public static void processDeletedPrivilegesOnCPPage(SiteUserRolePrivilegeBean siteUserRolePrivilegeBean, Long cpId)
	{
		SiteUserRolePrivilegeBean bean = siteUserRolePrivilegeBean;
		String groupName = null;
		String pgName = null;
		User user = bean.getUser();
		
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		
		List<Group> grpList = new ArrayList<Group>();
		List<ProtectionGroup> pgList = new ArrayList<ProtectionGroup>();
		
		groupName = Constants.getCPUserGroupName(cpId, user.getCsmUserId());
		pgName = Constants.getCollectionProtocolPGName(cpId);
		
		Group group = new Group();
		group.setGroupName(groupName);
		GroupSearchCriteria groupSearchCriteria = new GroupSearchCriteria(group);
		try 
		{
			grpList = privilegeUtility.getUserProvisioningManager().getObjects(groupSearchCriteria);
			
			if (grpList != null && !grpList.isEmpty())
			{
				group = grpList.get(0);
			}
			
			ProtectionGroup pg = new ProtectionGroup();
			pg.setProtectionGroupName(pgName);
			ProtectionGroupSearchCriteria pgSearchCriteria = new ProtectionGroupSearchCriteria(pg);
			pgList = privilegeUtility.getUserProvisioningManager().getObjects(pgSearchCriteria);
			if (pgList != null && !pgList.isEmpty())
			{
				pg = pgList.get(0);
			}
			
			if(pgList.isEmpty() || grpList.isEmpty())
			{
				return;
			}
			new PrivilegeUtility().getUserProvisioningManager().removeGroupFromProtectionGroup(pg.getProtectionGroupId().toString(), group.getGroupId().toString());
			PrivilegeManager.getInstance().removePrivilegeCache(user.getLoginName());
		} 
		catch (CSException e) 
		{
			Logger.out.debug(e.getMessage(), e);
		}
	}
	
	/**
	 * @param collectionProtocol
	 * @param session 
	 * @return
	 */
	public static CollectionProtocolDTO getCoolectionProtocolDTO(CollectionProtocol collectionProtocol, HttpSession session)
	{
		CollectionProtocolDTO collectionProtocolDTO = new CollectionProtocolDTO();
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap  = (Map<String, SiteUserRolePrivilegeBean>)session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		collectionProtocolDTO.setCollectionProtocol(collectionProtocol);
		collectionProtocolDTO.setRowIdBeanMap(rowIdBeanMap);
		return collectionProtocolDTO;
	}
	
	public static UserNotAuthorizedException getUserNotAuthorizedException(String privilegeName, String protectionElementName)
	{
		 UserNotAuthorizedException ex = new UserNotAuthorizedException();
         ex.setPrivilegeName(privilegeName);
         if (protectionElementName != null && (protectionElementName.contains("Site") || protectionElementName.contains("CollectionProtocol")))
         {
             String [] arr = protectionElementName.split("_");
             String [] nameArr = arr[0].split("\\.");
             String baseObject = nameArr[nameArr.length-1];
             ex.setBaseObject(baseObject);
             ex.setBaseObjectIdentifier(arr[1]);
         }
         return ex;
	}
	
	public static boolean hasPrivilegeToView(String objName, Long identifier, SessionDataBean sessionDataBean, String privilegeName)
	{
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}
		
		List cpIdsList = new ArrayList();
		Set<Long> cpIds = new HashSet<Long>();
		
		cpIdsList = Utility.getCPIdsList(objName, identifier, sessionDataBean, cpIdsList);
		
		if(cpIdsList == null)
		{
			return false;
		}
		
		if(cpIdsList.isEmpty())
		{
			return new CSMValidator().hasPrivilegeToViewGlobalParticipant(sessionDataBean);
		}
		
		for(Object cpId : cpIdsList)
		{
			cpId = cpIdsList.get(0);
			cpIds.add(Long.valueOf(cpId.toString()));
		}
		
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME).append("_");
		boolean isPresent = false;
		
		for (Long cpId : cpIds)
		{
			String [] privilegeNames = privilegeName.split(",");
			if(privilegeNames.length > 1)
			{	
				isPresent = privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), privilegeNames[0]);
				
				if(!isPresent)
				{
					isPresent = checkForAllCurrentAndFutureCPs(null, privilegeNames[0], sessionDataBean, cpId.toString());
				}
				
				if(isPresent)
				{
					isPresent = privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), privilegeNames[1]);
					isPresent = !isPresent;
				}
			}
			
			if (!isPresent)
			{
				return false;
			} 
		}
    	return true;
	}
}