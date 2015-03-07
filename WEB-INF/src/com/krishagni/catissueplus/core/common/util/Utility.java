package com.krishagni.catissueplus.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;

import edu.wustl.common.util.XMLPropertyHandler;
import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

public class Utility {

	public static String getDisabledValue(String value) {
		if(isBlank(value))
		{
			return value;
		}
		return value+"_"+getCurrentTimeStamp();
	}

	private static String getCurrentTimeStamp() {
		return new SimpleDateFormat().format(Calendar.getInstance().getTime());
	}
	
	public static int getMaxParticipantCnt(){
		int maxParticipants = 200;
		String participantsCnt = XMLPropertyHandler.getValue("participant.list.count");
		
		if(NumberUtils.isNumber(participantsCnt)){
			maxParticipants = Integer.valueOf(participantsCnt);
		}
		return maxParticipants;
	}
	
	public static Long numberToLong(Object number) {
		if (number == null) {
			return null;
		}

		if (!(number instanceof Number)) {
			throw new IllegalArgumentException("Input object is not a number");
		}

		return ((Number)number).longValue();
	}
	
	public static boolean isEmptyOrSuperset(Set<?> leftOperand, Set<?> rightOperand) {
		if (CollectionUtils.isEmpty(leftOperand)) {
			return true;
		}
		
		return leftOperand.containsAll(rightOperand);		
	}	
}
