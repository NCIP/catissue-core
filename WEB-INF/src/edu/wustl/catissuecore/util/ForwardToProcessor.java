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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.OrderDetails;
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

	public HashMap populateForwardToData(AbstractActionForm actionForm, AbstractDomainObject domainObject)
	{
		HashMap forwardToHashMap = new HashMap();

		if (domainObject instanceof Participant)
		{
			ParticipantForm participantForm = (ParticipantForm) actionForm;

			forwardToHashMap.put("participantId", domainObject.getId());
			if (participantForm.getCpId() != -1)
			{
				forwardToHashMap.put("collectionProtocolId",Long.valueOf(participantForm.getCpId()));
			}

		}
		else if (domainObject instanceof StorageType) //Added this if condition to resolve Bug: 1938
		{
			forwardToHashMap.put("storageTypeId", domainObject.getId());
		}
		else if (domainObject instanceof CollectionProtocolRegistration)
		{
			CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm) actionForm;

			forwardToHashMap.put("collectionProtocolId", Long.valueOf(collectionProtocolRegistrationForm.getCollectionProtocolID()));
			forwardToHashMap.put("participantId", Long.valueOf(collectionProtocolRegistrationForm.getParticipantID()));
			forwardToHashMap.put("participantProtocolId", collectionProtocolRegistrationForm.getParticipantProtocolID());
		}
		else if (domainObject instanceof SpecimenCollectionGroup)
		{
			/* bug 7476*/
			//SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) actionForm;
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) domainObject;
			forwardToHashMap.put("specimenCollectionGroupId", domainObject.getId().toString());
			forwardToHashMap.put("specimenCollectionGroupName", scg.getName());
		}
		else if (domainObject instanceof Specimen)
		{
			Specimen specimen = (Specimen) domainObject;
			// this if loop has been added as a fix for bug no.7439
			//Bug description: the derivatives of derivatives are not created it gives error SpecimenCollectionGroupName is null
			if(!forwardToHashMap.containsKey("specimenCollectionGroupName") && specimen.getSpecimenCollectionGroup().getGroupName()!= null)
			{
				forwardToHashMap.put("specimenCollectionGroupName", specimen.getSpecimenCollectionGroup().getGroupName().toString());
			}
			if(!forwardToHashMap.containsKey("generateLabel") && specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration() != null)
			{
				forwardToHashMap.put("generateLabel", specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel());
			}
			//end of fix for bug no.7439
			//Derive New from This Specimen
			if (actionForm.getForwardTo().equals("createNew") || actionForm.getForwardTo().equals("CPQueryPrintSpecimenAdd")||actionForm.getForwardTo().equals("CPQueryPrintSpecimenEdit") || actionForm.getForwardTo().equals("PrintSpecimenEdit") || actionForm.getForwardTo().equals("addSpecimenToCartForwardtoDerive") || actionForm.getForwardTo().equals("addSpecimenToCartPrintAndForward"))
			{
				forwardToHashMap.put("parentSpecimenId", domainObject.getId());
				forwardToHashMap.put(Constants.SPECIMEN_LABEL, specimen.getLabel());
			}
			//Add To Same Collection Group
			else if (actionForm.getForwardTo().equals("sameCollectionGroup"))
			{
				/*NewSpecimenForm newSpecimenForm = (NewSpecimenForm) actionForm;*/
				if (specimen.getSpecimenCollectionGroup().getId() != null)
				{
					forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId().toString());
					if (actionForm instanceof NewSpecimenForm)
					{
						forwardToHashMap.put("specimenCollectionGroupName", ((NewSpecimenForm) actionForm).getSpecimenCollectionGroupName());
					}

				}
			}
			//Add Events
			else if (actionForm.getForwardTo().equals("eventParameters")|| actionForm.getForwardTo().equals("CPQueryPrintDeriveSpecimen"))
			{
				forwardToHashMap.put("specimenId", domainObject.getId().toString());
				forwardToHashMap.put(Constants.SPECIMEN_LABEL, specimen.getLabel());
				forwardToHashMap.put("specimenClass", specimen.getClassName());
			}
			else if (actionForm.getForwardTo().equals("distribution") || actionForm.getForwardTo().equals("CPQueryPrintSpecimenEdit") || actionForm.getForwardTo().equals("CPQueryPrintSpecimenAdd") || actionForm.getForwardTo().equals("CPQueryPrintDeriveSpecimen"))
			{
				forwardToHashMap.put("specimenObjectKey", domainObject);
			}
			/*
			 * smita_kadam
			 * Bug ID: 4447
			 * Patch ID: 1
			 * Reviewer: Sachin Lale
			 * "ParentSpecimenID" added to forwardMap when page is "pageOfCreateAliquot"
			 */
			else if (actionForm.getForwardTo().equals("pageOfAliquot") || actionForm.getForwardTo().equals("pageOfCreateAliquot") || actionForm.getForwardTo().equals("addSpecimenToCartForwardtoAliquot"))
			{
				forwardToHashMap.put("parentSpecimenId", domainObject.getId().toString());
				forwardToHashMap.put(Constants.SPECIMEN_LABEL, specimen.getLabel());
			}
			else if (actionForm.getForwardTo().equals("deriveMultiple"))
			{
				forwardToHashMap.put("specimenLabel", specimen.getLabel());
			}

			//Aniruddha:17/07/06 :: Added for aliquot result page
			if (Constants.ALIQUOT.equals(((Specimen) domainObject).getLineage()) && actionForm.getOperation().equals(Constants.ADD))
			{
				forwardToHashMap = (HashMap) ((Specimen) domainObject).getAliqoutMap();
				return forwardToHashMap;
			}

             //updated to solve printing issue in case of "Create Aliquot/Derived Specimen as per CP"
			if(actionForm.getForwardTo().equals(Constants.CP_CHILD_SUBMIT)|| actionForm.getForwardTo().equals("addSpecimenToCartPrintAndForward") || actionForm.getForwardTo().equals("CPQueryPrintSpecimenAdd")||actionForm.getForwardTo().equals("CPQueryPrintSpecimenEdit") || actionForm.getForwardTo().equals("PrintSpecimenEdit") || actionForm.getForwardTo().equals("addSpecimenToCartForwardtoCpChild"))
			{
				forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId());
				forwardToHashMap.put("specimenId", specimen.getId());
			}
		}
		//added for specimenArrayAliquot Summary page.
		else if (domainObject instanceof SpecimenArray)
		{
			forwardToHashMap = (HashMap) ((SpecimenArray) domainObject).getAliqoutMap();
		}
		//Checking weather any of order item is distributed. If order item distributed then forward to distribition report page
		else if (domainObject instanceof OrderDetails)
		{

			int specimenDistributedCnt = 0;
			int specimenArrayDistributedCnt = 0;
			OrderDetails order = (OrderDetails) domainObject;
			Collection distributionColl = order.getDistributionCollection();
			if (distributionColl != null && distributionColl.size() > 0)
			{
				Iterator itr1 = distributionColl.iterator();
				while (itr1.hasNext())
				{
					Distribution distribution = (Distribution) itr1.next();
					Collection distributedItemColl = distribution.getDistributedItemCollection();
					Iterator itr2 = distributedItemColl.iterator();
					while (itr2.hasNext())
					{
						DistributedItem distributedItem = (DistributedItem) itr2.next();
						if (distributedItem.getSpecimen() != null)
							specimenDistributedCnt++;
						else if (distributedItem.getSpecimenArray() != null)
							specimenArrayDistributedCnt++;

					}
					forwardToHashMap.put("distributionId", distribution.getId());
					if (specimenDistributedCnt > 0 && specimenArrayDistributedCnt == 0)
					{
						actionForm.setForwardTo("specimenDistributionReport");
					}
					if (specimenArrayDistributedCnt > 0 && specimenDistributedCnt == 0)
					{
						actionForm.setForwardTo("specimenArrayDistributionReport");
					}
				}
			}
			else
			{
				actionForm.setForwardTo("success");
			}
		}

		return forwardToHashMap;
	}
}