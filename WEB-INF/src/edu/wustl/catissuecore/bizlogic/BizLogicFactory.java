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
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.util.logger.Logger;

/**
 * BizLogicFactory is a factory for DAO instances of various domain objects.
 * @author gautam_shetty
 */
public class BizLogicFactory //extends AbstractBizLogicFactory
{
	//Singleton instace
	private static BizLogicFactory factory = null;
	
	
	static
	{
		factory = new BizLogicFactory();
	}
	
	protected BizLogicFactory()
	{
	}
	
	/**
	 * Setter method in singleton class is to setup mock unit testing.
	 * */
	public static void setBizLogicFactory(BizLogicFactory externalFactory)
	{
		factory = externalFactory;
	}
	
	public static BizLogicFactory getInstance()
	{
		return factory;
	}
	
    /**
     * Returns DAO instance according to the form bean type.
     * @param FORM_ID The form bean type.
     * @return An AbstractDAO object.
     */
	public IBizLogic getBizLogic(int FORM_ID)
    {
		Logger.out.debug("In Biz Logic Factory , Form ID: "+FORM_ID);
		
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
          		bizLogic = new CreateSpecimenBizLogic();
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
        	case Constants.ADVANCE_QUERY_INTERFACE_ID:
        		bizLogic = new AdvanceQueryBizlogic();
        		break;
        	case Constants.QUERY_INTERFACE_ID:
        		bizLogic = new QueryBizLogic();
    			break;
    		
    		case Constants.BIOHAZARD_FORM_ID:
    			bizLogic = new BiohazardBizLogic();
    			break;
    		
    		case Constants.ALIQUOT_FORM_ID:
    			bizLogic = new AliquotBizLogic();
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
     * @return An AbstractDAO object.
     */
    public IBizLogic getBizLogic(String className)
    {
    	IBizLogic bizLogic = null;
    	
    	if(className.equals("edu.wustl.catissuecore.domain.User"))
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
    		bizLogic = new AliquotBizLogic();
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
    	else
    	{
    		bizLogic = new DefaultBizLogic();
    	}
    	
    	return bizLogic;
    }
    
}