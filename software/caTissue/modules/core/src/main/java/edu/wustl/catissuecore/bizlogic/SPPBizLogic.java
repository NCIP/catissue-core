
package edu.wustl.catissuecore.bizlogic;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;
import org.hibernate.Hibernate;
import org.xml.sax.SAXException;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import sun.misc.IOUtils;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.uiobject.SPPUIObject;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SpecimenEventsUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.TitliSearchConstants;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.MySQLDAOImpl;
import edu.wustl.dao.OracleDAOImpl;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author sri
 *
 */
public class SPPBizLogic extends CatissueDefaultBizLogic
{

	private transient final Logger logger = Logger.getCommonLogger(SPPBizLogic.class);

	/**
	 * Saves the Specimen Processing Procedure object in the database.
	 * @param dao : DAO object
	 * @param obj
	 *            The SPP object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void insert(final Object obj, Object uiObject, final DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			SPPUIObject sppUIObject = (SPPUIObject) uiObject; 
			SpecimenProcessingProcedure spp = (SpecimenProcessingProcedure) obj;
			SPPXMLParser parser = SPPXMLParser.getInstance();
			FormFile xmlFile = sppUIObject.getXmlFileName();
			String xmlFileName = xmlFile.getFileName();
			InputStream inputStream = xmlFile.getInputStream();
			Set<Action> actionList = parser.parseXML(xmlFileName, inputStream);

			for (Action action : actionList)
			{
				if (action.getActivityStatus() != null
						&& action.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
				{
					ApplicationException appExp = new ApplicationException(null, null,
							Constants.SPP_ADD_ERROR_MSG);
					appExp.setCustomizedMsg(Constants.SPP_ADD_ERROR_MSG);
					this.logger.error(Constants.SPP_ADD_ERROR_MSG);
					throw this.getBizLogicException(appExp, "", Constants.SPP_ADD_ERROR_MSG);
				}
			}

			spp.setActionCollection(actionList);
			dao.insert(spp);
			
			
		}
		catch (final DAOException daoExp)
		{
			if (daoExp.getWrapException().getCause().getMessage().contains(Constants.NAME_KEY))
			{
				ApplicationException appExp = new ApplicationException(null, null,
						Constants.SPP_NAME_ERROR);
				appExp.setCustomizedMsg(Constants.SPP_NAME_ERROR);
				this.logger.error(Constants.SPP_NAME_ERROR, daoExp);
				throw this.getBizLogicException(appExp, "", Constants.SPP_NAME_ERROR);
			}
			else if (daoExp.getWrapException().getCause().getMessage().contains(
					Constants.BARCODE_KEY))
			{
				ApplicationException appExp = new ApplicationException(null, null,
						Constants.SPP_BARCODE_ERROR);
				appExp.setCustomizedMsg(Constants.SPP_BARCODE_ERROR);
				this.logger.error(Constants.SPP_BARCODE_ERROR, daoExp);
				throw this.getBizLogicException(appExp, "", Constants.SPP_BARCODE_ERROR);
			}
			else
			{
				this.logger.error(daoExp.getMessage(), daoExp);
				daoExp.printStackTrace();
				throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp
						.getMsgValues());
			}
		}
		catch (final IOException ioExp)
		{
			this.logger.error(ioExp.getMessage(), ioExp);
			throw this.getBizLogicException(ioExp, "", ioExp.getLocalizedMessage());
		}
		catch (DynamicExtensionsSystemException deExp)
		{
			this.logger.error(deExp.getMessage(), deExp);
			throw this.getBizLogicException(deExp, "", deExp.getLocalizedMessage());
		}
		catch (SAXException saxExp)
		{
			this.logger.error(saxExp.getMessage(), saxExp);
			throw this.getBizLogicException(saxExp, "", saxExp.getLocalizedMessage());
		}
		catch (MissingResourceException e)
		{
			ApplicationException appExp = new ApplicationException(null, null,
					Constants.INVALID_XML_MSG);
			appExp.setCustomizedMsg(Constants.INVALID_XML_MSG);
			this.logger.error(Constants.INVALID_XML_MSG, e);
			throw this.getBizLogicException(appExp, "", Constants.INVALID_XML_MSG);
		}
		catch (ApplicationException e) 
		{
			this.logger.error(Constants.INVALID_XML_MSG, e);
			throw this.getBizLogicException(e, "", "Error while inserting the SPP xml in the db");
		} 
	}
	@Override
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean,Object uiObject)
			throws BizLogicException
	{
		SPPUIObject sppuiObject = (SPPUIObject)uiObject;
		SpecimenProcessingProcedure spp = (SpecimenProcessingProcedure)obj;
		updateXMLTemplateInDB(spp.getId(), sppuiObject.getXmlFileName());
		
	}
	/**
	 * Updates the persistent object in the database.
	 * @param dao : dao
	 * @param currentObj current object
	 * @param oldObj : oldObj
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected void update(DAO dao, Object currentObj, Object oldObj, Object uiObject,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			final SpecimenProcessingProcedure sppOldObj = (SpecimenProcessingProcedure) oldObj;
			final SpecimenProcessingProcedure sppNewObj = (SpecimenProcessingProcedure) currentObj;
			final SPPUIObject sppUIObj = (SPPUIObject) uiObject;

			final String sppSpecReqHQL = Constants.SPP_SPEC_REQ_HQL + sppOldObj.getId();
			final List specReqList = AppUtility.executeQuery(sppSpecReqHQL);

			final String cpeHQL = Constants.SPP_CPE_HQL + sppOldObj.getId();
			final List cpeList = AppUtility.executeQuery(cpeHQL);

			SPPXMLParser parser = SPPXMLParser.getInstance();
			FormFile xmlFile = sppUIObj.getXmlFileName();
			String xmlFileName = xmlFile.getFileName();
			InputStream inputStream = xmlFile.getInputStream();
			Set<Action> actionList = parser.parseXML(xmlFileName, inputStream);
			Set<Action> newActList = new HashSet<Action>();
			Collection<Action> oldList = sppOldObj.getActionCollection();
			sppNewObj.setActionCollection(new HashSet<Action>());
			for (Action action : actionList)
			{
				for (Action oldAction : oldList)
				{
					if (action.getUniqueId().equals(oldAction.getUniqueId()))
					{
						if (!specReqList.isEmpty() || !cpeList.isEmpty())
						{
							if (oldAction.getBarcode() != null
									&& !oldAction.getBarcode().equals(action.getBarcode())
									|| !oldAction.getActionOrder().equals(action.getActionOrder())
									|| action.getActivityStatus().equals(Constants.DISABLED))
							{
								ApplicationException appExp = new ApplicationException(null, null,
										Constants.SPP_EDIT_WARNING);
								appExp.setCustomizedMsg(Constants.SPP_EDIT_WARNING);
								this.logger.error(Constants.SPP_EDIT_WARNING, appExp);
								throw this.getBizLogicException(appExp, "",
										Constants.SPP_EDIT_WARNING);
							}
							if (oldAction.getApplicationDefaultValue() != null)
							{
								action.setApplicationDefaultValue(oldAction
										.getApplicationDefaultValue());
							}
						}
						else
						{
							if (oldAction.getApplicationDefaultValue() != null)
							{
								action.setApplicationDefaultValue(oldAction
										.getApplicationDefaultValue());
							}
						}
						action.setId(oldAction.getId());
						break;
					}
				}
				newActList.add(action);
			}
			sppNewObj.getActionCollection().addAll(newActList);
			dao.update(sppNewObj, sppOldObj);
			updateXMLTemplateInDB(sppNewObj.getId(), xmlFile);
		}
		catch (final DAOException daoExp)
		{
			if (daoExp.getWrapException().getCause().getMessage().contains(Constants.NAME_KEY))
			{
				ApplicationException appExp = new ApplicationException(null, null,
						Constants.SPP_NAME_ERROR);
				appExp.setCustomizedMsg(Constants.SPP_NAME_ERROR);
				this.logger.error(Constants.SPP_NAME_ERROR, appExp);
				throw this.getBizLogicException(appExp, "", Constants.SPP_NAME_ERROR);
			}
			else if (daoExp.getWrapException().getCause().getMessage().contains(
					Constants.BARCODE_KEY))
			{
				ApplicationException appExp = new ApplicationException(null, null,
						Constants.SPP_BARCODE_ERROR);
				appExp.setCustomizedMsg(Constants.SPP_BARCODE_ERROR);
				this.logger.error(Constants.SPP_BARCODE_ERROR, appExp);
				throw this.getBizLogicException(appExp, "", Constants.SPP_BARCODE_ERROR);
			}
			else
			{
				this.logger.error(daoExp.getMessage(), daoExp);
				daoExp.printStackTrace();
				throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp
						.getMsgValues());
			}
		}
		catch (final IOException ioExp)
		{
			this.logger.error(ioExp.getMessage(), ioExp);
			throw this.getBizLogicException(ioExp, "", ioExp.getLocalizedMessage());
		}
		catch (DynamicExtensionsSystemException deExp)
		{
			this.logger.error(deExp.getMessage(), deExp);
			throw this.getBizLogicException(deExp, "", deExp.getLocalizedMessage());
		}
		catch (SAXException saxExp)
		{
			this.logger.error(saxExp.getMessage(), saxExp);
			throw this.getBizLogicException(saxExp, "", saxExp.getLocalizedMessage());
		}
		catch (ApplicationException e)
		{
			this.logger.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (MissingResourceException e)
		{
			ApplicationException appExp = new ApplicationException(null, null,
					Constants.INVALID_XML_MSG);
			appExp.setCustomizedMsg(Constants.INVALID_XML_MSG);
			this.logger.error(Constants.INVALID_XML_MSG, e);
			throw this.getBizLogicException(appExp, "", Constants.INVALID_XML_MSG);
		} 
	}
	
	/**
	 * This method will update the caTissue_SPP table with the latest SPP XML Template
	 * @param id
	 * @param xmlFile
	 * @throws BizLogicException 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ApplicationException
	 * @throws DAOException
	 * @throws SQLException
	 */
	private void updateXMLTemplateInDB(
			final Long id, FormFile xmlFile) throws BizLogicException
			{
		JDBCDAO jdbcdao = null;
		InputStream stream;
		try {
			stream = xmlFile.getInputStream();
		
		
		jdbcdao = AppUtility.openJDBCSession();
		PreparedStatement statement = jdbcdao.getPreparedStatement("update catissue_spp set spp_template_xml=? where identifier=?");
		
		BufferedInputStream bis = new BufferedInputStream(stream);
		//To move it to utility class n make it simpler
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while(result != -1) {
		  byte b = (byte)result;
		  buf.write(b);
		  result = bis.read();
		}        
		String res = AppUtility.convertStreamToString(stream);
		if(jdbcdao instanceof MySQLDAOImpl)
		{
			statement.setString(1, res);
		}
		else if(jdbcdao instanceof OracleDAOImpl)
		{
			StringReader reader = new StringReader(res);
			statement.setCharacterStream(1, reader, res.length());
		}
		
		statement.setLong(2, id);
		statement.execute();
		jdbcdao.commit();
		} 
		catch (Exception e) 
		{
			this.logger.error("Error while inserting SPP XML in database, Please contact Administrator", e);
			throw this.getBizLogicException(e, "", "Error while inserting SPP XML in database, Please contact Administrator");
		} 
		finally
		{
			try {
				AppUtility.closeDAOSession(jdbcdao);
			} catch (ApplicationException e) {
				this.logger.error("Errpr While inserting Template in db", e);
				throw this.getBizLogicException(e, "", "Errpr While inserting Template in db");
			}
		}
	}

