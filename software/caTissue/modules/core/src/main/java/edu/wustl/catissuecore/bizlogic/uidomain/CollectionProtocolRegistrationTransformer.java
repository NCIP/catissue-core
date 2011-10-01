package edu.wustl.catissuecore.bizlogic.uidomain;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;

@InputUIRepOfDomain(CollectionProtocolRegistrationForm.class)
public class CollectionProtocolRegistrationTransformer
        implements
            UIDomainTransformer<CollectionProtocolRegistrationForm, CollectionProtocolRegistration> {

    public CollectionProtocolRegistration createDomainObject(CollectionProtocolRegistrationForm uiRepOfDomain) {
    	InstanceFactory<CollectionProtocolRegistration> instFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolRegistration.class);
        CollectionProtocolRegistration cpr = instFact.createObject();
        overwriteDomainObject(cpr, uiRepOfDomain);
        return cpr;
    }

    public void overwriteDomainObject(CollectionProtocolRegistration domainObject,
            CollectionProtocolRegistrationForm uiRepOfDomain) {
        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getCollectionProtocol())) {
        	InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
            domainObject.setCollectionProtocol(cpInstFact.createObject());
        }

        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getRegistrationDate())) {
            domainObject.setRegistrationDate(new Date());
        }

        domainObject.getCollectionProtocol().setId(new Long(uiRepOfDomain.getCollectionProtocolID()));

        if (uiRepOfDomain.getParticipantID() != -1 && uiRepOfDomain.getParticipantID() != 0) {
            InstanceFactory<Participant> partiInstFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
        	domainObject.setParticipant(partiInstFact.createObject());
            domainObject.getParticipant().setId(new Long(uiRepOfDomain.getParticipantID()));
        } else {
            domainObject.setParticipant(null);
        }

        domainObject.setProtocolParticipantIdentifier(uiRepOfDomain.getParticipantProtocolID().trim());
        if (domainObject.getProtocolParticipantIdentifier().equals("")) {
            domainObject.setProtocolParticipantIdentifier(null);
        }

        try {
            domainObject.setRegistrationDate(CommonUtilities.parseDate(uiRepOfDomain.getRegistrationDate(),
                    CommonUtilities.datePattern(uiRepOfDomain.getRegistrationDate())));
        } catch (final ParseException e) {
            // CollectionProtocolRegistration.logger.error(e.getMessage(), e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        domainObject.setBarcode(CommonUtilities.toString(uiRepOfDomain.getBarcode()));

        // For Consent Tracking ----Ashish 1/12/06
        // Setting the consent sign date.
        try {
            domainObject.setConsentSignatureDate(CommonUtilities.parseDate(uiRepOfDomain.getConsentDate()));
        } catch (final ParseException e) {
            // CollectionProtocolRegistration.logger.error(e.getMessage(), e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Setting the signed doc url
        domainObject.setSignedConsentDocumentURL(uiRepOfDomain.getSignedConsentUrl());
        if (domainObject.getSignedConsentDocumentURL().equals("")) {
            domainObject.setSignedConsentDocumentURL(null);
        }
        // Setting the consent witness
        if (uiRepOfDomain.getWitnessId() > 0) {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            domainObject.setConsentWitness(instFact.createObject());
            domainObject.getConsentWitness().setId(new Long(uiRepOfDomain.getWitnessId()));
        }
        // Preparing Consent tier response Collection
        domainObject
                .setConsentTierResponseCollection(prepareParticipantResponseCollection(domainObject, uiRepOfDomain));

        // Mandar: 16-jan-07 : - For withdraw options
       // domainObject.setConsentWithdrawalOption(uiRepOfDomain.getWithdrawlButtonStatus());
        // offset changes 27th 2007
        domainObject.setOffset(new Integer(uiRepOfDomain.getOffset()));
    }

    /**
     * For Consent Tracking. Setting the Domain Object
     *
     * @param form CollectionProtocolRegistrationForm
     * @return consentResponseColl
     */
    private Collection prepareParticipantResponseCollection(CollectionProtocolRegistration domainObject,
            CollectionProtocolRegistrationForm form) {
        final MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection beanObjColl = null;
        try {
            beanObjColl = mapdataParser.generateData(form.getConsentResponseValues());
        } catch (final Exception e) {
            // CollectionProtocolRegistration.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        final Iterator iter = beanObjColl.iterator();
        final Collection consentResponseColl = new HashSet();
        ConsentUtil.createConsentResponseColl(consentResponseColl, iter);
        return consentResponseColl;
    }
}
