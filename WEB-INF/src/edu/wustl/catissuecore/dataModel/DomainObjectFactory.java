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
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
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
                abstractDomain = new User(userForm);
                break;
            case Constants.PARTICIPANT_FORM_ID:
            	abstractDomain = new Participant(form);
            	break;
            case Constants.BIOHAZARD_FORM_ID:
            	abstractDomain = new Biohazard(form);
            	break;
            case Constants.STORAGE_TYPE_FORM_ID:
            	abstractDomain = new StorageType(form);
            	break;
            case Constants.STORAGE_CONTAINER_FORM_ID:
            	abstractDomain = new StorageContainer(form);            	
            	break;
            case Constants.SITE_FORM_ID:
            	abstractDomain = new Site(form);
            	break;
            case Constants.REPORTEDPROBLEM_FORM_ID:
                ReportedProblemForm reportedProblemForm = (ReportedProblemForm)form;
            	abstractDomain = new ReportedProblem(reportedProblemForm);
            	break;
            case Constants.DEPARTMENT_FORM_ID:
            	abstractDomain = new Department(form);
            	break;
            case Constants.INSTITUTION_FORM_ID:
            	abstractDomain = new Institution(form);
            	break;
            case Constants.CANCER_RESEARCH_GROUP_FORM_ID:
            	abstractDomain = new CancerResearchGroup(form);
            	break;
            case Constants.COLLECTION_PROTOCOL_FORM_ID:
            	abstractDomain = new CollectionProtocol(form);
            	break;
            case Constants.DISTRIBUTIONPROTOCOL_FORM_ID:
            	abstractDomain = new DistributionProtocol(form);
            	break;
            	
            case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new FrozenEventParameters(form);            	
            	break;

            case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new CheckInCheckOutEventParameter(form);            	
            	break;
            	
            case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new ReceivedEventParameters(form);            	
            	break;
            case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID:
			   abstractDomain = new CollectionProtocolRegistration(form);
			   break;
			  
            case Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new FluidSpecimenReviewEventParameters (form);            	
            	break;
            	
            case Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
            	abstractDomain = new CellSpecimenReviewParameters (form);            	
            	break; 
			
            case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new TissueSpecimenReviewEventParameters (form);            	
            	break;
            case Constants.NEW_SPECIMEN_FORM_ID:
            	NewSpecimenForm newForm = (NewSpecimenForm) form;
            	String type = newForm.getClassName();
            	
            	if(type.equals("Tissue"))
            	{
            		abstractDomain = new TissueSpecimen(newForm);
            	}
            	else if(type.equals("Fluid"))
            	{
            		abstractDomain = new FluidSpecimen(newForm);
            	}
            	else if(type.equals("Cell"))
            	{
            		abstractDomain = new CellSpecimen(newForm);
            	}
            	else if(type.equals("Molecular"))
            	{
            		abstractDomain = new MolecularSpecimen(newForm);
            	}
            	break;
        }
        return abstractDomain;
    }
}