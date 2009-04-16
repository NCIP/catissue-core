
/**
 * <p>Title: ParticipantMedicalIdentifierBizLogic Class>
 * <p>Description:	ParticipantMedicalIdentifierBizLogic </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kalpana Thakur
 * @version 1.00
 */

package edu.wustl.catissuecore.bizlogic;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.DAO;


public class ParticipantMedicalIdentifierBizLogic extends CatissueDefaultBizLogic
{

	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{	
		
		//throw new DAOException(ApplicationProperties.getValue("participant.medical.identifier.creation.error"));
		
		ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) obj;
			
		Site site = participantMedicalIdentifier.getSite();
		Participant participant = participantMedicalIdentifier.getParticipant();
		String medicalRecordNumber = participantMedicalIdentifier.getMedicalRecordNumber();
		if (site==null || site.getId()==null)
		{
		
			throw getBizLogicException(null, "errors.participant.extiden.missing", "");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		if (participant==null || participant.getId()==null)
		{
		
			throw getBizLogicException(null, "participant.medical.identifier.creation.error", "");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		Validator validate = new Validator();
		if (validate.isEmpty(medicalRecordNumber))
		{
		
			throw getBizLogicException(null, "errors.participant.extiden.missing", "");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}

}
