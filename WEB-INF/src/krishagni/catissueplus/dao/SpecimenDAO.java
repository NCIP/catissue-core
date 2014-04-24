
package krishagni.catissueplus.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.bizlogic.SpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.NotEqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class SpecimenDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(SpecimenBizLogic.class);

	public Long getCpId(Long specimenId, HibernateDAO hibernateDao) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, specimenId==null?0l:specimenId));
		List specimenDetailColl = hibernateDao.executeNamedQuery(Specimen.class.getName()+".getCpIdFromSpecimenId", params);
		if (specimenDetailColl != null && specimenDetailColl.size() > 0)
		{
			return Long.valueOf(specimenDetailColl.get(0).toString());
		}
		return null;
	}

	public Long getCpId(String specimenLabel, HibernateDAO hibernateDao) throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, specimenLabel));
		List specimenDetailColl = hibernateDao.executeNamedQuery(Specimen.class.getName()+".getCpIdFromSpecimenLabel", params);
		if (specimenDetailColl != null && specimenDetailColl.size() > 0)
		{
			return Long.valueOf(specimenDetailColl.get(0).toString());
		}
		return null;
	}

	public Specimen getParentSpecimenByLabelOrBarcode(HibernateDAO hibernateDao, String label) throws ApplicationException
	{
		Specimen specimen = null;
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		Collection<?> specimenDetailColl;
		params.put("0", new NamedQueryParam(DBTypes.STRING, label));
		specimenDetailColl = hibernateDao.executeNamedQuery(Specimen.class.getName()+".selectParentSpecimenDetailsForAliquot", params);
		if (specimenDetailColl == null || specimenDetailColl.isEmpty())
		{
			specimenDetailColl = hibernateDao.executeNamedQuery(Specimen.class.getName()+".selectParentSpecimenDetailsForAliquotByBarcode",
					params);
		}
		if (specimenDetailColl == null || specimenDetailColl.isEmpty())
		{
			throw new CatissueException(SpecimenErrorCodeEnum.INVALID_LABEL_BARCODE.getDescription(),
					SpecimenErrorCodeEnum.INVALID_LABEL_BARCODE.getCode());
		}
		Iterator specimenDetailIterator = specimenDetailColl.iterator();
		if (specimenDetailIterator.hasNext())
		{
			final Object[] valArr = (Object[]) specimenDetailIterator.next();
			if (valArr != null)
			{
				specimen = new Specimen();
				SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
				scg.setId(Long.parseLong(valArr[1].toString()));
				specimen.setSpecimenCollectionGroup(scg);
				specimen.setId(Long.parseLong(valArr[2].toString()));
				specimen.setLabel(valArr[3].toString());
				if(valArr[4] != null)
				{
					specimen.setBarcode(valArr[4].toString());
				}
				specimen.setSpecimenClass(valArr[5].toString());
				specimen.setSpecimenType(valArr[6].toString());
				specimen.setPathologicalStatus(valArr[7].toString());
				specimen.setTissueSite(valArr[8].toString());
				specimen.setTissueSide(valArr[9].toString());
				specimen.setAvailableQuantity(Double.parseDouble(valArr[10].toString()));
				if (valArr[11] != null)
				{
					specimen.setConcentrationInMicrogramPerMicroliter(Double.parseDouble(valArr[11].toString()));
				}
				specimen.setInitialQuantity(Double.parseDouble(valArr[12].toString()));
				if(valArr[13] != null)
				{
					specimen.setIsAvailable(Boolean.valueOf(valArr[13].toString()));
				}
			}
		}
		return specimen;
	}

	private Specimen getSpecimenByLabel(String specimenLabel, HibernateDAO hibernateDao)
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, specimenLabel));
		substParams.put("1", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));
		List<Specimen> specimenList;
		try
		{
			specimenList = hibernateDao.executeNamedQuery(Specimen.class.getName()+".getSpecimenBylabel", substParams);
			if (specimenList != null && specimenList.size() > 0)
			{
				return specimenList.get(0);
			}
			else
			{
				LOGGER.error("Error: Specimen object not found with the given label.");
				throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
						SpecimenErrorCodeEnum.NOT_FOUND.getCode());
			}
		}
		catch (DAOException e)
		{
			LOGGER.error("Error while retereiving Specimen object with the given label");
			LOGGER.error(e);
			throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
					SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}

	}

	public Specimen getSpecimenByBarcode(String specimenBarcode, HibernateDAO hibernateDao)
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, specimenBarcode));
		substParams.put("1", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));

		List<Specimen> specimenList;
		try
		{
			specimenList = hibernateDao.executeNamedQuery(Specimen.class.getName()+".getSpecimenByBarcode", substParams);

			if (specimenList != null && specimenList.size() > 0)
			{
				return specimenList.get(0);
			}
			else
			{
				LOGGER.error("Specimen object not found with the given barcode.");
				throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
						SpecimenErrorCodeEnum.NOT_FOUND.getCode());
			}
		}
		catch (DAOException e)
		{
			LOGGER.error("Error while retereiving Specimen object with the given barcode");
			LOGGER.error(e);
			throw new CatissueException(SpecimenErrorCodeEnum.NOT_FOUND.getDescription(),
					SpecimenErrorCodeEnum.NOT_FOUND.getCode());
		}

	}

	public Specimen getSpecimenByLabelOrBarcode(String label, String barcode, HibernateDAO hibernateDao)
	{
		Specimen specimen = null;
		if (Validator.isEmpty(barcode))
		{
			specimen = getSpecimenByLabel(label, hibernateDao);
		}
		else if (Validator.isEmpty(label))
		{
			specimen = getSpecimenByBarcode(barcode, hibernateDao);
		}
		return specimen;
	}

	public Long getSiteIdBySpecimenLabelOrId(HibernateDAO hibernateDAO, String specimenLabel, Long specimenId)
			throws DAOException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, specimenId==null?0l:specimenId));
		substParams.put("1", new NamedQueryParam(DBTypes.STRING, specimenLabel==null?"":specimenLabel));
		substParams.put("2", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));

		List siteList = hibernateDAO.executeNamedQuery(Specimen.class.getName()+".getSiteIdFromContainer", substParams);
		if (siteList != null && siteList.size() > 0)
		{
			return Long.valueOf(siteList.get(0).toString());
		}
		return null;
	}

	public void disableChildSpecimens(HibernateDAO hibernateDao, Long specimenId) throws DAOException
	{
		final String sourceObjectName = Specimen.class.getName();
		final String[] selectColumnName = {edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause
				.addCondition(new EqualClause("parentSpecimen.id", Long.valueOf(specimenId)))
				.andOpr()
				.addCondition(
						new NotEqualClause(Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS_DISABLED
								.toString()));

		List listOfSpecimenIDs = hibernateDao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		listOfSpecimenIDs = CommonUtilities.removeNull(listOfSpecimenIDs);

	}

	public String reduceQuantity(Double quantityReducedBy, Long specimenId, HibernateDAO hibernateDAO) throws DAOException
	{
		ColumnValueBean columnValueBean = new ColumnValueBean(specimenId);
		columnValueBean.setColumnName("id");
		List specimens = hibernateDAO.retrieve(Specimen.class.getName(), columnValueBean);

		if (specimens.isEmpty())
		{
			return ApplicationProperties.getValue("specimen.closed.unavailable");
		}

		Specimen specimen = (Specimen) specimens.get(0);

		if (!Constants.ACTIVITY_STATUS_ACTIVE.equalsIgnoreCase(specimen.getActivityStatus()))
		{
			return ApplicationProperties.getValue("specimen.closed.unavailable");
		}
		Double previousQuantity = specimen.getAvailableQuantity();
		Double remainingQuantity = previousQuantity.doubleValue() - quantityReducedBy;
		final DecimalFormat dFormat = new DecimalFormat("#.000");
		remainingQuantity = Double.parseDouble(dFormat.format(remainingQuantity));
		specimen.setAvailableQuantity(remainingQuantity);

		int remainingQuantityMoreThenZero = remainingQuantity.compareTo(0D);
		if (remainingQuantityMoreThenZero == -1)
		{
			List<String> parameters = new ArrayList<String>();
			parameters.add(previousQuantity.toString());
			return ApplicationProperties.getValue("requested.quantity.exceeds", parameters);
		}
		else if (remainingQuantity.compareTo(0D) == 0)
		{
			specimen.setIsAvailable(false);
		}
		else
		{
			specimen.setIsAvailable(true);
		}

		hibernateDAO.update(specimen);
		return Constants.SUCCESS;
	}

	public Specimen getSpecimenById(Long id, HibernateDAO hibernateDAO) throws DAOException
	{
		return (Specimen) hibernateDAO.retrieveById(Specimen.class.getName(), id);
	}
	
	public void populateEventWithUserId(SpecimenEventParameters specimenEventParameter, HibernateDAO hibernateDao)
			throws DAOException, BizLogicException
	{
		List userIdList = null;
		String message = "";
		if (specimenEventParameter.getUser().getId() != null)
		{
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG, specimenEventParameter.getUser().getId()));
			params.put("1", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));
			userIdList = hibernateDao.executeNamedQuery("getUserListById", params);
			message = ApplicationProperties.getValue("app.UserID");
		}
		else if (specimenEventParameter.getUser().getLoginName() != null
				|| specimenEventParameter.getUser().getLoginName().length() > 0)
		{
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.STRING, specimenEventParameter.getUser().getLoginName()));
			params.put("1", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));
			userIdList = hibernateDao.executeNamedQuery("getUserIdFromLoginName", params);
			message = ApplicationProperties.getValue("user.loginName");
		}
		if (userIdList != null && !userIdList.isEmpty())
		{
//			Object[] object = (Object[]) userIdList.get(0);
			final User user = new User();
			user.setId((Long) userIdList.get(0));
			specimenEventParameter.setUser(user);
		}
		else
		{
			LOGGER.error(ApplicationProperties.getValue("errors.item.forboformat", message));
			throw new CatissueException(ApplicationProperties.getValue("errors.item.forboformat", message),
					SpecimenErrorCodeEnum.INVALID_USER.getCode());
		}
	}
}