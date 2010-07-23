/**
 * <p>Title: AppUtility Class>
 * <p>Description:  AppUtility Class contain general methods used through out the application. </p>
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.actionForm.IPrinterTypeLocation;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SiteBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
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
import edu.wustl.catissuecore.tree.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.CSMValidator;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.exception.PasswordEncryptionException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.PagenatedResultData;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.query.beans.QueryResultObjectDataBean;
import edu.wustl.query.bizlogic.QueryOutputSpreadsheetBizLogic;
import edu.wustl.query.executor.AbstractQueryExecutor;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.exception.UserNotAuthorizedException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.locator.CSMGroupLocator;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import edu.wustl.security.privilege.PrivilegeUtility;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;
import edu.wustl.wustlkey.util.global.WUSTLKeyUtility;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionGroupSearchCriteria;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * AppUtility Class contain general methods used through out the application.
 *
 * @author kapil_kaveeshwar
 */
public class AppUtility
{

	/**
	 * Class Logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(AppUtility.class);

	public static Set getSpecimenClassCDE()
	{
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		return setPV;
	}

	/* Method returns the storage position list */
	public static List getStoragePositionTypeList()
	{
		final List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();

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
		final List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();

		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_AUTO,
				Constants.STORAGE_TYPE_POSITION_AUTO_VALUE_FOR_TRANSFER_EVENT));
		storagePositionTypeList.add(new NameValueBean(Constants.STORAGE_TYPE_POSITION_MANUAL,
				Constants.STORAGE_TYPE_POSITION_MANUAL_VALUE_FOR_TRANSFER_EVENT));

		return storagePositionTypeList;
	}

	public static List getSpecimenClassList()
	{
		final List specimenClassList = new ArrayList();
		final Set setPV = getSpecimenClassCDE();
		final Iterator itr = setPV.iterator();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		while (itr.hasNext())
		{
			final Object obj = itr.next();
			final PermissibleValue persmissibleVal = (PermissibleValue) obj;
			final String tmpStr = persmissibleVal.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));
		}
		return specimenClassList;

	}

	public static Map<String, List<NameValueBean>> getSpecimenTypeMap()
	{
		final Set setPV = getSpecimenClassCDE();
		final Iterator itr = setPV.iterator();
		final Map<String, List<NameValueBean>> subTypeMap =
			new HashMap<String, List<NameValueBean>>();
		while (itr.hasNext())
		{
			final List<NameValueBean> innerList = new ArrayList<NameValueBean>();
			final Object obj = itr.next();
			final PermissibleValue permissibleVal = (PermissibleValue) obj;
			final Set list1 = permissibleVal.getSubPermissibleValues();
			final Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				final Object obj1 = itr1.next();
				final PermissibleValue pv1 = (PermissibleValue) obj1;
				// Setting Specimen Type
				final String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(permissibleVal.getValue(), innerList);
		}
		return subTypeMap;
	}

	public static List<NameValueBean> getSpecimenTypes(String specimenClass)
	{
		final Map<String, List<NameValueBean>> specimenTypeMap = getSpecimenTypeMap();
		final List<NameValueBean> typeList = (List<NameValueBean>) specimenTypeMap.get(specimenClass);
		return typeList;
	}

	public static String getSpecimenClassName(AbstractSpecimen specimen)
	{
		if (specimen instanceof CellSpecimen)
		{
			return Constants.CELL;
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			return Constants.MOLECULAR;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			return Constants.FLUID;
		}
		else if (specimen instanceof TissueSpecimen)
		{
			return Constants.TISSUE;
		}

		return null;
	}

	public static int getEventParametersFormId(SpecimenEventParameters eventParameter)
	{
		if (eventParameter instanceof CheckInCheckOutEventParameter)
		{
			return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof CollectionEventParameters)
		{
			return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof EmbeddedEventParameters)
		{
			return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof FixedEventParameters)
		{
			return Constants.FIXED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof FrozenEventParameters)
		{
			return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof ReceivedEventParameters)
		{
			return Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof TissueSpecimenReviewEventParameters)
		{
			return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof TransferEventParameters)
		{
			return Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof DisposalEventParameters)
		{
			return Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID;
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
	public static boolean isQuantityDouble(String className, String type)
	{
		if (Constants.CELL.equals(className))
		{
			return false;
		}
		else if (Constants.TISSUE.equals(className))
		{
			if (Constants.MICRODISSECTED.equals(type) || Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
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
	public static String getUnit(String className, String type)
	{
		if (className == null || type == null || className.equals("-1"))
		{
			return "";
		}

		if (Constants.CELL.equals(className))
		{
			return Constants.UNIT_CC;
		}
		else if (Constants.FLUID.equals(className))
		{
			return Constants.UNIT_ML;
		}
		else if (Constants.MOLECULAR.equals(className))
		{
			return Constants.UNIT_MG;
		}
		else if (Constants.TISSUE.equals(className))
		{
			if (Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type))
			{
				return Constants.UNIT_CN;
			}
			else if (Constants.MICRODISSECTED.equals(type))
			{
				return Constants.UNIT_CL;
			}
			else
			{
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
	public static Specimen getSpecimen(Specimen specimen)
	{
		if (specimen instanceof CellSpecimen)
		{
			return new CellSpecimen();
		}
		else if (specimen instanceof FluidSpecimen)
		{
			return new FluidSpecimen();
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			return new MolecularSpecimen();
		}
		else if (specimen instanceof TissueSpecimen)
		{
			return new TissueSpecimen();
		}

		return new Specimen();
	}

	public static List getSpecimenClassTypes()
	{
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		final Iterator itr = setPV.iterator();

		final List specimenClassTypeList = new ArrayList();

		while (itr.hasNext())
		{

			final Object obj = itr.next();
			final PermissibleValue permissibleVal = (PermissibleValue) obj;
			final String tmpStr = permissibleVal.getValue();
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
		final List storageTypeList = new ArrayList();
		final Iterator typeItr = list.iterator();
		while (typeItr.hasNext())
		{
			final StorageType type = (StorageType) typeItr.next();
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
			storageTypeList.add(0, new NameValueBean(Constants.SELECT_OPTION, "-1"));
		}
		return storageTypeList;

	}

	/*
	 * this Function gets the list of all Specimen Class Types as argument and
	 * create a list in which nameValueBean is stored with Name and Identifier
	 * of specimen Class Type. and returns this list
	 */

	public static List getSpecimenClassTypeListWithAny()
	{
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		final Iterator itr = setPV.iterator();

		final List specimenClassTypeList = new ArrayList();
		specimenClassTypeList.add(new NameValueBean("--All--", "-1"));

		while (itr.hasNext())
		{
			// List innerList = new ArrayList();
			final Object obj = itr.next();
			final PermissibleValue permissibleVal = (PermissibleValue) obj;
			final String tmpStr = permissibleVal.getValue();
			logger.info("specimen class:" + tmpStr);
			specimenClassTypeList.add(new NameValueBean(tmpStr, tmpStr));

		} // class and values set

		return specimenClassTypeList;

	}

	/*
	 * This function gets the list of all collection protocols as argument and
	 * create a list in which nameValueBean is stored with Title and Identifier
	 * of Collection Protocol. and returns this list
	 */
	public static List getCollectionProtocolList(List list)
	{
		final List collectionProtocolList = new ArrayList();

		final Iterator cpItr = list.iterator();
		while (cpItr.hasNext())
		{
			final CollectionProtocol collProtocol = (CollectionProtocol) cpItr.next();
			collectionProtocolList.add(new NameValueBean(collProtocol.getTitle(), collProtocol.getId()));
		}
		Collections.sort(collectionProtocolList);
		collectionProtocolList.add(0, new NameValueBean(Constants.SELECT_OPTION, "-1"));
		return collectionProtocolList;
	}

	/*
	 * this Function gets the list of all specimen Array types as argument and
	 * create a list in which nameValueBean is stored with specimen array and
	 * Identifier of specimen Array type. and returns this list
	 */
	public static List getSpecimenArrayTypeList(List list)
	{
		NameValueBean typeAny = null;
		final List spArrayTypeList = new ArrayList();
		final Iterator typeItr = list.iterator();

		while (typeItr.hasNext())
		{
			final SpecimenArrayType type = (SpecimenArrayType) typeItr.next();
			if (type.getId().longValue() == 2)
			{
				typeAny = new NameValueBean(Constants.HOLDS_ANY, type.getId());
			}
			else
			{
				spArrayTypeList.add(new NameValueBean(type.getName(), type.getId()));
			}
		}
		Collections.sort(spArrayTypeList);
		if (typeAny != null)
		{
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
	public static int getMonth(String date, String pattern)
	{
		int month = 0;
		month = getCalendar(date, pattern).get(Calendar.MONTH);
		month = month + 1;
		return month;
	}

	public static int getMonth(String date)
	{
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
	public static int getDay(String date, String pattern)
	{
		int day = 0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static int getDay(String date)
	{
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
	public static int getYear(String date, String pattern)
	{
		int year = 0;
		year = getCalendar(date, pattern).get(Calendar.YEAR);
		return year;
	}

	public static int getYear(String date)
	{
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
	private static Calendar getCalendar(String date, String pattern)
	{
		Calendar calendar = Calendar.getInstance();
		try
		{
			if(!Constants.DOUBLE_QUOTES.equals(date.trim()))
			{
				final SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
				final Date givenDate = dateformat.parse(date);
				calendar.setTime(givenDate);
			}
		}
		catch (final Exception e)
		{
			AppUtility.logger.error(e.getMessage(),e);
		}
		return calendar;
	}

	public static void main(String[] args)
	{
		/*String s = "ds fsbsdjf hsfsdfdsh f,sd fsdfjbhsdj sdf,sdf s,sd fds,sd ffs,sd f\"sd fs \"sd fsdF \"sf";
		Object s1 = toNewGridFormat(s);
		System.out.println("Original String : " + s);
		System.out.println("Updated String : " + s1);*/

		// // AppUtility u = new AppUtility();
		// String dt = "18-10-06";
		// String pt = "dd-MM-yy";
		//
		// System.out.println(AppUtility.getMonth(dt, pt) + "/" +
		// AppUtility.getDay(dt, pt) + "/"
		// + AppUtility.getYear(dt, pt));
		//
		// dt = "28-11-06";
		// pt = "dd-MM-yy";
		// System.out.println(AppUtility.getMonth(dt, pt) + "/" +
		// AppUtility.getDay(dt, pt) + "/"
		// + AppUtility.getYear(dt, pt));
		//
		// dt = "18-21-06";
		// pt = "MM-dd-yy";
		// System.out.println(AppUtility.getMonth(dt, pt) + "/" +
		// AppUtility.getDay(dt, pt) + "/"
		// + AppUtility.getYear(dt, pt));
		//
		// dt = "18-asa-06";
		// pt = "dd-MM-yy";
		// System.out.println(AppUtility.getMonth(dt, pt) + "/" +
		// AppUtility.getDay(dt, pt) + "/"
		// + AppUtility.getYear(dt, pt));
		final ArrayList<String> attributeValuesInProperOrder = getAttributeValuesInProperOrder(
				"date", "13-02-2006", "12-02-2006");
	}

	/**
	 * This method returns a list of string values for a given CDE.
	 *
	 * @param cdeName
	 * @return
	 */
	public static List getListForCDE(String cdeName)
	{
		final CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
		final List valueList = new ArrayList();

		if (cde != null)
		{
			final Iterator iterator = cde.getPermissibleValues().iterator();
			while (iterator.hasNext())
			{
				final PermissibleValue permissibleValue = (PermissibleValue) iterator.next();

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
	private static List loadPermissibleValue(PermissibleValue permissibleValue)
	{
		final List pvList = new ArrayList();
		final String value = permissibleValue.getValue();
		pvList.add(value);

		final Iterator iterator = permissibleValue.getSubPermissibleValues().iterator();
		while (iterator.hasNext())
		{
			final PermissibleValue subPermissibleValue = (PermissibleValue) iterator.next();
			final List subPVList = loadPermissibleValue(subPermissibleValue);
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
	public static Object toNewGridFormat(Object obj)
	{
		obj = Utility.toGridFormat(obj);
		if (obj instanceof String)
		{
			final String objString = (String) obj;
			final StringBuffer tokenedString = new StringBuffer();

			final StringTokenizer tokenString = new StringTokenizer(objString, ",");

			while (tokenString.hasMoreTokens())
			{
				tokenedString.append(tokenString.nextToken() + " ");
			}
			final String gridFormattedStr = new String(tokenedString);
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
		final List<NameValueBean> listOfResponces = new ArrayList<NameValueBean>();
		listOfResponces.add(new NameValueBean(Constants.NOT_SPECIFIED, Constants.NOT_SPECIFIED));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_YES, Constants.BOOLEAN_YES));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_NO, Constants.BOOLEAN_NO));
		if (addeditOperation.equalsIgnoreCase(Constants.EDIT))
		{
			listOfResponces.add(new NameValueBean(Constants.WITHDRAWN, Constants.WITHDRAWN));
		}
		return listOfResponces;
	}

	public static Long toLong(String string) throws NumberFormatException
	{
		if ((string != null) && (string.trim() != ""))
		{
			return Long.valueOf(string);
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
	 * @see edu.wustl.catissuecore.util.global.AppUtility#toNewGridFormat(java.lang.Object)
	 *      Patch ID: SimpleSearchEdit_6
	 */
	public static Object toNewGridFormatWithHref(List<String> row,
			Map<Integer, QueryResultObjectData> hyperlinkColumnMap, int index)
	{
		Object obj = row.get(index);

		if (obj instanceof String)
		{
			obj = toNewGridFormat(obj);

			final QueryResultObjectData queryResultObjectData = hyperlinkColumnMap.get(index);
			/**
			 * row contains '##' means the user is not authorized to see the page in edit mode
			 * thus column is not shown as hyperlink.
			 */
			if (!row.contains("##"))//bug 12280
			{
				if (queryResultObjectData != null)// This column is to be shown as
				// hyperlink.
				{
					if (obj == null || obj.equals(""))
					{
						obj = "NA";
					}

					/**
					 * Name : Prafull Bug ID: 4223 Patch ID: 4223_1 Description:
					 * Edit User: password fields empty & error on submitting new
					 * password Added PageOf Attribute as request parameter in the
					 * link.
					 */
					final String aliasName = queryResultObjectData.getAliasName();
					final String link = "SimpleSearchEdit.do?"
							+ edu.wustl.common.util.global.Constants.TABLE_ALIAS_NAME + "="
							+ aliasName + "&"
							+ edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER + "="
							+ row.get(queryResultObjectData.getIdentifierColumnId()) + "&"
							+ Constants.PAGE_OF + "=" + Variables.aliasAndPageOfMap.get(aliasName);
					/**
					 * bug ID: 4225 Patch id: 4225_1 Description : Passing a
					 * different name to the popup window
					 */
					final String onclickStr = " onclick=javascript:NewWindow('" + link
							+ "','simpleSearch','800','600','yes') ";
					final String hrefTag = "<a class='normalLink' href='#'" + onclickStr + ">"
							+ obj + "</a>";
					// String hrefTag = "<a href='"+ link+ "'>"+obj+"</a>";
					obj = hrefTag;
				}
			}
		}
		return obj;
	}

	/**
	 * This method creates a comma separated string of numbers representing
	 * column width.
	 *
	 */
	public static String getColumnWidth(List columnNames)
	{
		String colWidth = getColumnWidth((String) columnNames.get(0));

		for (int col = 1; col < columnNames.size(); col++)
		{
			final String columnName = (String) columnNames.get(col);
			colWidth = colWidth + "," + getColumnWidth(columnName);
		}
		return colWidth;
	}

	private static String getColumnWidth(String columnName)
	{
		/*
		 * Patch ID: Bug#3090_31 Description: The first column which is just a
		 * checkbox, used to select the rows, was always given a width of 100.
		 * Now width of 20 is set for the first column. Also, width of 100 was
		 * being applied to each column of the grid, which increasing the total
		 * width of the grid. Now the width of each column is set to 80.
		 */
		String columnWidth = null;
		if ("ID".equals(columnName.trim()))
		{
			columnWidth = "0";
		}
		else if ("".equals(columnName.trim()))
		{
			columnWidth = "80";
		}
		else
		{
			columnWidth = "150";
		}
		return columnWidth;
	}

	/**
	 * This method set TissueList with only Leaf node.
	 *
	 * @return tissueList
	 * @throws BizLogicException BizLogic Exception.
	 */
	public static List tissueSiteList() throws BizLogicException
	{
		final CDE cde = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_TISSUE_SITE);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final CDEBizLogic cdeBizLogic = (CDEBizLogic) factory.getBizLogic(Constants.CDE_FORM_ID);
		final List tissueList = new ArrayList();
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
	public static boolean validateSpecimenTypeClass(String specimenClass, String specimenType)
			throws ApplicationException
	{
		final List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		if (specimenClass == null || !Validator.isEnumeratedValue(specimenClassList, specimenClass))
		{
			final ErrorKey errorKey = ErrorKey.getErrorKey("protocol.class.errMsg");
			throw new DAOException(errorKey, null, "");
		}
		if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass), specimenType))
		{
			final ErrorKey errorKey = ErrorKey.getErrorKey("protocol.type.errMsg");
			throw new DAOException(errorKey, null, "");
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
	public static List getListFromCDE(String listType)
	{
		final List CDEList = CDEManager.getCDEManager().getPermissibleValueList(listType, null);
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
	 * @throws DAOException DAO Exception
	 * @throws BizLogicException BizLogic Exception
	 * @throws Exception
	 *             generic exception
	 */
	public static String getToolTipText(NewSpecimenForm specimenForm) throws ApplicationException
	{
		final StringBuffer toolTipText = new StringBuffer("");

		if (specimenForm != null)
		{
			/**
			 * Patch ID: 4176_2 See also: 1-3 Description: Field name and field
			 * value was displayed in different style. Italics style for value
			 * is removed.
			 */
			toolTipText
					.append("<HTML><table border='0'><tr><td colspan='2'><b>CollectedEvents</b></td></tr><tr><td>");
			toolTipText.append("Collector: ");
			toolTipText.append(AppUtility.getUserNameById(specimenForm.getCollectionEventUserId()));
			toolTipText.append("</td><td>Date: ");
			toolTipText.append(specimenForm.getCollectionEventdateOfEvent());
			toolTipText.append("</td></tr><tr><td>Procedure: ");
			toolTipText.append(specimenForm.getCollectionEventCollectionProcedure());
			toolTipText.append("</td><td>Container: ");
			toolTipText.append(specimenForm.getCollectionEventContainer());

			toolTipText.append("</td></tr><tr><td colspan='2'><b>Received Events</b></td></tr>");
			toolTipText.append("<tr><td>Reciever: ");
			toolTipText.append(AppUtility.getUserNameById(specimenForm.getReceivedEventUserId()));
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
	 * @throws BizLogicException BizLogic Exception
	 * @throws DAOException generic DAO Exception
	 */
	public static String getUserNameById(Long userId) throws BizLogicException, DAOException
	{
		final String className = User.class.getName();
		final String colName = edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER;
		final String colValue = userId.toString();
		String userName = "";
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic bizLogic = (UserBizLogic) factory.getBizLogic(User.class.getName());
		final List userList = bizLogic.retrieve(className, colName, colValue);
		if (userList != null && userList.size() > 0)
		{
			final User user = (User) userList.get(0);
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
			SpecimenCollectionGroupForm specimenCollectionGroupForm)
	{
		final NewSpecimenForm newSpecimenForm = (NewSpecimenForm) form;

		newSpecimenForm.setCollectionEventId(specimenCollectionGroupForm.getCollectionEventId());
		newSpecimenForm.setCollectionEventSpecimenId(specimenCollectionGroupForm
				.getCollectionEventSpecimenId());
		newSpecimenForm.setCollectionEventUserId(specimenCollectionGroupForm
				.getCollectionEventUserId());
		newSpecimenForm.setCollectionEventdateOfEvent(specimenCollectionGroupForm
				.getCollectionEventdateOfEvent());
		newSpecimenForm.setCollectionEventTimeInHours(specimenCollectionGroupForm
				.getCollectionEventTimeInHours());
		newSpecimenForm.setCollectionEventTimeInMinutes(specimenCollectionGroupForm
				.getCollectionEventTimeInMinutes());
		newSpecimenForm.setCollectionEventCollectionProcedure(specimenCollectionGroupForm
				.getCollectionEventCollectionProcedure());
		newSpecimenForm.setCollectionEventContainer(specimenCollectionGroupForm
				.getCollectionEventContainer());
		newSpecimenForm.setCollectionEventComments(specimenCollectionGroupForm
				.getCollectionEventComments());

		newSpecimenForm.setReceivedEventId(specimenCollectionGroupForm.getReceivedEventId());
		newSpecimenForm.setReceivedEventSpecimenId(specimenCollectionGroupForm
				.getReceivedEventSpecimenId());
		newSpecimenForm
				.setReceivedEventUserId(specimenCollectionGroupForm.getReceivedEventUserId());
		newSpecimenForm.setReceivedEventDateOfEvent(specimenCollectionGroupForm
				.getReceivedEventDateOfEvent());
		newSpecimenForm.setReceivedEventTimeInHours(specimenCollectionGroupForm
				.getReceivedEventTimeInHours());
		newSpecimenForm.setReceivedEventTimeInMinutes(specimenCollectionGroupForm
				.getReceivedEventTimeInMinutes());
		newSpecimenForm.setReceivedEventReceivedQuality(specimenCollectionGroupForm
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
	 * @throws BizLogicException BizLogic Exception.
	 */
	public static SpecimenCollectionGroup getSpecimenCollectionGroup(
			String specimenCollectionGroupId) throws ApplicationException
	{
		final String sourceObjectName = SpecimenCollectionGroup.class.getName();

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final SpecimenCollectionGroupBizLogic scgbizLogic = (SpecimenCollectionGroupBizLogic) factory
				.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		final Object object = scgbizLogic.retrieve(sourceObjectName, Long
				.valueOf(specimenCollectionGroupId));
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (object != null)
		{
			specimenCollectionGroup = (SpecimenCollectionGroup) object;
		}

		return specimenCollectionGroup;
	}

	public static String getSCGId(String scgName) throws Exception
	{
		String scgId = Utility.toString(null);
		final String sourceObjectName = SpecimenCollectionGroup.class.getName();
		final String[] selectColumnName = new String[]{"id"};
		final String[] whereColumnName = new String[]{"name"};
		final String[] whereColumnCondition = new String[]{"="};
		final Object[] whereColumnValue = new String[]{scgName};
		final String joinCondition = null;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final SpecimenCollectionGroupBizLogic scgBizLogic = (SpecimenCollectionGroupBizLogic) factory
				.getBizLogic(SpecimenCollectionGroup.class.getName());
		final List scgList = scgBizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (scgList.size() > 0)
		{
			scgId = Utility.toString(scgList.get(0));
		}
		return scgId;
	}

	/**
	 * Generates key for ParticipantMedicalIdentifierMap
	 *
	 * @param identifier
	 *            serial number
	 * @param keyFor
	 *            atribute based on which rspective key is to generate
	 * @return key for map attribute
	 */
	public static String getParticipantMedicalIdentifierKeyFor(int identifier, String keyFor)
	{
		return (Constants.PARTICIPANT_MEDICAL_IDENTIFIER + identifier + keyFor);
	}

	/**
	 * To get the array of ids from the given DomainObject collection.
	 *
	 * @param domainObjectCollection
	 *            The collectio of domain objects.
	 * @return The array of ids from the given DomainObject collection.
	 */
	public static long[] getobjectIds(Collection domainObjectCollection)
	{
		final long ids[] = new long[domainObjectCollection.size()];
		int counter = 0;
		final Iterator iterator = domainObjectCollection.iterator();
		while (iterator.hasNext())
		{
			final AbstractDomainObject domainObject = (AbstractDomainObject) iterator.next();
			ids[counter] = domainObject.getId().longValue();
			counter++;
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
			QuerySessionData querySessionData) throws ApplicationException
	{
		List paginationDataList;
		querySessionData.setRecordsPerPage(recordsPerPage);
		final int startIndex = recordsPerPage * (pageNum - 1);
		final QueryBizLogic qBizLogic = new QueryBizLogic();
		final PagenatedResultData pagenatedResultData = qBizLogic.execute(sessionData,
				querySessionData, startIndex);
		paginationDataList = pagenatedResultData.getResult();
		final String isSimpleSearch = (String) request.getSession().getAttribute(
				Constants.IS_SIMPLE_SEARCH);
		if (isSimpleSearch == null || (!isSimpleSearch.equalsIgnoreCase(Constants.TRUE)))
		{
			final Map<Long, QueryResultObjectDataBean> queryResultObjectDataBeanMap = querySessionData
					.getQueryResultObjectDataMap();
			if (queryResultObjectDataBeanMap != null)
			{
				for (final Long id : queryResultObjectDataBeanMap.keySet())
				{
					final QueryResultObjectDataBean bean = queryResultObjectDataBeanMap.get(id);
					if (bean.isClobeType())
					{
						final List<String> columnsList = (List<String>) request.getSession()
								.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
						final QueryOutputSpreadsheetBizLogic queryBizLogic = new QueryOutputSpreadsheetBizLogic();
						final Map<Integer, Integer> fileTypeIndexMainEntityIndexMap = queryBizLogic
								.updateSpreadSheetColumnList(columnsList,
										queryResultObjectDataBeanMap);
						//	QueryOutputSpreadsheetBizLogic.updateDataList(paginationDataList, fileTypeIndexMainEntityIndexMap);
						final Map exportMetataDataMap = QueryOutputSpreadsheetBizLogic
								.updateDataList(paginationDataList, fileTypeIndexMainEntityIndexMap);
						request.getSession().setAttribute(Constants.ENTITY_IDS_MAP,
								exportMetataDataMap.get(Constants.ENTITY_IDS_MAP));
						request.getSession().setAttribute(Constants.EXPORT_DATA_LIST,
								exportMetataDataMap.get(Constants.EXPORT_DATA_LIST));
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
	public static List<Object[]> executeQuery(String hql) throws ApplicationException
	{
		final IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
				Constants.APPLICATION_NAME);
		final DAO dao = daofactory.getDAO();
		dao.openSession(null);
		final List list = dao.executeQuery(hql);
		dao.closeSession();
		return list;
	}

	/**
	 * Executes sql Query and returns the results.
	 *
	 * @param sql
	 *            String hql
	 * @throws DAOException
	 *             DAOException
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	public static List executeSQLQuery(String sql) throws ApplicationException
	{
		final JDBCDAO jdbcDAO = openJDBCSession();
		final List list = jdbcDAO.executeQuery(sql);
		closeJDBCSession(jdbcDAO);
		return list;
	}

	/**
	 * Executes sql Query and returns the results.
	 * @param sql sql query String
	 * @param valueBeanList list of column value beans
	 * @return results list
	 * @throws ApplicationException instance of ApplicationException
	 */
	public static List executeSQLQuery(String sql, List<ColumnValueBean> valueBeanList) throws ApplicationException
	{
		final JDBCDAO jdbcDAO = openJDBCSession();
		final List list = jdbcDAO.executeQuery(sql,null , valueBeanList);
		closeJDBCSession(jdbcDAO);
		return list;
	}

	public static Long getLastAvailableValue(String sql) throws ApplicationException
	{
		Long noOfRecords = Long.valueOf("0");
		List list = null;
		List records = null;
		try
		{
			list = executeSQLQuery(sql.toString());
			if (list != null && list.size() > 0)
			{

				records = (List) list.get(0);
				if (records != null && records.size() > 0 && !records.isEmpty())
				{
					if (!((String) records.get(0)).equals(""))
					{
						noOfRecords = Long.valueOf((String) records.get(0));
					}
				}
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
		}
		catch (final ApplicationException e)
		{
			AppUtility.logger.error(e.getMessage(),e);
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new ApplicationException(e.getErrorKey(), e, e.getMessage());
		}
		return noOfRecords;
	}

	// for conflictResolver pagination:kalpana
	public static PagenatedResultData executeForPagination(String sql,
			SessionDataBean sessionDataBean, boolean isSecureExecute, Map queryResultObjectDataMap,
			boolean hasConditionOnIdentifiedField, int startIndex, int totoalRecords)
			throws DAOException, SQLException
	{
		try
		{
			final IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME);
			final JDBCDAO dao = daofactory.getJDBCDAO();
			dao.openSession(null);
			logger.debug("SQL************" + sql);
			final AbstractQueryExecutor queryExecutor = edu.wustl.query.util.global.Utility
					.getQueryExecutor();
			final PagenatedResultData pagenatedResultData = queryExecutor.getQueryResultList(sql,
					null, sessionDataBean, isSecureExecute, hasConditionOnIdentifiedField,
					queryResultObjectDataMap, startIndex, totoalRecords);
			dao.closeSession();
			return pagenatedResultData;
		}
		catch (final SMException exception)
		{
			AppUtility.logger.error("Security Exception:"+exception.getMessage(), exception);
			exception.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("sm.operation.error");
			throw new DAOException(errorKey, exception, "");
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
	public static String getQueryTitle(String title)
	{
		String multilineTitle = "";
		if (title.length() <= Constants.CHARACTERS_IN_ONE_LINE)
		{
			multilineTitle = title;
		}
		else
		{
			multilineTitle = title.substring(0, Constants.CHARACTERS_IN_ONE_LINE) + ".....";
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
	public static String getTooltip(String title)
	{
		final String tooltip = title.replaceAll("'", Constants.SINGLE_QUOTE_ESCAPE_SEQUENCE); // escape sequence
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
	public static boolean isQuarantined(Long reportId) throws ApplicationException
	{
		final String hqlString = "select ispr.deIdentifiedSurgicalPathologyReport.id "
				+ " from edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport as ispr, "
				+ " edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport as deidReport"
				+ " where ispr.id = " + reportId
				+ " and ispr.deIdentifiedSurgicalPathologyReport.id=deidReport.id"
				+ " and ispr.deIdentifiedSurgicalPathologyReport.isQuarantined='"
				+ Constants.QUARANTINE_REQUEST + "'";

		final List reportIDList = AppUtility.executeQuery(hqlString);
		if (reportIDList != null && reportIDList.size() > 0)
		{
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
	public static ArrayList<String> getAttributeValuesInProperOrder(String dataType, String value1,
			String value2)
	{
		String val1 = value1;
		String val2 = value2;
		final ArrayList<String> attributeValues = new ArrayList<String>();
		if (dataType.equalsIgnoreCase(EntityManagerConstantsInterface.DATE_ATTRIBUTE_TYPE))
		{
			final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try
			{
				final Date date1 = dateFormat.parse(value1);
				final Date date2 = dateFormat.parse(value2);
				if (date1.after(date2))
				{
					val1 = value2;
					val2 = value1;
				}
			}
			catch (final ParseException e)
			{
				AppUtility.logger.error("Can not parse the given date" +
						" in getAttributeValuesInProperOrder() method :"
								+ e.getMessage(),e);
				e.printStackTrace();
			}
		}
		else
		{
			if (dataType.equalsIgnoreCase(EntityManagerConstantsInterface.INTEGER_ATTRIBUTE_TYPE)
					|| dataType
							.equalsIgnoreCase(EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE))
			{
				if (Long.parseLong(value1) > Long.parseLong(value2))
				{
					val1 = value2;
					val2 = value1;
				}

			}
			else
			{
				if (dataType
						.equalsIgnoreCase(EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE))
				{
					if (Double.parseDouble(value1) > Double.parseDouble(value2))
					{
						val1 = value2;
						val2 = value1;
					}

				}
			}
		}
		attributeValues.add(val1);
		attributeValues.add(val2);
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
	public static Date getNewDateByAdditionOfDays(Date date, int daysToBeAdded)
	{
		final Calendar calendar = new GregorianCalendar();
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
			final String sourceObjectName = User.class.getName();
			final String[] selectColumnName = new String[]{edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
			//String[] whereColumnName = new String[]{Constants.LOGINNAME};
			//String[] whereColumnCondition = new String[]{"="};
			//String[] whereColumnValue = new String[]{sessionDataBean.getUserName()};
			//String joinCondition = "";

			final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.addCondition(new EqualClause(Constants.LOGINNAME, sessionDataBean
					.getUserName()));

			final List userIDList = dao.retrieve(sourceObjectName, selectColumnName,
					queryWhereClause);
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
		final Iterator orderItemIdsIterator = orderItemIds.iterator();
		String ids = "";

		while (orderItemIdsIterator.hasNext())
		{
			ids = ids + orderItemIdsIterator.next();
			if (counter < orderItemIds.size())
			{
				ids = ids + ",";
			}
			counter++;
		}
		return ids;
	}

	/**
	 * This function will return CollectionProtocolRegistration object
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return collectionProtocolRegistration
	 */
	public static CollectionProtocolRegistration getcprObj(String cpr_id)
			throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final CollectionProtocolRegistrationBizLogic collectionProtocolRegistrationBizLogic = (CollectionProtocolRegistrationBizLogic) factory
				.getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);

		final Object object = collectionProtocolRegistrationBizLogic.retrieve(
				CollectionProtocolRegistration.class.getName(), Long.valueOf(cpr_id));
		final CollectionProtocolRegistration collectionProtocolRegistrationObject = (CollectionProtocolRegistration) object;
		return collectionProtocolRegistrationObject;
	}

	/**
	 * This function will return SpecimenCollectionGroup object
	 * @param scg_id Selected SpecimenCollectionGroup ID
	 * @return specimenCollectionGroupObject
	 */
	public static SpecimenCollectionGroup getSCGObj(String scg_id, DAO dao)
			throws ApplicationException
	{
		final Object object = dao.retrieveById(SpecimenCollectionGroup.class.getName(), Long
				.valueOf(scg_id));
		SpecimenCollectionGroup specimenCollectionGroupObject = null;
		if (object != null)
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
			throws ApplicationException
	{
		long collectionEventUserId = 0;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, userCollection);
		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		if (sessionData != null)
		{
			final String user = sessionData.getLastName() + ", " + sessionData.getFirstName();
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
			throws ApplicationException
	{
		final JDBCDAO jdbcDAO = openJDBCSession();
		final List list = jdbcDAO.retrieve(sourceObjectName, selectColumnName);
		closeJDBCSession(jdbcDAO);

		if (!list.isEmpty())
		{
			final List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				final String str = (String) columnList.get(0);
				if (!"".equals(str))
				{
					final int number = Integer.parseInt(str);
					return number + 1;
				}
			}
		}
		return 1;
	}

	/**
	 * @return
	 */
	public static String getlLabel(String lastName, String firstName)
	{
		if (lastName != null && !lastName.equals("") && firstName != null && !firstName.equals(""))
		{
			return lastName + "," + firstName;
		}
		else if (lastName != null && !lastName.equals(""))
		{
			return lastName;
		}
		else if (firstName != null && !firstName.equals(""))
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
		final ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		logger.info("Errors:" + errors);
		if (errors != null || errors.size() != 0)
		{
			final Iterator iterator = errors.get();
			while (iterator.hasNext())
			{
				final ActionError next = (ActionError) iterator.next();
				final Object[] values = next.getValues();
				for (final Object value : values)
				{
					responseString = (String) value;
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
		if (Constants.CELL.equals(classType))
		{
			specimen = new CellSpecimen();
		}
		else if (Constants.MOLECULAR.equals(classType))
		{
			specimen = new MolecularSpecimen();
		}
		else if (Constants.FLUID.equals(classType))
		{
			specimen = new FluidSpecimen();
		}
		else
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
		DAO dao = null;
		Specimen specimen = null;
		try
		{
			dao = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDAO();
			dao.openSession(null);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
					.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			specimen = newSpecimenBizLogic.getSpecimenObj(specimenId, dao);
			dao.closeSession();
		}
		catch (final ApplicationException e)
		{
			// TODO Auto-generated catch block
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return specimen;
	}

	/**
	 * @param row - List
	 * @param myData - myData
	 * @return String
	 */
	private static String getDataFromRow(List row, String myData)
	{
		int counter;
		if(myData != null && !"".equals(myData) && (myData.length() > 1))
		{
			myData = myData + ",";
		}
		myData = myData + "\"";
		for (counter = 0; counter < (row.size() - 1); counter++)
		{
			final Object obj = AppUtility.toNewGridFormat(row.get(counter));
			if (obj != null)
			{
				myData = myData + obj.toString();
			}
			else
			{
				myData = myData + "";
			}
			myData = myData + ",";
		}
		final Object obj = AppUtility.toNewGridFormat(row.get(counter));
		if (obj != null)
		{
			myData = myData + obj.toString();
		}
		else
		{
			myData = myData + "";
		}
		myData = myData + "\"";

		return myData;
	}

	/**
	 * @param dataList - dataList
	 * @return - String
	 */
	//Added null check as label was coming null when label generation is off.
	//bug 13487
	public static String getmyData(List dataList)
	{
		String myData = "[";
		int counter = 0;
		if (dataList != null && dataList.size() != 0)
		{
			for (counter = 0; counter < (dataList.size() - 1); counter++)
			{
				final List row = (List) dataList.get(counter);
				myData = getDataFromRow(row, myData);
			}

			final List row = (List) dataList.get(counter);
			myData = getDataFromRow(row, myData);
		}
		myData = myData + "]";
		return myData;
	}

	public static String getcolumns(List columnList)
	{
		String columns = "\"";
		int col;
		if (columnList != null)
		{
			for (col = 0; col < (columnList.size() - 1); col++)
			{
				columns = columns + columnList.get(col);
				columns = columns + ",";
			}
			columns = columns + columnList.get(col);
		}
		columns = columns + "\"";
		return columns;
	}

	public static String getcolWidth(List columnList, boolean isWidthInPercent)
	{

		String colWidth = "\"";
		int col;
		if (columnList != null)
		{
			String fixedColWidth = null;
			if (isWidthInPercent)
			{
				fixedColWidth = String.valueOf(120 / columnList.size());
			}
			else
			{
				fixedColWidth = "100";
			}
			for (col = 0; col < (columnList.size() - 1); col++)
			{
				colWidth = colWidth + fixedColWidth;
				colWidth = colWidth + ",";
			}
			colWidth = colWidth + fixedColWidth;
		}
		colWidth = colWidth + "\"";
		return colWidth;
	}

	public static String getcolTypes(List dataList)
	{
		final StringBuffer colTypes = new StringBuffer();
		colTypes.append("\"");
		colTypes.append(Variables.prepareColTypes(dataList));
		colTypes.append("\"");
		return colTypes.toString();
	}

	public static void setGridData(List dataList, List columnList, HttpServletRequest request)
	{
		request.setAttribute("myData", getmyData(dataList));
		request.setAttribute("columns", getcolumns(columnList));
		boolean isWidthInPercent = false;
		if (columnList.size() < 10)
		{
			isWidthInPercent = true;
		}
		request.setAttribute("colWidth", getcolWidth(columnList, isWidthInPercent));
		request.setAttribute("isWidthInPercent", isWidthInPercent);
		request.setAttribute("colTypes", getcolTypes(dataList));
		int heightOfGrid = 100;
		if (dataList != null)
		{
			final int noOfRows = dataList.size();
			heightOfGrid = (noOfRows + 2) * 20;
			if (heightOfGrid > 240)
			{
				heightOfGrid = 230;
			}
		}
		request.setAttribute("heightOfGrid", heightOfGrid);
		int col = 0;
		int i = 0;
		String hiddenColumnNumbers = "";
		if (columnList != null)
		{
			for (col = 0; col < columnList.size(); col++)
			{
				if (columnList.get(col).toString().trim().equals("ID")
						|| columnList.get(col).toString().trim().equals("Status")
						|| columnList.get(col).toString().trim().equals("Site Name")
						|| columnList.get(col).toString().trim().equals("Report Collection Date"))
				{
					hiddenColumnNumbers = hiddenColumnNumbers + "hiddenColumnNumbers[" + i + "] = "
							+ col + ";";
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
	public static User getUser(String loginName) throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(User.class.getName());
		final String[] whereColumnName = {"activityStatus", "loginName"};
		final String[] whereColumnCondition = {"=", "="};
		final String[] whereColumnValue = {Status.ACTIVITY_STATUS_ACTIVE.toString(), loginName};

		final List users = userBizLogic.retrieve(User.class.getName(), whereColumnName,
				whereColumnCondition, whereColumnValue, Constants.AND_JOIN_CONDITION);

		if (!users.isEmpty())
		{
			final User validUser = (User) users.get(0);
			return validUser;
		}
		return null;
	}

	public static List getParentContainerTypeList()
	{
		final List<NameValueBean> storagePositionTypeList = new ArrayList<NameValueBean>();

		storagePositionTypeList.add(new NameValueBean(Constants.SITE, Constants.SITE));
		storagePositionTypeList
				.add(new NameValueBean(Constants.CDE_NAME_CONTAINER + " ("
						+ Constants.STORAGE_TYPE_POSITION_AUTO + ")",
						Constants.STORAGE_TYPE_POSITION_AUTO));
		storagePositionTypeList.add(new NameValueBean(Constants.CDE_NAME_CONTAINER + " ("
				+ Constants.STORAGE_TYPE_POSITION_MANUAL + ")",
				Constants.STORAGE_TYPE_POSITION_MANUAL));
		return storagePositionTypeList;
	}

	public static boolean checkForAllCurrentAndFutureCPs(String privilegeName,
			SessionDataBean sessionDataBean, String cpId)
	{
		boolean allowOperation = false;
		DAO dao = null;
		Collection<CollectionProtocol> cpCollection = null;
		try
		{
			dao = openDAOSession(null);
			final Set<Long> cpIds = new HashSet<Long>();
			final User user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
					.getUserId());
			cpCollection = user.getAssignedProtocolCollection();

			if (cpCollection != null && !cpCollection.isEmpty())
			{
				for (final CollectionProtocol cp : cpCollection)
				{
					cpIds.add(cp.getId());
				}
			}
			// Check for Over-ridden privileges
			if (cpId != null && cpIds.contains(Long.valueOf(cpId)))
			{
				return false;
			}

			final String privilegeNames[] = privilegeName.split(",");
			Collection<Site> siteCollection = null;
			if (cpId != null && cpId.trim().length() != 0)
			{
				siteCollection = new CollectionProtocolBizLogic().getRelatedSites(dao, Long
						.valueOf(cpId));
			}
			else
			{
				final Set<Long> siteIds = new UserBizLogic().getRelatedSiteIds(sessionDataBean
						.getUserId());
				if (siteIds != null && !siteIds.isEmpty())
				{
					siteCollection = new ArrayList<Site>();
					for (final Long siteId : siteIds)
					{
						final Site site = new Site();
						site.setId(siteId);
						siteCollection.add(site);
					}
				}
			}
			final Set<Long> idSet = new HashSet<Long>();

			if (siteCollection == null)
			{
				return false;
			}

			for (final Site site : siteCollection)
			{
				idSet.add(site.getId());
			}
			// Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
			/*if (dao instanceof HibernateDAO)
			{
				try
				{
					((HibernateDAO) dao).openSession(null);
				}
				catch (DAOException e)
				{
					logger.debug(e.getMessage(), e);
				}
			}*/
			for (final Long id : idSet)
			{
				if (privilegeNames.length > 1)
				{
					if ((PrivilegeManager.getInstance().getPrivilegeCache(
							sessionDataBean.getUserName()).hasPrivilege(Constants
							.getCurrentAndFuturePGAndPEName(id), privilegeNames[0]))
							|| (PrivilegeManager.getInstance().getPrivilegeCache(
									sessionDataBean.getUserName()).hasPrivilege(Constants
									.getCurrentAndFuturePGAndPEName(id), privilegeNames[1])))
					{
						allowOperation = true;
					}
				}
				else if (PrivilegeManager.getInstance().getPrivilegeCache(
						sessionDataBean.getUserName()).hasPrivilege(
						Constants.getCurrentAndFuturePGAndPEName(id), privilegeName))
				{
					allowOperation = true;
				}

				if (allowOperation)
				{
					return true;
				}
			}

		}
		catch (final ApplicationException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (final DAOException e)
			{
				AppUtility.logger.error(e.getMessage(), e);
				e.printStackTrace();
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
		final Map<String, SiteUserRolePrivilegeBean> rowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = siteUserRolePrivBean;

		final List<Site> siteList = siteUserRolePrivilegeBean.getSiteList();

		final NameValueBean role = siteUserRolePrivilegeBean.getRole();
		final List<NameValueBean> sitePrivileges = new ArrayList<NameValueBean>();
		final List<NameValueBean> cpPrivileges = new ArrayList<NameValueBean>();
		final Set<String> sitePriv = getSitePrivileges();
		final Set<String> cpPriv = getCPPrivileges();

		final List<NameValueBean> allPrivileges = siteUserRolePrivilegeBean.getPrivileges();

		for (final NameValueBean nmv : allPrivileges)
		{
			if (sitePriv.contains(nmv.getName()))
			{
				sitePrivileges.add(nmv);
			}
			else
			{
				cpPrivileges.add(nmv);
			}
		}

		final SiteUserRolePrivilegeBean bean1 = new SiteUserRolePrivilegeBean();
		final SiteUserRolePrivilegeBean bean2 = new SiteUserRolePrivilegeBean();

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
		final List<NameValueBean> list = edu.wustl.common.util.global.Variables.privilegeGroupingMap
				.get("SITE");
		final Set<String> sitePrivileges = new HashSet<String>();
		for (final NameValueBean nmv : list)
		{
			sitePrivileges.add(nmv.getName());
		}
		return sitePrivileges;
	}

	public static Set getCPPrivileges()
	{
		final List<NameValueBean> list = edu.wustl.common.util.global.Variables.privilegeGroupingMap
				.get("CP");
		final Set<String> cpPrivileges = new HashSet<String>();
		for (final NameValueBean nmv : list)
		{
			cpPrivileges.add(nmv.getName());
		}
		return cpPrivileges;
	}

	public static void processDeletedPrivileges(SiteUserRolePrivilegeBean siteUserRolePrivilegeBean)
			throws ApplicationException
	{
		final SiteUserRolePrivilegeBean bean = siteUserRolePrivilegeBean;
		String groupName = null;
		String pgName = null;

		try
		{
			if (bean.getSiteList().isEmpty())
			{
				processDeletedPrivilegesOnCPPage(siteUserRolePrivilegeBean, bean
						.getCollectionProtocol().getId());
				return;
			}
			final Site site = bean.getSiteList().get(0);
			final User user = bean.getUser();
			final CollectionProtocol collectionProt = bean.getCollectionProtocol();
			final PrivilegeUtility privilegeUtility = new PrivilegeUtility();

			final List<Group> grpList = new ArrayList<Group>();
			final List<ProtectionGroup> pgList = new ArrayList<ProtectionGroup>();

			if (bean.getCollectionProtocol() != null)
			{
				groupName = Constants.getCPUserGroupName(collectionProt.getId(), user.getCsmUserId());
				pgName = CSMGroupLocator.getInstance().getPGName(collectionProt.getId(),
						Class.forName("edu.wustl.catissuecore.domain.CollectionProtocol"));

			}
			else
			{
				if (bean.isAllCPChecked())
				{
					pgName = Constants.getCurrentAndFuturePGAndPEName(site.getId());
				}
				else
				{
					pgName = CSMGroupLocator.getInstance().getPGName(site.getId(),
							Class.forName("edu.wustl.catissuecore.domain.Site"));
				}

				groupName = Constants.getSiteUserGroupName(site.getId(), user.getCsmUserId());
			}
			removePrivilageCache(groupName, pgName, user);

		}
		catch (final SMException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
			handleSMException(e);
		}
		catch (final CSTransactionException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw getApplicationException(e, "utility.error", "");
		}
		catch (final ClassNotFoundException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw getApplicationException(e, "clz.not.found.error", "");
		}
	}

	private static void removePrivilageCache(String groupName, String pgName, User user)
			throws SMException, CSTransactionException
	{
		Group group = new Group();
		List<Group> grpList = new ArrayList<Group>();
		List<ProtectionGroup> pgList = new ArrayList<ProtectionGroup>();
		final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		group.setGroupName(groupName);
		final GroupSearchCriteria groupSearchCriteria = new GroupSearchCriteria(group);

		grpList = privilegeUtility.getUserProvisioningManager().getObjects(groupSearchCriteria);

		if (grpList != null && !grpList.isEmpty())
		{
			group = grpList.get(0);
		}

		ProtectionGroup protectionGroup = new ProtectionGroup();
		protectionGroup.setProtectionGroupName(pgName);
		final ProtectionGroupSearchCriteria pgSearchCriteria = new ProtectionGroupSearchCriteria(protectionGroup);
		pgList = privilegeUtility.getUserProvisioningManager().getObjects(pgSearchCriteria);

		if (pgList != null && !pgList.isEmpty())
		{
			protectionGroup = pgList.get(0);
		}

		new PrivilegeUtility().getUserProvisioningManager().removeGroupFromProtectionGroup(
				protectionGroup.getProtectionGroupId().toString(), group.getGroupId().toString());
		PrivilegeManager.getInstance().removePrivilegeCache(user.getLoginName());
	}

	/**
	 * This method wraps SMException to BizLogic Exception.
	 * @param e SMException instance
	 * @return BizLogicException instance
	 */
	public static BizLogicException handleSMException(SMException smExp)
	{
		logger.debug(smExp.getLogMessage());
		final ErrorKey errorKey = ErrorKey.getErrorKey(smExp.getErrorKeyAsString());
		return new BizLogicException(errorKey, smExp, smExp.getLogMessage());
	}

	public static void processDeletedPrivilegesOnCPPage(
			SiteUserRolePrivilegeBean siteUserRolePrivilegeBean, Long cpId)

	{
		try
		{
			final SiteUserRolePrivilegeBean bean = siteUserRolePrivilegeBean;
			String groupName = null;
			String pgName = null;
			final User user = bean.getUser();

			final PrivilegeUtility privilegeUtility = new PrivilegeUtility();

			final List<Group> grpList = new ArrayList<Group>();

			final List<ProtectionGroup> pgList = new ArrayList<ProtectionGroup>();

			groupName = Constants.getCPUserGroupName(cpId, user.getCsmUserId());

			pgName = CSMGroupLocator.getInstance().getPGName(cpId, CollectionProtocol.class);

			removePrivilageCache(groupName, pgName, user);
		}
		catch (final ApplicationException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final CSTransactionException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	/**
	 * @param collectionProtocol
	 * @param session
	 * @return
	 */
	public static CollectionProtocolDTO getCoolectionProtocolDTO(
			CollectionProtocol collectionProtocol, HttpSession session)
	{
		final CollectionProtocolDTO collectionProtocolDTO = new CollectionProtocolDTO();
		final Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session
				.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		collectionProtocolDTO.setCollectionProtocol(collectionProtocol);
		collectionProtocolDTO.setRowIdBeanMap(rowIdBeanMap);
		return collectionProtocolDTO;
	}

	/*
		//bug 11611 and 11659 start
		*//**
				 * @param privilegeName - privilege name
				 * @param protectionElementName - protection element name
				 * @return UserNotAuthorizedException - exception if user is not authorized
				 */
	/*
		public static UserNotAuthorizedException getUserNotAuthorizedException(String privilegeName,
				String protectionElementName)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("user.not.auth");
			UserNotAuthorizedException ex = new UserNotAuthorizedException(errorKey, null,
					"User not authorized");
			ex.setPrivilegeName(privilegeName);
			if (protectionElementName != null
					&& (protectionElementName.contains("Site") || protectionElementName
							.contains("CollectionProtocol")))
			{
				String[] arr = protectionElementName.split("_");
				String[] nameArr = arr[0].split("\\.");
				String baseObject = nameArr[nameArr.length - 1];
				ex.setBaseObject(baseObject);
				ex.setBaseObjectIdentifier(arr[1]);
			}
			return ex;
		}*/

	public static BizLogicException getUserNotAuthorizedException(String privilegeName,
			String protectionElementName)
	{
		final BizLogicException bizExp = getUserNotAuthorizedException(privilegeName,
				protectionElementName, null);
		return bizExp;
	}

	/**
	 * @param privilegeName - privilege name
	 * @param protectionElementName - protection element name
	 * @param domainObjName - domain object
	 * @return UserNotAuthorizedException - exception if user is not authorized
	 */

	public static BizLogicException getUserNotAuthorizedException(String privilegeName,
			String protectionElementName, String domainObjName)
	{
		String baseObjectUpdated = domainObjName;
		String baseObjectId = "";

		if (protectionElementName != null
				&& (protectionElementName.contains("Site") || protectionElementName
						.contains("CollectionProtocol")))
		{
			final String[] arr = protectionElementName.split("_");
			final String[] nameArr = arr[0].split("\\.");
			final String baseObject = nameArr[nameArr.length - 1];
			baseObjectUpdated = baseObject;
			baseObjectId = arr[1];
		}

		final String className = getActualClassName(baseObjectUpdated);
		final String decoratedPrivilegeName = AppUtility
				.getDisplayLabelForUnderscore(privilegeName);

		if (!(baseObjectUpdated != null && baseObjectUpdated.trim().length() != 0))
		{
			baseObjectUpdated = className;
		}

		if (domainObjName == null)
		{
			domainObjName = baseObjectUpdated;
		}
		/*List<String> list  = new ArrayList<String>();
		list.add(domainObjName);
		list.add(decoratedPrivilegeName);
		list.add(baseObjectUpdated);

		String message = ApplicationProperties.getValue("access.addedit.object.denied", list);*/
		final ErrorKey errorKey = ErrorKey.getErrorKey("access.addedit.object.denied");
		return new BizLogicException(errorKey, null, domainObjName + ":" + decoratedPrivilegeName
				+ ":" + baseObjectUpdated);
	}

	/**
	* @param name
	* @return
	*/
	private static String getActualClassName(String name)
	{
		if (name != null && name.trim().length() != 0)
		{
			final String splitter = "\\.";
			final String[] arr = name.split(splitter);
			if (arr != null && arr.length != 0)
			{
				return arr[arr.length - 1];
			}
		}
		return name;
	}

	//bug 11611 and 11659 end
	public static boolean hasPrivilegeToView(String objName, Long identifier,
			SessionDataBean sessionDataBean, String privilegeName)
	{
		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return true;
		}

		List cpIdsList = new ArrayList();
		final Set<Long> cpIds = new HashSet<Long>();

		cpIdsList = edu.wustl.query.util.global.Utility.getCPIdsList(objName, identifier,
				sessionDataBean);

		if (cpIdsList == null)
		{
			return false;
		}

		if (cpIdsList.isEmpty())
		{
			return new CSMValidator().hasPrivilegeToViewGlobalParticipant(sessionDataBean);
		}

		if (cpIdsList.size() > 1 && objName.equals(Participant.class.getName()))
		{
			try
			{
				return hasPrivilegeToViewMatchingParticipant(cpIdsList, sessionDataBean,
						privilegeName);
			}
			catch (final ApplicationException e)
			{
				AppUtility.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		else
		{
			for (Object cpId : cpIdsList)
			{
				cpId = cpIdsList.get(0);
				cpIds.add(Long.valueOf(cpId.toString()));
			}
		}

		PrivilegeCache privilegeCache;
		try
		{
			privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			final StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME).append("_");
			boolean isPresent = false;

			for (final Long cpId : cpIds)
			{
				isPresent = returnHasPrivilege(sessionDataBean, privilegeName, privilegeCache, strBuffer,
						cpId);

				if (!isPresent)
				{
					return false;
				}
			}
		}
		catch (final SMException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return true;
	}

	private static boolean hasPrivilegeToViewMatchingParticipant(List cpIdsList,
			SessionDataBean sessionDataBean, String privilegeName) throws ApplicationException
	{
		final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
				sessionDataBean.getUserName());
		final StringBuffer strBUffer = new StringBuffer();
		strBUffer.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME).append("_");
		boolean isPresent = false;

		for (final Object cpId : cpIdsList)
		{
			isPresent = returnHasPrivilege(sessionDataBean, privilegeName, privilegeCache, strBUffer, cpId);

			if (isPresent)
			{
				return true;
			}
		}
		return false;
	}

	private static boolean returnHasPrivilege(SessionDataBean sessionDataBean,
			String privilegeName, PrivilegeCache privilegeCache, StringBuffer strBuffer, Object cpId)
	{
		DAO dao = null;
		boolean isPresent = false;
		Collection<CollectionProtocol> cpCollection = null;

		try
		{
			dao = openDAOSession(null);
			final Set<Long> cpIds = new HashSet<Long>();
			final User user = (User) dao.retrieveById(User.class.getName(), sessionDataBean
					.getUserId());
			cpCollection = user.getAssignedProtocolCollection();

			if (cpCollection != null && !cpCollection.isEmpty())
			{
				for (final CollectionProtocol cp : cpCollection)
				{
					cpIds.add(cp.getId());
				}
			}

			final String[] privilegeNames = privilegeName.split(",");
			if (privilegeNames.length > 1)
			{
				isPresent = privilegeCache.hasPrivilege(strBuffer.toString() + cpId.toString(),
						privilegeNames[0]);

				// Check for Over-ridden privileges
				if (!isPresent && cpIds.contains(cpId))
				{
					return false;
				}
				if (!isPresent)
				{
					isPresent = checkForAllCurrentAndFutureCPs(privilegeNames[0], sessionDataBean,
							cpId.toString());
				}
				if (isPresent)
				{
					isPresent = privilegeCache.hasPrivilege(strBuffer.toString() + cpId.toString(),
							privilegeNames[1]);
					isPresent = !isPresent;
				}
			}
			else
			{
				isPresent = privilegeCache.hasPrivilege(strBuffer.toString() + cpId.toString(),
						privilegeName);
				if (!isPresent && Permissions.REGISTRATION.equals(privilegeName))
				{
					isPresent = checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean, cpId
							.toString());
				}
				if (privilegeName != null
						&& privilegeName.equalsIgnoreCase(Permissions.READ_DENIED))
				{
					isPresent = !isPresent;
				}
			}
		}
		catch (final ApplicationException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				closeDAOSession(dao);
			}
			catch (final ApplicationException e)
			{
				AppUtility.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}

		return isPresent;
	}

	/**
	 * This method will retrive the collection protocol for a Specific site identifier
	 * @param request request object
	 * @param siteId Site identifier
	 * @return req	uest
	 * @throws DAOException Databse related exception
	 */

	public static HttpServletRequest setCollectionProtocolList(HttpServletRequest request,
			Long siteId, DAO dao) throws ApplicationException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final SiteBizLogic siteBizLogic = (SiteBizLogic) factory
				.getBizLogic(Constants.SITE_FORM_ID);
		Collection<CollectionProtocol> cpCollection = null;
		if (siteId <= 0)
		{
			cpCollection = new ArrayList<CollectionProtocol>();
		}
		else
		{
			cpCollection = siteBizLogic.getRelatedCPs(siteId, dao);
		}
		final List<NameValueBean> cpList = new ArrayList<NameValueBean>();
		final Map<Long, String> cpTitleMap = new HashMap<Long, String>();
		if (cpCollection != null && !cpCollection.isEmpty())
		{
			for (final CollectionProtocol cp : cpCollection)
			{
				cpList.add(new NameValueBean(cp.getShortTitle(), cp.getId()));
				cpTitleMap.put(cp.getId(), cp.getTitle());
			}
		}

		Collections.sort(cpList);

		request.setAttribute(Constants.PROTOCOL_LIST, cpList);
		request.setAttribute(Constants.CP_ID_TITLE_MAP, cpTitleMap);
		return request;
	}

	/**
	 * This method is used to process Roles in case Custom role is added / edited
	 * On User and CP page
	 * @param roleName Role name
	 */
	public static void processRole(String roleName)
	{
		if (roleName.startsWith("0"))
		{
			final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			Role role = null;
			try
			{
				role = privilegeUtility.getRole(roleName);

				if (role != null && role.getId() != null)
				{
					privilegeUtility.getUserProvisioningManager().removeRole(
							role.getId().toString());
				}
			}
			catch (final Exception e)
			{
				AppUtility.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Common Code - return authorization info depending upon Privileges
	 * @param sessionDataBean Session Data Bean
	 * @param privilegeName Privilege Name
	 * @param protectionElementName Protection Element Name
	 * @return
	 * @throws UserNotAuthorizedException
	 */
	public static boolean returnIsAuthorized(SessionDataBean sessionDataBean, String privilegeName,
			String protectionElementName) throws BizLogicException
	{
		boolean isAuthorized = false;
		PrivilegeCache privilegeCache;
		try
		{
			privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName());

			if (protectionElementName != null)
			{
				final String[] prArray = protectionElementName.split(Constants.UNDERSCORE);
				final String baseObjectId = prArray[0];
				String objId = null;
				boolean isAuthorized1 = false;

				for (int i = 1; i < prArray.length; i++)
				{
					objId = baseObjectId + Constants.UNDERSCORE + prArray[i];
					isAuthorized1 = privilegeCache.hasPrivilege(objId.toString(), privilegeName);
					if (!isAuthorized1)
					{
						break;
					}
				}

				isAuthorized = isAuthorized1;
			}
			else
			{
				isAuthorized = false;
			}
		}
		catch (final SMException e)
		{
			AppUtility.logger.error(e.getMessage(), e);
			e.printStackTrace();
			handleSMException(e);
		}
		if (!isAuthorized)
		{
			//bug 11611 and 11659
			throw getUserNotAuthorizedException(privilegeName, protectionElementName);
		}
		return isAuthorized;
	}

	public static boolean checkPrivilegeOnCP(Object domainObject, String protectionElementName,
			String privilegeName, SessionDataBean sessionDataBean) throws ApplicationException
	{
		boolean isAuthorized = false;

		if (protectionElementName.equals(Constants.allowOperation))
		{
			return true;
		}

		final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
				sessionDataBean.getUserName());
		//Checking whether the logged in user has the required privilege on the given protection element
		isAuthorized = privilegeCache.hasPrivilege(protectionElementName, privilegeName);

		if (isAuthorized)
		{
			return isAuthorized;
		}
		else
		// Check for ALL CURRENT & FUTURE CASE
		{
			final String protectionElementNames[] = protectionElementName.split("_");

			final Long cpId = Long.valueOf(protectionElementNames[1]);
			final Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean
					.getUserId(), false);

			if (cpIdSet.contains(cpId))
			{
				//bug 11611 and 11659
				throw AppUtility
						.getUserNotAuthorizedException(privilegeName, protectionElementName);
			}
			isAuthorized = edu.wustl.catissuecore.util.global.AppUtility
					.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean,
							protectionElementNames[1]);
		}
		return isAuthorized;
	}

	public static boolean checkOnCurrentAndFuture(SessionDataBean sessionDataBean,
			String protectionElementName, String privilegeName) throws BizLogicException
	{
		boolean isAuthorized = false;
		final String protectionElementNames[] = protectionElementName.split("_");

		final Long cpId = Long.valueOf(protectionElementNames[1]);
		final Set<Long> cpIdSet = new UserBizLogic().getRelatedCPIds(sessionDataBean.getUserId(),
				false);

		if (cpIdSet.contains(cpId))
		{
			//bug 11611 and 11659
			throw AppUtility.getUserNotAuthorizedException(privilegeName, protectionElementName);
		}
		isAuthorized = edu.wustl.catissuecore.util.global.AppUtility
				.checkForAllCurrentAndFutureCPs(privilegeName, sessionDataBean,
						protectionElementNames[1]);
		return isAuthorized;
	}

	public static void setDefaultPrinterTypeLocation(IPrinterTypeLocation form)
	{
		if (form.getPrinterLocation() == null)
		{
			form.setPrinterLocation((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_PRINTER_LOCATION));
		}
		if (form.getPrinterType() == null)
		{
			form.setPrinterType((String) DefaultValueManager
					.getDefaultValue(Constants.DEFAULT_PRINTER_TYPE));
		}
	}

	// Suhas Khot : Added for show_hide_forms
	/**
	 * This method is used to get Object from the DE metadata
	 * @param whereColumnValue value of where column in the query
	 * @param selectObjName name of object on which the query is to fire
	 * @param whereColumnName name of where column in the query
	 * @return the identifier of the Object
	 * @throws DAOException fails to do database operation
	 */
	public static Object getObjectIdentifier(String whereColumnValue, String selectObjName,
			String whereColumnName, String appName) throws ApplicationException
	{
		Object identifier = null;
		final DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		defaultBizLogic.setAppName(appName);
		final String[] selectColName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		final String[] whereColName = {whereColumnName};
		final Object[] whereColValue = {whereColumnValue};
		final String[] whereColCondition = {Constants.EQUALS};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final List identifierList = defaultBizLogic.retrieve(selectObjName, selectColName,
				whereColName, whereColCondition, whereColValue, joinCondition);
		if (identifierList != null && !identifierList.isEmpty())
		{
			identifier = identifierList.get(0);
		}
		return identifier;
	}

	/**
	 * This method return all entityId Vs containerId map.
	 * @param entityGroupIds entityIds Collection
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException if it fails to do database operation
	 */
	public static Map<Long, Long> getAllContainers() throws DynamicExtensionsSystemException,
			ApplicationException
	{
		// Map for storing containers corresponding to entitiesIds
		Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();
		final EntityManagerInterface entityManager = EntityManager.getInstance();
		final String[] colName = {Constants.NAME};
		final Collection<NameValueBean> entityGrpBeanColl = entityManager.getAllEntityGroupBeans();
		final Set<Long> entityGroupIds = new HashSet<Long>();
		for (final NameValueBean entityGrpBean : entityGrpBeanColl)
		{
			entityGroupIds.add(Long.parseLong(entityGrpBean.getValue()));
		}
		entityIdsVsContId = getAllContainers(entityGroupIds);
		return entityIdsVsContId;
	}

	/**
	 * This method return entityId Vs containerId map for selective entityGroup and all forms.
	 * @param entityGroupIds entityGroupIds Collection
	 * @throws DynamicExtensionsSystemException fails to get forms,entity
	 * @throws DAOException fail to do database operation
	 */
	public static Map<Long, Long> getAllContainers(Set<Long> entityGroupIds)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		// Map for storing containers corresponding to entitiesIds
		final Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();
		final EntityManagerInterface entityManager = EntityManager.getInstance();
		final DefaultBizLogic defaultBizLogic = edu.common.dynamicextensions.bizlogic.BizLogicFactory
				.getDefaultBizLogic();
		for (final Long entityGroupId : entityGroupIds)
		{
			final Collection<Long> entityIds = entityManager
					.getAllEntityIdsForEntityGroup(entityGroupId);
			if (entityIds != null && !entityIds.isEmpty())
			{
				for (final Long entityId : entityIds)
				{
					final Long containerId = entityManager.getContainerIdFromEntityId(entityId);
					if (containerId != null)
					{
						entityIdsVsContId.put(entityId, containerId);
					}
				}
			}
		}
		final String[] colName = {Constants.NAME};
		final List<String> formNameColl = defaultBizLogic.retrieve(Category.class.getName(),
				colName);
		for (final String formName : formNameColl)
		{
			final Long rootCategoryEntityId = entityManager
					.getRootCategoryEntityIdByCategoryName(formName);
			if (rootCategoryEntityId != null)
			{
				final Long containerId = entityManager
						.getContainerIdFromEntityId(rootCategoryEntityId);
				if (containerId != null)
				{
					entityIdsVsContId.put(rootCategoryEntityId, containerId);
				}
			}
		}
		return entityIdsVsContId;
	}


	/**
	 * To check the validity of the date format specified by user in the config file.
	 */
	public static boolean isValidDateFormat(String dateFormat)
	{
		boolean result = false;
		boolean result1 = false;
		boolean isValidDatePattern = false;
		try
		{
			Pattern pattern = Pattern.compile("MM-dd-yyyy", Pattern.CASE_INSENSITIVE);
			Pattern re1 = Pattern.compile("dd-MM-yyyy", Pattern.CASE_INSENSITIVE);
			Matcher mat = pattern.matcher(dateFormat);
			Matcher mat1 = re1.matcher(dateFormat);
			result = mat.matches();
			result1 = mat1.matches();
			if (!(result || result1))
			{
				pattern = Pattern.compile("MM/dd/yyyy", Pattern.CASE_INSENSITIVE);
				re1 = Pattern.compile("dd/MM/yyyy", Pattern.CASE_INSENSITIVE);
				mat = pattern.matcher(dateFormat);
				mat1 = re1.matcher(dateFormat);
				result = mat.matches();
				result1 = mat1.matches();
			}
			if (result || result1)
			{
				isValidDatePattern = true;
			}
			else
			{
				isValidDatePattern = false;
			}
		}
		catch (final Exception exp)
		{
			AppUtility.logger.error("IsValidDatePattern : exp : " + exp.getMessage(),exp);
			return false;
		}
		//System.out.println("dtCh : " +dtCh );
		return isValidDatePattern;
	}

	/**
	 * This method gets Display Label For Underscore.
	 * @param objectName object Name.
	 * @return Label.
	 */
	public static String getDisplayLabelForUnderscore(String objectName)
	{
		final StringBuffer formatedStr = new StringBuffer();
		final String[] tokens = objectName.split("_");
		for (int i = 0; i < tokens.length; i++)
		{
			if (!TextConstants.EMPTY_STRING.equals(tokens[i]))
			{
				formatedStr.append(initCap(tokens[i]));
				formatedStr.append(edu.wustl.common.util.global.Constants.CONST_SPACE_CAHR);
			}
		}
		return formatedStr.toString();
	}

	/**
	 * @param str String to be converted to Proper case.
	 * @return The String in Proper case.
	 */
	public static String initCap(String str)
	{
		StringBuffer retStr;
		if (Validator.isEmpty(str))
		{
			retStr = new StringBuffer();
			logger.debug("Utility.initCap : - String provided is either empty or null" + str);
		}
		else
		{
			retStr = new StringBuffer(str.toLowerCase(CommonServiceLocator.getInstance()
					.getDefaultLocale()));
			retStr.setCharAt(0, Character.toUpperCase(str.charAt(0)));
		}
		return retStr.toString();
	}

	public static ApplicationException getApplicationException(Exception exception,
			String errorName, String msgValues)
	{
		return new ApplicationException(ErrorKey.getErrorKey(errorName), exception, msgValues);

	}

	public static JDBCDAO openJDBCSession() throws ApplicationException
	{
		JDBCDAO jdbcDAO = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
			jdbcDAO.openSession(null);
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(), daoExp
					.getErrorKeyName());
		}
		return jdbcDAO;
	}

	public static void closeJDBCSession(JDBCDAO jdbcDAO) throws ApplicationException
	{
		try
		{
			if (jdbcDAO != null)
			{
				jdbcDAO.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	public static DAO openDAOSession(SessionDataBean sessionDataBean) throws ApplicationException
	{
		DAO dao = null;
		try
		{
			final String applicationName = CommonServiceLocator.getInstance().getAppName();
			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getDAO();
			dao.openSession(sessionDataBean);
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return dao;
	}

	public static void closeDAOSession(DAO dao) throws ApplicationException
	{
		try
		{
			if (dao != null)
			{
				dao.closeSession();
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw getApplicationException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}

	/**
	 * @param sText String containing the text to be checked
	 * @return boolean isNumber -true if given String is in proper number
	 *         format or else returns false
	 */
	public static boolean isNumeric(String sText)
	{
		final String validChars = "0123456789.E";
		boolean isNumber = true;
		Character charTemp;

		for (int i = 0; i < sText.length() && isNumber; i++)
		{
			charTemp = sText.charAt(i);
			if (validChars.indexOf(charTemp) == -1)
			{
				isNumber = false;
				break;
			}
		}
		return isNumber;
	}

	/**
	 *
	 * @param setPV : setPV
	 * @param specimenClassList : specimenClassList
	 * @return Map : Map
	 */
	public static Map getSubTypeMap(Set setPV, List specimenClassList)
	{
		final Iterator itr = setPV.iterator();

		final Map subTypeMap = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			final List innerList = new ArrayList();
			final Object obj = itr.next();
			final PermissibleValue permissibleVal = (PermissibleValue) obj;
			final String tmpStr = permissibleVal.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			final Set list1 = permissibleVal.getSubPermissibleValues();
			final Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				final Object obj1 = itr1.next();
				final PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				final String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(permissibleVal.getValue(), innerList);
		}
		return subTypeMap;
	}

	/**
	 * This is a recursive method to make the final-vector for DHTML tree of
	 * storage containers and Tissue site.
	 * @param datalist : datalist
	 * @param finalDataListVector : finalDataListVector
	 */
	public static Vector<StorageContainerTreeNode> createTreeNodeVector(List datalist,
			Vector<StorageContainerTreeNode> finalDataListVector)
	{
		if (datalist != null && datalist.size() != 0)
		{
			final Iterator itr = datalist.iterator();
			while (itr.hasNext())
			{
				final StorageContainerTreeNode node = (StorageContainerTreeNode) itr.next();
				final boolean contains = finalDataListVector.contains(node.getValue());
				if (contains == false)
				{
					finalDataListVector.add(node);
				}
				final List childNodeVector = node.getChildNodes();
				createTreeNodeVector(childNodeVector, finalDataListVector);
			}
			return finalDataListVector;
		}
		else
		{
			return finalDataListVector;
		}
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node.
	 * @param nodeName
	 *            Name of the site or container
	 * @param parentId
	 *            parent identifier of the selected node
	 * @return tissueSiteNodeVector This vector contains all the containers
	 * @throws ApplicationException throws ApplicationException.
	 * @Description This method will retrieve all the Tissue Sites under the
	 *              selected node
	 */
	public static List<StorageContainerTreeNode> getTissueSiteNodes(Long identifier,
			String nodeName, String parentId) throws ApplicationException
	{
		JDBCDAO dao = null;
		List<StorageContainerTreeNode> tissueSiteNodeVector = new LinkedList<StorageContainerTreeNode>();
		try
		{
			dao = openJDBCSession();
			List resultList = new ArrayList();
			final String sql = createSql(identifier, parentId);
			resultList = dao.executeQuery(sql);

			String tissueSiteName = null;
			Long nodeIdentifier;
			Long tissueParentId;
			Long childCount;
			String activityStatus = null;

			final Iterator iterator = resultList.iterator();
			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				nodeIdentifier = Long.valueOf((String) rowList.get(0));
				tissueSiteName = (String) rowList.get(1);
				String parId = (String) rowList.get(2);
				activityStatus = Status.ACTIVITY_STATUS_CLOSED.toString();
				if ((parId == null) || ("".equals(parId)))
				{
					parId = "1";
				}
				tissueParentId = Long.valueOf(parId);
				childCount = Long.valueOf(getChildCount(nodeIdentifier, dao));
				if (childCount > 0)
				{
					activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();
				}
				tissueSiteNodeVector = getTreeNodeDataVector(tissueSiteNodeVector, nodeIdentifier,
						tissueSiteName, activityStatus, childCount, tissueParentId, nodeName);
			}
			if (tissueSiteNodeVector.isEmpty())
			{
				final StorageContainerTreeNode containerNode = new StorageContainerTreeNode(
						identifier, nodeName, nodeName, activityStatus);
				tissueSiteNodeVector.add(containerNode);
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw new ApplicationException(daoExp.getErrorKey(), daoExp, daoExp.getMsgValues());
		}
		finally
		{
			closeJDBCSession(dao);
		}
		return tissueSiteNodeVector;
	}

	/**
	 * This method creates the new node required for DHTML tree node.
	 * @param TreeNodeDataVector Vector which stores the created node.
	 * @return TreeNodeDataVector by adding the new parent and child node
	 */

	public static List<StorageContainerTreeNode> getTreeNodeDataVector(
			List<StorageContainerTreeNode> treeNodeDataVector, Long childNodeId,
			String childNodeName, String activityStatus, Long childCount, Long parentNodeId,
			String parentNodeName)
	{
		final String dummyNodeName = Constants.DUMMY_NODE_NAME;
		StorageContainerTreeNode containerNode = new StorageContainerTreeNode(childNodeId,
				childNodeName, childNodeName, activityStatus);
		final StorageContainerTreeNode parneContainerNode = new StorageContainerTreeNode(
				parentNodeId, parentNodeName, parentNodeName, activityStatus);

		if (childCount != null && childCount > 0)
		{
			final StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(
					childNodeId, dummyNodeName, dummyNodeName, activityStatus);
			dummyContainerNode.setParentNode(containerNode);
			containerNode.getChildNodes().add(dummyContainerNode);
		}

		if (treeNodeDataVector.contains(containerNode))
		{
			containerNode = treeNodeDataVector.get(treeNodeDataVector
					.indexOf(containerNode));
		}
		else
		{
			treeNodeDataVector.add(containerNode);
		}
		containerNode.setParentNode(parneContainerNode);
		parneContainerNode.getChildNodes().add(containerNode);

		return treeNodeDataVector;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node
	 * @param parentId
	 *            Parent identifier of the selected node
	 * @return String sql This method with return the sql depending on the node
	 *         clicked (i.e parent Node or child node)
	 */

	public static String createSql(Long identifier, String parentId)
	{
		String sql = "";

		if (Constants.ZERO_ID.equals(parentId))
		{
			sql = " SELECT IDENTIFIER, VALUE, PARENT_IDENTIFIER "
					+ " FROM CATISSUE_PERMISSIBLE_VALUE ,CATISSUE_CDE "
					+ "	WHERE CATISSUE_PERMISSIBLE_VALUE.PUBLIC_ID = CATISSUE_CDE.PUBLIC_ID "
					+ "	AND CATISSUE_PERMISSIBLE_VALUE.PUBLIC_ID LIKE '%Tissue_Site_PID%'";
		}
		else
		{
			sql = " SELECT CA.IDENTIFIER , CA.VALUE, CA.PARENT_IDENTIFIER "
					+ "	FROM CATISSUE_PERMISSIBLE_VALUE CA ,CATISSUE_PERMISSIBLE_VALUE CA1 "
					+ "	WHERE CA1.IDENTIFIER   = CA.IDENTIFIER "
					+ "	AND CA.PARENT_IDENTIFIER   = CA1.PARENT_IDENTIFIER "
					+ "	AND CA1.PARENT_IDENTIFIER  ='" + identifier + "'";
		}
		return sql;
	}

	/**
	 * This method is defined for counting the child of the Tissue Site.
	 * @param identifier identifier of the container node whose child count is going to be done.
	 * @param dao JDBCDAO object for excuting the sql query.
	 * @return child count of the container identifier
	 */

	public static long getChildCount(Long identifier, JDBCDAO dao) throws ApplicationException
	{
		long count = 0;
		final String sql = " SELECT COUNT(CA1.PARENT_IDENTIFIER)"
				+ " FROM CATISSUE_PERMISSIBLE_VALUE CA ,CATISSUE_PERMISSIBLE_VALUE CA1"
				+ " WHERE CA1.IDENTIFIER   = CA.IDENTIFIER"
				+ " AND CA.PARENT_IDENTIFIER   = CA1.PARENT_IDENTIFIER"
				+ " AND CA1.PARENT_IDENTIFIER =? "
				+ " GROUP BY CA1.PARENT_IDENTIFIER";

		ColumnValueBean columnValueBean = new ColumnValueBean(identifier);
		List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
		columnValueBeanList.add(columnValueBean);
		try
		{
			final List resList = dao.executeQuery(sql,null,columnValueBeanList);
			if (!(resList.isEmpty()))
			{
				final Iterator iterator = resList.iterator();
				while (iterator.hasNext())
				{
					final List rowList = (List) iterator.next();
					count = Long.parseLong((String) rowList.get(0));
				}
			}
		}
		catch (final DAOException daoExp)
		{
			AppUtility.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			// TODO Auto-generated catch block
			throw new ApplicationException(daoExp.getErrorKey(), daoExp, daoExp.getMsgValues());
		}
		return count;
	}

	/**
	 *
	 *
	 * @param userName
	 * @return
	 * @throws ApplicationException
	 */
	public static SessionDataBean getSessionDataBean(String userName) throws BizLogicException
	{
		SessionDataBean sessionDataBean = null;

		if (Variables.sessionDataMap.containsKey(userName))
		{
			sessionDataBean = Variables.sessionDataMap.get(userName);
		}
		else
		{
			User user = null;
			sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(userName);

			sessionDataBean.setAdmin(false);
			try
			{
				user = getUser(userName);
			    if (user == null)
                    throw new BizLogicException(ErrorKey.getErrorKey("user.not.exists"),
                            null,"User does not exist");
			}
			catch (final ApplicationException e)
			{
				AppUtility.logger.error(e.getMessage(), e);
				e.printStackTrace();
				throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
			}
			if (user.getRoleId().equalsIgnoreCase(Constants.ADMIN_USER))
			{
				sessionDataBean.setAdmin(true);
			}

			sessionDataBean.setUserId(user.getId());
			Variables.sessionDataMap.put(userName, sessionDataBean);
		}

		return sessionDataBean;
	}

	/**This method will return the ssn value in format xxx-xx-xxxx
	 * @param ssn
	 * @return
	 */
	public static String getSSN(String ssn)
	{
		String ssnA = "";
		String ssnB = "";
		String ssnC = "";
		boolean result = false;

		final Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}",
				Pattern.CASE_INSENSITIVE);
		final Matcher mat = pattern.matcher(ssn);
		result = mat.matches();
		if (result)
		{
			return ssn;
		}
		if (ssn.length() >= 9)
		{
			ssnA = ssn.substring(0, 3);
			ssnB = ssn.substring(3, 5);
			ssnC = ssn.substring(5, 9);
		}
		else if (ssn.length() >= 4)
		{
			ssnC = ssn.substring(0, 3);
		}
		else
		{
			return ssn;
		}
		ssn = ssnA + "-" + ssnB + "-" + ssnC;
		return ssn;
	}
	/**
	 * Return  specimenTypeList
	 * @return specimenTypeList
	 */
	public static List getAllSpecimenType()
	{
		List<String> classList = new LinkedList<String>();
	    List<NameValueBean> specimenTypeList = new LinkedList<NameValueBean>();
	    classList.add(Constants.TISSUE);
	    classList.add(Constants.FLUID);
	    classList.add(Constants.CELL);
	    classList.add(Constants.MOLECULAR);

	    Iterator<String> classItr = classList.iterator();
	    while(classItr.hasNext())
	    {
	    	List<NameValueBean> subList = new LinkedList<NameValueBean>();
	    	subList.addAll(AppUtility.getSpecimenTypes(classItr.next()));
	    	subList.remove(0);
	    	specimenTypeList.addAll(subList);
	    }
	    specimenTypeList.add(0,new NameValueBean(Constants.HOLDS_ANY, "-1"));
	    return specimenTypeList;
	}
	/**
	 * @return specimenClassTypeList
	 */
	public static List<String> getSpecimenTypes()
	{
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_TYPE);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		final Iterator itr = setPV.iterator();
		final List specimenClassTypeList = new ArrayList();
		while (itr.hasNext())
		{
			final Object obj = itr.next();
			final PermissibleValue permissibleVal = (PermissibleValue) obj;
			final String tmpStr = permissibleVal.getValue();
			specimenClassTypeList.add(tmpStr);
		}
		return specimenClassTypeList;
	}

	/**
	 * @param identifier
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws ApplicationException
	 */
	public static List getSpecimenClassList(String identifier) throws ApplicationException
	{
		final String sql = " SELECT SP.SPECIMEN_CLASS CLASS FROM CATISSUE_STOR_CONT_SPEC_CLASS SP "
				+ "WHERE SP.STORAGE_CONTAINER_ID = ?";
		ColumnValueBean valueBean = new ColumnValueBean(identifier);
		List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
		valueBeansList.add(valueBean);
		return getResult(sql,valueBeansList);
	}
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws ApplicationException
	 */
	public static List getClassAndTypeList(String id) throws ApplicationException
	{
		final String sql = "SELECT PV1.value Class, SP.SPECIMEN_TYPE FROM catissue_permissible_value PV,catissue_permissible_value PV1," +
				"CATISSUE_STOR_CONT_SPEC_TYPE SP WHERE PV.parent_identifier = PV1.identifier and " +
				"PV1.value in (select specimen_class from catissue_stor_cont_spec_class where storage_container_id=? ) and " +
				"pv.value = sp.specimen_type and SP.STORAGE_CONTAINER_ID =? order by Class";
		ColumnValueBean valueBean = new ColumnValueBean(id);
		List<ColumnValueBean> valueBeansList = new ArrayList<ColumnValueBean>();
		valueBeansList.add(valueBean);
		valueBeansList.add(valueBean);
		return getResult(sql,valueBeansList);
	}
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws ApplicationException
	 */
	public static List getSpecimenTypeList(String id) throws ApplicationException
	{
		final String sql = " SELECT SP.SPECIMEN_TYPE FROM CATISSUE_STOR_CONT_SPEC_TYPE SP "
				+ "WHERE SP.STORAGE_CONTAINER_ID = ?";
		ColumnValueBean bean = new ColumnValueBean(id);
		List<ColumnValueBean> valueBeanList = new ArrayList<ColumnValueBean>();
		valueBeanList.add(bean);
		return getResult(sql,valueBeanList);
	}
	/**
	 *
	 * @param sql SQL query.
	 * @param valueBeanList
	 * @return List
	 * @throws ApplicationException ApplicationException
	 */
	private static List getResult(final String sql, List<ColumnValueBean> valueBeanList) throws ApplicationException
	{
		final List resultList = executeSQLQuery(sql,valueBeanList);
		return getListOfString(resultList);
	}


	/**
	 * @param resultList getListOfString
	 * @return List resultlist
	 */
	private static List getListOfString(final List resultList)
	{
		final Iterator iterator = resultList.iterator();
		final List returnList = new ArrayList();
		while (iterator.hasNext())
		{
			final List list = (List) iterator.next();
			for (int cnt = 0; cnt < list.size(); cnt++)
			{
				final String data = (String) list.get(cnt);
				returnList.add(data);
			}
		}
		if (returnList.isEmpty())
		{
			returnList.add(new String(Constants.NONE));
		}
		return returnList;
	}
	/**
	 * @param id
	 *            Identifier of the StorageContainer related to which the
	 *            collectionProtocol titles are to be retrieved.
	 * @return List of collectionProtocol title.
	 * @throws ApplicationException
	 */
	public static List getCollectionProtocolList(String id) throws ApplicationException
	{
		// Query to return titles of collection protocol related to given
		final String sql = " SELECT SP.TITLE TITLE FROM CATISSUE_SPECIMEN_PROTOCOL SP, CATISSUE_ST_CONT_COLL_PROT_REL SC "
				+ " WHERE SP.IDENTIFIER = SC.COLLECTION_PROTOCOL_ID AND SC.STORAGE_CONTAINER_ID = "
				+ id;
		final List resultList = executeSQLQuery(sql);
		final Iterator iterator = resultList.iterator();
		final List returnList = new ArrayList();
		while (iterator.hasNext())
		{
			final List list = (List) iterator.next();
			final String data = (String) list.get(0);
			returnList.add(data);
		}

		if (returnList.isEmpty())
		{
			returnList.add(new String(Constants.ALL));
		}
		return returnList;
	}

	/**
	 * @param siteidSet - siteidSet.
	 * @param siteId - siteId
	 * @return boolean value
	 */
	public static boolean hasPrivilegeonSite(Set<Long> siteidSet, Long siteId)
	{
		boolean hasPrivilege = true;
		if (siteidSet != null)
		{
			if (!siteidSet.contains(siteId))
			{
				hasPrivilege = false;
			}
		}
		return hasPrivilege;
	}
	/**
	 * @param cpId Collection Protocol Identifier
	 * @param spClass Specimen Class
	 * @param aliquotCount aliquot count
	 * @param spType Specimen Type
	 * @return List<Object>
	 */
	public static List<Object> setparameterList(
	long cpId, String spClass, int aliquotCount, String spType)
	{
		List<Object> parameterList = new LinkedList<Object>();
		parameterList.add(cpId);
		parameterList.add(spClass);
		parameterList.add(aliquotCount);
		parameterList.add(spType);
		return parameterList;
	}
	/**
	 * This method will return all the specimen type under specimen class.
	 * @return List of SpecimenType
	 * @throws ApplicationException
	 */
	public static List<String> getAllSpType() throws ApplicationException
	{
		String sql = "select value from catissue_permissible_value where parent_identifier in (1,2,3,4)";
		final List resultList = executeSQLQuery(sql);
		return getListOfString(resultList);
	}
	/**
	 * This method will return all the specimen type under specimen class.
	 * @return List of SpecimenType
	 * @throws ApplicationException
	 */
	public static List<String> getAllTissueSpType() throws ApplicationException
	{
		String sql = "select value from catissue_permissible_value where parent_identifier =4";
		final List resultList = executeSQLQuery(sql);
		return getListOfString(resultList);
	}
	/**
	 * This method will return all the specimen type under specimen class.
	 * @return List of SpecimenType
	 * @throws ApplicationException
	 */
	public static List<String> getAllFluidSpType() throws ApplicationException
	{
		String sql = "select value from catissue_permissible_value where parent_identifier =3";
		final List resultList = executeSQLQuery(sql);
		return getListOfString(resultList);
	}
	/**
	 * This method will return all the specimen type under specimen class.
	 * @return List of SpecimenType
	 * @throws ApplicationException
	 */
	public static List<String> getAllMolecularType() throws ApplicationException
	{
		String sql = "select value from catissue_permissible_value where parent_identifier =1";
		final List resultList = executeSQLQuery(sql);
		return getListOfString(resultList);
	}
	/**
	 * This method will return all the specimen type under specimen class.
	 * @return List of SpecimenType
	 * @throws ApplicationException
	 */
	public static List<String> getAllCellType() throws ApplicationException
	{
		String sql = "select value from catissue_permissible_value where parent_identifier =2";
		final List resultList = executeSQLQuery(sql);
		return getListOfString(resultList);
	}
	/**
	 * @param valueToBeRoundOff
	 * @param precision
	 * @return
	 */
	public static double RoundOff(double valueToBeRoundOff, int precision)
	 {
		//Round-off function which will do actual round-off. It will auto-consider exponential values.
		double doubleNumber = Math.pow(10,precision);
		valueToBeRoundOff = valueToBeRoundOff * doubleNumber;
	  	double tmp = Math.round(valueToBeRoundOff);
	  	return tmp/doubleNumber;
	  }

	/**
	 * @param valueToBeRoundOff
	 * @param precision
	 * @return
	 */
	 public static double truncate(double valueToBeTruncateOff, int precision)
	 {
		// If valueToBeRoundOff is exponantial value then it wull not truncate it
	    String val = new Double(valueToBeTruncateOff).toString();
		if(val.contains("E") || val.contains("e")) {
			return valueToBeTruncateOff;
		}
		double doubleNumber = Math.pow(10,precision);
		int newInt = (int) (valueToBeTruncateOff * doubleNumber);
	  	return newInt/doubleNumber;
	  }

    /**
     *
     * @param url
     * @param target
     * @return
     */
    public static String getSubParameterValue(String url,String target)
    {
        String value="";
        String arr[]=url.split(CDMSIntegrationConstants.DELIMETER);

        for (String string : arr)
        {
            if(string.contains(target))
            {
                String []values=string.split(CDMSIntegrationConstants.EQUALS);
                value=values[1];
            }
        }
        return value;
    }

       /**
     * returns password from csm db on loginname
     * @param loginName
     * @throws BizLogicException
     */
    public static String getPassord(String loginName) throws BizLogicException
    {
        String password="";
        try
        {
            if (loginName != null)
            {
                String queryStr = "SELECT PASSWORD FROM CSM_USER WHERE LOGIN_NAME='"
                        + loginName + "' ";
                List result=WUSTLKeyUtility.executeQueryUsingDataSource(queryStr, false,
                        edu.wustl.wustlkey.util.global.Constants.APPLICATION_NAME);
                if(!result.isEmpty())
                {
                   result=(List)result.get(0);
                   password=result.get(0).toString();
                   password= PasswordManager.decrypt(password);
                }
            }
        }
        catch (ApplicationException e)
        {
            logger.debug(e.getMessage(), e);
            throw new BizLogicException(ErrorKey
                    .getErrorKey("biz.update.error"), e,
                    "Error in database operation");
        }
        catch (PasswordEncryptionException e)
        {
            logger.debug(e.getMessage(), e);
            throw new BizLogicException(null, null, e.getMessage());
        }
        return password;
    }

    /**
     * returns csmUserName from csm db based on csmUserId
     * @param csmUserId
     * @throws BizLogicException
     */
    public static String getCsmUserName(String csmUserId) throws BizLogicException
    {
        String csmUserName = null;
        try
        {
             StringBuffer loginNameHQL = new StringBuffer(60);
            loginNameHQL.append("select user.loginName from ");
            loginNameHQL.append(User.class.getName());
            loginNameHQL.append(" as user where user.csmUserId=");
            loginNameHQL.append(csmUserId);

            List  loginNameList = executeQuery(loginNameHQL.toString());
            if (loginNameList != null && !loginNameList.isEmpty())
            {
                csmUserName = loginNameList.get(0).toString();
            }
        }
        catch (ApplicationException e)
        {
            logger.debug(e.getMessage(), e);
            throw new BizLogicException(ErrorKey
                    .getErrorKey("biz.update.error"), e,
                    "Error in database operation");
        }
        return csmUserName;
    }
	/**
	 * This method is called from BaseDistributionReportAction.java and DistributionReportSaveAction.java
	 * Method to get the array of column names selected. If this is called for export, the column
	 * name should not contain "Print" column.
	 * @param selectedColumnsList
	 * @param columnNames
	 * @param index
	 * @return
	 */
    public static String[] getColNames(String[] selectedColumnsList, String[] columnNames, int index)
	{
		for (int i=index; i < selectedColumnsList.length; i++)
		{
			/*
			 * Split the string which is in the form
			 * TableAlias.columnNames.columnDisplayNames to get the column Names
			 */
			final StringTokenizer strTokenizer = new StringTokenizer(selectedColumnsList[i], ".");
			while (strTokenizer.hasMoreTokens())
			{
				strTokenizer.nextToken();
				strTokenizer.nextToken();
				columnNames[i] = strTokenizer.nextToken();
				if (strTokenizer.hasMoreTokens())
				{
					strTokenizer.nextToken();
				}
			}
		}
		return columnNames;
	}
}