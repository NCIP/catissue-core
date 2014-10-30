/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.beanutils.Converter;
import com.krishagni.catissueplus.bulkoperator.metadata.DateValue;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class CustomDateConverter implements Converter {

	private final static String DEFAULT_FORMAT_SLASH = ApplicationProperties.getValue("timestamp.pattern.slash");
	private final static String DEFAULT_FORMAT_HIFEN = ApplicationProperties.getValue("timestamp.pattern");
	private static final Logger logger = Logger.getCommonLogger(CustomDateConverter.class);
	
	public Object convert(Class type, Object value)
	{
		SimpleDateFormat format = null;
		String formatString=null;
		String dateValue=null;
		Date date=null;
		if (value instanceof DateValue)	{
			formatString=((DateValue) value).getFormat();
			dateValue = ((DateValue) value).getValue();
		} else {
			dateValue=value.toString();
			if (dateValue.contains("-"))
			{
				formatString=DEFAULT_FORMAT_HIFEN;
			}
			else if (dateValue.contains("/"))
			{
				formatString=DEFAULT_FORMAT_SLASH;
            }
			
			
		}
		 try {
				if(formatString.contains(":") && dateValue!=null && !dateValue.contains(":"))
	            {
	                dateValue=dateValue+" 00:00";
	            }
				format = new SimpleDateFormat(formatString); 
			 	format.setLenient(false);
			 	date=format.parse(dateValue);
		} catch (ParseException e) {
			logger.error("Error while parsing date.", e);
			throw new RuntimeException("Invalid Date Format!! Expected Date format is: " + formatString);
		}
		return date;
	}

}