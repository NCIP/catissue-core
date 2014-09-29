package krishagni.catissueplus.util;

import java.util.Date;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;


public class SpecimenUtil
{
	public static boolean isSpecimenLabelGeneratorAvl(Long identifier, HibernateDAO hibernateDao) throws DAOException
	{
		boolean generateLabel = Variables.isSpecimenLabelGeneratorAvl;

		if (Variables.isTemplateBasedLblGeneratorAvl)
		{
		    SpecimenCollectionGroup scg = (SpecimenCollectionGroup) hibernateDao.retrieveById(SpecimenCollectionGroup.class.getName(), identifier);

				final CollectionProtocolRegistration collectionProtocolRegistration = scg.getCollectionProtocolRegistration();

				String parentLabelFormat = collectionProtocolRegistration.getCollectionProtocol()
						.getSpecimenLabelFormat();

				String derivativeLabelFormat = collectionProtocolRegistration.getCollectionProtocol()
						.getDerivativeLabelFormat();

				String aliquotLabelFormat = collectionProtocolRegistration.getCollectionProtocol()
						.getAliquotLabelFormat();

				generateLabel = SpecimenUtil.isLblGenOnForCP(parentLabelFormat, derivativeLabelFormat,
						aliquotLabelFormat, Constants.NEW_SPECIMEN);
		}
		return generateLabel;
	}

	public static boolean isLblGenOnForCP(String parentLabelformat, String deriveLabelFormat,
			String aliquotLabelFormat, String lineage)
	{
		boolean isGenLabel = false;
		if(Constants.NEW_SPECIMEN.equals(lineage))
		{
			isGenLabel = !Validator.isEmpty(parentLabelformat) && !"%CP_DEFAULT%".equals(parentLabelformat);
		}
		else if(Constants.DERIVED_SPECIMEN.equals(lineage))
		{
			isGenLabel = getGenLabelForChildSpecimen(deriveLabelFormat,parentLabelformat);
		}
		else if(Constants.ALIQUOT.equals(lineage))
		{
			isGenLabel = getGenLabelForChildSpecimen(aliquotLabelFormat,parentLabelformat);
		}
		return isGenLabel;
	}


	private static boolean getGenLabelForChildSpecimen(String format,
			String parentLabelformat)
	{
		boolean isGenLabel = false;

		if(!Validator.isEmpty(format) && !format.contains("%CP_DEFAULT%"))
		{
			isGenLabel = true;
		}
		else if(!Validator.isEmpty(format) && format.contains("%CP_DEFAULT%"))
		{
			isGenLabel = !Validator.isEmpty(parentLabelformat) && !"%CP_DEFAULT%".equals(parentLabelformat);
		}
		return isGenLabel;
	}

	/**
	 * Checks if is gen label.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is gen label
	 */
	public static boolean isGenLabel(final Specimen objSpecimen)
	{
		boolean isGenerateLabel = false;

		String lineage = objSpecimen.getLineage();
		if(lineage == null)
		{
			lineage = objSpecimen.getSpecimenRequirement().getLineage();
		}
		String pLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		String derLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getDerivativeLabelFormat();
		String aliqLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getAliquotLabelFormat();

		if(objSpecimen.getSpecimenRequirement() != null && Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()))
		{
			isGenerateLabel = false;
		}
		else if(objSpecimen.getSpecimenRequirement() != null && !Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()) && !objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
		{
			isGenerateLabel = true;
		}
		else if(objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
		{
			isGenerateLabel = SpecimenUtil.isLblGenOnForCP(pLabelFormat, derLabelFormat, aliqLabelFormat, lineage);
		}
		else if(objSpecimen.getSpecimenRequirement() == null)
		{
			isGenerateLabel = SpecimenUtil.isLblGenOnForCP(pLabelFormat, derLabelFormat, aliqLabelFormat, lineage);
		}
		return isGenerateLabel;
	}
	
	public static DisposalEventParameters createDisposeEvent(SessionDataBean sessionDataBean, AbstractSpecimen specimen,
			String disposalReason)
	{
		final DisposalEventParameters disposalEvent = new DisposalEventParameters();
		disposalEvent.setSpecimen(specimen);
		disposalEvent.setReason(disposalReason);
		disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		final User user = new User();
		user.setId(sessionDataBean.getUserId());
		disposalEvent.setUser(user);
		disposalEvent.setActivityStatus(specimen.getActivityStatus());
		return disposalEvent;
	}
	
}
