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
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.ContainerType;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.util.global.Status;


/**
 * This is the utility class for API Search.
 * This class set the default values for various domain object
 * @author jitendra_agrawal
 */
public final class ApiSearchUtil
{
	static InstanceFactory<User> userInstFact = DomainInstanceFactory.getInstanceFactory(User.class);
	/**
	 * creates a singleton object.
	 */
	private static ApiSearchUtil apiUtil= new ApiSearchUtil();

	/*
	 * Private constructor
	 */
	private ApiSearchUtil()
	{

	}
	/**
	 * returns the single object.
	 *
	 */
	public static ApiSearchUtil getInstance()
	{
		return apiUtil;
	}


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
    		//user.setAddress(new Address());
    		user.setAddress((Address)DomainInstanceFactory.getInstanceFactory(Address.class).createObject());
    	}

    	if (SearchUtil.isNullobject(user.getInstitution()))
    	{
    		//user.setInstitution(new Institution());
    		user.setInstitution((Institution)DomainInstanceFactory.getInstanceFactory(Institution.class).createObject());
    	}

    	if (SearchUtil.isNullobject(user.getDepartment()))
    	{
    		//user.setDepartment(new Department());
    		user.setDepartment((Department)DomainInstanceFactory.getInstanceFactory(Department.class).createObject());
    	}

    	if (SearchUtil.isNullobject(user.getCancerResearchGroup()))
    	{
    		//user.setCancerResearchGroup(new CancerResearchGroup());
    		user.setCancerResearchGroup((CancerResearchGroup)DomainInstanceFactory.getInstanceFactory(CancerResearchGroup.class).createObject());
    	}

    	if (SearchUtil.isNullobject(user.getFirstTimeLogin()))
    	{
    		user.setFirstTimeLogin(Boolean.TRUE);
    	}

    	if (SearchUtil.isNullobject(user.getActivityStatus()))
    	{
    		user.setActivityStatus(Status.ACTIVITY_STATUS_NEW.toString());
    	}
	}
	public static void setSpecimenRequirementDefault(DistributionSpecimenRequirement specRequirement)
	{
    	if (SearchUtil.isNullobject(specRequirement.getQuantity()))
    	{
    		specRequirement.setQuantity(new Double(0));
    	}
	}

	public static void setSpecimenArrayContentDefault(SpecimenArrayContent specimenArrayContent)
	{
    	if (SearchUtil.isNullobject(specimenArrayContent.getInitialQuantity()))
    	{
    		specimenArrayContent.setInitialQuantity(new Double(0));
    	}

    	if (SearchUtil.isNullobject(specimenArrayContent.getSpecimen()))
    	{
    		InstanceFactory<Specimen> instFact= DomainInstanceFactory.getInstanceFactory(Specimen.class);
			specimenArrayContent.setSpecimen(instFact.createObject());
    	}

		if (SearchUtil.isNullobject(specimenArrayContent.getSpecimenArray()))
		{
			InstanceFactory<SpecimenArray> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArray.class);
			specimenArrayContent.setSpecimenArray(instFact.createObject());
		}

	}

	public static void setSpecimenDefault(Specimen specimen)
	{
//    	if (SearchUtil.isNullobject(specimen.getStorageContainer()))
//    	{
//    		//TODO specimen.setStorageContainer(new StorageContainer());
//    	}

    	if (SearchUtil.isNullobject(specimen.getSpecimenCollectionGroup()))
    	{
    		specimen.setSpecimenCollectionGroup(new SpecimenCollectionGroup());
    	}


		if (SearchUtil.isNullobject(specimen.getSpecimenCharacteristics()))
		{
			InstanceFactory<SpecimenCharacteristics> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCharacteristics.class);
			specimen.setSpecimenCharacteristics(instFact.createObject());
		}


    	if (SearchUtil.isNullobject(specimen.getInitialQuantity()))
    	{
    		specimen.setInitialQuantity(new Double(0));
    	}

    	if (SearchUtil.isNullobject(specimen.getAvailableQuantity()))
    	{
    		specimen.setAvailableQuantity(new Double(0));
    	}
	}

	public static void setReqSpecimenDefault(SpecimenRequirement requirementSpecimen)
	{
    	if (SearchUtil.isNullobject(requirementSpecimen.getCollectionProtocolEvent()))
    	{
    		InstanceFactory<CollectionProtocolEvent> cpeInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolEvent.class);
    		requirementSpecimen.setCollectionProtocolEvent(cpeInstFact.createObject());
    	}


		if (SearchUtil.isNullobject(requirementSpecimen.getSpecimenCharacteristics()))
		{
			InstanceFactory<SpecimenCharacteristics> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCharacteristics.class);
			requirementSpecimen.setSpecimenCharacteristics(instFact.createObject());
		}


    	if (SearchUtil.isNullobject(requirementSpecimen.getInitialQuantity()))
    	{
    		requirementSpecimen.setInitialQuantity(new Double(0));
    	}
 	}
	public static void setSiteDefault(Site site)
	{
    	if (SearchUtil.isNullobject(site.getCoordinator()))
		{
			site.setCoordinator(userInstFact.createObject());
		}
    	if (SearchUtil.isNullobject(site.getAddress()))
    	{
    		//site.setAddress(new Address());
    		site.setAddress((Address)DomainInstanceFactory.getInstanceFactory(Address.class).createObject());
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
			InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
			password.setUser(instFact.createObject());
    	}
	}

	public static void setParticipantMedicalIdentifierDefault(ParticipantMedicalIdentifier participantMedicalIdentifier)
	{
		if (SearchUtil.isNullobject(participantMedicalIdentifier.getSite()))
    	{
			participantMedicalIdentifier.setSite(new Site());
    	}
	}

	public static void setEventParametersDefault(SpecimenEventParameters eventParameters)
	{

		if (SearchUtil.isNullobject(eventParameters.getUser()))
		{
			eventParameters.setUser(userInstFact.createObject());
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
    		InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
			distribution.setToSite(instFact.createObject());
    	}

    	if (SearchUtil.isNullobject(distribution.getDistributedItemCollection()))
    	{
    		InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
    		distribution.setDistributionProtocol(instFact.createObject());
    	}


		if (SearchUtil.isNullobject(distribution.getDistributedBy()))
		{
			distribution.setDistributedBy(userInstFact.createObject());
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
				InstanceFactory<Specimen> instFact = DomainInstanceFactory.getInstanceFactory(Specimen.class);
				distributedItem.setSpecimen(instFact.createObject());
			}

    	}

    	if (SearchUtil.isNullobject(distributedItem.getDistribution()))
    	{
    		InstanceFactory<Distribution> instFact = DomainInstanceFactory.getInstanceFactory(Distribution.class);
    		distributedItem.setDistribution(instFact.createObject());//new Distribution();
    	}
	}


	public static void setSpecimenArrayDefault(SpecimenArray specimenArray)
	{
		if (SearchUtil.isNullobject(specimenArray.getSpecimenArrayType()))
		{
			InstanceFactory<SpecimenArrayType> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenArrayType.class);
			specimenArray.setSpecimenArrayType(instFact.createObject());
		}

		if (SearchUtil.isNullobject(specimenArray.getCreatedBy()))
		{
			specimenArray.setCreatedBy(userInstFact.createObject());
		}
		if (SearchUtil.isNullobject(specimenArray.getLocatedAtPosition()))
		{
			InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
			specimenArray.setLocatedAtPosition(instFact.createObject());
		}
		if (SearchUtil.isNullobject(specimenArray.getLocatedAtPosition().getParentContainer()))
		{
			InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
			specimenArray.getLocatedAtPosition().setParentContainer(scInstFact.createObject());
		}


		if (SearchUtil.isNullobject(specimenArray.getAvailable()))
    	{
    		specimenArray.setAvailable(Boolean.TRUE);
    	}
	}

	public static void setContainerDefault(Container container)
	{
		if (SearchUtil.isNullobject(container.getCapacity()))
    	{
			container.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject());//new Capacity()
    	}
	}

	public static void setCollectionProtocolRegistrationDefault(CollectionProtocolRegistration collectionProtocolRegistration)
	{
    	if (SearchUtil.isNullobject(collectionProtocolRegistration.getCollectionProtocol()))
    	{
    		InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
    		collectionProtocolRegistration.setCollectionProtocol(cpInstFact.createObject());
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
			containerType.setCapacity((Capacity)DomainInstanceFactory.getInstanceFactory(Capacity.class).createObject());//new Capacity()
    	}
	}

	public static void setSpecimenProtocolDefault(SpecimenProtocol specimenProtocol)
	{
		if (SearchUtil.isNullobject(specimenProtocol.getPrincipalInvestigator()))
    	{
			specimenProtocol.setPrincipalInvestigator(userInstFact.createObject());
    	}
	}

}
