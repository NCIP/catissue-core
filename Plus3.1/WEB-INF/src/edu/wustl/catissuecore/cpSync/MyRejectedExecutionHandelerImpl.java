package edu.wustl.catissuecore.cpSync;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejectedExecutionHandelerImpl
implements RejectedExecutionHandler
{
    @Override
    public void rejectedExecution(Runnable runnable,
            ThreadPoolExecutor executor)
    {
        System.out.println(runnable.toString() + " : I've been rejected ! ");
    }
}

