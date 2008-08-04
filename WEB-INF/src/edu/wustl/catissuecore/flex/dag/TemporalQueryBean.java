package edu.wustl.catissuecore.flex.dag;

import java.text.ParseException;
import java.util.Date;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IConnector;
import edu.wustl.common.querysuite.queryobject.ICustomFormula;
import edu.wustl.common.querysuite.queryobject.IDateOffsetAttribute;
import edu.wustl.common.querysuite.queryobject.IDateOffsetLiteral;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionAttribute;
import edu.wustl.common.querysuite.queryobject.ILiteral;
import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.util.Utility;

public class TemporalQueryBean
{
	private IDateOffsetAttribute dateOffsetAttr1 = null;
	private IDateOffsetAttribute dateOffsetAttr2 = null;
	private IExpressionAttribute IExpression1 =  null;
	private IExpressionAttribute IExpression2 = null;
	private IDateOffsetLiteral dateOffSetLiteral =  null;
	private ILiteral dateLiteral = null;
	private ITerm lhsTerm = null;
	private ITerm rhsTerm = null;
	private IConnector iCon = null;
	private ICustomFormula customFormula = null;
	private IExpression srcIExpression = null;
	private IExpression destIExpression = null;
	
	private ArithmeticOperator arithOp = null;
	private RelationalOperator relOp = null;
	private int srcExpressionId = 0;
	private int destExpressionId = 0;
	
	private AttributeInterface srcAttributeById = null;
	private AttributeInterface destAttributeById = null;
	
	private String firstAttributeType = null;
	private String secondAttributeType = null; 
	
	private TimeInterval timeInterval = null;
	
	//private String timeIntervalValue = null;
	//private String timeValue = null; 
	
	/**
	 * @return Returns the firstAttributeType.
	 */
	public String getFirstAttributeType()
	{
		return firstAttributeType;
	}


	
	/**
	 * @param firstAttributeType The firstAttributeType to set.
	 */
	public void setFirstAttributeType(String firstAttributeType)
	{
		this.firstAttributeType = firstAttributeType;
	}


	
	/**
	 * @return Returns the secondAttributeType.
	 */
	public String getSecondAttributeType()
	{
		return secondAttributeType;
	}


	
	/**
	 * @param secondAttributeType The secondAttributeType to set.
	 */
	public void setSecondAttributeType(String secondAttributeType)
	{
		this.secondAttributeType = secondAttributeType;
	}


	/**
	 * @return Returns the destAttributeById.
	 */
	public AttributeInterface getDestAttributeById()
	{
		return destAttributeById;
	}

	
	/**
	 * @param destAttributeById The destAttributeById to set.
	 */
	public void setDestAttributeById(AttributeInterface destAttributeById)
	{
		this.destAttributeById = destAttributeById;
	}

	
	/**
	 * @return Returns the srcAttributeById.
	 */
	public AttributeInterface getSrcAttributeById()
	{
		return srcAttributeById;
	}

	
	/**
	 * @param srcAttributeById The srcAttributeById to set.
	 */
	public void setSrcAttributeById(AttributeInterface srcAttributeById)
	{
		this.srcAttributeById = srcAttributeById;
	}

	/**
	 * @return Returns the arithOp.
	 */
	public ArithmeticOperator getArithOp()
	{
		return arithOp;
	}
	
	/**
	 * @param arithOp The arithOp to set.
	 */
	public void setArithOp(ArithmeticOperator arithOp)
	{
		this.arithOp = arithOp;
	}
	
	/**
	 * @return Returns the customFormula.
	 */
	public ICustomFormula getCustomFormula()
	{
		return customFormula;
	}
	
	/**
	 * @param customFormula The customFormula to set.
	 */
	public void setCustomFormula(ICustomFormula customFormula)
	{
		this.customFormula = customFormula;
	}
	
	/**
	 * @return Returns the dateLiteral.
	 */
	public ILiteral getDateLiteral()
	{
		return dateLiteral;
	}
	
	/**
	 * @param dateLiteral The dateLiteral to set.
	 */
	public void setDateLiteral(ILiteral dateLiteral)
	{
		this.dateLiteral = dateLiteral;
	}
	
	/**
	 * @return Returns the dateOffsetAttr1.
	 */
	public IDateOffsetAttribute getDateOffsetAttr1()
	{
		return dateOffsetAttr1;
	}
	
