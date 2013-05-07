package edu.wustl.catissuecore.util;


public class Position
{
	int xPos;
	int yPos;

	
	public int getXPos()
	{
		return xPos;
	}
	
	public void setXPos(int pos)
	{
		xPos = pos;
	}
	
	public int getYPos()
	{
		return yPos;
	}
	
	public void setYPos(int pos)
	{
		yPos = pos;
	}
	@Override
	public boolean equals(Object obj)
	{
		boolean returnValue = false;
		Position pos = (Position)obj;
		if(pos.getXPos() == xPos && pos.getYPos()== yPos)
		{
			returnValue=true;
		}
		return returnValue;
		
	}
	@Override
	public int hashCode()
	{
		
		return 1;
	}

}
