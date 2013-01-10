
package edu.wustl.catissuecore.scheduleImpls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
//import edu.wustl.clinportal.bizlogic.ClinicalStudyBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.scheduler.util.IHostAppUserDataRetriever;
import edu.wustl.common.scheduler.util.SchedulerDataUtility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;

public class HostAppUserDataRetrieverImpl implements IHostAppUserDataRetriever
{

	public String getUserEmail(Long userId) throws Exception
	{

		DAO dao = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getDAO();
		dao.openSession(null);
		List list = null;
		try
		{
			list = dao.executeQuery("select user.emailAddress from " + User.class.getName()
					+ " user where user.id = " + userId, null);
		}
		finally
		{
			dao.closeSession();
		}

		return list.get(0).toString();
	}

	public List<Object[]> getUserIdAndMailAddressList(Collection<Long> idList) throws Exception
	{
		// TODO Auto-generated method stub
		DAO dao = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getDAO();
		dao.openSession(null);
		List<Object[]> list = null;
		try
		{
			list = dao.executeQuery(
					"select user.emailAddress, user.id from " + User.class.getName()
							+ " user where user.id in "
							+ SchedulerDataUtility.getQueryInClauseStringFromIdList(idList), null);
		}
		finally
		{
			dao.closeSession();
		}

		return list;
	}

	public List<NameValueBean> getUserIdNameListForReport(Long id) throws Exception
	{
		DAO dao = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getDAO();
		dao.openSession(null);
		List<NameValueBean> nmList = new ArrayList<NameValueBean>();
		try
		{
			UserBizLogic userBiz = new UserBizLogic();

			if (null == id || id == 0l)
			{
				nmList = userBiz.getUsers("add");
			}
			else
			{
				CollectionProtocolBizLogic cpBiz = new CollectionProtocolBizLogic();
				nmList = cpBiz.getCPPIAndCordinators(id);
			//	User user = userBiz.getUserById(Long.valueOf(studyBiz.getPIByStudyId(id).getValue()));
				//nmList.add(new NameValueBean(user.getLastName() + "," + user.getFirstName(), user
					//	.getId()));
			}
		}
		finally
		{
			dao.closeSession();
		}

		return nmList;
	}

	public List<String> getUserNamesList(Collection<Long> idList) throws Exception
	{
		DAO dao = DAOConfigFactory.getInstance()
				.getDAOFactory(CommonServiceLocator.getInstance().getAppName()).getDAO();
		dao.openSession(null);
		List<String> nameList = new ArrayList<String>();

		try
		{
			nameList = dao.executeQuery(
					"select user.lastName ||','|| user.firstName from " + User.class.getName()
							+ " user where user.id in "
							+ SchedulerDataUtility.getQueryInClauseStringFromIdList(idList), null);
		}
		finally
		{
			dao.closeSession();

		}

		return nameList;
	}

}
