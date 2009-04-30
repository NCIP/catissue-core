/**
 * <p>Title: ForwardToProcessor Class>
 * <p>Description:	ForwardToProcessor populates data required for ForwardTo activity</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author falguni_sachde
 * @version 1.00
 *  
 */

package edu.wustl.catissuecore.util;

import java.util.HashMap;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.AbstractForwardToProcessor;

/**
 * ForwardToPrintProcessor populates data required for ForwardTo activity
 * 
 */
public class ForwardToPrintProcessor extends AbstractForwardToProcessor
{

	public HashMap populateForwardToData(AbstractActionForm actionForm, AbstractDomainObject domainObject)
	{

		HashMap forwardToPrintMap = new HashMap();

		if (domainObject instanceof SpecimenCollectionGroup)
		{
			SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) actionForm;
			forwardToPrintMap.put("specimenCollectionGroupId", domainObject.getId().toString());
			forwardToPrintMap.put("specimenCollectionGroupName", specimenCollectionGroupForm.getName());
		}
		else if (domainObject instanceof Specimen)
		{
			Specimen specimen = (Specimen) domainObject;
		
			if (actionForm.getForwardTo().equals("createNew") )
			{
				forwardToPrintMap.put("parentSpecimenId", domainObject.getId());
			}
		
			else if (actionForm.getForwardTo().equals("sameCollectionGroup"))
			{
				
				if (specimen.getSpecimenCollectionGroup().getId() != null)
				{
					forwardToPrintMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId().toString());
					if (actionForm instanceof NewSpecimenForm)
					{
						forwardToPrintMap.put("specimenCollectionGroupName", ((NewSpecimenForm) actionForm).getSpecimenCollectionGroupName());
					}

				}
			}
			//Add Events
			else if (actionForm.getForwardTo().equals("eventParameters"))
			{
				forwardToPrintMap.put("specimenId", domainObject.getId().toString());
				forwardToPrintMap.put(Constants.SPECIMEN_LABEL, specimen.getLabel());
				forwardToPrintMap.put("specimenClass", specimen.getClassName());
			}
			else if (actionForm.getForwardTo().equals("distribution") )
			{
				forwardToPrintMap.put("specimenObjectKey", domainObject);
			}
		
			else if (actionForm.getForwardTo().equals("pageOfAliquot") || actionForm.getForwardTo().equals("pageOfCreateAliquot"))
			{
				forwardToPrintMap.put("parentSpecimenId", domainObject.getId().toString());
			}
			else if (actionForm.getForwardTo().equals("addSpecimenToCartPrintAndForward") ||actionForm.getForwardTo().equals("CPQueryPrintSpecimenEdit") || actionForm.getForwardTo().equals("PrintSpecimenEdit") || actionForm.getForwardTo().equals("CPQueryPrintSpecimenAdd") || actionForm.getForwardTo().equals("CPQueryPrintDeriveSpecimen") || actionForm.getForwardTo().equals("printDeriveSpecimen"))
			{
				forwardToPrintMap.put("specimenId", domainObject.getId().toString());
			}
		
			if (Constants.ALIQUOT.equals(((Specimen) domainObject).getLineage()) && actionForm.getOperation().equals(Constants.ADD))
			{
				if (actionForm instanceof AliquotForm && (((AliquotForm)actionForm).getPrintCheckbox() != null))
				{
					forwardToPrintMap = (HashMap) ((Specimen) domainObject).getAliqoutMap();
					return forwardToPrintMap;
				}
			
			} 
			if (actionForm.getForwardTo().equals("deriveMultiple"))
			{
				forwardToPrintMap.put("specimenLabel", specimen.getLabel());
			}

		}
		else if (domainObject instanceof StorageContainer)
		{
			forwardToPrintMap.put("StorageContainerObjID", domainObject.getId().toString());
		}
		 
		return forwardToPrintMap;
	}
	

}