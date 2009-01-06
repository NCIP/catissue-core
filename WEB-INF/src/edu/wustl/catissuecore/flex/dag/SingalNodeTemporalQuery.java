package edu.wustl.catissuecore.flex.dag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
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


public class SingalNodeTemporalQuery
{
	private IDateOffsetAttribute dateOffsetAttr = null;
	private IExpressionAttribute attributeIExpression =  null;
	private AttributeInterface attributeById = null;
	private IDateOffsetLiteral lhsDateOffSetLiteral =  null;
	private IDateOffsetLiteral rhsDateOffSetLiteral = null;
	private ILiteral lhsDateLiteral = null;
	private ILiteral rhsDateLiteral =  null;
	
	private ITerm lhsTerm = null;
	private ITerm rhsTerm = null;
	private IConnector iCon = null;
	private ICustomFormula customFormula = null;
	private IExpression entityIExpression = null;
	private int entityExpressionId =  0;
	
	private ArithmeticOperator arithOp = null;
	private RelationalOperator relOp = null;

	private String attributeType = null;
	private TimeInterval rhsTimeInterval = null;
    private TimeInterval lhsTimeInterval = null;
    
    private TimeInterval qAttrInterval = null;
	private SimpleDateFormat formatter;
	
	public TimeInterval getQAttrInterval() {
		return qAttrInterval;
	}

