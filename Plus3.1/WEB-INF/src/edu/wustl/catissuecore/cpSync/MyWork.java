package edu.wustl.catissuecore.cpSync;

import edu.wustl.catissuecore.bizlogic.SynchronizeCollectionProtocolBizLogic;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;

public class MyWork implements Runnable
{
    String name;
    SessionDataBean sessionDataBean;
    public MyWork(String name,SessionDataBean sessionDataBean)
    {
        this.name = name;
        this.sessionDataBean=sessionDataBean;
    }
 
    @Override
    public void run()
    {
 
        try
        {
            SynchronizeCollectionProtocolBizLogic bizLogic = new SynchronizeCollectionProtocolBizLogic();
            bizLogic.synchronizeCP(name, sessionDataBean);
        }
        catch (BizLogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    }
    
 
    @Override
    public String toString()
    {
        return (this.name);
    }
}

