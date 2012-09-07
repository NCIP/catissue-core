package edu.wustl.catissuecore.cpSync;

import edu.wustl.catissuecore.bizlogic.SynchronizeCollectionProtocolBizLogic;
import edu.wustl.common.exception.BizLogicException;

public class MyWork implements Runnable
{
    String name;
 
    public MyWork(String name)
    {
        this.name = name;
    }
 
    @Override
    public void run()
    {
 
        try
        {
            SynchronizeCollectionProtocolBizLogic bizLogic = new SynchronizeCollectionProtocolBizLogic();
            bizLogic.synchronizeCP(name, null);
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