	public void setQAttrInterval(TimeInterval attrInterval) {
		qAttrInterval = attrInterval;
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
	 * @return Returns the attributeIExpression.
	 */
	public IExpressionAttribute getAttributeIExpression()
	{
		return attributeIExpression;
	}
	
	/**
	 * @param attributeIExpression The attributeIExpression to set.
	 */
	public void setAttributeIExpression(IExpressionAttribute attributeIExpression)
	{
		this.attributeIExpression = attributeIExpression;
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
	 * @return Returns the lhsDateLiteral.
	 */
	public ILiteral getLhsDateLiteral()
	{
		return lhsDateLiteral;
	}

	
	/**
	 * @param lhsDateLiteral The lhsDateLiteral to set.
	 */
	public void setLhsDateLiteral(ILiteral lhsDateLiteral)
	{
		this.lhsDateLiteral = lhsDateLiteral;
	}

	
	/**
	 * @return Returns the rhsDateLiteral.
	 */
	public ILiteral getRhsDateLiteral()
	{
		return rhsDateLiteral;
	}

	
	/**
	 * @param rhsDateLiteral The rhsDateLiteral to set.
	 */
	public void setRhsDateLiteral(ILiteral rhsDateLiteral)
	{
		this.rhsDateLiteral = rhsDateLiteral;
	}

	/**
	 * @return Returns the dateOffsetAttr.
	 */
	public IDateOffsetAttribute getDateOffsetAttr()
	{
		return dateOffsetAttr;
	}
	
	/**
	 * @param dateOffsetAttr The dateOffsetAttr to set.
	 */
	public void setDateOffsetAttr(IDateOffsetAttribute dateOffsetAttr)
	{
		this.dateOffsetAttr = dateOffsetAttr;
	}


	
	/**
	 * @return Returns the lhsDateOffSetLiteral.
	 */
	public IDateOffsetLiteral getLhsDateOffSetLiteral()
	{
		return lhsDateOffSetLiteral;
	}

	
	/**
	 * @param lhsDateOffSetLiteral The lhsDateOffSetLiteral to set.
	 */
	public void setLhsDateOffSetLiteral(IDateOffsetLiteral lhsDateOffSetLiteral)
	{
		this.lhsDateOffSetLiteral = lhsDateOffSetLiteral;
	}

	
	/**
	 * @return Returns the rhsDateOffSetLiteral.
	 */
	public IDateOffsetLiteral getRhsDateOffSetLiteral()
	{
		return rhsDateOffSetLiteral;
	}

	
	/**
	 * @param rhsDateOffSetLiteral The rhsDateOffSetLiteral to set.
	 */
	public void setRhsDateOffSetLiteral(IDateOffsetLiteral rhsDateOffSetLiteral)
	{
		this.rhsDateOffSetLiteral = rhsDateOffSetLiteral;
	}

	/**
	 * @return Returns the entityIExpression.
	 */
	public IExpression getEntityIExpression()
	{
		return entityIExpression;
	}
	
	/**
	 * @param entityIExpression The entityIExpression to set.
	 */
	public void setEntityIExpression(IExpression entityIExpression)
	{
		this.entityIExpression = entityIExpression;
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
	 * @return Returns the lhsTimeInterval.
	 */
	public TimeInterval getLhsTimeInterval()
	{
		return lhsTimeInterval;
	}
	
	/**
	 * @param lhsTimeInterval The lhsTimeInterval to set.
	 */
	public void setLhsTimeInterval(TimeInterval lhsTimeInterval)
	{
		this.lhsTimeInterval = lhsTimeInterval;
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
	 * @return Returns the rhsTimeInterval.
	 */
	public TimeInterval getRhsTimeInterval()
	{
		return rhsTimeInterval;
	}
	
	/**
	 * @param rhsTimeInterval The rhsTimeInterval to set.
	 */
	public void setRhsTimeInterval(TimeInterval rhsTimeInterval)
	{
		this.rhsTimeInterval = rhsTimeInterval;
	}

	
	/**
	 * @return Returns the entityExpressionId.
	 */
	public int getEntityExpressionId()
	{
		return entityExpressionId;
	}

	
	/**
	 * @param entityExpressionId The entityExpressionId to set.
	 */
	public void setEntityExpressionId(int entityExpressionId)
	{
		this.entityExpressionId = entityExpressionId;
	}

	
	/**
	 * @return Returns the attributeById.
	 */
	public AttributeInterface getAttributeById()
	{
		return attributeById;
	}

	
	/**
	 * @param attributeById The attributeById to set.
	 */
	public void setAttributeById(AttributeInterface attributeById)
	{
		this.attributeById = attributeById;
	}
	
	public void createOnlyLHS()
	{
		lhsTerm = QueryObjectFactory.createTerm();
	    //Updating lhsTerm
	    if(lhsDateLiteral != null)
        {
        	//if DatePicker exists on LHS
        	lhsTerm.addOperand(lhsDateLiteral);
        	addSecondLhsOperand();
       }
        else
        {
        	//If DatePicker doesn't exists on LHS
        	lhsTerm.addOperand(lhsDateOffSetLiteral);
        	addSecondLhsOperand();
        }

		
	}
	
	public void createLHSAndRHS()
	{
		lhsTerm = QueryObjectFactory.createTerm();
	    rhsTerm = 	QueryObjectFactory.createTerm();
        
	    //Updating lhsTerm
	    if(lhsDateLiteral != null)
        {
        	//if DatePicker exists on LHS
        	lhsTerm.addOperand(lhsDateLiteral);
        	addSecondLhsOperand();
       }
        else
        {
        	//If DatePicker doesn't exists on LHS
        	lhsTerm.addOperand(lhsDateOffSetLiteral);
        	addSecondLhsOperand();
        }
	    
	    //Updating rhsTerm
	    if(rhsDateLiteral != null)
	    {
	    	//IF DatePicker on RHS
	    	rhsTerm.addOperand(rhsDateLiteral);
	    }
	    else
	    {
	    	//If No datePciker
	    	rhsTerm.addOperand(rhsDateOffSetLiteral);
	    }
	    
	}
	private void addSecondLhsOperand()
	{
    	if(attributeIExpression != null)
    	{
            //If attribute selected is of type Date
    		lhsTerm.addOperand(iCon,attributeIExpression);
    	}
    	else
    	{
    		//If attribute selected is Int type
    		lhsTerm.addOperand(iCon,dateOffsetAttr);                      		
    	}

	}
	
	/**
	 * 
	 *
	 */
	public void createExpressions()
	{
		if(attributeType.equals(Constants.DATE_TYPE))
		{
			//Means Attribute is of Date type , then it is Expression attribute
			attributeIExpression = QueryObjectFactory.createExpressionAttribute(entityIExpression,attributeById);			
		}
		else
		{
			//It will be DateOffSetLiteral
//			dateOffsetAttr = QueryObjectFactory.createDateOffsetAttribute(entityIExpression,attributeById,TimeInterval.Day);
			if(qAttrInterval !=null)
			{
				dateOffsetAttr = QueryObjectFactory.createDateOffsetAttribute(entityIExpression,attributeById,qAttrInterval);
			}
			else
			dateOffsetAttr = QueryObjectFactory.createDateOffsetAttribute(entityIExpression,attributeById,TimeInterval.Day);
		}
	}
	
	public void createRhsDateOffSetLiteral(String rhsTimeInterval)
	{
		if((!rhsTimeInterval.equals("null")))
		{
			this.rhsTimeInterval = getTimeInterval(rhsTimeInterval);
			rhsDateOffSetLiteral = QueryObjectFactory.createDateOffsetLiteral(this.rhsTimeInterval);
		}
	}
	/**
	 * 
	 * @param rhsTimeValue
	 * @param rhsTimeInterval
	 */
	public void createRightLiterals(String rhsTimeValue, String rhsTimeInterval)
	{
		if((!rhsTimeValue.equals("null")) && (!rhsTimeInterval.equals("null")))
		{
			//It  means there exists TextInput and Time Intervals on LHS, so create dateOffSetLiteral
			this.rhsTimeInterval = getTimeInterval(rhsTimeInterval);
			rhsDateOffSetLiteral = QueryObjectFactory.createDateOffsetLiteral(rhsTimeValue, this.rhsTimeInterval);
		}
		else
		{
			if((!rhsTimeValue.equals("null")) && (rhsTimeInterval.equals("null")))
			{
				//It  means there exists TextInput and Time Intervals on LHS, so create dateOffSetLiteral
				Date date=null;
				String pattern="";
				try 
				{
					//Date date = Utility.parseDate(rhsTimeValue, "MM/dd/yyyy HH:MM:SS");
					if(attributeType.equals("DateTime"))
						pattern = Variables.dateFormat+" HH:mm:ss";
					else
						pattern = Variables.dateFormat;
					
					formatter = new SimpleDateFormat(pattern);						
					date = formatter.parse(rhsTimeValue);
					rhsDateLiteral = QueryObjectFactory.createDateLiteral(new java.sql.Date(date.getTime()));
				} catch (ParseException e) 
				{
				    throw new RuntimeException("Cann't Occur");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param lhsTimeValue
	 * @param lhsTimeInterval
	 */
	public void createLeftLiterals(String lhsTimeValue, String lhsTimeInterval)
	{
		if((!lhsTimeValue.equals("null")) && (!lhsTimeInterval.equals("null")))
		{
			//It  means there exists TextInput and Time Intervals on LHS, so create dateOffSetLiteral
			this.lhsTimeInterval = getTimeInterval(lhsTimeInterval);
			lhsDateOffSetLiteral = QueryObjectFactory.createDateOffsetLiteral(lhsTimeValue, this.lhsTimeInterval);
		}
		else
		{
			if((!lhsTimeValue.equals("null")) && (lhsTimeInterval.equals("null")))
			{
				//This is the case when there exists DataPicker on LHS, so create only Date Literal
				try 
				{					
					Date date = Utility.parseDate(lhsTimeValue, "MM/dd/yyyy");
					lhsDateLiteral = QueryObjectFactory.createDateLiteral(new java.sql.Date(date.getTime()));
				} catch (ParseException e) 
				{
				    throw new RuntimeException("Cann't Occur");
				}				
			}
		}
	}
	
	/**
	 * 
	 * @param timeIntervalValue
	 * @return
	 */
	public TimeInterval getTimeInterval(String timeIntervalValue)
	{
		TimeInterval timeInterval = null;
		for(TimeInterval time: TimeInterval.values())
		{
			if(timeIntervalValue.equals(time.name() + "s"))
			{
			    timeInterval = time;
				break;
			}
		}
		return timeInterval;
	}

}
