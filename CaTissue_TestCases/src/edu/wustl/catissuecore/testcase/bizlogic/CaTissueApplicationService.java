package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;


public class CaTissueApplicationService
{

	public Object createObject(Object obj) throws Exception
	{
		SessionDataBean bean = CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN;
		obj = new CaCoreAppServicesDelegator().delegateAdd(bean.getUserName(), obj);
		return obj;
	}

	public List<Participant> getParticipantMatchingObects(Object obj) throws Exception
	{
		List<Participant> participantList = null;
		SessionDataBean bean = CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN;
		participantList = new CaCoreAppServicesDelegator().delegateGetParticipantMatchingObects(obj,null,bean.getUserName());
		return participantList;
	}

	public String getSpecimenCollectionGroupLabel(Object obj) throws Exception
	{
		String label = null;
		SessionDataBean bean = CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN;
		new CaCoreAppServicesDelegator().delegateGetSpecimenCollectionGroupLabel(bean.getUserName(),obj);
		return label;
	}

	public Object updateObject(Object obj) throws Exception
	{
		SessionDataBean bean = CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN;
		obj = new CaCoreAppServicesDelegator().delegateEdit(bean.getUserName(),obj);
		return obj;
	}

	public List search(String hql) throws Exception
	{
		List resultList = null;
		resultList = AppUtility.executeQuery(hql);
		return resultList;
	}

//	@Override
	public List query(String hql) throws Exception
	{
		List resultList = null;
		resultList = AppUtility.executeQuery(hql);
		return resultList;
	}
	public List delegateSearchFilter(String userName, List list) throws Exception
	{
		return new CaCoreAppServicesDelegator().delegateSearchFilter(userName, list);
	}
	public void auditAPIQuery(String queryObject, String userName) throws Exception
	{
		new CaCoreAppServicesDelegator().auditAPIQuery(queryObject, userName);
	}
//
//	@Override
//	public List query(HQLCriteria arg0, String arg1) throws ApplicationException
//	{
//		return null;
//	}

//	@Override
//	public List query(Object arg0, int arg1, int arg2, String arg3) throws ApplicationException
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}


//	@Override
//	public List search(Class arg0, Object arg1) throws ApplicationException
//	{
//		return null;
//	}

//	@Override
//	public List search(Class arg0, List arg1) throws ApplicationException
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//

//
//	@Override
//	public List search(String arg0, List arg1) throws ApplicationException
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
}
