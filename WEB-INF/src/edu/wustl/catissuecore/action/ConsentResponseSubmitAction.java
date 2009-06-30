/**
 * <p>Title: ConsentResponseSubmitAction Class>
 * <p>Description: ConsentResponseSubmitAction class is for creating session object of consent response for selected collection protocol registration </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek Mehta
 * @version 1.00
 * Created on Sept 5, 2007
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConsentResponseForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

public class ConsentResponseSubmitAction extends BaseAction
{

	private static org.apache.log4j.Logger logger = Logger
			.getLogger(ConsentResponseSubmitAction.class);

	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		logger.debug("Inside  consent response submit ");
		ConsentResponseForm consentForm = (ConsentResponseForm) form;
		HttpSession session = request.getSession();
		if (consentForm != null)
		{
			long collectionProtocolID = consentForm.getCollectionProtocolID();
			String signedConsentUrl = consentForm.getSignedConsentUrl();
			long witnessId = consentForm.getWitnessId();
			String consentSignatureDate = consentForm.getConsentDate();
			Map consentResponseValues = consentForm.getConsentResponseValues();
			MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
			Collection consentResponseCollection = mapdataParser
					.generateData(consentResponseValues);

			/*Iterator itr = consentResponseCollection.iterator();
			
			while(itr.hasNext())
			{
				ConsentBean consentBean = (ConsentBean)itr.next();
				logger.debug(":::submit  participant response ::"+consentBean.getParticipantResponse());
				logger.debug(":::submit  participant response id ::"+consentBean.getParticipantResponseID());
			}*/

			String withdrawlButtonStatus = consentForm.getWithdrawlButtonStatus();
			ConsentResponseBean consentResponseBean = new ConsentResponseBean(collectionProtocolID,
					signedConsentUrl, witnessId, consentSignatureDate, consentResponseCollection,
					withdrawlButtonStatus);
			String consentResponseKey = Constants.CONSENT_RESPONSE_KEY + collectionProtocolID;
			Map consentResponseHashTable = (Map) session.getAttribute(Constants.CONSENT_RESPONSE);
			if (consentResponseHashTable == null)
			{
				consentResponseHashTable = new LinkedHashMap();
			}
			if (consentResponseHashTable.containsKey(consentResponseKey))
			{
				consentResponseHashTable.remove(consentResponseKey);
			}
			consentResponseHashTable.put(consentResponseKey, consentResponseBean);
			session.setAttribute(Constants.CONSENT_RESPONSE, consentResponseHashTable);
		}
		return null;
	}
}
