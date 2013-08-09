package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Date;

public class LabelTokenForDateOfcollection implements LabelTokens
{
	public String getTokenValue(Object object)
	{
		Specimen objSpecimen = (Specimen) object;
		String valToReplace="";
		int  yearOfcoll = Calendar.getInstance().get(Calendar.YEAR);
		int  monthOfcoll = Calendar.getInstance().get(Calendar.MONTH)+1;
		int  dayOfcoll = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);		
		Iterator specEveItr = objSpecimen.getSpecimenEventCollection().iterator();
		while(specEveItr.hasNext())
		{
			Object specEveParam = specEveItr.next();
			if (specEveParam instanceof CollectionEventParameters)
			{
				CollectionEventParameters collEveParam = (CollectionEventParameters) specEveParam;
				Calendar cal = Calendar.getInstance();
				cal.setTime(collEveParam.getTimestamp());
				yearOfcoll = cal.get(Calendar.YEAR);
				monthOfcoll = cal.get(Calendar.MONTH)+1;
				dayOfcoll = cal.get(Calendar.DAY_OF_MONTH);
			}
		}
		valToReplace = monthOfcoll+"/"+dayOfcoll+"/"+yearOfcoll+Constants.DOUBLE_QUOTES;
		return valToReplace;
	}
}