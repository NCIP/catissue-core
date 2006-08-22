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
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
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
        HashMap forwardToHashMap=new HashMap();
        
        if(domainObject instanceof Participant)
        {
            forwardToHashMap.put("participantId", domainObject.getSystemIdentifier());
        }
        else if (domainObject instanceof CollectionProtocolRegistration)
        {
            CollectionProtocolRegistrationForm collectionProtocolRegistrationForm= (CollectionProtocolRegistrationForm)actionForm;
            
            forwardToHashMap.put("collectionProtocolId", new Long( collectionProtocolRegistrationForm.getCollectionProtocolID()) );
            forwardToHashMap.put("participantId", new Long(collectionProtocolRegistrationForm.getParticipantID()));
            forwardToHashMap.put("participantProtocolId", collectionProtocolRegistrationForm.getParticipantProtocolID());
        }
        else if (domainObject instanceof SpecimenCollectionGroup)
        {
            forwardToHashMap.put("specimenCollectionGroupId", domainObject.getSystemIdentifier().toString());
            
        }
        else if (domainObject instanceof Specimen)
        {
        	 //Aniruddha:17/07/06 :: Added for aliquot result page
        	if(Constants.ALIQUOT.equals(((Specimen)domainObject).getLineage()))
        	{
        		forwardToHashMap = (HashMap)((Specimen)domainObject).getAliqoutMap();
            	return forwardToHashMap;
        	}
            //Derive New from This Specimen
            if(actionForm.getForwardTo().equals("createNew"))
            {
                forwardToHashMap.put("parentSpecimenId", domainObject.getSystemIdentifier());
            }
            //Add To Same Collection Group
            else if(actionForm.getForwardTo().equals("sameCollectionGroup"))
            {
                NewSpecimenForm newSpecimenForm= (NewSpecimenForm)actionForm;
                
                forwardToHashMap.put("specimenCollectionGroupId", newSpecimenForm.getSpecimenCollectionGroupId());
            }
            //Add Events
            else if(actionForm.getForwardTo().equals("eventParameters"))
            {
                forwardToHashMap.put("specimenId", domainObject.getSystemIdentifier().toString());
            }
        }
        
        return forwardToHashMap;
    }
}
