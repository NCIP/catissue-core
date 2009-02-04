/*
 * Created on Oct 6, 2006
 */
package edu.wustl.catissuecore.util;

import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerType;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * This is the utility class for API Search.
 * This class set the default values for various domain object
 * @author jitendra_agrawal
 */
public class ApiSearchUtil
{
	
	public static void setUserDefault(User user)
	{
		if (SearchUtil.isNullobject(user.getLastName()))
    	{
    		 user.setLastName("");
    	}		
    	
    	if (SearchUtil.isNullobject(user.getFirstName()))
    	{
    		user.setFirstName("");
    	}
		
    	if (SearchUtil.isNullobject(user.getLoginName()))
    	{
    		user.setLoginName("");
    	}
    	
    	if (SearchUtil.isNullobject(user.getEmailAddress()))
    	{
    		user.setEmailAddress("");
    	}
    	
    	if (SearchUtil.isNullobject(user.getAddress()))
    	{
    		user.setAddress(new Address());
    	}		
    	
    	if (SearchUtil.isNullobject(user.getInstitution()))
    	{
    		user.setInstitution(new Institution());
    	}		
    	
    	if (SearchUtil.isNullobject(user.getDepartment()))
    	{
    		user.setDepartment(new Department());
    	}		
    	
    	if (SearchUtil.isNullobject(user.getCancerResearchGroup()))
    	{
    		user.setCancerResearchGroup(new CancerResearchGroup());
    	}     	
    	
    	if (SearchUtil.isNullobject(user.getFirstTimeLogin()))
    	{
    		user.setFirstTimeLogin(new Boolean(true));
    	}	
    	
    	if (SearchUtil.isNullobject(user.getActivityStatus()))
    	{
    		user.setActivityStatus(Constants.ACTIVITY_STATUS_NEW);
    	}	
	}
	public static void setSpecimenRequirementDefault(SpecimenRequirement specimenRequirement)
	{		
    	if (SearchUtil.isNullobject(specimenRequirement.getQuantity()))
    	{
    		specimenRequirement.setQuantity(new Quantity());
    	}
	}
	
	public static void setSpecimenArrayContentDefault(SpecimenArrayContent specimenArrayContent)
	{		
    	if (SearchUtil.isNullobject(specimenArrayContent.getInitialQuantity()))
    	{
    		specimenArrayContent.setInitialQuantity(new Quantity());
    	}
    	
    	if (SearchUtil.isNullobject(specimenArrayContent.getSpecimen()))
    	{
    		specimenArrayContent.setSpecimen(new Specimen());
    	}
    	
    	if (SearchUtil.isNullobject(specimenArrayContent.getSpecimenArray()))
    	{
    		specimenArrayContent.setSpecimenArray(new SpecimenArray());
    	}
	}
	
	public static void setSpecimenDefault(Specimen specimen)
	{			
    	if (SearchUtil.isNullobject(specimen.getStorageContainer()))
    	{
    		//TODO specimen.setStorageContainer(new StorageContainer());
    	}		
		
    	if (SearchUtil.isNullobject(specimen.getSpecimenCollectionGroup()))
    	{
    		specimen.setSpecimenCollectionGroup(new SpecimenCollectionGroup());
    	}		
		
    	if (SearchUtil.isNullobject(specimen.getSpecimenCharacteristics()))
    	{
    		specimen.setSpecimenCharacteristics(new SpecimenCharacteristics());
    	}		
		
    	if (SearchUtil.isNullobject(specimen.getInitialquantity()))
    	{
    		specimen.setInitialquantity(new Quantity());
    	}		
		
    	if (SearchUtil.isNullobject(specimen.getAvailableQuantity()))
    	{
    		specimen.setAvailableQuantity(new Quantity());
    	}
	}
	
	public static void setSiteDefault(Site site)
	{		
    	if (SearchUtil.isNullobject(site.getCoordinator()))
    	{
    		site.setCoordinator(new User());
    	}    	
    	if (SearchUtil.isNullobject(site.getAddress()))
    	{
    		site.setAddress(new Address());
    	}        	 
	}
	
	public static void setReportedProblemDefault(ReportedProblem reportedProblem)
	{
		if (SearchUtil.isNullobject(reportedProblem.getReportedDate()))
    	{
			reportedProblem.setReportedDate(new Date());
    	}
	}
	
