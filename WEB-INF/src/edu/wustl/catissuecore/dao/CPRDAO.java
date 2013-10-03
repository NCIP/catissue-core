package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.participant.domain.ISite;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;


public class CPRDAO
{
	private static final Logger LOGGER = Logger.getCommonLogger(CPRDAO.class);
	public Long getCPRIDByPMI(HibernateDAO hibernateDAO, String cpShortTitle, ISite site, String medicalRecordNo) throws BizLogicException
	{
		String query = "";
		if(!Validator.isEmpty(site.getName()))
		{
		query = "select cpr.identifier from catissue_coll_prot_reg cpr join catissue_participant part on " +
				"cpr.participant_id=part.identifier join catissue_part_medical_id pmi on pmi.participant_id = part.identifier " +
				"join catissue_site site on pmi.site_id=site.identifier join catissue_specimen_protocol cp on " +
				"cpr.COLLECTION_PROTOCOL_ID = cp.identifier where site.name='"+site.getName()+"' and pmi.MEDICAL_RECORD_NUMBER='"+medicalRecordNo+"' " +
				"and cp.SHORT_TITLE='"+cpShortTitle+"'";
		}
		else if(site.getId() != null || site.getId() != 0l)
		{
			query = "select cpr.identifier from catissue_coll_prot_reg cpr join catissue_participant part on " +
					"cpr.participant_id=part.identifier join catissue_part_medical_id pmi on pmi.participant_id = part.identifier " +
					"join catissue_site site on pmi.site_id=site.identifier join catissue_specimen_protocol cp on " +
					"cpr.COLLECTION_PROTOCOL_ID = cp.identifier where site.identifier="+site.getId()+" and pmi.MEDICAL_RECORD_NUMBER='"+medicalRecordNo+"' " +
					"and cp.SHORT_TITLE='"+cpShortTitle+"'";
		}
		try
		{
			
			List cprList = AppUtility.executeSQLQuery(query);
			if(cprList == null || cprList.isEmpty())
			{
				LOGGER.error("PMI details are not correct, no matching CPR found with the given PMI details");
				throw new BizLogicException(null, null, "invalid.site.name", site.getName());
			}
			ArrayList arr = (ArrayList) cprList.get(0);
			return Long.valueOf(arr.get(0).toString());
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e);
			throw new BizLogicException(null, null, "invalid.site.name", site.getName());
		}
	}
	
	public Long getCPRIDByPPID(HibernateDAO hibernateDAO,String ppid, String cpShortTitle) throws BizLogicException
	{
		try
		{
			List cprList = AppUtility
				.executeSQLQuery("select cpr.identifier from catissue_coll_prot_reg cpr, catissue_specimen_protocol sp "
						+ "where cpr.PROTOCOL_PARTICIPANT_ID='"
						+ ppid
						+ "' and cpr.COLLECTION_PROTOCOL_ID=sp.identifier and sp.SHORT_TITLE='"
						+ cpShortTitle + "'");
			if(cprList == null || cprList.isEmpty())
			{
				LOGGER.error("PMI details are not correct, no matching CPR found with the given PMI details");
				throw new BizLogicException(null, null, "invalid.site.name", ppid);
			}
			ArrayList arr = (ArrayList) cprList.get(0);
			return Long.valueOf(arr.get(0).toString());
		}
		catch (ApplicationException e)
		{
			LOGGER.error(e);
			String message = ApplicationProperties
					.getValue("specimenCollectionGroup.collectedByProtocolParticipantNumber");
			throw new BizLogicException(null,null,"errors.item.invalid", message);
		}
	}

}
