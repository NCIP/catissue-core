package edu.wustl.catissuecore.bizlogic.uidomain;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.util.MapDataParser;

@InputUIRepOfDomain(SpecimenCollectionGroupForm.class)
public class SpecimenCollectionGroupTransformer
        implements
            UIDomainTransformer<SpecimenCollectionGroupForm, SpecimenCollectionGroup> {

    public SpecimenCollectionGroup createDomainObject(SpecimenCollectionGroupForm uiRepOfDomain) {
    	InstanceFactory<SpecimenCollectionGroup> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCollectionGroup.class);
        SpecimenCollectionGroup scg =instFact.createObject();// new SpecimenCollectionGroup();
        overwriteDomainObject(scg, uiRepOfDomain);
        return scg;
    }

    public void overwriteDomainObject(SpecimenCollectionGroup domainObject, SpecimenCollectionGroupForm uiRepOfDomain) {
        // from AbstractSpecimenCollectionGroup.setAllValues()...
        domainObject.setClinicalDiagnosis(uiRepOfDomain.getClinicalDiagnosis());
        domainObject.setClinicalStatus(uiRepOfDomain.getClinicalStatus());
        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
        if(uiRepOfDomain.getCollectionDate()!=null)
        {
        	domainObject.setEncounterTimestamp(EventsUtil.setTimeStamp(uiRepOfDomain.getCollectionDate(), uiRepOfDomain.getTimeInHour(), uiRepOfDomain.getTimeInMinute()));
        }
		if(uiRepOfDomain.getAgeAtCollection()!=null && !uiRepOfDomain.getAgeAtCollection().equals(""))
		{
			domainObject.setAgeAtCollection(Integer.valueOf(uiRepOfDomain.getAgeAtCollection()));
		}
        InstanceFactory<Site> siteInstFact = DomainInstanceFactory.getInstanceFactory(Site.class);
        domainObject.setSpecimenCollectionSite(siteInstFact.createObject());

        domainObject.getSpecimenCollectionSite().setId(Long.valueOf(uiRepOfDomain.getSiteId()));

        // from SpecimenCollectionGroup.setAllValues()

        // try {
        domainObject.setName(uiRepOfDomain.getName());
        String barCode = uiRepOfDomain.getBarcode();
        if("" != barCode) {
        	domainObject.setBarcode(barCode);
        }
//        if (Constants.TRUE.equals(uiRepOfDomain.getRestrictSCGCheckbox())) {
//            domainObject.setIsCPBasedSpecimenEntryChecked(Boolean.TRUE);
//        } else {
//            domainObject.setIsCPBasedSpecimenEntryChecked(Boolean.FALSE);
//        }

        // Bug no. 7390
        // adding the collection status in the add specimen collection group
        // page
        // removed the addOperation() if loop
        if (uiRepOfDomain.getCollectionStatus() == null) {
            domainObject.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
        } else {
            domainObject.setCollectionStatus(uiRepOfDomain.getCollectionStatus());
        }

        /**
         * Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_1 See also: 1_1 to 1_5
         * Description : A comment field is set from uiRepOfDomain bean to
         * domain object.
         */
        domainObject.setComment(uiRepOfDomain.getComment());

        InstanceFactory<CollectionProtocolEvent> cpeInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolEvent.class);

        domainObject.setCollectionProtocolEvent(cpeInstFact.createObject());//new CollectionProtocolEvent()
        domainObject.getCollectionProtocolEvent().setId(Long.valueOf(uiRepOfDomain.getCollectionProtocolEventId()));

        // logger.debug("uiRepOfDomain.getParticipantsMedicalIdentifierId() "
        // + uiRepOfDomain.getParticipantsMedicalIdentifierId());

        domainObject.setSurgicalPathologyNumber(uiRepOfDomain.getSurgicalPathologyNumber());

        InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolRegistration.class);
        domainObject.setCollectionProtocolRegistration(cprInstFact.createObject());
        domainObject.getCollectionProtocolRegistration().setId(uiRepOfDomain.getCollectionProtocolRegistrationId());
        /**
         * Name: Vijay Pande Reviewer Name: Aarti Sharma Variable checkedButton
         * name is changed to radioButton hence its getter method name is
         * changed
         */
        if (uiRepOfDomain.getRadioButtonForParticipant() == 1) {
            // value of radio button is 2 when participant name is selected
        	InstanceFactory<Participant> instFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
            final Participant participant = instFact.createObject();//new Participant();
            /** For Migration Start* */
            // uiRepOfDomain.setParticipantId(AppUtility.getParticipantId(uiRepOfDomain.getParticipantName()));
            /** For Migration End* */
            participant.setId(Long.valueOf(uiRepOfDomain.getParticipantId()));
            domainObject.getCollectionProtocolRegistration().setParticipant(participant);
            domainObject.getCollectionProtocolRegistration().setProtocolParticipantIdentifier(null);

            InstanceFactory<ParticipantMedicalIdentifier> pmiInstFact = DomainInstanceFactory.getInstanceFactory(ParticipantMedicalIdentifier.class);
            final ParticipantMedicalIdentifier participantMedicalIdentifier = pmiInstFact.createObject(); //new ParticipantMedicalIdentifier();
            participantMedicalIdentifier.setId(Long.valueOf(uiRepOfDomain.getParticipantsMedicalIdentifierId()));
        } else {
            domainObject.getCollectionProtocolRegistration().setProtocolParticipantIdentifier(
                    uiRepOfDomain.getProtocolParticipantIdentifier());
            domainObject.getCollectionProtocolRegistration().setParticipant(null);
        }

        InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
        final CollectionProtocol collectionProtocol = cpInstFact.createObject();
        collectionProtocol.setId(Long.valueOf(uiRepOfDomain.getCollectionProtocolId()));
        domainObject.getCollectionProtocolRegistration().setCollectionProtocol(collectionProtocol);

        /**
         * Setting the consentTier responses for SCG Level. Virender Mehta
         */
        domainObject.setConsentTierStatusCollection(prepareParticipantResponseCollection(uiRepOfDomain));     
        domainObject.setOffset(Integer.valueOf(uiRepOfDomain.getOffset()));
    }

   

    /**
     * For Consent Tracking. Setting the Domain Object.
     *
     * @param form CollectionProtocolRegistrationForm.
     * @return consentResponseColl.
     */
    private Collection<ConsentTierStatus> prepareParticipantResponseCollection(SpecimenCollectionGroupForm form) {
        final MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection beanObjColl = null;
        try {
            beanObjColl = mapdataParser.generateData(form.getConsentResponseForScgValues());
        } catch (final Exception e) {
            // SpecimenCollectionGroup.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        final Iterator iter = beanObjColl.iterator();
        final Collection<ConsentTierStatus> consentResponseColl = new HashSet<ConsentTierStatus>();
        while (iter.hasNext()) {
            final ConsentBean consentBean = (ConsentBean) iter.next();
            final ConsentTierStatus consentTierstatus = (ConsentTierStatus)DomainInstanceFactory.getInstanceFactory(ConsentTierStatus.class).createObject();//new ConsentTierStatus();
            // Setting response
            consentTierstatus.setStatus(consentBean.getSpecimenCollectionGroupLevelResponse());
            if (consentBean.getSpecimenCollectionGroupLevelResponseID() != null
                    && consentBean.getSpecimenCollectionGroupLevelResponseID().trim().length() > 0) {
                consentTierstatus.setId(Long.parseLong(consentBean.getSpecimenCollectionGroupLevelResponseID()));
            }
            // Setting consent tier
            final ConsentTier consentTier = (ConsentTier)DomainInstanceFactory.getInstanceFactory(ConsentTier.class).createObject();//new ConsentTier();
            consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
            consentTierstatus.setConsentTier(consentTier);
            consentResponseColl.add(consentTierstatus);
        }
        return consentResponseColl;
    }
}
