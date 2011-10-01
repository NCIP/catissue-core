package edu.wustl.catissuecore.uiobject;

import edu.wustl.common.domain.UIObject;


public class ShipmentRequestUIObject implements UIObject
{
	boolean requestProcessed=false;


	public boolean isRequestProcessed()
	{
		return requestProcessed;
	}


	public void setRequestProcessed(boolean requestProcessed)
	{
		this.requestProcessed = requestProcessed;
	}



}