	public static void setPasswordDefault(Password password)
	{
		if (SearchUtil.isNullobject(password.getUser()))
    	{
			password.setUser(new User());
    	}
	}
	
	public static void setParticipantMedicalIdentifierDefault(ParticipantMedicalIdentifier participantMedicalIdentifier)
	{
		if (SearchUtil.isNullobject(participantMedicalIdentifier.getSite()))
    	{
			participantMedicalIdentifier.setSite(new Site());
    	}
	}	
	
	public static void setEventParametersDefault(EventParameters eventParameters)
	{		
    	if (SearchUtil.isNullobject(eventParameters.getUser()))
    	{
    		eventParameters.setUser(new User());
    	}
        
    	if (SearchUtil.isNullobject(eventParameters.getTimestamp()))
    	{
    		eventParameters.setTimestamp(Calendar.getInstance().getTime());
    	}
	}
	
	public static void setDistributionDefault(Distribution distribution)
	{				
    	if (SearchUtil.isNullobject(distribution.getToSite()))
    	{
    		distribution.setToSite(new Site());
    	}    	
        
    	if (SearchUtil.isNullobject(distribution.getDistributedItemCollection()))
    	{
    		distribution.setDistributionProtocol(new DistributionProtocol());
    	}
    	
    	if (SearchUtil.isNullobject(distribution.getUser()))
    	{
    		distribution.setUser(new User());
    	}
        
    	if (SearchUtil.isNullobject(distribution.getTimestamp()))
    	{
    		distribution.setTimestamp(Calendar.getInstance().getTime());
    	}
	}
	
	public static void setDistributedItemDefault(DistributedItem distributedItem)
	{
		if(distributedItem.getSpecimenArray() == null)
		{
	    	if (SearchUtil.isNullobject(distributedItem.getSpecimen()))
	    	{
	    		distributedItem.setSpecimen(new Specimen());
	    	}   
		}
      
    	if (SearchUtil.isNullobject(distributedItem.getDistribution()))
    	{
    		distributedItem.setDistribution(new Distribution());
    	}
	}
	
	
	public static void setSpecimenArrayDefault(SpecimenArray specimenArray)
	{				 
		if (SearchUtil.isNullobject(specimenArray.getSpecimenArrayType()))
    	{
    		specimenArray.setSpecimenArrayType(new SpecimenArrayType());
    	}
    	    	
    	if (SearchUtil.isNullobject(specimenArray.getCreatedBy()))
    	{
    		specimenArray.setCreatedBy(new User());
    	}
    	
    	if (SearchUtil.isNullobject(specimenArray.getStorageContainer()))
    	{
    		specimenArray.setStorageContainer(new StorageContainer());
    	}
    	
    	if (SearchUtil.isNullobject(specimenArray.getAvailable()))
    	{
    		specimenArray.setAvailable(new Boolean(true));
    	}	
	}
	
	public static void setContainerDefault(Container container)
	{
		if (SearchUtil.isNullobject(container.getCapacity()))
    	{
			container.setCapacity(new Capacity());
    	}	
	}
	
	public static void setCollectionProtocolRegistrationDefault(CollectionProtocolRegistration collectionProtocolRegistration)
	{		
    	if (SearchUtil.isNullobject(collectionProtocolRegistration.getCollectionProtocol()))
    	{
    		collectionProtocolRegistration.setCollectionProtocol(new CollectionProtocol());
    	}    	
      
    	if (SearchUtil.isNullobject(collectionProtocolRegistration.getRegistrationDate()))
    	{
    		collectionProtocolRegistration.setRegistrationDate(new Date());
    	}
	}
	
	public static void setContainerTypeDefault(ContainerType containerType)
	{
		if (SearchUtil.isNullobject(containerType.getCapacity()))
    	{
			containerType.setCapacity(new Capacity());
    	}		
	}
	
	public static void setSpecimenProtocolDefault(SpecimenProtocol specimenProtocol)
	{
		if (SearchUtil.isNullobject(specimenProtocol.getPrincipalInvestigator()))
    	{
			specimenProtocol.setPrincipalInvestigator(new User());
    	}		
	}	
	
}
