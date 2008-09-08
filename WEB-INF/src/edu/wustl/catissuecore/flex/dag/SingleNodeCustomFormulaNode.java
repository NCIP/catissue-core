package edu.wustl.catissuecore.flex.dag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class SingleNodeCustomFormulaNode implements Externalizable
{ 
	
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String nodeType = "";
	private String attributeID ="";
	private String attributeType = "";
	private String attributeName = "";
	private String entityName = "";
    private int nodeExpressionId = 0;
	private String  selectedArithmeticOp = "";
	private String  selectedLogicalOp ="";
	private String  timeValue = "";
	private String  timeInterval ="";
	private String  lhsTimeValue ="";
	private String  lhsTimeInterval = "";
	private String  operation = "";
	private String  customColumnName = "";
	
	private String customFormulaString = "";
	
	private String qAttrInterval = "";
	
	
	/**
	 * @return Returns the customFormulaString.
	 */
	public String getQAttrInterval()
	{
		return qAttrInterval;
	}

	
	/**
	 * @param customFormulaString The customFormulaString to set.
	 */
	public void setQAttrInterval(String qAttrInterval)
	{
		this.qAttrInterval = qAttrInterval;
	}
	
	/**
	 * @return Returns the customFormulaString.
	 */
	public String getCustomFormulaString()
	{
		return customFormulaString;
	}


	
	/**
	 * @param customFormulaString The customFormulaString to set.
	 */
	public void setCustomFormulaString(String customFormulaString)
	{
		this.customFormulaString = customFormulaString;
	}


	/**
	 * @return Returns the attributeID.
	 */
	public String getAttributeID()
	{
		return attributeID;
	}

	
	/**
	 * @param attributeID The attributeID to set.
	 */
	public void setAttributeID(String attributeID)
	{
		this.attributeID = attributeID;
	}

	
	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName()
	{
		return attributeName;
	}

	
	/**
	 * @param attributeName The attributeName to set.
	 */
	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	
	/**
	 * @return Returns the attributeType.
	 */
	public String getAttributeType()
	{
		return attributeType;
	}

	
	/**
	 * @param attributeType The attributeType to set.
	 */
	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	
	/**
	 * @return Returns the customColumnName.
	 */
	public String getCustomColumnName()
	{
		return customColumnName;
	}

	
	/**
	 * @param customColumnName The customColumnName to set.
	 */
	public void setCustomColumnName(String customColumnName)
	{
		this.customColumnName = customColumnName;
	}

	
	/**
	 * @return Returns the entityName.
	 */
	public String getEntityName()
	{
		return entityName;
	}

	
	/**
	 * @param entityName The entityName to set.
	 */
	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	
	/**
	 * @return Returns the lhsTimeInterval.
	 */
	public String getLhsTimeInterval()
	{
		return lhsTimeInterval;
	}

	
	/**
	 * @param lhsTimeInterval The lhsTimeInterval to set.
	 */
	public void setLhsTimeInterval(String lhsTimeInterval)
	{
		this.lhsTimeInterval = lhsTimeInterval;
	}

	
	/**
	 * @return Returns the lhsTimeValue.
	 */
	public String getLhsTimeValue()
	{
		return lhsTimeValue;
	}

	
	/**
	 * @param lhsTimeValue The lhsTimeValue to set.
	 */
	public void setLhsTimeValue(String lhsTimeValue)
	{
		this.lhsTimeValue = lhsTimeValue;
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
	 * @return Returns the nodeExpressionId.
	 */
	public int getNodeExpressionId()
	{
		return nodeExpressionId;
	}

	
	/**
	 * @param nodeExpressionId The nodeExpressionId to set.
	 */
	public void setNodeExpressionId(int nodeExpressionId)
	{
		this.nodeExpressionId = nodeExpressionId;
	}

	
	/**
	 * @return Returns the nodeType.
	 */
	public String getNodeType()
	{
		return nodeType;
	}

	
	/**
	 * @param nodeType The nodeType to set.
	 */
	public void setNodeType(String nodeType)
	{
		this.nodeType = nodeType;
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

	public void writeExternal(ObjectOutput out) throws IOException 
	{
    	  out.writeUTF(this.name);
       	  out.writeUTF(nodeType);
       	  out.writeUTF(attributeID);
       	  out.writeUTF(attributeType);
       	  out.writeUTF(attributeName);
       	  out.writeUTF(entityName);
       	  out.writeInt(nodeExpressionId);
       	  out.writeUTF(selectedArithmeticOp); 
       	  out.writeUTF(selectedLogicalOp);
       	  out.writeUTF(timeValue);
       	  out.writeUTF(timeInterval);
       	  out.writeUTF(lhsTimeValue);
       	  out.writeUTF(lhsTimeInterval);
       	  out.writeUTF(operation);
       	  out.writeUTF(customColumnName);
       	  out.writeUTF(customFormulaString);
       	  out.writeUTF(qAttrInterval);

	}
	
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException 
	{
			this.name = input.readUTF();
  			nodeType = input.readUTF();
  			attributeID = input.readUTF();
  			attributeType = input.readUTF();
  			attributeName = input.readUTF();
  			entityName = input.readUTF();
  			nodeExpressionId = input.readInt();
  			selectedArithmeticOp = input.readUTF();
  			selectedLogicalOp = input.readUTF();
  			timeValue = input.readUTF();
  			timeInterval = input.readUTF();
  			lhsTimeValue = input.readUTF();
  			lhsTimeInterval = input.readUTF();
  			operation = input.readUTF();
  			customColumnName = input.readUTF();
  			customFormulaString = input.readUTF();
  			qAttrInterval = input.readUTF();
	}

}
