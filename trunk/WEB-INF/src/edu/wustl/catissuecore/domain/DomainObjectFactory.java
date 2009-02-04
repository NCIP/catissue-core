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

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.query.ShoppingCart;
import edu.wustl.common.util.logger.Logger;


/**
 * DomainObjectFactory is a factory for instances of various domain objects.
 * @author gautam_shetty
 */
public class DomainObjectFactory extends AbstractDomainObjectFactory
{
	
    /**
     * Returns the fully qualified name of the class according to the form bean type.
     * @param FORM_TYPE Form bean Id.
     * @return the fully qualified name of the class according to the form bean type.
     */
    public String getDomainObjectName(int FORM_TYPE)
    {
        String className = null;
        switch(FORM_TYPE)
        {
            case Constants.PARTICIPANT_FORM_ID:
                className = Participant.class.getName();
            	break;
            case Constants.INSTITUTION_FORM_ID:
                className = Institution.class.getName();
            	break;
            case Constants.REPORTED_PROBLEM_FORM_ID:
                className = ReportedProblem.class.getName();
            	break;
            case Constants.USER_FORM_ID:
                className = User.class.getName();
            	break;
//            case Constants.SIGNUP_FORM_ID:
//                className = SignUpUser.class.getName();
//            	break;	
            case Constants.DEPARTMENT_FORM_ID:
                className = Department.class.getName();
            	break;
            case Constants.APPROVE_USER_FORM_ID:
                className = User.class.getName();
        		break;

    /* 
            Constants.APPROVE_USER_FORM_ID:
            Constants.ACTIVITY_STATUS_FORM_ID:
            Constants.FORGOT_PASSWORD_FORM_ID = 35;
           	
      */      
            case Constants.COLLECTION_PROTOCOL_FORM_ID:
                className = CollectionProtocol.class.getName();
            	break;
            case Constants.DISTRIBUTIONPROTOCOL_FORM_ID:
    	   		className = DistributionProtocol.class.getName();
      			break;
            case Constants.STORAGE_CONTAINER_FORM_ID:
				className = StorageContainer.class.getName();
   				break;
            case Constants.STORAGE_TYPE_FORM_ID:
		   		className = StorageType.class.getName();
	   			break;
            case Constants.SITE_FORM_ID:
		   		className = Site.class.getName();
	   			break;
            case Constants.CANCER_RESEARCH_GROUP_FORM_ID:
		   		className = CancerResearchGroup.class.getName();
	   			break;
            case Constants.BIOHAZARD_FORM_ID:
		   		className = Biohazard.class.getName();
	   			break;
            case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID:
		   		className = FrozenEventParameters.class.getName();
	   			break;
            case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID:
		   		className = CheckInCheckOutEventParameter.class.getName();
	   			break;
            case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID:
		   		className = ReceivedEventParameters.class.getName();
	   			break;
            case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID:
		   		className = CollectionProtocolRegistration.class.getName();
	   			break;
            case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID:
		   		className = SpecimenCollectionGroup.class.getName();
	   			break;
            case Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
		   		className = FluidSpecimenReviewEventParameters.class.getName();
	   			break;
            case Constants.NEW_SPECIMEN_FORM_ID:
            case Constants.CREATE_SPECIMEN_FORM_ID:
		   		className = Specimen.class.getName();
	   			break;
            case Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
		   		className = CellSpecimenReviewParameters.class.getName();
	   			break;
            case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
		   		className = TissueSpecimenReviewEventParameters.class.getName();
	   			break;
            case Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID:
		   		className = DisposalEventParameters.class.getName();
	   			break;
            case Constants.THAW_EVENT_PARAMETERS_FORM_ID:
		   		className = ThawEventParameters.class.getName();
	   			break;
            case Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
		   		className = MolecularSpecimenReviewParameters.class.getName();
	   			break;
            case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID:
		   		className = CollectionEventParameters.class.getName();
	   			break;
            case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID:
		   		className = TransferEventParameters.class.getName();
	   			break;
            case Constants.SPUN_EVENT_PARAMETERS_FORM_ID:
		   		className = SpunEventParameters.class.getName();
	   			break;
            case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID:
		   		className = EmbeddedEventParameters.class.getName();
	   			break;
            case Constants.FIXED_EVENT_PARAMETERS_FORM_ID:
		   		className = FixedEventParameters.class.getName();
	   			break;
            case Constants.PROCEDURE_EVENT_PARAMETERS_FORM_ID:
		   		className = ProcedureEventParameters.class.getName();
	   			break;
            case Constants.DISTRIBUTION_FORM_ID:
		   		className = Distribution.class.getName();
	   			break;
            case Constants.SPECIMEN_EVENT_PARAMETERS_FORM_ID:
		   		className = SpecimenEventParameters.class.getName();
	   			break;
            case Constants.SHOPPING_CART_FORM_ID:
		   		className = ShoppingCart.class.getName();
	   			break;
            case Constants.SIMILAR_CONTAINERS_FORM_ID:
            	className = StorageContainer.class.getName();
            	break;
            case Constants.ALIQUOT_FORM_ID:
                className = Specimen.class.getName();//AliquotSpecimen.class.getName();
            	break;
            case Constants.SPECIMEN_ARRAY_TYPE_FORM_ID:
            	className = SpecimenArrayType.class.getName();
            	break;
            case Constants.SPECIMEN_ARRAY_FORM_ID:
            	className = SpecimenArray.class.getName();
            	break;            	
            
            case Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID:
            	className = SpecimenArray.class.getName();
        	 break;
        	//Ordering System
            case Constants.REQUEST_LIST_FILTERATION_FORM_ID:
            	className = OrderDetails.class.getName();
        	 break;
        	 
            case Constants.REQUEST_DETAILS_FORM_ID:
            	className = OrderDetails.class.getName();
        	 break;
        	
            case Constants.ORDER_FORM_ID:
            	className = OrderDetails.class.getName();
		 		break;
		 		
			case Constants.ORDER_ARRAY_FORM_ID:
            	className = OrderDetails.class.getName();
				break;
			case Constants.ORDER_PATHOLOGY_FORM_ID:
            	className = OrderDetails.class.getName();
				break;
			case Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID:
				className = PathologyReportReviewParameter.class.getName();
				break;
			case Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID:
				className = QuarantineEventParameter.class.getName();
				break;

				
        }
        return className;
    }
	
