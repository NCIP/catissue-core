/**
 * <p>Title: AbstractDomain Class>
 * <p>Description:  AbstractDomain class is the superclass of all the domain classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.audit.Auditable;
import edu.wustl.catissuecore.exception.AssignDataException;
import edu.wustl.catissuecore.query.ShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * AbstractDomain class is the superclass of all the domain classes.
 * @author gautam_shetty
 */
public abstract class AbstractDomainObject implements Auditable
{
    /**
     * Returns the fully qualified name of the class according to the form bean type.
     * @param FORM_TYPE Form bean Id.
     * @return the fully qualified name of the class according to the form bean type.
     */
    public static String getDomainObjectName(int FORM_TYPE)
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
            case Constants.REPORTEDPROBLEM_FORM_ID:
                className = ReportedProblem.class.getName();
            	break;
            case Constants.USER_FORM_ID:
                className = User.class.getName();
            	break;
            case Constants.SIGNUP_FORM_ID:
                className = SignUpUser.class.getName();
            	break;	
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
     
        }
        return className;
    }
    
    /**
     * Copies all values from the AbstractForm object
     * @param abstractForm The AbstractForm object
     */
    public abstract void setAllValues(AbstractActionForm abstractForm) throws AssignDataException;
    
    /**
	 * Returns the unique systemIdentifier assigned to the domain object.
     * @return returns a unique systemIdentifier assigned to the domain object.
     * @see #setIdentifier(Long)
	 * */
    public abstract Long getSystemIdentifier();

    /**
	 * Sets an systemIdentifier for the domain object.
	 * @param systemIdentifier systemIdentifier for the domain object.
	 * @see #getIdentifier()
	 * */
    public abstract void setSystemIdentifier(Long systemIdentifier);
}