	/**
	 * @param dateOffsetAttr1 The dateOffsetAttr1 to set.
	 */
	public void setDateOffsetAttr1(IDateOffsetAttribute dateOffsetAttr1)
	{
		this.dateOffsetAttr1 = dateOffsetAttr1;
	}
	
	/**
	 * @return Returns the dateOffsetAttr2.
	 */
	public IDateOffsetAttribute getDateOffsetAttr2()
	{
		return dateOffsetAttr2;
	}
	
	/**
	 * @param dateOffsetAttr2 The dateOffsetAttr2 to set.
	 */
	public void setDateOffsetAttr2(IDateOffsetAttribute dateOffsetAttr2)
	{
		this.dateOffsetAttr2 = dateOffsetAttr2;
	}
	
	/**
	 * @return Returns the dateOffSetLiteral.
	 */
	public IDateOffsetLiteral getDateOffSetLiteral()
	{
		return dateOffSetLiteral;
	}
	
	/**
	 * @param dateOffSetLiteral The dateOffSetLiteral to set.
	 */
	public void setDateOffSetLiteral(IDateOffsetLiteral dateOffSetLiteral)
	{
		this.dateOffSetLiteral = dateOffSetLiteral;
	}
	
	/**
	 * @return Returns the destExpressionId.
	 */
	public int getDestExpressionId()
	{
		return destExpressionId;
	}
	
	/**
	 * @param destExpressionId The destExpressionId to set.
	 */
	public void setDestExpressionId(int destExpressionId)
	{
		this.destExpressionId = destExpressionId;
	}
	
	/**
	 * @return Returns the iCon.
	 */
	public IConnector getICon()
	{
		return iCon;
	}
	
	/**
	 * @param con The iCon to set.
	 */
	public void setICon(IConnector con)
	{
		iCon = con;
	}
	
	/**
	 * @return Returns the iExpression1.
	 */
	public IExpressionAttribute getIExpression1()
	{
		return IExpression1;
	}
	
	/**
	 * @param expression1 The iExpression1 to set.
	 */
	public void setIExpression1(IExpressionAttribute expression1)
	{
		IExpression1 = expression1;
	}
	
	/**
	 * @return Returns the iExpression2.
	 */
	public IExpressionAttribute getIExpression2()
	{
		return IExpression2;
	}
	
	/**
	 * @param expression2 The iExpression2 to set.
	 */
	public void setIExpression2(IExpressionAttribute expression2)
	{
		IExpression2 = expression2;
	}
	
	/**
	 * @return Returns the lhsTerm.
	 */
	public ITerm getLhsTerm()
	{
		return lhsTerm;
	}
	
	/**
	 * @param lhsTerm The lhsTerm to set.
	 */
	public void setLhsTerm(ITerm lhsTerm)
	{
		this.lhsTerm = lhsTerm;
	}
	
	/**
	 * @return Returns the relOp.
	 */
	public RelationalOperator getRelOp()
	{
		return relOp;
	}
	
	/**
	 * @param relOp The relOp to set.
	 */
	public void setRelOp(RelationalOperator relOp)
	{
		this.relOp = relOp;
	}
	
	/**
	 * @return Returns the rhsTerm.
	 */
	public ITerm getRhsTerm()
	{
		return rhsTerm;
	}
	
	/**
	 * @param rhsTerm The rhsTerm to set.
	 */
	public void setRhsTerm(ITerm rhsTerm)
	{
		this.rhsTerm = rhsTerm;
	}
	
	/**
	 * @return Returns the srcExpressionId.
	 */
	public int getSrcExpressionId()
	{
		return srcExpressionId;
	}
	
	/**
	 * @param srcExpressionId The srcExpressionId to set.
	 */
	public void setSrcExpressionId(int srcExpressionId)
	{
		this.srcExpressionId = srcExpressionId;
	}
	
	/**
	 * @return Returns the srcIExpression.
	 */
	public IExpression getSrcIExpression()
	{
		return srcIExpression;
	}
	
	/**
	 * @param srcIExpression The srcIExpression to set.
	 */
	public void setSrcIExpression(IExpression srcIExpression)
	{
		this.srcIExpression = srcIExpression;
	}
	
