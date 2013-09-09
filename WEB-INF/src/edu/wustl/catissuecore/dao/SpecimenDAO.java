
package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.dto.BiohazardDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

/**
 * @author rinku
 *
 */
public class SpecimenDAO
{

	/**Get the specimen label and id for given scgId.
	 * @param scgId
	 * @return
	 * @throws DAOException
	 */
	public List<NameValueBean> getSpecimenLableAndId(HibernateDAO hibernateDAO, Long scgId) throws DAOException
	{

		List<NameValueBean> nvBeanList = new ArrayList<NameValueBean>();
		try
		{
			Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG, scgId));
			
			List specimens = hibernateDAO.executeNamedQuery("getSpecimenIdLabelPairForScg", params);

			for (Object specimen : specimens)
			{
				Object[] sp = (Object[]) specimen;
				NameValueBean nvb = new NameValueBean();
				nvb.setName((String) sp[1]);
				nvb.setValue((Long) sp[0]);
				nvBeanList.add(nvb);
			}
		}
		catch (DAOException daoException)
		{
			daoException.printStackTrace();
			throw daoException;
		}
		return nvBeanList;
	}
	public static Long getcpId(Long specimenId, HibernateDAO dao)
			throws ApplicationException, DAOException
	{

		List<Object> list = null;
		Long cpID = 0l;
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
			substParams.put("0",new NamedQueryParam(DBTypes.LONG, specimenId));
			list = dao.executeNamedQuery("getCPID", substParams);
			if(list != null && list.size()>0)
			{
				cpID = Long.valueOf(list.get(0).toString());
			}
		return cpID;
	}

	public static Long getAssociatedIdentifiedReportId(Long specimenId, HibernateDAO dao)
			throws ApplicationException
	{
		Long reportId = -1l;
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0",new NamedQueryParam(DBTypes.LONG, specimenId));
		final List reportIDList = dao.executeNamedQuery("getAssociatedReportId", substParams);
		if (reportIDList != null && !reportIDList.isEmpty())
		{
			reportId = ((Long) reportIDList.get(0));
		}
		else if (AppUtility.isQuarantined(reportId))
		{
			reportId = -2l;
		}
		return reportId;
	}
	
	public static ArrayList<BiohazardDTO> getBiohazardList(DAO dao) throws DAOException
	{
		List<Biohazard> biohazardList = dao.retrieve(Biohazard.class.getName());

		ArrayList<BiohazardDTO> biohazardTypeNameList = new ArrayList<BiohazardDTO>();
		for (Biohazard biohazard : biohazardList)
		{
			BiohazardDTO biohazardDTO = new BiohazardDTO();
			biohazardDTO.setId(biohazard.getId());
			biohazardDTO.setName(biohazard.getName());
			biohazardDTO.setType(biohazard.getType());

			biohazardTypeNameList.add(biohazardDTO);
		}
		return biohazardTypeNameList;
	}

}
