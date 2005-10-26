/**
 * <p>Title: AdvanceQueryInterfaceAction Class>
 * <p>Description:	This class constructs the name-value bean of operators as per the given datatype.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 25, 2005
 */

package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wustl.catissuecore.query.Operator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author aniruddha_phadnis
 */
public class SearchUtil
{
	/**
     * Constant for datatype DATE
     */
    public static final int DATATYPE_DATE = 1;
    
    /**
     * Constant for datatype NUMERIC
     */
    public static final int DATATYPE_NUMERIC = 2;
    
    private HashMap map = new HashMap();
    
    /**
     * Constant for datatype STRING
     */
    public static final int DATATYPE_STRING = 3;
    
    /**
     * Constant for datatype STRING
     */
    public static final int DATATYPE_ENUMERATED = 4;
    
    
	//Function returns the list of operators as per the datatype.
	public static final List getOperatorList(int dataType)
	{
		List operatorList = new ArrayList();
		operatorList.add(new NameValueBean(Constants.ANY,Constants.ANY));
		
		switch(dataType)
		{
			case DATATYPE_DATE:
			case DATATYPE_NUMERIC:		
				operatorList.add(new NameValueBean(Operator.EQUAL,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS,Operator.NOT_EQUALS));
				operatorList.add(new NameValueBean(Operator.LESS_THAN,Operator.LESS_THAN));
				operatorList.add(new NameValueBean(Operator.LESS_THAN_OR_EQUALS,Operator.LESS_THAN_OR_EQUALS));
				operatorList.add(new NameValueBean(Operator.GREATER_THAN,Operator.GREATER_THAN));
				operatorList.add(new NameValueBean(Operator.GREATER_THAN_OR_EQUALS,Operator.GREATER_THAN_OR_EQUALS));
				operatorList.add(new NameValueBean("BETWEEN",Operator.BETWEEN));
				operatorList.add(new NameValueBean("NOT BETWEEN",Operator.NOT_BETWEEN));
				break;
			
			case DATATYPE_STRING:
				operatorList.add(new NameValueBean("STARTS WITH",Operator.LIKE));
				operatorList.add(new NameValueBean("ENDS WITH",Operator.LIKE));
				operatorList.add(new NameValueBean("CONTAINS",Operator.LIKE));
				operatorList.add(new NameValueBean(Operator.EQUAL,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS,Operator.NOT_EQUALS));
				break;
				
			case DATATYPE_ENUMERATED:
				operatorList.add(new NameValueBean(Operator.EQUAL,Operator.EQUAL));
				operatorList.add(new NameValueBean(Operator.NOT_EQUALS,Operator.NOT_EQUALS));
				break;
		}
		
		return operatorList;
	}
	public String getLink(String item){
		getMap();
		String link = (String)map.get(item);
		System.out.println("link--->"+link);
		return link;
	
	}
	private void getMap(){
		map.put("Participant","ParticipantAdvanceSearch.do?pageOf=pageOfParticipantAdvanceSearch");
		map.put("CollectionProtocol","CollectionProtocolAdvanceSearch.do?pageOf=pageOfCollectionProtocolAdvanceSearch");
		map.put("Specimen","#");
		map.put("SpecimenCollectionGroup","#");
		map.put("Distribution","#");
		map.put("DistributionProtocol","#");	
		
	}
}