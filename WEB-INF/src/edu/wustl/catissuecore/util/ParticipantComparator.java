package edu.wustl.catissuecore.util;

import java.util.Comparator;

 
public class ParticipantComparator implements Comparator
{

	public int compare(Object obj1, Object obj2)
	{
		StringBuffer sb1 = new StringBuffer(obj1.toString());
	    StringBuffer sb2 = new StringBuffer(obj2.toString());
	    String compString1 = sb1.substring(sb1.indexOf(":")+1);
	    String compString2 = sb2.substring(sb2.indexOf(":")+1);
	    boolean comparisonDone = false;
	    String ppi1 = sb1.substring(sb1.indexOf("(")+1,sb1.indexOf(")"));
	    String ppi2 = sb2.substring(sb2.indexOf("(")+1,sb2.indexOf(")"));
	  	    
	    
	    if(sb1.substring(sb1.indexOf(":")+1,sb1.indexOf("(")).equals("N/A ") && 
	       sb2.substring(sb2.indexOf(":")+1,sb2.indexOf("(")).equals("N/A ")) 
	    {
	    	comparisonDone = true;
	    	compString1 = sb1.substring(sb1.indexOf("(")+1);
	    	compString2 = sb2.substring(sb2.indexOf("(")+1);
	    }
	    if(comparisonDone && ((!ppi1.equals("N/A")) || (!ppi2.equals("N/A"))))
	    {
	    	compString1 = ppi1;
	    	compString2 = ppi2;
	    }
	    int returnVal=compString1.toLowerCase().compareTo(compString2.toLowerCase());
	    
	    if(!comparisonDone && sb1.substring(sb1.indexOf(":")+1,sb1.indexOf("(")).equals("N/A ") )
	    {
	    	returnVal = 1;
	    }
	    else if(!comparisonDone && sb2.substring(sb2.indexOf(":")+1,sb2.indexOf("(")).equals("N/A "))
	    {
	    	returnVal = -1;
	    }
	   
	    return returnVal;
	  }          
}