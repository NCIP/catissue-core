package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.common.util.KeySequenceGeneratorUtil;
import edu.wustl.dao.exception.DAOException;


public class LabelTokenForPpiUId implements LabelTokens
{

	public String getTokenValue(Object object, String token, Long currVal)
	{
		return getSpecimenCount((Specimen)object);
	}

	private String getSpecimenCount(Specimen  specimen)
	{
		long cprId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getId();
		long cpId = specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId();
		String yearOfColl = SpecimenUtil.getCollectionYear(specimen);
		String key = cpId+"_"+cprId+"_"+yearOfColl;
		String type = "SpecimenCount";
		long ctr = 0;
		try
		{
			ctr = KeySequenceGeneratorUtil.getNextUniqeId(key, type);
		}
		catch (DAOException e1)
		{
			e1.printStackTrace();
		}
		return ctr+"";
	}

}
