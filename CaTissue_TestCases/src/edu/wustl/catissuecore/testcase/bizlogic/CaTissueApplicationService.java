package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.List;


public class CaTissueApplicationService 
{
	

	
//	public void setUp() throws Exception {
//		super.setUp();
//		if (appService == null)
//		{
//			appService = new CaTissueApplicationService(); //ApplicationServiceProvider.getApplicationService();
//		}
//	}
	
	public Object createObject(Object obj, SessionDataBean bean ) throws Exception
	{
		//this.setUp();
	//	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		obj = new CaCoreAppServicesDelegator().delegateAdd(bean.getUserName(), obj);
		return obj;
	}

	
	public List getParticipantMatchingObects(Object obj, SessionDataBean bean) throws Exception
	{
		//this.setUp();
		List participantList = null;
		//SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		participantList = new CaCoreAppServicesDelegator().delegateGetParticipantMatchingObects(bean.getUserName(), obj);
		return participantList;
	}

	
	public String getSpecimenCollectionGroupLabel(Object obj, SessionDataBean bean) throws ApplicationException
	{
		String label = null;
		try{
			//this.setUp();
			//SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			new CaCoreAppServicesDelegator().delegateGetSpecimenCollectionGroupLabel(bean.getUserName(),obj);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return label;
	}
	
	public Object updateObject(Object obj, SessionDataBean bean) throws Exception
	{
		//this.setUp();
		//SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		obj = new CaCoreAppServicesDelegator().delegateEdit(bean.getUserName(),obj);
		return obj;
	}


//	@Override
//	public List query(DetachedCriteria arg0, String arg1) throws ApplicationException
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
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
	
	public List search(String hql) throws Exception
	{ 
		List resultList = null;
		resultList = AppUtility.executeQuery(hql);
		return resultList;
	}
//
//	@Override
//	public List search(String arg0, List arg1) throws ApplicationException
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
}
