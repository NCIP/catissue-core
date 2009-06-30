
package edu.wustl.catissuecore.container.cache;

public class Position
{

	int position1;
	int position2;

	/**
	 * @return the position1
	 */
	public int getPosition1()
	{
		return position1;
	}

	/**
	 * @param position1 the position1 to set
	 */
	public void setPosition1(int position1)
	{
		this.position1 = position1;
	}

	/**
	 * @return the position2
	 */
	public int getPosition2()
	{
		return position2;
	}

	/**
	 * @param position2 the position2 to set
	 */
	public void setPosition2(int position2)
	{
		this.position2 = position2;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean returnValue = false;
		Position pos = (Position) obj;
		if (pos.getPosition1() == position1 && pos.getPosition2() == position2)
		{
			returnValue = true;
		}
		return returnValue;

	}

	@Override
	public int hashCode()
	{

		return 1;
	}

}
