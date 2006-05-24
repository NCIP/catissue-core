package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import edu.wustl.catissuecore.domain.Participant;

/**
 * @author vaishali_khandelwal
 *
 * This interface is for finding out the matching participant with respect to given participant
 */

public interface ParticipantLookupLogic
{
	List participantLookup(Participant participant) throws Exception;
}
