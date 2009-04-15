
					 /**
 * <p>Title: ConsentVerificationAction Class>
 * <p>Description:        This class used to verify the consents at order view page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana thakur
 * @version 1.00
 * Created on oct 5, 2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
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
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;


public class ConsentVerificationAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(ConsentVerificationAction.class);
    //This counter will keep track of the no of consentTiers 
	int consentTierCounter;
	List listOfMap=null;
	List listOfStringArray=null;	
	String labelIndexCount="";

	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	protected ActionForward executeAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception 
    {
	      DistributionForm dForm = (DistributionForm) form;
    	     
       
        //Show Consents for Specimen
        String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS); //"specimenConsents"
		
        String specimenIdentifier = (String) request.getParameter(Constants.SPECIMEN_ID);
		Long specimenId=null;
		

		
		if(specimenIdentifier !=null)
		{
			specimenId= Long.parseLong(specimenIdentifier);
			Specimen specimen=getListOfSpecimen(specimenId);    
		    showConsents(dForm,specimen,request,(String)specimen.getLabel()); 
		        
		    request.setAttribute("barcodeStatus",Constants.VALID);//valid
		    return mapping.findForward(Constants.POPUP);
        	
        }
        else
        {	
			        if(specimenConsents!=null && specimenConsents.equalsIgnoreCase(Constants.YES))
					{
						String speciemnIdValue = request.getParameter("speciemnIdValue");//barcodelabel
						labelIndexCount = request.getParameter("labelIndexCount");
						StringTokenizer stringToken = new StringTokenizer(speciemnIdValue,"|");
						StringTokenizer stringTokenForIndex = new StringTokenizer(labelIndexCount,"|");
						listOfMap=new ArrayList();
						listOfStringArray=new ArrayList();
						while (stringToken.hasMoreTokens()) 
						{
							specimenId=Long.parseLong(stringToken.nextToken());
							
							Specimen specimen=getListOfSpecimen(specimenId); 
							showConsents(dForm,specimen,request,(String)specimen.getLabel());
						}
						request.setAttribute("listOfStringArray",listOfStringArray);
						request.setAttribute("listOfMap",listOfMap);
						request.setAttribute("labelIndexCount",labelIndexCount);
						
						return mapping.findForward(Constants.VIEWAll);//ViewAll
					}
			       
					
        }	
        //Consent Tracking 
		logger.debug("executeSecureAction");
        String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

        return mapping.findForward((String) request.getParameter(Constants.PAGE_OF));
    }	
	/**
	 * This function will fetch witness name,url,consent date for a barcode/lable
	 * @param dForm Instance of Distribution form
	 * @param specimen Specimen object
	 * @param request With request parameter we will fetch specimenconsent Variable present in request. 
	 * @param barcodeLable This parameter have barcode or lable value
	 * @throws DAOException 
	 * @throws ClassNotFoundException 
	 */
	private void showConsents(DistributionForm dForm ,Specimen specimen, HttpServletRequest request, String barcodeLable) throws ApplicationException
	{
		
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String initialURLValue="";
		String initialWitnessValue="";
		String initialSignedConsentDateValue="";
		
	    Long specimenId= (Long) specimen.getId();
        String colProtHql = "select scg.collectionProtocolRegistration"+ 
		" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg," +
		" edu.wustl.catissuecore.domain.Specimen as spec " +
		" where spec.specimenCollectionGroup.id=scg.id and spec.id="+specimenId;
        
        List collectionProtocolRegistrationList= AppUtility.executeQuery(colProtHql);
        CollectionProtocolRegistration collectionProtocolRegistration=null;
        if(collectionProtocolRegistrationList!=null)
        {
        	 collectionProtocolRegistration = (CollectionProtocolRegistration) collectionProtocolRegistrationList.get(0);
        }
        
        
        if(collectionProtocolRegistration.getSignedConsentDocumentURL()==null)
		{
			initialURLValue=Constants.NULL;
		}
        User consentWitness = null;
		if(collectionProtocolRegistration.getId()!= null)
		{
			consentWitness = (User)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),collectionProtocolRegistration.getId(), "consentWitness");
		}
		//Resolved Lazy ---- User consentWitness= collectionProtocolRegistration.getConsentWitness();
		if(consentWitness==null)
		{
			initialWitnessValue=Constants.NULL;
		}
		if(collectionProtocolRegistration.getConsentSignatureDate()==null)
		{
			initialSignedConsentDateValue=Constants.NULL;
		}
		List cprObjectList=new ArrayList();
		cprObjectList.add(collectionProtocolRegistration);
		SessionDataBean sessionDataBean=(SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		String userName = sessionDataBean.getUserName().toString();	
		List collProtObject=null;
		try
		{
			collProtObject = caCoreAppServicesDelegator.delegateSearchFilter(userName,cprObjectList);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		CollectionProtocolRegistration cprObject = collectionProtocolRegistration;//(CollectionProtocolRegistration)collProtObject.get(0);
        //Getting WitnessName,Consent Date,Signed Url using collectionProtocolRegistration object
		String witnessName="";
		String getConsentDate="";
		String getSignedConsentURL="";
		User witness=cprObject.getConsentWitness();
		if(witness==null)
		{
			if(initialWitnessValue.equals(Constants.NULL))
			{
				witnessName="";
			}
			else
			{
				witnessName=Constants.HASHED_OUT;
			}
			dForm.setWitnessName(witnessName);
		}
		else
		{
			witness = (User)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(),cprObject.getId(), "consentWitness");
			String witnessFullName = witness.getLastName()+", "+witness.getFirstName();
			dForm.setWitnessName(witnessFullName);
		}
		if(cprObject.getConsentSignatureDate()==null)
		{
			if(initialSignedConsentDateValue.equals(Constants.NULL))
			{
				getConsentDate="";
			}
			else
			{
				getConsentDate=Constants.HASHED_OUT;
			}
		}
		else
		{
			getConsentDate=Utility.parseDateToString(cprObject.getConsentSignatureDate(),
					CommonServiceLocator.getInstance().getDatePattern());
		}
		
		if(cprObject.getSignedConsentDocumentURL()==null)
		{
			if(initialURLValue.equals(Constants.NULL))
			{
				getSignedConsentURL="";
			}
			else
			{
				getSignedConsentURL=Constants.HASHED_OUT;
			}
		}
		else
		{
			getSignedConsentURL=Utility.toString(cprObject.getSignedConsentDocumentURL());
		}
		//Setting WitnessName,ConsentDate and Signed Consent Url				
		dForm.setConsentDate(getConsentDate);
		dForm.setSignedConsentUrl(getSignedConsentURL);
		
		//Getting ConsentResponse collection for CPR level
		//Resolved lazy ---  collectionProtocolRegistration.getConsentTierResponseCollection();
		Collection participantResponseCollection = (Collection)bizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), collectionProtocolRegistration.getId(),"elements(consentTierResponseCollection)");
		//Getting ConsentResponse collection for Specimen level
		//Resolved lazy --- specimen.getConsentTierStatusCollection();
		Collection specimenLevelResponseCollection=(Collection)bizLogic.retrieveAttribute(Specimen.class.getName(), specimen.getId(),"elements(consentTierStatusCollection)");
		//Prepare Map and iterate both Collections  
		Map tempMap=prepareConsentMap(participantResponseCollection, specimenLevelResponseCollection);
		//Setting map and counter in the form 
		dForm.setConsentResponseForDistributionValues(tempMap);
		dForm.setConsentTierCounter(consentTierCounter);
		String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS);
		if(specimenConsents!=null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			//For no consents and Consent waived
			if(consentTierCounter>0&&!(specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED)))//disabled
			{
				String[] barcodeLabelAttribute=new String[5];
				barcodeLabelAttribute[0]=witnessName;
				barcodeLabelAttribute[1]=getConsentDate;
				barcodeLabelAttribute[2]=getSignedConsentURL;
				barcodeLabelAttribute[3]=Integer.toString(consentTierCounter);
				barcodeLabelAttribute[4]=barcodeLable;
				listOfMap.add(tempMap);
				listOfStringArray.add(barcodeLabelAttribute);
			}
			
		}
		
	}
	
	/**
	 * @param dForm object of DistributionForm
	 * @param request object of HttpServletRequest
	 * @throws DAOException DAO exception
	 */
	
	