	public void createExpressions()
	{
		//If Both attributes have type 
		if(firstAttributeType.equals(secondAttributeType))
		{
			IExpression1 = QueryObjectFactory.createExpressionAttribute(srcIExpression,srcAttributeById);
			IExpression2 = QueryObjectFactory.createExpressionAttribute(destIExpression,destAttributeById);
		}
		else
		{
			//If The attribute type is of type Date
			if(firstAttributeType.equals(Constants.DATE_TYPE))
			{
				IExpression1 = QueryObjectFactory.createExpressionAttribute(srcIExpression,srcAttributeById);
				dateOffsetAttr2 = QueryObjectFactory.createDateOffsetAttribute(destIExpression,destAttributeById,TimeInterval.Day);
			}
			else
			{
				IExpression2 = QueryObjectFactory.createExpressionAttribute(destIExpression,destAttributeById);
				dateOffsetAttr1 = QueryObjectFactory.createDateOffsetAttribute(srcIExpression,srcAttributeById,TimeInterval.Day);
			}
		}
	}
	
	/**
	 * This method will create only lhsTERM
	 * @param iCon
	 */
	public void createOnlyLHS()
	{
		lhsTerm = QueryObjectFactory.createTerm();
		if(IExpression1 != null && IExpression2 != null)
		{
			lhsTerm.addOperand(IExpression1);
		    lhsTerm.addOperand(iCon,IExpression2);
		}
		else
		{
			if(IExpression1 != null && dateOffsetAttr2 != null)
			{
				 lhsTerm.addOperand(IExpression1);
				 lhsTerm.addOperand(iCon,dateOffsetAttr2);
			}
			else
			{
		        lhsTerm.addOperand(IExpression2);
		        lhsTerm.addOperand(iCon,dateOffsetAttr1);
			}
		}
	}
	
	/**
	 * This method creates either date Literal or dateOffset Literal depending on the time Interval values 
	 * @param timeIntervalValue
	 * @param timeValue
	 */
	public void createLiterals(String timeIntervalValue, String timeValue)
	{
		if((!timeIntervalValue.equals("null")) && (!timeValue.equals("null")))
		{
			//Creating the dateOffSet Literal 
			setTimeInterval(timeIntervalValue);
   	 	    dateOffSetLiteral = QueryObjectFactory.createDateOffsetLiteral(timeValue, timeInterval);
		}
		else
		{
			if(!timeValue.equals("null") )
			{
				try 
				{
					Date date = Utility.parseDate(timeValue, "MM/dd/yyyy");
					dateLiteral = QueryObjectFactory.createDateLiteral(new java.sql.Date(date.getTime()));
				} catch (ParseException e) 
				{
				    throw new RuntimeException("Cann't Occur");
				}
			}
		}
	}

	private void setTimeInterval(String timeIntervalValue)
	{
		for(TimeInterval time: TimeInterval.values())
		{
			if(timeIntervalValue.equals(time.name() + "s"))
			{
				timeInterval = time;
				break;
			}
		}
	}
    
	
	public void createLHSAndRHS()
	{
		lhsTerm = QueryObjectFactory.createTerm();
	    rhsTerm = 	QueryObjectFactory.createTerm();
	    if(IExpression1 != null && IExpression2 != null)
		{
			lhsTerm.addOperand(IExpression1);
		    lhsTerm.addOperand(iCon,IExpression2);
		    rhsTerm.addOperand(dateOffSetLiteral);
		}
	    else
	    {
	    	if(IExpression1 != null && dateOffsetAttr2 != null)
			{
				lhsTerm.addOperand(IExpression1);
			    lhsTerm.addOperand(iCon,dateOffsetAttr2);

			    rhsTerm.addOperand(dateLiteral);
			    
			}
			else
			{
				lhsTerm.addOperand(dateOffsetAttr1);
				lhsTerm.addOperand(iCon,IExpression2);
				
			    rhsTerm.addOperand(dateLiteral);
				
			}
	    }
	}
	
	
	/**
	 * @return Returns the timeInterval.
	 */
	public TimeInterval getTimeInterval()
	{
		return timeInterval;
	}



	
	/**
	 * @param timeInterval The timeInterval to set.
	 */
	public void setTimeInterval(TimeInterval timeInterval)
	{
		this.timeInterval = timeInterval;
	}



	
	/**
	 * @return Returns the destIExpression.
	 */
	public IExpression getDestIExpression()
	{
		return destIExpression;
	}



	
	/**
	 * @param destIExpression The destIExpression to set.
	 */
	public void setDestIExpression(IExpression destIExpression)
	{
		this.destIExpression = destIExpression;
	}



	
	
	
}
