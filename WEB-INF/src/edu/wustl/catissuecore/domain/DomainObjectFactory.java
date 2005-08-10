/**
 * <p>Title: DomainObjectFactory Class>
 * <p>Description:	DomainObjectFactory is a factory for instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 * */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.actionForm.UserForm;
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
            case Constants.SIGNUP_FORM_ID:
                UserForm signUpForm = (UserForm)form;
            	abstractDomain = new SignUpUser(signUpForm);
            	break;
            case Constants.APPROVE_USER_FORM_ID:
                UserForm approveUserForm = (UserForm)form;
            	abstractDomain = new User(approveUserForm);
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
            	
            case Constants.CREATE_SPECIMEN_FORM_ID:
            	CreateSpecimenForm crForm = (CreateSpecimenForm)form;
            	String sType = crForm.getClassName();
            	
            	if(sType.equals("Tissue"))
            	{
            		abstractDomain = new TissueSpecimen(crForm);
            	}
            	else if(sType.equals("Fluid"))
            	{
            		abstractDomain = new FluidSpecimen(crForm);
            	}
            	else if(sType.equals("Cell"))
            	{
            		abstractDomain = new CellSpecimen(crForm);
            	}
            	else if(sType.equals("Molecular"))
            	{
            		abstractDomain = new MolecularSpecimen(crForm);
            	}
            	break;
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID:
			   abstractDomain = new SpecimenCollectionGroup(form);
			   break;
	           
			case Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
            	abstractDomain = new MolecularSpecimenReviewParameters(form);            	
            	break;
            	
			 case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID :
            	abstractDomain = new CollectionEventParameters(form);            	
            	break;
            	
			 case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new TransferEventParameters(form);            	
            	break;
            	
			 case Constants.THAW_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new ThawEventParameters(form);            	
            	break;
            	
			 case Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new DisposalEventParameters(form);            	
            	break;
            	
			 case Constants.SPUN_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new SpunEventParameters(form);            	
            	break;
            	
			 case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new EmbeddedEventParameters(form);            	
            	break;
            	
			 case Constants.FIXED_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new FixedEventParameters(form);            	
            	break;
            	
			 case Constants.PROCEDURE_EVENT_PARAMETERS_FORM_ID:
            	abstractDomain = new ProcedureEventParameters(form);            	
            	break;
        }
        return abstractDomain;
    }
}