    /**
     * Returns an AbstractDomain object copy of the form bean object. 
     * @param FORM_TYPE Form bean Id.
     * @param form Form bean object.
     * @return an AbstractDomain object copy of the form bean object.
     */
    public AbstractDomainObject getDomainObject(int FORM_TYPE,AbstractActionForm form) throws AssignDataException
    {
        AbstractDomainObject abstractDomain = null;
        switch(FORM_TYPE)
        {
            case Constants.USER_FORM_ID:
            case Constants.APPROVE_USER_FORM_ID:
                UserForm userForm = (UserForm)form;
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
            case Constants.REPORTED_PROBLEM_FORM_ID:
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
            
            case Constants.ALIQUOT_FORM_ID:
            	abstractDomain = new Specimen(form);//new AliquotSpecimen(form);
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

			 case Constants.DISTRIBUTION_FORM_ID :
            	abstractDomain = new Distribution(form);
            	break;
            	
			 case Constants.SIMILAR_CONTAINERS_FORM_ID :
				 abstractDomain = new StorageContainer(form);
				 break;
				 
			 case Constants.SPECIMEN_ARRAY_TYPE_FORM_ID :
				 abstractDomain = new SpecimenArrayType(form);
				 break;
				 
			 case Constants.SPECIMEN_ARRAY_FORM_ID :
				 abstractDomain = new SpecimenArray(form);
				 break;			

			case Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID:
		     	abstractDomain = new SpecimenArray(form);
		 		break;		 	
				 
			case Constants.ORDER_FORM_ID:
				abstractDomain = new OrderDetails(form);
		 		break;
		 		
			case Constants.ORDER_ARRAY_FORM_ID:
				abstractDomain = new OrderDetails(form);
				break;
			case Constants.ORDER_PATHOLOGY_FORM_ID:
				abstractDomain = new OrderDetails(form);
				break;
			case Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID:
				abstractDomain = new PathologyReportReviewParameter(form);
				break;
			case Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID:
				abstractDomain = new QuarantineEventParameter(form);
				break;
				
				
			//added as per bug 79
             default:
            		abstractDomain = null;
            		Logger.out.error("Incompatible object ID");
            		break;
        }
        return abstractDomain;
    }
}