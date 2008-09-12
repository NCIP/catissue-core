package edu.wustl.catissuecore.flex.dag;

import edu.wustl.common.querysuite.queryobject.ICustomFormula;


public class CustomFormulaUIBean
{
	private ICustomFormula cf;
	private CustomFormulaNode twoNode;
	private SingleNodeCustomFormulaNode singleNode;
	public CustomFormulaUIBean(ICustomFormula cf,CustomFormulaNode twoNode,SingleNodeCustomFormulaNode singleNode)
	{
		this.cf =cf;
		this.singleNode = singleNode;
		this.twoNode = twoNode;
	}
	/**
	 * @return Returns the cf.
	 */
	public ICustomFormula getCf()
	{
		return cf;
	}
	
	/**
	 * @param cf The cf to set.
	 */
	public void setCf(ICustomFormula cf)
	{
		this.cf = cf;
	}
	
	/**
	 * @return Returns the singleNode.
	 */
	public SingleNodeCustomFormulaNode getSingleNode()
	{
		return singleNode;
	}
	
	/**
	 * @param singleNode The singleNode to set.
	 */
	public void setSingleNode(SingleNodeCustomFormulaNode singleNode)
	{
		this.singleNode = singleNode;
	}
	
	/**
	 * @return Returns the twoNode.
	 */
	public CustomFormulaNode getTwoNode()
	{
		return twoNode;
	}
	
	/**
	 * @param twoNode The twoNode to set.
	 */
	public void setTwoNode(CustomFormulaNode twoNode)
	{
		this.twoNode = twoNode;
	} 

}
