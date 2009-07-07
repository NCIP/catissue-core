/**
 * <p>
 * Title: ScgEventsAjaxHandlerAction Class>
 * <p>
 * Description: This class populates the events fields in the Specimen page
 * based on the SCG selected.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Ashish Gupta
 * @version 1.00 Created on April 17, 2007
 */

package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * @author ashish_gupta
 */
public class ScgEventsAjaxHandlerAction extends BaseAction
{

	/**
	 * collectionEventParameters.
	 */

	CollectionEventParameters collectionEventParameters = null;

	/**
	 * receivedEventParameters.
	 */

	ReceivedEventParameters receivedEventParameters = null;

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final String scgId = request.getParameter("scgId");

		StringBuffer xmlData = new StringBuffer();
		if (scgId != null && !scgId.equals(""))
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic = (SpecimenCollectionGroupBizLogic) factory
					.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);

			final Object object = specimenCollectionGroupBizLogic.retrieve(
					SpecimenCollectionGroup.class.getName(), new Long(scgId));
			if (object != null)
			{
				final SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) object;
				final Collection eventsColl = specimenCollectionGroup
						.getSpecimenEventParametersCollection();
				if (eventsColl != null && !eventsColl.isEmpty())
				{
					final Iterator iter = eventsColl.iterator();
					while (iter.hasNext())
					{
						final Object temp = iter.next();
						if (temp instanceof CollectionEventParameters)
						{
							this.collectionEventParameters = (CollectionEventParameters) temp;
						}
						else if (temp instanceof ReceivedEventParameters)
						{
							this.receivedEventParameters = (ReceivedEventParameters) temp;
						}

					}
					xmlData = this.makeXMLData(xmlData);
				}
			}
		}
		// Writing to response
		final PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		out.write(xmlData.toString());

		return null;
	}

	/**
	 *
	 * @param xmlData : xmlData
	 * @return StringBuffer : StringBuffer
	 */
	private StringBuffer makeXMLData(StringBuffer xmlData)
	{

		xmlData.append("<Events>");

		xmlData.append("<CollectionEvents>");
		xmlData = this.appendCollectionEvents(xmlData);
		xmlData.append("</CollectionEvents>");

		xmlData.append("<ReceivedEvents>");
		xmlData = this.appendReceivedEvents(xmlData);
		xmlData.append("</ReceivedEvents>");

		xmlData.append("</Events>");
		return xmlData;

	}

	/**
	 * @param xmlData : xmlData
	 * @return StringBuffer : StringBuffer
	 */
	private StringBuffer appendCollectionEvents(StringBuffer xmlData)
	{
		xmlData.append("<CollectorId>");
		xmlData.append(this.collectionEventParameters.getUser().getId().toString());
		xmlData.append("</CollectorId>");

		xmlData.append("<CollectorName>");
		xmlData.append(this.collectionEventParameters.getUser().getLastName() + ","
				+ this.collectionEventParameters.getUser().getFirstName());
		xmlData.append("</CollectorName>");

		xmlData.append("<CollectionDate>");
		xmlData.append(CommonUtilities.parseDateToString(this.collectionEventParameters
				.getTimestamp(), CommonServiceLocator.getInstance().getDatePattern()));
		xmlData.append("</CollectionDate>");

		final Calendar calender = Calendar.getInstance();
		calender.setTime(this.collectionEventParameters.getTimestamp());

		xmlData.append("<CollectionTimeHours>");
		xmlData.append(CommonUtilities.toString(Integer
				.toString(calender.get(Calendar.HOUR_OF_DAY))));
		xmlData.append("</CollectionTimeHours>");

		xmlData.append("<CollectionTimeMinutes>");
		xmlData.append(CommonUtilities.toString(Integer.toString(calender.get(Calendar.MINUTE))));
		xmlData.append("</CollectionTimeMinutes>");

		xmlData.append("<CollectionProcedure>");
		xmlData.append(this.collectionEventParameters.getCollectionProcedure());
		xmlData.append("</CollectionProcedure>");

		xmlData.append("<CollectionContainer>");
		xmlData.append(this.collectionEventParameters.getContainer());
		xmlData.append("</CollectionContainer>");

		xmlData.append("<CollectionComments>");
		/*
		 * Bug Id: 4134 Patch ID: 4134_2 Description: Added
		 * AppUtility.toString()
		 */
		xmlData.append(CommonUtilities.toString(this.collectionEventParameters.getComment()));
		xmlData.append("</CollectionComments>");

		return xmlData;
	}

	/**
	 * @param xmlData : xmlData
	 * @return StringBuffer : StringBuffer
	 */
	private StringBuffer appendReceivedEvents(StringBuffer xmlData)
	{
		xmlData.append("<ReceiverId>");
		xmlData.append(this.receivedEventParameters.getUser().getId().toString());
		xmlData.append("</ReceiverId>");

		xmlData.append("<ReceiverName>");
		xmlData.append(this.receivedEventParameters.getUser().getLastName() + ","
				+ this.receivedEventParameters.getUser().getFirstName());
		xmlData.append("</ReceiverName>");

		xmlData.append("<ReceivedDate>");
		xmlData.append(CommonUtilities.parseDateToString(this.receivedEventParameters
				.getTimestamp(), CommonServiceLocator.getInstance().getDatePattern()));
		xmlData.append("</ReceivedDate>");

		final Calendar calender = Calendar.getInstance();
		calender.setTime(this.receivedEventParameters.getTimestamp());

		xmlData.append("<ReceivedTimeHours>");
		xmlData.append(CommonUtilities.toString(Integer
				.toString(calender.get(Calendar.HOUR_OF_DAY))));
		xmlData.append("</ReceivedTimeHours>");

		xmlData.append("<ReceivedTimeMinutes>");
		xmlData.append(CommonUtilities.toString(Integer.toString(calender.get(Calendar.MINUTE))));
		xmlData.append("</ReceivedTimeMinutes>");

		xmlData.append("<ReceivedQuality>");
		xmlData.append(this.receivedEventParameters.getReceivedQuality());
		xmlData.append("</ReceivedQuality>");

		xmlData.append("<ReceivedComments>");
		/*
		 * Bug Id: 4134 Patch ID: 4134_1 Description: Added
		 * AppUtility.toString()
		 */
		xmlData.append(CommonUtilities.toString(this.receivedEventParameters.getComment()));
		xmlData.append("</ReceivedComments>");

		return xmlData;
	}
}
