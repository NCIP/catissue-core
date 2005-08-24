package edu.wustl.catissuecore.query;

import java.util.Vector;
 



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SimpleConditionsImpl extends ConditionsImpl {

	/**
	 * Vector of SimpleConditionsNode objects 
	 * This forms the storage of conditions in Simple Query Interface
	 */
    private Vector whereConditions = new Vector();
	

	public SimpleConditionsImpl(){

	}

	/**
	 * Adds condition to whereConditions Vector 
	 * @param condition
	 * @return true (as per the general contract of Collection.add).
	 */
	public boolean addCondition(SimpleConditionsNode condition){
		return whereConditions.add(condition);
	}
	
	/**
	 * 
	 * @return
	 */
	public SimpleConditionsNode getCondition(){
		return null;
	}

	public boolean editCondition(){
		return false;
	}

    /* (non-Javadoc)
     * @see edu.wustl.caTISSUECore.query.ConditionsImpl#getString()
     */
    public String getString(int tableSufix)
    {
        StringBuffer whereConditionsString = new StringBuffer();
        for(int i=0; i < whereConditions.size(); i++)
        {
            whereConditionsString.append(" ");
            if(i != whereConditions.size()-1)
                whereConditionsString.append(((SimpleConditionsNode) whereConditions.get(i)).toSQLString(tableSufix));
            else
                whereConditionsString.append(((SimpleConditionsNode)whereConditions.get(i)).getCondition().toSQLString(tableSufix));
            whereConditionsString.append(" ");
        }

        return whereConditionsString.toString();
    }

//    public HashSet getConditionObjects()
//    {
//        HashSet set = new HashSet();
//        
//        /**
//         * For all elements in whereConditions add  
//         * objects to the set
//         */ 
//        for(int i=0; i<whereConditions.size();i++)
//        {
//            SimpleConditionsNode conditionsNode = (SimpleConditionsNode)whereConditions.get(i);
//            set.add(conditionsNode.getCondition().getDataElement().getTable());
//        }
//        return set;
//    }
    
    /**
     * Inserts the specified condition at the specified position in whereConditions Vector. Shifts the element currently at 
     * that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param position index at which the specified element is to be inserted.
     * @param condition condition to be inserted
     */
    public boolean addCondition(int position, SimpleConditionsNode condition)
    {
        try
        {
         whereConditions.add(position,condition);
         return true;
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            return false;
        }
        
    }

}