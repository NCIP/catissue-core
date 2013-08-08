package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
 

/*Class LabelTokenForEventNameAbbreviation*/

/*@author gupta/dimaggio */

 

public class LabelTokenForEventNameAbbv implements LabelTokens

{

	public String getTokenValue(Object object)

	{
		// Approach using "[", "]" as the starting and ending point for event label abbreviation.
		// This approach only considers the first instance of square-bracket pair for extracting the abbreviation. Rest all instances are ingnored.
		Specimen objSpecimen = (Specimen) object;
		String eventLabel = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionPointLabel();
		int startPosition = eventLabel.indexOf("[");
		int newStartPosition = startPosition + 1;
		int endPosition = eventLabel.indexOf("]");

		if (startPosition != -1) //means "[" is not missing
		{ 
			if (endPosition != -1) //means "]" is not missing
			{
				if (endPosition > startPosition) //means "]" comes after "["
				{
					if (endPosition == startPosition+1) //means there is no value between "[]"
					{
						//String abbv = eventLabel.substring(0,startPosition); //returns everything before "[]"
						//return(abbv);
						return(eventLabel);
					}
					else // means there is value between "[]"
					{
						String abbv = eventLabel.substring(newStartPosition,endPosition);
						return(abbv);			
					}
				}
				else //means "[" comes after "]"
				{
					//String abbv = eventLabel.substring(0,endPosition); //returns everything before "]"
					//return(abbv);
					return(eventLabel);
				}
			}
			else // means "]" is missing but not "["
			{
				//String abbv = eventLabel.substring(0,startPosition); //returns everything before "["
				//return(abbv);
				return(eventLabel);
			}
		}
		else // means "[" is missing
		{
			if (endPosition != -1) //means "[" is missing but not "]"
			{
				//String abbv = eventLabel.substring(0,endPosition); //returns everything before "]"
				//return(abbv);
				return(eventLabel);
			}
			else // means "[" and "]" both are missing
			{
				return(eventLabel);
			}
		}		
	}

}