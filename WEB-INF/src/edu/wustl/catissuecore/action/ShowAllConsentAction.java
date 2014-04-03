package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.bean.ConsentDisplayBean;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Validator;

public class ShowAllConsentAction extends BaseAction
{

	String labelIndexCount = "";
	/**
	 * consentTierCounter.
	 */
	int consentTierCounter;
	/**
	 * listOfMap.
	 */

	List listOfMap = null;
	/**
	 * listOfStringArray.
	 */
	List listOfStringArray = null;
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		final DistributionForm dForm = (DistributionForm) form;

		// Show Consents for Specimen
		final String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS);
		Long orderId = 0l;
		if(!Validator.isEmpty(request.getParameter("orderId")))
		{
			orderId = Long.valueOf(String.valueOf(request.getParameter("orderId")));
		}
		Long specimenId = null;
		List consentWaivedList = new ArrayList();
		
			if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
			{
				final String speciemnIdValue = (String)request.getSession().getAttribute("speciemnIdValue");//request.getParameter("speciemnIdValue");// barcodelabel
				this.labelIndexCount = request.getParameter("labelIndexCount");
				final StringTokenizer stringToken = new StringTokenizer(speciemnIdValue, "|");
				// StringTokenizer stringTokenForIndex = new
				// StringTokenizer(labelIndexCount, "|");
				this.listOfMap = new ArrayList();
				this.listOfStringArray = new ArrayList();
				Map<String, ConsentDisplayBean> consentMap = new HashMap<String, ConsentDisplayBean>();
				Map<Long, Specimen> specMap = OrderingSystemUtil.populateSpecimenMap(orderId);
				String forwardTo = "noConsent";
				if(!specMap.isEmpty())
				{
					forwardTo = Constants.VIEWAll;
					while (stringToken.hasMoreTokens())
					{
						String[] arr = stringToken.nextToken().split(",");
						specimenId = Long.parseLong(arr[0]);
						
	//					final Specimen specimen = OrderingSystemUtil.getListOfSpecimen(specMap,specimenId);
						final Specimen specimen = specMap.get(specimenId);
						if(specimen != null)
						{
							specimen.setLabel(arr[1]);
							if(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getConsentsWaived())
							{
								consentWaivedList.add(specimen.getLabel());
							}
							else
							{
								String key = createMapKey(specimen,consentMap);
								if(consentMap.containsKey(key))
								{
									updateConsentMap(consentMap,key,specimen.getLabel());
								}
								else
								{
									this.showConsents(dForm, specimen, request,consentMap);						
								}
							}
						}
					}
					populateListOfMap(consentMap);
					
				}
				request.setAttribute("consentWaivedList",consentWaivedList);
				request.setAttribute("listOfStringArray", this.listOfStringArray);
				request.setAttribute("listOfMap", this.listOfMap);
				request.setAttribute("labelIndexCount", this.labelIndexCount);
				request.setAttribute("speciemnIdValue", speciemnIdValue);
				return mapping.findForward(forwardTo);// ViewAll
				
			}
		// Consent Tracking
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}
	private String createMapKey(Specimen specimen,Map<String, ConsentDisplayBean> consentMap) 
	{
		StringBuilder builder = new StringBuilder(100);
//		if(!specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
//		{
			builder.append(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId());
			
//			Collection<ConsentTierStatus> specimenCon = specimen.getConsentTierStatusCollection();
			List<ConsentTierStatus> consentList = new ArrayList<ConsentTierStatus>();
		/*	consentList.addAll(specimenCon);
			Collections.sort(consentList, new IdComparator());
			for (ConsentTierStatus consentTierStatus : consentList) 
			{
				builder.append(consentTierStatus.getConsentTier().getId());
				builder.append(consentTierStatus.getStatus());
			}*/
//		}
		return builder.toString();
	}
	private void updateConsentMap(Map<String, ConsentDisplayBean> consentMap,String key, String label) 
	{
		ConsentDisplayBean bean = consentMap.get(key);
		bean.setSpecimenLabel(label);
	}
	private void populateListOfMap(Map<String, ConsentDisplayBean> consentMap) 
	{
		Collection<ConsentDisplayBean> beans= consentMap.values();
		for (ConsentDisplayBean consentDisplayBean : beans) 
		{
			listOfMap.add(consentDisplayBean.getConsentMap());
			listOfStringArray.add(consentDisplayBean.getConsentValues());
		}
		
	}
	
	
	/**
	 * This function will fetch witness name,url,consent date for a
	 * barcode/lable.
	 * @param dForm : dForm
	 * @param specimen : specimen
	 * @param request : request
	 * @param consentMap 
	 * @param barcodeLable : barcodeLable
	 * @throws ApplicationException : ApplicationException
	 */
	private void showConsents(DistributionForm dForm, Specimen specimen,
			HttpServletRequest request, Map<String, ConsentDisplayBean> consentMap) throws ApplicationException
	{

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String initialURLValue = "";
		String initialSignedConsentDateValue = "";
		String initialWitnessValue = "";

		final CollectionProtocolRegistration collectionProtocolRegistration = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration();





		// Prepare Map and iterate both Collections
		StringBuilder key = new StringBuilder(100);
		final Map tempMap = this.prepareConsentMap(specimen,key);
		
		// Setting map and counter in the form
		dForm.setConsentResponseForDistributionValues(tempMap);
		dForm.setConsentTierCounter(this.consentTierCounter);
		final String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS);
		if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			// For no consents and Consent waived
			if (this.consentTierCounter > 0)
