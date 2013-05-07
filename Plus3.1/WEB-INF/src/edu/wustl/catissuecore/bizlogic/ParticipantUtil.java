
package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;

public class ParticipantUtil
{

	private static final transient Logger LOGGER = Logger.getCommonLogger(ParticipantUtil.class);

	public static LinkedHashSet<Long> getUserIdSet(final Object obj, final Long userId, DAO dao)
			throws DAOException
	{
		final Participant participant = (Participant) obj;
		long participantId = participant.getId();
		LinkedHashSet<Long> userIdSet = new LinkedHashSet<Long>();
		// added the logged in user id first
		userIdSet.add(userId);

		// then add all the PI co-ordinators ids
		userIdSet.addAll(getParticipantPICordinators(participantId, dao));
		return userIdSet;
	}

	//changed by amol
	public static Set<Long> getParticipantPICordinators(long participantId, DAO dao)
			throws DAOException
	{
		LinkedHashSet<Long> userIdSet = new LinkedHashSet<Long>();
		String hql = "select CPReg.collectionProtocol.principalInvestigator.id,"
				+ " CPReg.collectionProtocol.coordinatorCollection.id from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration "
				+ " CPReg where CPReg.participant.id= ? ";

		List<ColumnValueBean> colValueBeanList = new ArrayList<ColumnValueBean>();
		colValueBeanList.add(new ColumnValueBean(participantId, DBTypes.LONG));

		List list = dao.executeQuery(hql, colValueBeanList);
		Iterator itr = list.iterator();
		while (itr.hasNext())
		{
			for (Object object : (Object[]) itr.next())
			{
				userIdSet.add((Long) object);
			}
		}
		hql = "select CPReg.collectionProtocol.principalInvestigator.id " + " from "
				+ " edu.wustl.catissuecore.domain.CollectionProtocolRegistration "
				+ " CPReg where CPReg.participant.id= ? ";
		colValueBeanList = new ArrayList<ColumnValueBean>();
		colValueBeanList.add(new ColumnValueBean(participantId, DBTypes.LONG));
		list = dao.executeQuery(hql, colValueBeanList);
		Iterator itertor = list.iterator();
		while (itertor.hasNext())
		{
			userIdSet.add((Long) itertor.next());
		}
		return userIdSet;
	}

	public static boolean isEMPIEnable(long cpId) throws ApplicationException
	{
		JDBCDAO dao = null;
		try
		{
			boolean status = false;
			dao = AppUtility.openJDBCSession();
			String query = "SELECT CP.IS_EMPI_ENABLE FROM  CATISSUE_COLLECTION_PROTOCOL CP WHERE CP.IDENTIFIER = ?";
			LinkedList<ColumnValueBean> columnValueBeanList = new LinkedList<ColumnValueBean>();
			columnValueBeanList.add(new ColumnValueBean("IDENTIFIER", cpId, DBTypes.LONG));
			List statusList = dao.executeQuery(query, null, columnValueBeanList);
			if (isNotEmptyList(statusList))
			{
				//				status = (Boolean)(statusList.get(0));
				List idList = (List) statusList.get(0);
				if (isNotEmptyList(idList)
						&& ("1".equals(idList.get(0)) || "true".equals(idList.get(0))))
				{
					status ^= true;
				}
			}
			return status;
		}
		catch (DAOException exp)
		{
			throw new BizLogicException(exp);
		}
		finally
		{
			closeDao(dao);
		}
	}

	private static void closeDao(JDBCDAO dao) throws ApplicationException
	{
		try
		{
			AppUtility.closeJDBCSession(dao);
		}
		catch (DAOException exp)
		{
			LOGGER.warn("Error while closing dao object - " + exp.getMessage());
			LOGGER.debug("Stacktrace - ", exp);
		}
	}

	private static boolean isNotEmptyList(List givenList)
	{
		return givenList != null && !givenList.isEmpty() && !"".equals(givenList.get(0));
	}
}
