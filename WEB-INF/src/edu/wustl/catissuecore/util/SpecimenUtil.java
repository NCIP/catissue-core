
package edu.wustl.catissuecore.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

/**
 * The Class SpecimenUtil.
 * @author nitesh_marwaha
 *
 */
public final class SpecimenUtil
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenUtil.class);

	/**
	 * Validate specimen status.
	 *
	 * @param specimen the specimen
	 * @param dao the dao
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	public static void validateSpecimenStatus(long specimenId, DAO dao) throws BizLogicException
	{
		final String sourceObjectName1 = Specimen.class.getName();
		final String[] selectColumnName1 = {"activityStatus"};
		final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
		try
		{
			queryWhereClause1.addCondition(new EqualClause("id", specimenId));
			final List list = dao.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);

			if (list.size() > 0)
			{
				String previousStatus = list.get(0).toString();
				if (previousStatus != null
						&& previousStatus.equals(Constants.ACTIVITY_STATUS_VALUES[3]))
				{

					ErrorKey errorKey = ErrorKey.getErrorKey("errors.disabled.specimen");
					throw new BizLogicException(errorKey, null, "");
					//	throw new BizLogicException(null, "errors.item", "errors.disabled.specimen");
				}
			}

		}
		catch (DAOException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = daoExp.getErrorKey();
			throw new BizLogicException(errorKey, null, "");
		}
	}

	/**
	 * Retrieve specimenId by label name.
	 * @param dao DAO
	 * @param specimenEventParametersObject SpecimenEventParameters
	 * @return Object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	public static long retrieveSpecimenIdByLabelName(DAO dao,
			SpecimenEventParameters specimenEventParametersObject) throws DAOException,
			BizLogicException
	{
		Object specimenObject = null;
		List list = null;
		final String sourceObjectName1 = Specimen.class.getName();
		final String[] selectColumnName1 = {"id"};
		final QueryWhereClause queryWhereClause1 = new QueryWhereClause(sourceObjectName1);
		long specimenId = 0l;
		try
		{
			if (specimenEventParametersObject.getSpecimen().getId() != null)
			{
				queryWhereClause1.addCondition(new EqualClause("id", specimenEventParametersObject
						.getSpecimen().getId()));
				list = dao.retrieve(sourceObjectName1, selectColumnName1, queryWhereClause1);

			}

			else if (specimenEventParametersObject.getSpecimen().getLabel() != null
					|| specimenEventParametersObject.getSpecimen().getLabel().length() > 0)
			{
				String column = "label";

				queryWhereClause1.addCondition(new EqualClause(column,
						specimenEventParametersObject.getSpecimen().getLabel()));
				list = dao.retrieve(Specimen.class.getName(), selectColumnName1, queryWhereClause1);
				if (list.isEmpty())
				{

					final ErrorKey errorKey = ErrorKey.getErrorKey("invalid.label");

					throw new BizLogicException(errorKey, null, "");

				}
				else
				{
					specimenObject = list.get(0);

				}
			}

			if (list != null && list.size() > 0)
			{
				specimenId = Long.valueOf(list.get(0).toString());
			}
		}
		catch (DAOException daoExp)
		{
			LOGGER.debug(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = daoExp.getErrorKey();
			throw new BizLogicException(errorKey, null, "");
		}
		return specimenId;
	}

	/**
	 * @param objSpecimen
	 * @return
	 */
	public static String getCollectionYear(Specimen objSpecimen)
	{
		String valToReplace="";
		int  yearOfcoll = Calendar.getInstance().get(Calendar.YEAR);
//		valToReplace = objSpecimen.getc
		Iterator specEveItr = objSpecimen.getSpecimenEventCollection().iterator();
		while(specEveItr.hasNext())
		{
			Object specEveParam = specEveItr.next();
			if (specEveParam instanceof CollectionEventParameters)
			{
				CollectionEventParameters collEveParam = (CollectionEventParameters) specEveParam;
				//				Date date =
				Calendar cal = Calendar.getInstance();
				cal.setTime(collEveParam.getTimestamp());
				yearOfcoll = cal.get(Calendar.YEAR);

			}
		}
		valToReplace = yearOfcoll+"";
		return valToReplace;
	}

	public static boolean isLblGenOnForCP(String parentLabelformat, String deriveLabelFormat,
			String aliquotLabelFormat, String lineage)
	{
		boolean isGenLabel = false;
		if(Constants.NEW_SPECIMEN.equals(lineage))
		{
			isGenLabel = !Validator.isEmpty(parentLabelformat) && !parentLabelformat.equals("%CP_DEFAULT%");
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
			isGenLabel = !Validator.isEmpty(parentLabelformat) && !parentLabelformat.equals("%CP_DEFAULT%");
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
}
