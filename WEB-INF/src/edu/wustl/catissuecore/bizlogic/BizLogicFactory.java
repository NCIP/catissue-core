/**
 * <p>Title: BizLogicFactory Class>
 * <p>Description:	BizLogicFactory is a factory for DAO instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.bizlogic.bulkOperations.BulkOperationsBizlogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentReceivingBizLogic;
import edu.wustl.catissuecore.bizlogic.shippingtracking.ShipmentRequestBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;

/**
 * BizLogicFactory is a factory for DAO instances of various domain objects.
 * @author gautam_shetty
 */
public class BizLogicFactory implements IFactory
{

	private transient Logger logger = Logger.getCommonLogger(BizLogicFactory.class);
    /**
     * Returns DAO instance according to the form bean type.
     * @param FORM_ID The form bean type.
     * @return An DAO object.
     */
	public IBizLogic getBizLogic(int FORM_ID)
    {
		logger.debug("In Biz Logic Factory , Form ID: "+FORM_ID);
		
    	IBizLogic bizLogic = null;
        
        switch(FORM_ID)
        {
            case Constants.FORGOT_PASSWORD_FORM_ID:
            case Constants.USER_FORM_ID:
            	bizLogic = new UserBizLogic();
            	break;
            case Constants.APPROVE_USER_FORM_ID:
            	bizLogic = new ApproveUserBizLogic();
            	break;
            case Constants.REPORTED_PROBLEM_FORM_ID:
            	bizLogic = new ReportedProblemBizLogic();
            	break;
        	case Constants.STORAGE_TYPE_FORM_ID :
        		bizLogic = new StorageTypeBizLogic();
        		break;
        	case Constants.STORAGE_CONTAINER_FORM_ID:
        		bizLogic = new StorageContainerBizLogic();
        		break;
        	
        	case Constants.SITE_FORM_ID:
        		bizLogic = new SiteBizLogic();
        		break;
        	case Constants.PARTICIPANT_FORM_ID:
        		bizLogic = new ParticipantBizLogic();
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
         		bizLogic = new SpecimenEventParametersBizLogic();
        		break;
        		
         	case Constants.BULK_OPERATIONS_FORM_ID:
         		bizLogic = new BulkOperationsBizlogic();
        		break;

        	case Constants.COLLECTION_PROTOCOL_FORM_ID:
        		bizLogic = new CollectionProtocolBizLogic();
        		break;
        	case Constants.DISTRIBUTIONPROTOCOL_FORM_ID:
        		bizLogic = new DistributionProtocolBizLogic();
        		break;
			case Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID:
				bizLogic = new CollectionProtocolRegistrationBizLogic();
				break;	
			
			case Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID:
				bizLogic = new SpecimenCollectionGroupBizLogic();
				break;
				
          	case Constants.NEW_SPECIMEN_FORM_ID:
          		bizLogic = new NewSpecimenBizLogic();
        		break;

          	case Constants.CREATE_SPECIMEN_FORM_ID:
          		bizLogic = new NewSpecimenBizLogic();
        		break;
        		
        	case Constants.SHOPPING_CART_FORM_ID:
        		bizLogic = new ShoppingCartBizLogic();
        		break;

        	case Constants.DISTRIBUTION_FORM_ID:
        		bizLogic = new DistributionBizLogic();
        		break;
        		
        	case Constants.SIMPLE_QUERY_INTERFACE_ID:
        		bizLogic = new QueryBizLogic();
        		break;
        	/*case Constants.ADVANCE_QUERY_INTERFACE_ID:
        		bizLogic = new AdvanceQueryBizlogic();
        		break;*/
        	case edu.wustl.common.util.global.Constants.QUERY_INTERFACE_ID:
        		bizLogic = new QueryBizLogic();
    			break;
    		
    		case Constants.BIOHAZARD_FORM_ID:
    			bizLogic = new BiohazardBizLogic();
    			break;
    		
    		case Constants.ALIQUOT_FORM_ID:
    			bizLogic = new NewSpecimenBizLogic();
    			break;
    			
    		case Constants.SIMILAR_CONTAINERS_FORM_ID :
    			bizLogic = new SimilarContainerBizLogic();
    			break;
    			
    		case Constants.SPECIMEN_ARRAY_TYPE_FORM_ID :
    			bizLogic = new SpecimenArrayTypeBizLogic();
    			break;

    		case Constants.SPECIMEN_ARRAY_FORM_ID :
    			bizLogic = new SpecimenArrayBizLogic();
    			break;
    			
    		case Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID:
    			bizLogic = new SpecimenArrayAliquotsBizLogic();
    			break;
    		case Constants.ASSIGN_PRIVILEGE_FORM_ID:
    			bizLogic = new AssignPrivilegePageBizLogic();
				break;
//   			 Added by Ashish
    		case Constants.INSTITUTION_FORM_ID :
    			bizLogic = new InstitutionBizLogic();
    			break;
    		case Constants.DEPARTMENT_FORM_ID :
    			bizLogic = new DepartmentBizLogic();
    			break;
    		case Constants.CANCER_RESEARCH_GROUP_FORM_ID :
    			bizLogic = new CancerResearchBizLogic();
    			break;
    		case Constants.CDE_FORM_ID :
    			bizLogic = new CDEBizLogic();
    			break;
    			
    		case Constants.REQUEST_DETAILS_FORM_ID:
    			bizLogic = new OrderBizLogic();
    			break;	    			    			
    		case Constants.ORDER_PATHOLOGY_FORM_ID:
    			bizLogic = new OrderBizLogic();
    		break;	
    		case Constants.NEW_PATHOLOGY_FORM_ID:
    			bizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
    		break;	
    		case Constants.ORDER_FORM_ID:
    			bizLogic = new OrderBizLogic();
    			break;
    		case Constants.ORDER_ARRAY_FORM_ID:
    			bizLogic = new OrderBizLogic();
    			break;
//				Ordering System
    		case Constants.REQUEST_LIST_FILTERATION_FORM_ID:
    			bizLogic = new OrderBizLogic();
    			break;
    			//	View Surgical Pathology Report
    		case Constants.DEIDENTIFIED_SURGICAL_PATHOLOGY_REPORT_FORM_ID:
    			bizLogic=new DeidentifiedSurgicalPathologyReportBizLogic();
    			break;	
    		case Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID:
    			bizLogic=new PathologyReportReviewParameterBizLogic();
    			break;	
    		case Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID:
    			bizLogic=new QuarantineEventParameterBizLogic();
    			break;
    		/*case Constants.CATISSUECORE_QUERY_INTERFACE_ID://CatissecoreQueryBizLogic
    			bizLogic = new CatissuecoreQueryBizLogic();
    			break;*/
    		case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_FORM_ID:
            	bizLogic = new ShipmentBizLogic();
            	break;
    		case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_REQUEST_FORM_ID:
            	bizLogic = new ShipmentRequestBizLogic();
            	break;
    		case edu.wustl.catissuecore.util.shippingtracking.Constants.SHIPMENT_RECEIVING_FORM_ID:
            	bizLogic = new ShipmentReceivingBizLogic();
            	break;
    		case Constants.SUMMARY_BIZLOGIC_ID:
            	bizLogic = new SummaryBizLogic();
            	break;

    		//END
    		case Constants.DEFAULT_BIZ_LOGIC:
            default:
            	bizLogic = new DefaultBizLogic();
            	break;
            	
        }
        return bizLogic;
    }
    
