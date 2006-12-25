/**
 * <p>Title: ForwardToProcessor Class>
 * <p>Description:	ForwardToProcessor populates data required for ForwardTo activity</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 08, 2006
 */

package edu.wustl.catissuecore.util;

import java.util.HashMap;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.AbstractForwardToProcessor;

/**
 * ForwardToProcessor populates data required for ForwardTo activity
 * @author Krunal Thakkar
 */
public class ForwardToProcessor extends AbstractForwardToProcessor
{

	public HashMap populateForwardToData(AbstractActionForm actionForm,
			AbstractDomainObject domainObject)
	{
		HashMap forwardToHashMap = new HashMap();

		if (domainObject instanceof Participant)
		{
			ParticipantForm participantForm = (ParticipantForm) actionForm;
			
			forwardToHashMap.put("participantId", domainObject.getId());
			if(participantForm.getCpId() != -1)
			{
				forwardToHashMap.put("collectionProtocolId", new Long(participantForm.getCpId()));
			}
			
			
		}
		else if (domainObject instanceof StorageType) //Added this if condition to resolve Bug: 1938
		{
			forwardToHashMap.put("storageTypeId", domainObject.getId());
		}
		else if (domainObject instanceof CollectionProtocolRegistration)
		{
			CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm) actionForm;

			forwardToHashMap.put("collectionProtocolId", new Long(
					collectionProtocolRegistrationForm.getCollectionProtocolID()));
			forwardToHashMap.put("participantId", new Long(collectionProtocolRegistrationForm
					.getParticipantID()));
			forwardToHashMap.put("participantProtocolId", collectionProtocolRegistrationForm
					.getParticipantProtocolID());
		}
		else if (domainObject instanceof SpecimenCollectionGroup)
		{
			SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm)actionForm;
			forwardToHashMap.put("specimenCollectionGroupId", domainObject.getId().toString());
			forwardToHashMap.put("specimenCollectionGroupName", specimenCollectionGroupForm.getName());
		}
		else if (domainObject instanceof Specimen)
		{
			Specimen specimen = (Specimen) domainObject;
			//Derive New from This Specimen
			if (actionForm.getForwardTo().equals("createNew"))
			{
				forwardToHashMap.put("parentSpecimenId", domainObject.getId());
			}
			//Add To Same Collection Group
			else if (actionForm.getForwardTo().equals("sameCollectionGroup"))
			{
				/*NewSpecimenForm newSpecimenForm = (NewSpecimenForm) actionForm;*/
				if (specimen.getSpecimenCollectionGroup().getId() != null)
				{
					forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId().toString());
				}
			}
			//Add Events
			else if (actionForm.getForwardTo().equals("eventParameters"))
			{
				forwardToHashMap.put("specimenId", domainObject.getId().toString());
			}
			else if (actionForm.getForwardTo().equals("distribution"))
			{
				forwardToHashMap.put("specimenObjectKey", domainObject);
			}
			else if (actionForm.getForwardTo().equals("pageOfAliquot"))
			{
				forwardToHashMap.put("parentSpecimenId", domainObject.getId().toString());
			}

			//Aniruddha:17/07/06 :: Added for aliquot result page
			if (Constants.ALIQUOT.equals(((Specimen) domainObject).getLineage()) && actionForm.getOperation().equals(Constants.ADD))
			{
				forwardToHashMap = (HashMap) ((Specimen) domainObject).getAliqoutMap();
				return forwardToHashMap;
			}

		}
		//added for specimenArrayAliquot Summary page.
		else if (domainObject instanceof SpecimenArray)
        {
        	forwardToHashMap= (HashMap)((SpecimenArray)domainObject).getAliqoutMap();        	
        }        

		return forwardToHashMap;
	}
}