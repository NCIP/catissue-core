/**
 * <p>Title: ConsentResponseDisplayAction Class>
 * <p>Description: ConsentResponseDisplayAction class is for
 * displaying consent response on ParticipantConsentTracking.jsp </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek Mehta
 * @version 1.00
 * Created on Sept 5, 2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConsentResponseForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author renuka_bajpai
 *
 */
public class ConsentResponseDisplayAction extends BaseAction
{

	/**
	 * consentCounter.
	 */
	int consentCounter;

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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final ConsentResponseForm consentForm = (ConsentResponseForm) form;
		final HttpSession session = request.getSession();

		//Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		//Gets the value of collection protocol id.
		String collectionProtocolId = null;
		if (request.getParameter(Constants.CP_SEARCH_CP_ID) != null)
		{
			collectionProtocolId = request.getParameter(Constants.CP_SEARCH_CP_ID);
		}
		String collectionProtocolRegIdValue = null;
		if (request.getParameter("collectionProtocolRegIdValue") != null)
		{
			collectionProtocolRegIdValue = request.getParameter("collectionProtocolRegIdValue");
		}

		final long cpId = new Long(collectionProtocolId).longValue();
		//Bug: 5935
		//Remove old list of specimen from Session.
		session.removeAttribute(Constants.SPECIMEN_LIST);
		//As per the collection protocol registration id of Participant set
		//All Participant's Specimen List to Session
		if (collectionProtocolRegIdValue != null && !(collectionProtocolRegIdValue.equals("")))
		{
			final List<String> columnList = ConsentUtil.columnNames();
			session.setAttribute(Constants.COLUMNLIST, columnList);
			final CollectionProtocolRegistration collectionProtocolRegistration = AppUtility
					.getcprObj(collectionProtocolRegIdValue);
			final List specimenDetails = new ArrayList();
			ConsentUtil.getSpecimenDetails(collectionProtocolRegistration, specimenDetails);
			session.setAttribute(Constants.SPECIMEN_LIST, specimenDetails);
		}
		//Getting witness name list for CollectionProtocolID
		final List witnessList = ConsentUtil.witnessNameList(collectionProtocolId);
		//Getting ResponseList if Operation=Edit
		// then "Withdraw" is added to the List
		final List<NameValueBean> responseList = AppUtility.responceList(operation);

		//Getting consent response map.
		final String consentResponseKey = Constants.CONSENT_RESPONSE_KEY + cpId;
		final Map consentResponseHashTable = (Map) session.getAttribute(Constants.CONSENT_RESPONSE);

		Map consentResponseMap;
		if (consentResponseHashTable != null
				&& consentResponseHashTable.containsKey(consentResponseKey))
		// If Map already exist in session
		{
			final ConsentResponseBean consentResponseBean = (ConsentResponseBean) consentResponseHashTable
					.get(consentResponseKey);
			final Collection consentResponseCollection = consentResponseBean.getConsentResponse();
			consentResponseMap = this.getConsentResponseMap(consentResponseCollection, true);
			consentForm.setSignedConsentUrl(consentResponseBean.getSignedConsentUrl());
			consentForm.setWitnessId(consentResponseBean.getWitnessId());
			consentForm.setConsentDate(consentResponseBean.getConsentDate());

		}
		else
		{
			final Collection consentResponseCollection = ConsentUtil
					.getConsentList(collectionProtocolId);
			consentResponseMap = this.getConsentResponseMap(consentResponseCollection, false);
		}

		consentForm.setCollectionProtocolID(cpId);
		consentForm.setConsentResponseValues(consentResponseMap);
		consentForm.setConsentTierCounter(this.consentCounter);
		final String pageOf = request.getParameter(Constants.PAGE_OF);

		request.setAttribute("witnessList", witnessList);
		request.setAttribute("responseList", responseList);
		request.setAttribute("cpId", collectionProtocolId);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		request.setAttribute(consentResponseKey, consentResponseMap);

		return mapping.findForward(pageOf);
	}

	/**
	 * Returns the Map of consent responses for given collection protocol.
	 * @param consentResponse : consentResponse
	 * @param isMapExist : isMapExist
	 * @return Map : Map
	 */
	private Map getConsentResponseMap(Collection consentResponse, boolean isMapExist)
	{
		final Map consentResponseMap = new LinkedHashMap();
		final Set consentResponseMapSorted = new LinkedHashSet();
		//bug 8905
		final List idList = new ArrayList();
		idList.addAll(consentResponse);
		Collections.sort(idList, new IdComparator());
		consentResponseMapSorted.addAll(idList);
		//		bug 8905
		if (consentResponseMapSorted != null)
		{
			int i = 0;
			final Iterator consentResponseIter = consentResponseMapSorted.iterator();
			String idKey = null;
			String statementKey = null;
			String responsekey = null;
			String participantResponceIdKey = null;
			while (consentResponseIter.hasNext())
			{
				idKey = "ConsentBean:" + i + "_consentTierID";
				statementKey = "ConsentBean:" + i + "_statement";
				responsekey = "ConsentBean:" + i + "_participantResponse";
				participantResponceIdKey = "ConsentBean:" + i + "_participantResponseID";

				if (isMapExist)
				{
					final ConsentBean consentBean = (ConsentBean) consentResponseIter.next();
					consentResponseMap.put(idKey, consentBean.getConsentTierID());
					consentResponseMap.put(statementKey, consentBean.getStatement());
					consentResponseMap.put(responsekey, consentBean.getParticipantResponse());
					consentResponseMap.put(participantResponceIdKey, consentBean
							.getParticipantResponseID());
				}
				else
				{
					final ConsentTier consent = (ConsentTier) consentResponseIter.next();
					consentResponseMap.put(idKey, consent.getId());
					consentResponseMap.put(statementKey, consent.getStatement());
					consentResponseMap.put(responsekey, "");
					consentResponseMap.put(participantResponceIdKey, "");
				}
				i++;
			}

			this.consentCounter = i;
		}
		return consentResponseMap;
	}

}