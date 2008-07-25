package edu.wustl.catissuecore.flex.dag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class CustomFormulaNode implements Externalizable
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	private String name = "";
	private String firstNodeName =  "";
	private String secondNodeName = "";
	
	private int firstNodeExpId = 0;
	private int secondNodeExpId = 0;
	
	private String firstSelectedAttrName = "";
	private String secondSelectedAttrName =  "";
	
	private String firstSelectedAttrId = "";
	private String secondSelectedAttrId =  "";
	
	private String firstSelectedAttrType = "";
	private String secondSelectedAttrType = "";
	
	private String selectedArithmeticOp = "";
	private String selectedLogicalOp = "";
		
	private String timeValue = "";
	private String timeInterval = "";
	
	private String operation = "";
	
	private int x;
	private int y;
	
	
	
	
	/**
	 * @return Returns the x.
	 */
	public int getX()
	{
		return x;
	}


	
	/**
	 * @param x The x to set.
	 */
	public void setX(int x)
	{
		this.x = x;
	}


	
	/**
	 * @return Returns the y.
	 */
	public int getY()
	{
		return y;
	}


	
	/**
	 * @param y The y to set.
	 */
	public void setY(int y)
	{
		this.y = y;
	}


	/**
	 * @return Returns the firstNodeExpId.
	 */
	public int getFirstNodeExpId()
	{
		return firstNodeExpId;
	}

	
	/**
	 * @param firstNodeExpId The firstNodeExpId to set.
	 */
	public void setFirstNodeExpId(int firstNodeExpId)
	{
		this.firstNodeExpId = firstNodeExpId;
	}

	
	/**
	 * @return Returns the firstNodeName.
	 */
	public String getFirstNodeName()
	{
		return firstNodeName;
	}

	
	/**
	 * @param firstNodeName The firstNodeName to set.
	 */
	public void setFirstNodeName(String firstNodeName)
	{
		this.firstNodeName = firstNodeName;
	}

	
	/**
	 * @return Returns the firstSelectedAttrId.
	 */
	public String getFirstSelectedAttrId()
	{
		return firstSelectedAttrId;
	}

	
	/**
	 * @param firstSelectedAttrId The firstSelectedAttrId to set.
	 */
	public void setFirstSelectedAttrId(String firstSelectedAttrId)
	{
		this.firstSelectedAttrId = firstSelectedAttrId;
	}

	
	/**
	 * @return Returns the firstSelectedAttrName.
	 */
	public String getFirstSelectedAttrName()
	{
		return firstSelectedAttrName;
	}

	
	/**
	 * @param firstSelectedAttrName The firstSelectedAttrName to set.
	 */
	public void setFirstSelectedAttrName(String firstSelectedAttrName)
	{
		this.firstSelectedAttrName = firstSelectedAttrName;
	}

	
	/**
	 * @return Returns the firstSelectedAttrType.
	 */
	public String getFirstSelectedAttrType()
	{
		return firstSelectedAttrType;
	}

	
	/**
	 * @param firstSelectedAttrType The firstSelectedAttrType to set.
	 */
	public void setFirstSelectedAttrType(String firstSelectedAttrType)
	{
		this.firstSelectedAttrType = firstSelectedAttrType;
	}

	
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	
	/**
	 * @return Returns the secondNodeExpId.
	 */
	public int getSecondNodeExpId()
	{
		return secondNodeExpId;
	}

	
	/**
	 * @param secondNodeExpId The secondNodeExpId to set.
	 */
	public void setSecondNodeExpId(int secondNodeExpId)
	{
		this.secondNodeExpId = secondNodeExpId;
	}

	
	/**
	 * @return Returns the secondNodeName.
	 */
	public String getSecondNodeName()
	{
		return secondNodeName;
	}

	
	/**
	 * @param secondNodeName The secondNodeName to set.
	 */
	public void setSecondNodeName(String secondNodeName)
	{
		this.secondNodeName = secondNodeName;
	}

	
	/**
	 * @return Returns the secondSelectedAttrId.
	 */
	public String getSecondSelectedAttrId()
	{
		return secondSelectedAttrId;
	}

	
	/**
	 * @param secondSelectedAttrId The secondSelectedAttrId to set.
	 */
	public void setSecondSelectedAttrId(String secondSelectedAttrId)
	{
		this.secondSelectedAttrId = secondSelectedAttrId;
	}

	
	/**
	 * @return Returns the secondSelectedAttrName.
	 */
	public String getSecondSelectedAttrName()
	{
		return secondSelectedAttrName;
	}

	
	/**
	 * @param secondSelectedAttrName The secondSelectedAttrName to set.
	 */
	public void setSecondSelectedAttrName(String secondSelectedAttrName)
	{
		this.secondSelectedAttrName = secondSelectedAttrName;
	}

	
	/**
	 * @return Returns the secondSelectedAttrType.
	 */
	public String getSecondSelectedAttrType()
	{
		return secondSelectedAttrType;
	}

	
	/**
	 * @param secondSelectedAttrType The secondSelectedAttrType to set.
	 */
	public void setSecondSelectedAttrType(String secondSelectedAttrType)
	{
		this.secondSelectedAttrType = secondSelectedAttrType;
	}

	
	/**
	 * @return Returns the selectedArithmeticOp.
	 */
	public String getSelectedArithmeticOp()
	{
		return selectedArithmeticOp;
	}

	
	/**
	 * @param selectedArithmeticOp The selectedArithmeticOp to set.
	 */
	public void setSelectedArithmeticOp(String selectedArithmeticOp)
	{
		this.selectedArithmeticOp = selectedArithmeticOp;
	}

	
	/**
	 * @return Returns the selectedLogicalOp.
	 */
	public String getSelectedLogicalOp()
	{
		return selectedLogicalOp;
	}

	
	/**
	 * @param selectedLogicalOp The selectedLogicalOp to set.
	 */
	public void setSelectedLogicalOp(String selectedLogicalOp)
	{
		this.selectedLogicalOp = selectedLogicalOp;
	}

	
	/**
	 * @return Returns the timeInterval.
	 */
	public String getTimeInterval()
	{
		return timeInterval;
	}

	
	/**
	 * @param timeInterval The timeInterval to set.
	 */
	public void setTimeInterval(String timeInterval)
	{
		this.timeInterval = timeInterval;
	}

	
	/**
	 * @return Returns the timeValue.
	 */
	public String getTimeValue()
	{
		return timeValue;
	}

	
	/**
	 * @param timeValue The timeValue to set.
	 */
	public void setTimeValue(String timeValue)
	{
		this.timeValue = timeValue;
	}

	/**
	 * Reading searilized data
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
	{
		name = in.readUTF();
		firstNodeName = in.readUTF();
		secondNodeName = in.readUTF();
		firstNodeExpId = in.readInt();
		secondNodeExpId = in.readInt();
		firstSelectedAttrName = in.readUTF();
		secondSelectedAttrName = in.readUTF();
		firstSelectedAttrId = in.readUTF();
		secondSelectedAttrId = in.readUTF();
		firstSelectedAttrType = in.readUTF();
		secondSelectedAttrType = in.readUTF();
		selectedArithmeticOp = in.readUTF();
		selectedLogicalOp = in.readUTF();
		timeValue = in.readUTF();
		timeInterval = in.readUTF();
		operation = in.readUTF();
	    x = in.readInt();
	    y = in.readInt();
	}
	
	/**
	 * Writing seraialized Id
	 */
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		out.writeUTF(name);
		out.writeUTF(firstNodeName);
		out.writeUTF(secondNodeName);
		out.writeInt(firstNodeExpId);
		out.writeInt(secondNodeExpId);
		out.writeUTF(firstSelectedAttrName);
		out.writeUTF(secondSelectedAttrName);
		out.writeUTF(firstSelectedAttrId);
		out.writeUTF(secondSelectedAttrId);
		out.writeUTF(firstSelectedAttrType);
		out.writeUTF(secondSelectedAttrType);
		out.writeUTF(selectedArithmeticOp);
		out.writeUTF(selectedLogicalOp);
		out.writeUTF(timeValue);
		out.writeUTF(timeInterval);
		out.writeUTF(operation);
		out.writeInt(x);
		out.writeInt(y);
	}



	
	/**
	 * @return Returns the operation.
	 */
	public String getOperation()
	{
		return operation;
	}



	
	/**
	 * @param operation The operation to set.
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	

}