//	Consent Tracking 
	/**
	 * Prepare Map for Consent tiers
	 * @param participantResponseList   This list will be iterated and added to map to populate participant Response status.
	 * @param specimenLevelResponseList This List will be iterated and added to map to populate Specimen Level response.
	 * @return tempMap
	 */
    private Map prepareConsentMap(Collection participantResponseList, Collection specimenLevelResponseList)
	{
		Map tempMap = new HashMap();
		Long consentTierID;
		Long consentID;
		if(participantResponseList!=null)
		{
			int i = 0;
			Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while(consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse)consentResponseCollectionIter.next();
				consentTierID=consentTierResponse.getConsentTier().getId();
				Iterator specimenCollectionIter = specimenLevelResponseList.iterator();	
				while(specimenCollectionIter.hasNext())
				{
					ConsentTierStatus specimenConsentResponse=(ConsentTierStatus)specimenCollectionIter.next();
					consentID=specimenConsentResponse.getConsentTier().getId();
					if(consentTierID.longValue()==consentID.longValue())						
					{
						ConsentTier consent = consentTierResponse.getConsentTier();
						String idKey="ConsentBean:"+i+"_consentTierID";
						String statementKey="ConsentBean:"+i+"_statement";
						String responseKey="ConsentBean:"+i+"_participantResponse";
						String participantResponceIdKey="ConsentBean:"+i+"_participantResponseID";
						String specimenResponsekey  = "ConsentBean:"+i+"_specimenLevelResponse";
						String specimenResponseIDkey ="ConsentBean:"+i+"_specimenLevelResponseID";
						//Adding Keys and its data into the Map
						tempMap.put(idKey, consent.getId());
						tempMap.put(statementKey,consent.getStatement());
						tempMap.put(responseKey,consentTierResponse.getResponse());
						tempMap.put(participantResponceIdKey, consentTierResponse.getId());
						tempMap.put(specimenResponsekey, specimenConsentResponse.getStatus());
						tempMap.put(specimenResponseIDkey, specimenConsentResponse.getId());
						i++;
						break;
					}
				}
			}
			consentTierCounter=i;
			return tempMap;
		}
		else
		{
			return null;
		}
		
	}
    
    
    
    /**
	 * This method sets all the common parameters for the SpecimenEventParameter pages
	 * @param request HttpServletRequest instance in which the data will be set. 
	 * @throws Exception Throws Exception. Helps in handling exceptions at one common point.
	 */

	 private Specimen getListOfSpecimen(Long specimenId) throws BizLogicException
	{

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
				.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		Object object = newSpecimenBizLogic.retrieve(Specimen.class.getName(), specimenId);
		Specimen specimen = (Specimen) object;
		return specimen;
	}

}