package edu.wustl.catissuecore.flex.dag;

import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;


public class CustomFormulaUIBean
{
	private ICustomFormula cf;
	private CustomFormulaNode twoNode;
	private SingleNodeCustomFormulaNode singleNode;
	private boolean isCalculatedResult = false;
	private IOutputTerm outputTerm ;
	
	/**
	 * @return Returns the isCalculatedResult.
	 */
	public boolean isCalculatedResult()
	{
		return isCalculatedResult;
	}
	
	/**
	 * @param isCalculatedResult The isCalculatedResult to set.
	 */
	public void setCalculatedResult(boolean isCalculatedResult)
	{
		this.isCalculatedResult = isCalculatedResult;
	}
	
	/**
	 * @return Returns the outputTerm.
	 */
	public IOutputTerm getOutputTerm()
	{
		return outputTerm;
	}
	
	/**
	 * @param outputTerm The outputTerm to set.
	 */
	public void setOutputTerm(IOutputTerm outputTerm)
	{
		this.outputTerm = outputTerm;
	}
	public CustomFormulaUIBean(ICustomFormula cf,CustomFormulaNode twoNode,SingleNodeCustomFormulaNode singleNode, IOutputTerm outputTerm)
	{
		this.cf =cf;
		this.singleNode = singleNode;
		this.twoNode = twoNode;
		this.outputTerm = outputTerm; 
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
