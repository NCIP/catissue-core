/**
 * <p>Title: DomainObjectFactory Class>
 * <p>Description:	DomainObjectFactory is a factory for instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 * */

package edu.wustl.catissuecore.dataModel;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ApplicationUser;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * DomainObjectFactory is a factory for instances of various domain objects.
 * @author gautam_shetty
 */
public class DomainObjectFactory
{
    /**
     * Returns an AbstractDomain object copy of the form bean object. 
     * @param FORM_TYPE Form bean Id.
     * @param form Form bean object.
     * @return an AbstractDomain object copy of the form bean object.
     */
    public static AbstractDomainObject getDomainObject(int FORM_TYPE,AbstractActionForm form)
    {
        AbstractDomainObject abstractDomain = null;
        switch(FORM_TYPE)
        {
            case Constants.USER_FORM_ID:
                UserForm userForm = (UserForm) form;
                abstractDomain = new ApplicationUser(userForm);
                break;
//            case Constants.PARTICIPANT_FORM_ID:
//                ParticipantForm participantForm = (ParticipantForm) form;
//            	abstractDomain = new Participant(participantForm);
//            	break;
//            case Constants.ACCESSION_FORM_ID:
//                AccessionForm accessionForm = (AccessionForm) form;
//            	abstractDomain = new Accession(accessionForm);
//            	break;
//            case Constants.INSTITUTE_FORM_ID:
//            	InstituteForm instituteForm = (InstituteForm) form;
//            	abstractDomain = new Institute(instituteForm);
//            	break;
            case Constants.STORAGE_TYPE_FORM_ID:
            	StorageTypeForm storageTypeForm = (StorageTypeForm) form;
            	abstractDomain = new StorageType(storageTypeForm);
            	break;
            case Constants.SITE_FORM_ID:
            	SiteForm siteForm = (SiteForm) form;
            	abstractDomain = new Site(siteForm);
            	break;
        }
        return abstractDomain;
    }
}