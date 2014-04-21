package com.krishagni.catissueplus.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
}