    /**
     * Returns DAO instance according to the fully qualified class name.
     * @param className The name of the class.
     * @return An DAO object.
     */
    public IBizLogic getBizLogic(String className)
    {
    	IBizLogic bizLogic = null;
    	
    	if(className.equals("edu.wustl.catissuecore.domain.OrderItem") || className.equals("edu.wustl.catissuecore.domain.PathologicalCaseOrderItem") || className.equals("edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem") || className.equals("edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem"))
    	{
    		bizLogic = new OrderBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.OrderDetails"))
    	{
    		bizLogic = new OrderBizLogic();
    	}
    	
    	else if(className.equals("edu.wustl.catissuecore.domain.User"))
    	{
    		bizLogic = new UserBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.ReportedProblem"))
    	{
    		bizLogic = new ReportedProblemBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.StorageType"))
    	{
    		bizLogic = new StorageTypeBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.StorageContainer"))
    	{
    		bizLogic = new StorageContainerBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.Site"))
    	{
    		bizLogic = new SiteBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.Participant"))
    	{
    		bizLogic = new ParticipantBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.CollectionProtocol"))
    	{
    		bizLogic = new CollectionProtocolBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.DistributionProtocol"))
    	{
    		bizLogic = new DistributionProtocolBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.AliquotSpecimen"))
    	{
    		bizLogic = new NewSpecimenBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.Specimen"))
    	{
    		bizLogic = new NewSpecimenBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.MolecularSpecimen")|className.equals("edu.wustl.catissuecore.domain.CellSpecimen")|className.equals("edu.wustl.catissuecore.domain.TissueSpecimen")|className.equals("edu.wustl.catissuecore.domain.FluidSpecimen"))
    	{
    		bizLogic = new NewSpecimenBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.SpecimenCollectionGroup"))
    	{
    		bizLogic = new SpecimenCollectionGroupBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.Distribution"))
    	{
    		bizLogic = new DistributionBizLogic();
    	}
    	else if(className.endsWith("EventParameters"))
    	{
    		bizLogic = new SpecimenEventParametersBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter"))
    	{
    		bizLogic = new SpecimenEventParametersBizLogic();
    	}
    	else if(className.equals(Constants.SPECIMEN_ARRAY_TYPE_CLASSNAME))
    	{
    		bizLogic = new SpecimenArrayTypeBizLogic();
    	}
    	else if(className.equals(Constants.SPECIMEN_ARRAY_CLASSNAME))
    	{
    		bizLogic = new SpecimenArrayBizLogic();
    	}
//    	 Added by Ashish
    	else if(className.equals("edu.wustl.catissuecore.domain.Department"))
    	{
    		bizLogic = new DepartmentBizLogic();
    	}    	
    	else if(className.equals("edu.wustl.catissuecore.domain.Biohazard"))
    	{
    		bizLogic = new BiohazardBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.CancerResearchGroup"))
    	{
    		bizLogic = new CancerResearchBizLogic();
    	}  
    	
    	else if(className.equals("edu.wustl.catissuecore.domain.Institution"))
    	{
    		bizLogic = new InstitutionBizLogic();
    	} 
    	else if(className.equals("edu.wustl.catissuecore.domain.CollectionProtocolRegistration"))
    	{
    		bizLogic = new CollectionProtocolRegistrationBizLogic();
    	} 
    	//END
    	// For caTIES
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport"))
    	{
    		bizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.TextContent"))
    	{
    		bizLogic = new TextReportContentBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"))
    	{
    		bizLogic = new DeidentifiedSurgicalPathologyReportBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter"))
    	{
    		bizLogic = new PathologyReportReviewParameterBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter"))
    	{
    		bizLogic = new QuarantineEventParameterBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue"))
    	{
    		bizLogic = new ReportLoaderQueueBizLogic();
    	}// caTIES END
    	else if(className.equals("edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier"))
    	{
    		bizLogic = new ParticipantMedicalIdentifierBizLogic();
    	}//kalpana:Bug#5941
    	else if(className.equals("edu.wustl.catissuecore.domain.SpecimenRequirement"))
    	{
    		bizLogic = new RequirementSpecimenBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.shippingtracking.Shipment"))
    	{
    		bizLogic=new ShipmentBizLogic();
    	}
    	else if(className.equals("edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest"))
    	{
    		bizLogic=new ShipmentRequestBizLogic();
    	}
    	else
    	{
    		bizLogic = new DefaultBizLogic();
    	}
    	
    	return bizLogic;
    }
    
}