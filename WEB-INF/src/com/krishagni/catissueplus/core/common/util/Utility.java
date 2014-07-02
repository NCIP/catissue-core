package com.krishagni.catissueplus.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
