/**
 * <p>Title: BizLogicFactory Class>
 * <p>Description:	BizLogicFactory is a factory for DAO instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * BizLogicFactory is a factory for DAO instances of various domain objects.
 * @author gautam_shetty
 */
public class BizLogicFactory
{
    /**
     * Returns DAO instance according to the form bean type.
     * @param FORM_TYPE The form bean type.
     * @return An AbstractDAO object.
     */
    public static AbstractBizLogic getBizLogic(int FORM_TYPE)
    {
        AbstractBizLogic abstractBizLogic = null;
        
        switch(FORM_TYPE)
        {
            case Constants.USER_FORM_ID:
                abstractBizLogic = new UserBizLogic();
            	break;
            case Constants.SIGNUP_FORM_ID:
                abstractBizLogic = new SignUpUserBizLogic();
            	break;
            case Constants.APPROVE_USER_FORM_ID:
                abstractBizLogic = new ApproveUserBizLogic();
            	break;
            case Constants.REPORTEDPROBLEM_FORM_ID:
                abstractBizLogic = new ReportedProblemBizLogic();
            	break;
        	case Constants.STORAGE_TYPE_FORM_ID :
        		abstractBizLogic = new StorageTypeBizLogic();
        		break;
        	case Constants.STORAGE_CONTAINER_FORM_ID:
        		abstractBizLogic = new StorageContainerBizLogic();
        		break;
        	case Constants.SITE_FORM_ID:
        		abstractBizLogic = new SiteBizLogic();
        		break;
        	case Constants.PARTICIPANT_FORM_ID:
        		abstractBizLogic = new ParticipantBizLogic();
        		break;
        		
        	// for all event parameters same object will be returned	
          	case Constants.FROZEN_EVENT_PARAMETERS_FORM_ID:
          	case Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID:
          	case Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
         	case Constants.CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
         	case Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID:
          	case Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID:
         	case Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID:
         	case Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID:
         	case Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID:
         	case Constants.THAW_EVENT_PARAMETERS_FORM_ID:
         	case Constants.DISPOSAL_EVENT_PARAMETERS_FORM_ID:
         	case Constants.SPUN_EVENT_PARAMETERS_FORM_ID:
         	case Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID:
         	case Constants.FIXED_EVENT_PARAMETERS_FORM_ID:
         	case Constants.PROCEDURE_EVENT_PARAMETERS_FORM_ID:	
         		abstractBizLogic = new SpecimenEventParametersBizLogic();
        		break;		
        		
        	case Constants.COLLECTION_PROTOCOL_FORM_ID:
        		abstractBizLogic = new CollectionProtocolBizLogic();
        		break;
        	case Constants.DISTRIBUTIONPROTOCOL_FORM_ID:
        		abstractBizLogic = new DistributionProtocolBizLogic();
        		break;
			case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID:
				abstractBizLogic = new CollectionProtocolRegistrationBizLogic();
				break;	
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID:
				abstractBizLogic = new SpecimenCollectionGroupBizLogic();
				break;
          	case Constants.NEW_SPECIMEN_FORM_ID:
        		abstractBizLogic = new NewSpecimenBizLogic();
        		break;
            default:
                abstractBizLogic = new DefaultBizLogic();
            	break;
            	
        }
        return abstractBizLogic;
    }
}