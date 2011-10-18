package edu.wustl.catissuecore.bizlogic;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.processingprocedure.EventType;
import edu.wustl.catissuecore.processingprocedure.SPPType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.metadata.XMLUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;

public final class SOPXMLParser
{
	private static SOPXMLParser parser;

	private SOPXMLParser()
	{

	}

	/**
	 * get the only instance of this class
	 * @return the only instance of this class
	 */
	public static SOPXMLParser getInstance()
	{
		if (parser==null)
		{
			parser= new SOPXMLParser();
		}
		return parser;
	}

	/**
	 * Parses the XML file and returns the list of action objects.
	 * @param xmlFile xmlFile
	 * @param inputStream inputStream
	 * @return actionList
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws SAXException SAXException
	 * @throws BizLogicException
	 */
	public Set<Action> parseXML(String xmlFile, InputStream inputStream) throws DynamicExtensionsSystemException, SAXException, BizLogicException
	{
		Set<Action> actionList = new HashSet<Action>();
		final String packageName = SPPType.class.getPackage().getName();

		final JAXBElement jAXBElement = (JAXBElement) XMLUtility.getJavaObjectForXML(xmlFile,
				inputStream, packageName, Constants.SOP_XSD_FILENAME);

		SPPType spp = (SPPType)jAXBElement.getValue();
		List<EventType> events = spp.getEvent();
		for(EventType event : events)
		{
			populateActionList(actionList, event);
		}
		handleActionErrors(actionList);
		return actionList;
	}

	/**
	 * Populates the list of actions belonging to an SPP.
	 * @param actionList actionList
	 * @param event event
	 * @throws DynamicExtensionsSystemException
	 */
	private void populateActionList(Set<Action> actionList, EventType event) throws DynamicExtensionsSystemException
	{
			String name = event.getName();
			String barcode = event.getBarcode();
			Long order = event.getOrder();
			String entityGrp = event.getEntityGroupName();
			String activity = event.getActivity();
			String uniqueId = event.getUniqueId();

			if(activity == null)
			{
				activity = Constants.ACTIVE;
			}
			if(barcode != null && barcode.length() == 0)
			{
				barcode = null;
			}
			Action actionObj = new Action();
			EntityGroupInterface entityGroup = DynamicExtensionsUtility.getEntityGroupByName(entityGrp);
			if(entityGroup == null)
			{
				throw new DynamicExtensionsSystemException(Constants.SOP_ENTITY_GRP_ERROR+": '"+entityGrp+"'");
			}
			EntityInterface entity = entityGroup.getEntityByName(name);
			if(entity == null)
			{
				throw new DynamicExtensionsSystemException(Constants.SOP_EVENT_ERROR+": '"+name+"'");
			}
			Collection<AbstractEntityInterface> containers = entity.getContainerCollection();
			Iterator<AbstractEntityInterface> itr = containers.iterator();
			if(itr.hasNext())
			{
				Container container = (Container)itr.next();
				actionObj.setContainerId(container.getId());
			}
			actionObj.setBarcode(barcode);
			actionObj.setActionOrder(order);
			actionObj.setUniqueId(uniqueId);
			actionObj.setActivityStatus(activity);
			actionList.add(actionObj);
	}

	/**
	 * Check the XML for validations and throw appropriate exception depending upon the error.
	 * @param actionList actionList
	 * @throws BizLogicException BizLogicException
	 */
	private void handleActionErrors(Set<Action> actionList) throws BizLogicException
	{
		for (Action action : actionList)
		{
			for (Action currentAction : actionList)
			{
				if (action != currentAction)
				{
					if (action.getUniqueId().equals(currentAction.getUniqueId()))
					{
						ApplicationException appExp = new ApplicationException(null, null,
								Constants.UNIQUE_ID_ERROR);
						appExp.setCustomizedMsg(Constants.UNIQUE_ID_ERROR);
						throw new BizLogicException(appExp.getErrorKey(), appExp, Constants.UNIQUE_ID_ERROR);
					}
					else if (action.getBarcode() != null
							&& action.getBarcode().equals(currentAction.getBarcode()))
					{
						ApplicationException appExp = new ApplicationException(null, null,
								Constants.EVENT_BARCODE_ERROR);
						appExp.setCustomizedMsg(Constants.EVENT_BARCODE_ERROR);
						throw new BizLogicException(appExp.getErrorKey(), appExp, Constants.EVENT_BARCODE_ERROR);
					}
					else if (action.getActionOrder().equals(currentAction.getActionOrder()))
					{
						ApplicationException appExp = new ApplicationException(null, null,
								Constants.EVENT_ORDER_ERROR);
						appExp.setCustomizedMsg(Constants.EVENT_ORDER_ERROR);
						throw new BizLogicException(appExp.getErrorKey(), appExp, Constants.EVENT_ORDER_ERROR);
					}
				}
			}
		}
	}
}