//					&& !(specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED)))// disabled
			{
				final String[] barcodeLabelAttribute = new String[5];
//				barcodeLabelAttribute[0] = witnessName;
//				barcodeLabelAttribute[1] = consentDate;
//				barcodeLabelAttribute[2] = signedConsentURL;
				barcodeLabelAttribute[3] = Integer.toString(this.consentTierCounter);
				barcodeLabelAttribute[4] = specimen.getLabel();
				ConsentDisplayBean bean = new ConsentDisplayBean();
//				bean.setWitnessName(witnessName);
//				bean.setConsentDate(consentDate);
//				bean.setSignedConsentURL(signedConsentURL);
				bean.setConsentTierCounter(Integer.toString(this.consentTierCounter));
				bean.setConsentMap(tempMap);
				bean.setSpecimenLabel(specimen.getLabel());
				consentMap.put(key.toString(), bean);
			}

		}

	}

	/**
	 * @param dForm
	 *            object of DistributionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @throws DAOException
	 *             DAO exception
	 */

	// Consent Tracking
	/**
	 * Prepare Map for Consent tiers.
	 *
	 * @param participantResponseList
	 *            This list will be iterated and added to map to populate
	 *            participant Response status.
	 * @param specimen
	 *            This List will be iterated and added to map to populate
	 *            Specimen Level response.
	 * @param builder 
	 * @return tempMap
	 */
	private Map prepareConsentMap(
			Specimen specimen, StringBuilder builder)
	{
		
		final Map tempMap = new HashMap();
		Long consentTierID;
		Long consentID;
		
		builder.append(specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId());
//		if (participantResponseList != null)
//		{
			int i = 0;
//			final Iterator consentResponseCollectionIter = participantResponseList.iterator();
//			while (consentResponseCollectionIter.hasNext())
//			{
//				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
//						.next();
//				consentTierID = consentTierResponse.getConsentTier().getId();
				List<ConsentTierStatus> consentList = new ArrayList<ConsentTierStatus>();
				//consentList.addAll(specimen.getConsentTierStatusCollection());
				Collections.sort(consentList, new IdComparator());
				final Iterator specimenCollectionIter = consentList.iterator();
				while (specimenCollectionIter.hasNext())
				{
					final ConsentTierStatus specimenConsentResponse = (ConsentTierStatus) specimenCollectionIter
							.next();
					consentID = specimenConsentResponse.getConsentTier().getId();
					
//					if (consentTierID.longValue() == consentID.longValue())
//					{
						builder.append(consentID);
						builder.append(specimenConsentResponse.getStatus());
						final ConsentTier consent = specimenConsentResponse.getConsentTier();
						final String idKey = "ConsentBean:" + i + "_consentTierID";
						final String statementKey = "ConsentBean:" + i + "_statement";
						final String responseKey = "ConsentBean:" + i + "_participantResponse";
						final String participantResponceIdKey = "ConsentBean:" + i
								+ "_participantResponseID";
						final String specimenResponsekey = "ConsentBean:" + i
								+ "_specimenLevelResponse";
						final String specimenResponseIDkey = "ConsentBean:" + i
								+ "_specimenLevelResponseID";
						// Adding Keys and its data into the Map
						tempMap.put(idKey, consent.getId());
						tempMap.put(statementKey, consent.getStatement());
//						tempMap.put(responseKey, consentTierResponse.getResponse());
//						tempMap.put(participantResponceIdKey, consentTierResponse.getId());
						tempMap.put(specimenResponsekey, specimenConsentResponse.getStatus());
						tempMap.put(specimenResponseIDkey, specimenConsentResponse.getId());
						i++;
//						break;
						System.out.println();
//					}
				}
//			}
			this.consentTierCounter = i;
			return tempMap;
//		}
//		else
//		{
//			return null;
//		}

	}


}

