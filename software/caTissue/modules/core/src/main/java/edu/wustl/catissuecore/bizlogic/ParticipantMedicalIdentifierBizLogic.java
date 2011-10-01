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

	/**
	 * @param obj : obj
	 * @param dao : dao
	 * @param operation : operation
	 * @return boolean
	 * @throws BizLogicException : BizLogicException
	 */
	@Override
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		//throw new DAOException(ApplicationProperties.getValue("participant.medical.identifier.creation.error"));
		final ParticipantMedicalIdentifier participantMedicalIdentifier = (ParticipantMedicalIdentifier) obj;
		final Site site = participantMedicalIdentifier.getSite();
		final Participant participant = participantMedicalIdentifier.getParticipant();
		final String medicalRecordNumber = participantMedicalIdentifier.getMedicalRecordNumber();
		if (site == null || site.getId() == null)
		{
			throw this.getBizLogicException(null, "errors.participant.extiden.missing", "");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		if (participant == null || participant.getId() == null)
		{
			throw this.getBizLogicException(null, "participant.medical.identifier.creation.error",
					"");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		new Validator();
		if (Validator.isEmpty(medicalRecordNumber))
		{
			throw this.getBizLogicException(null, "errors.participant.extiden.missing", "");
			//throw new DAOException("errors.item.required", new String[]{message});
		}
		return true;
	}

}