	/**
	 * @param sppIdentifier
	 * @return
	 * @throws BizLogicException
	 */
	public SpecimenProcessingProcedure getSPPById(Long sppIdentifier) throws BizLogicException
	{
		return (SpecimenProcessingProcedure) retrieve(SpecimenProcessingProcedure.class.getName(), sppIdentifier);
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * @param dao dao
	 * @param domainObject domainObject
	 * @return String ADMIN_PROTECTION_ELEMENT
	 */
	public String getObjectId(DAO dao, Object domainObject, Object uiObject)
	{
		return Constants.ADMIN_PROTECTION_ELEMENT;
	}

	/**
	 * To get PrivilegeName for authorization check from.
	 * 'PermissionMapDetails.xml' (non-Javadoc)
	 * @param domainObject domainObject
	 * @return String ADD_EDIT_SPP
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_SPP;
	}

	/**
	 * Gets the scgs id by sppid.
	 *
	 * @param sppId the spp id
	 * @return the scgs id by sppid
	 * @throws ApplicationException the application exception
	 */
	public List getScgsIdBySPPID(Long sppId) throws ApplicationException
	{
		String query = "select scg.identifier from CATISSUE_SPECIMEN_COLL_GROUP as scg "
				+ "inner join catissue_cpe_spp as ccs on ccs.cpe_identifier = scg.COLLECTION_PROTOCOL_EVENT_ID "
				+ "where ccs.spp_identifier=" + sppId
				+ " and scg.COLLECTION_STATUS not in ('Pending','overdue','not collected') ";
		List idList = AppUtility.executeSQLQuery(query);
		return idList;
	}

	/**
	 * Gets the specimens id by sppid.
	 *
	 * @param sppId the spp id
	 * @return the specimens id by sppid
	 * @throws ApplicationException the application exception
	 */
	public List getSpecimensIdBySPPID(Long sppId) throws ApplicationException
	{

		String query = "select cs.identifier from catissue_specimen as cs "
				+ "inner join catissue_cp_req_specimen cprs on cprs.identifier = cs.REQ_SPECIMEN_ID "
				+ "where cprs.SPP_IDENTIFIER = "
				+ sppId
				+ " and cs.COLLECTION_STATUS ='Collected' "
				+ "and cs.identifier not in (select caa.SPECIMEN_ID from catissue_action_application as caa where caa.SPECIMEN_ID= cs.identifier)";
		List idList = AppUtility.executeSQLQuery(query);
		return idList;
	}

	/**
	 * Gets the all spp names.
	 *
	 * @return the all spp names
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public List getAllSPPNames() throws BizLogicException
	{
		DAO dao;
		List<String> dataList = null;
		List<NameValueBean> sppNameList = new ArrayList<NameValueBean>();
		sppNameList.add(new NameValueBean(Constants.SELECT_OPTION, Constants.SELECT_OPTION));
		try
		{
			dao = openDAOSession(null);
			String hql = "Select name from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure";
			dataList = dao.executeQuery(hql);
			closeDAOSession(dao);
		}
		catch (DAOException e)
		{
			logger.error(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		Iterator<String> dataListIter = dataList.iterator();
		while (dataListIter.hasNext())
		{
			String sppName = dataListIter.next();
			sppNameList.add(new NameValueBean(sppName, sppName));
		}
		return sppNameList;
	}

	/**
	 * @param spp
	 * @return
	 * @throws ApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException
	 */
	public Map<Action, Long> generateContextRecordIdMap(SpecimenProcessingProcedure spp) throws ApplicationException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException, SQLException
	{
		Map<Action, Long> contextRecordIdMap = new HashMap<Action, Long>();
		for (Action action : spp.getActionCollection())
		{
			if (action.getApplicationDefaultValue() != null)
			{
				Long recordIdentifier = SpecimenEventsUtility.getRecordIdentifier(action
						.getApplicationDefaultValue().getId(), action.getContainerId());
				contextRecordIdMap.put(action, recordIdentifier);
			}
		}
		return contextRecordIdMap;
	}


	/**
	 * @param formContext
	 * @param action
	 * @return
	 * @throws ApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException
	 */
	public Map<AbstractFormContext, Long> generateContextRecordIdMap(
			AbstractFormContext formContext, Action action) throws ApplicationException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException, SQLException
	{
		Map<AbstractFormContext, Long> contextRecordIdMap = new HashMap<AbstractFormContext, Long>();
		if (action.getApplicationDefaultValue() != null)
		{
			Long recordIdentifier = SpecimenEventsUtility.getRecordIdentifier(action
					.getApplicationDefaultValue().getId(), action.getContainerId());
			contextRecordIdMap.put(formContext, recordIdentifier);
		}
		return contextRecordIdMap;
	}

	/**
	 * Gets the sPP name list.
	 *
	 * @param scg the scg
	 *
	 * @return the sPP name list
	 */
	public List<NameValueBean> getSPPNameList(SpecimenCollectionGroup scg)
	{
		List<NameValueBean> sppNameList=new ArrayList<NameValueBean>();
		sppNameList.add(new NameValueBean(Constants.SELECT_OPTION, Constants.SELECT_OPTION));

////		Collection<SpecimenProcessingProcedure> sppCollection=scg.getCollectionProtocolEvent().getsgetSppCollection();
//		Iterator<SpecimenProcessingProcedure> sppIter=sppCollection.iterator();
//		while(sppIter.hasNext())
//		{
//			SpecimenProcessingProcedure spp=sppIter.next();
//			String sppName=spp.getName();
//			sppNameList.add(new NameValueBean(sppName,sppName));
//		}
		return sppNameList;
	}

	/**
	 * Gets the all spp event form names.
	 *
	 * @param dynamicEventMap the dynamic event map
	 *
	 * @return the all spp event form names
	 */
	public String[] getAllSPPEventFormNames(Map<String, Long> dynamicEventMap)
	{
		dynamicEventMap.clear();
		EntityCache cache = EntityCache.getInstance();
		EntityGroupInterface entityGroup = cache.getEntityGroupById(1L);
		EntityInterface actionRecordEntry = entityGroup
				.getEntityByName("edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry");
		List<String> namesOfSPPEvents = new ArrayList<String>();
		if (actionRecordEntry != null)
		{
			Collection<AssociationInterface> allAsso = actionRecordEntry.getAllAssociations();
			for (AssociationInterface associationInterface : allAsso)
			{
				Collection<edu.common.dynamicextensions.domain.userinterface.Container> containerCollection = associationInterface
						.getTargetEntity().getContainerCollection();
				Iterator<edu.common.dynamicextensions.domain.userinterface.Container> contIter = containerCollection
						.iterator();
				if (contIter.hasNext())
				{
				/*	if(!"TransferEventParameters".equals(associationInterface.getTargetEntity().getName())
							&& !"DisposalEventParameters".equals(associationInterface.getTargetEntity().getName()))
					{*/
					namesOfSPPEvents.add(edu.wustl.cab2b.common.util.Utility
							.getFormattedString(associationInterface.getTargetEntity().getName()));
					edu.common.dynamicextensions.domain.userinterface.Container container = contIter
							.next();
					dynamicEventMap.put(edu.wustl.cab2b.common.util.Utility
							.getFormattedString(associationInterface.getTargetEntity().getName()),
							container.getId());
					//}
				}
			}
		}
		else
		{
			logger
					.error("edu.wustl.catissuecore.util.global.AppUtility.getAllSPPEventFormNames(Map<String, Long>): actionRecordEntry is NULL!");
		}
		Collections.sort(namesOfSPPEvents);
		String[] eventlist = new String[namesOfSPPEvents.size()];
		for (int k = 0, l = 0; l < namesOfSPPEvents.size();l++)
		{
			if(!"Transfer Event Parameters".equals(namesOfSPPEvents.get(l))
					&& !"Disposal Event Parameters".equals(namesOfSPPEvents.get(l)))
			{
				eventlist[k] = namesOfSPPEvents.get(l);
				k++;
			}

		}
		return eventlist;
	}

	/**
	 * Gets the all events for spp.
	 *
	 * @param sppName the spp name
	 *
	 * @return the all events for spp
	 * @throws BizLogicException
	 */
	public List getAllEventsForSPP(String sppName) throws BizLogicException
	{
		DAO dao;
		Collection<Action> dataList = null;
		List<NameValueBean> sppNameList = new ArrayList<NameValueBean>();
		try
		{
			dao = openDAOSession(null);
			String hql = "Select actionCollection from edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure where name='"
					+ sppName + "'";
			dataList = dao.executeQuery(hql);
			closeDAOSession(dao);
			Iterator<Action> dataListIter = dataList.iterator();
			while (dataListIter.hasNext())
			{
				Action action = dataListIter.next();
				final EntityManagerInterface entityManager = EntityManager.getInstance();
				String containerName = edu.wustl.cab2b.common.util.Utility
						.getFormattedString(entityManager.getContainerCaption(action
								.getContainerId()));
				String sppActionName = sppName.concat(" : ").concat(action.getActionOrder().toString()).concat(" : ").concat(containerName);
				sppNameList.add(new NameValueBean(sppActionName, sppActionName));
			}
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			this.logger.error(e.getMessage(), e);
			throw this.getBizLogicException(e, "", e.getLocalizedMessage());
		}
		Collections.sort(sppNameList);
		return sppNameList;
	}

	/**
	 * Gets the all event names.
	 *
	 * @return the all event names
	 */
	public static List getAllEventNames()
	{
		List eventList = new ArrayList();
		eventList.add(new NameValueBean(Constants.SELECT_OPTION, Constants.SELECT_OPTION));
		EntityCache cache = EntityCache.getInstance();
		EntityGroupInterface entityGroup = cache.getEntityGroupById(1L);
		EntityInterface actionRecordEntry = entityGroup
				.getEntityByName("edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry");
		List<String> namesOfSPPEvents = new ArrayList<String>();
		if (actionRecordEntry != null)
		{
			Collection<AssociationInterface> allAsso = actionRecordEntry.getAllAssociations();
			for (AssociationInterface associationInterface : allAsso)
			{
				Collection<edu.common.dynamicextensions.domain.userinterface.Container> containerCollection = associationInterface
						.getTargetEntity().getContainerCollection();
				Iterator<edu.common.dynamicextensions.domain.userinterface.Container> contIter = containerCollection
						.iterator();
				if (contIter.hasNext())
				{
					namesOfSPPEvents.add(edu.wustl.cab2b.common.util.Utility
							.getFormattedString(associationInterface.getTargetEntity().getName()));
					edu.common.dynamicextensions.domain.userinterface.Container container = contIter
							.next();
					String eventName = edu.wustl.cab2b.common.util.Utility
							.getFormattedString(associationInterface.getTargetEntity().getName());
					eventList.add(new NameValueBean(eventName, eventName));
				}
			}
		}
		return eventList;
	}

	public InputStream getTemplateAsStream(Long id)
	throws Exception 
	{
		InputStream in = null;
		JDBCDAO jdbcdao = null;
		try
		{
			jdbcdao = AppUtility.openJDBCSession();
			String query = "select spp_template_xml from catissue_spp where identifier = ?";
			java.sql.PreparedStatement statement = jdbcdao.getPreparedStatement(query);
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			
			 while (rs.next()){
			 in = rs.getBinaryStream(1);
			 }
		}
		
		finally
		{
			AppUtility.closeJDBCSession(jdbcdao);
		}
		return in;
		}
	}
