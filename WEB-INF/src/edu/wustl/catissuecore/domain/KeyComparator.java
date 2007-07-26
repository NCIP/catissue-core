

package edu.wustl.catissuecore.domain;

import java.util.Comparator;
/**
 * <p>Title: KeyComparator Class</p>
 * <p>Description:  This class is base class for comparing two keys.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author abhishek_mehta
 * @version 1.00
 * Created on July 26, 2007
 */
public class KeyComparator implements Comparator{

	public int compare(Object object1, Object object2) 
	{
		int outer1=0,outer2=0,inner1=0,inner2=0;
		String key1 = (String)object1;
		String key2 = (String)object2;
		
		int index1 = key1.indexOf("_");
		if(index1 != -1){
			String outerKey1 = key1.substring(0,index1);
			outer1 = Integer.parseInt(outerKey1.substring(outerKey1.indexOf(":")+1));
		}
		
		int index2 = key2.indexOf("_");
		if(index2 != -1){
			String outerKey2 = key2.substring(0,index2);
			outer2 = Integer.parseInt(outerKey2.substring(outerKey2.indexOf(":")+1));
		}
		
		if(outer1 > outer2)
			return 1;
		else if(outer1 < outer2)
			return -1;
		else{
			String innerKey1 = key1.substring(index1+1);
			index1 = innerKey1.indexOf("_");
			if(index1 != -1){
				innerKey1 = innerKey1.substring(0,index1);
				inner1 = Integer.parseInt(innerKey1.substring(innerKey1.indexOf(":")+1));
			}
			
			String innerKey2 = key2.substring(index2+1);
			index2 = innerKey2.indexOf("_");
			if(index2 != -1){
				innerKey2 = innerKey2.substring(0,index2);
				inner2 = Integer.parseInt(innerKey2.substring(innerKey2.indexOf(":")+1));
			}
			
				if(inner1 > inner2)
					return 1;
				else if(inner1==inner2)
					return 0;
				else
					return -1;
		}
	}
}
