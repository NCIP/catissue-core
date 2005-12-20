/**
 * <p>Title: Validator Class>
 * <p>Description:  This Class contains the methods used for validation of the fields in the userform.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 1, 2005
 */

package edu.wustl.common.util.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;


/**
 *  This Class contains the methods used for validation of the fields in the userform.
 *  @author gautam_shetty
 */

public class Validator
{

    /**
     * Checks that the input String is a valid email address.
     * @param aEmailAddress String containing the email address to be checked.
     * @return Returns true if its a valid email address, else returns false.  
     * */
    public boolean isValidEmailAddress(String aEmailAddress)
    {
        boolean result = true;
        try
        {
            if (isEmpty(aEmailAddress))
            {
                result = false;
            }
            else
            {
            	result = isValidEmailId(aEmailAddress);
            }
        }
        catch (Exception ex)
        {
            result = false;
        }
        return result;
    }
    
    /**
     * 
     * @param ssn Social Security Number to check
     * @return boolean depending on the value of ssn.
     */
    public boolean isValidSSN(String ssn)
    {
    	boolean result = true;
    	try
		{
    		Pattern re = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}", Pattern.CASE_INSENSITIVE);
    		Matcher  mat =re.matcher(ssn); 
    		result = mat.matches();
    		//System.out.println(result);
		}
    	catch(Exception exp)
		{
			//System.out.println("exp");
    		return false;
		}
    	return result;
    }
    
    /**
     * Checks whether a string is empty and adds an ActionError object in the ActionErrors object.
     * @param componentName Component which is to be checked.
     * @param labelName Label of the component on the jsp page which is checked. 
     * @param errors ActionErrors Object.
     * @return Returns true if the componentName is empty else returns false.
     */
    public boolean isEmpty(String str)
    {
        if (str == null || str.trim().length() == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Checks that the input String contains only alphabetic characters.
     * @param alphaString The string whose characters are to be checked.
     * @return Returns false if the String contains any digit else returns true. 
     * */
    public boolean isAlpha(String alphaString)
    {
        int i = 0;
        while (i < alphaString.length())
        {
            if (!Character.isLetter(alphaString.charAt(i)))
            {
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Checks that the input String contains only numeric digits.
     * @param numString The string whose characters are to be checked.
     * @return Returns false if the String contains any alphabet else returns true. 
     * */
    public boolean isNumeric(String numString)
    {
        try
        {
            long longValue = Long.parseLong(numString);
            if (longValue <= 0)
            {
                return false;
            }
            
            return true;
        }
        catch(NumberFormatException exp)
        {
            return false;
        }
    }

    /**
     * 
     * Checks that the input String contains only numeric digits.
     * @param numString The string whose characters are to be checked.
     * @param positiveCheck Positive integer to check for positive number
     * @return Returns false if the String contains any alphabet else returns true.
     * Depending on the value of the positiveCheck will check for positive values
     *   
     */
    public boolean isNumeric(String numString, int positiveCheck)
    {
        try
        {
            long longValue = Long.parseLong(numString);
           if(positiveCheck >0 )
           {
	            if (longValue <= 0)
	            {
	                return false;
	            }
           }
           else if(positiveCheck == 0 )
           {
	            if (longValue < 0)
	            {
	                return false;
	            }
           }
     
           return true;
        }
        catch(NumberFormatException exp)
        {
            return false;
        }
    }

    
    /**
     * Checks the given String for double value.
     * 
     * @param dblString
     * @return boolean True if the string contains double number or false if any non numeric character is present.
     */
    public boolean isDouble(String dblString)
    {
        try
        {
            double dblValue = Double.parseDouble(dblString);

            if (dblValue <= 0  || Double.isNaN(dblValue)) 
            {
                return false;
            }
            
            return true;
        }
        catch(NumberFormatException exp)
        {
        	//System.out.println("Error : "+exp);
            return false;
        }
    	
    }

    /**
     * @param dblString The String value to be verified for Double
     * @param positiveCheck Boolean value indicating whether to check for positive values only or not.
     * @return Boolean indicating the given string value is double or not.
     */
    public boolean isDouble(String dblString,boolean positiveCheck)
    {
        try
        {
            double dblValue = Double.parseDouble(dblString);

            if (Double.isNaN(dblValue)) 
            {
                return false;
            }
            if(positiveCheck)
            {
 	            if (dblValue < 0)
 	            {
 	                return false;
 	            }
            }     
      
            return true;
        }
        catch(NumberFormatException exp)
        {
        	//System.out.println("Error : "+exp);
            return false;
        }
    	
    }
    
    
    public boolean isValidOption(String option)
    {
        Logger.out.debug("option value: "+option);
    	if(option != null)
    	{
    		if(option.trim().equals("-1") || option.equals(Constants.SELECT_OPTION))
    			return false;
    		else
    			return true;
    	}
    	
    	return false;
    }

    
    private boolean isValidEmailId(String emailAddress)
    {
    	boolean result = true;
    	try
		{
    		
    		Pattern re = Pattern.compile("^\\w(\\.?[\\w-])*@\\w(\\.?[-\\w])*\\.([a-z]{3}(\\.[a-z]{2})?|[a-z]{2}(\\.[a-z]{2})?)$", Pattern.CASE_INSENSITIVE);
    		Matcher  mat =re.matcher(emailAddress); 
    		result = mat.matches();
		}
    	catch(Exception exp)
		{
			//System.out.println("5");
    		return false;
		}
    	return result;
    }
    
    public boolean containsSpecialCharacters( String mainString, String delimiter)
    {
    	try
		{
    		StringTokenizer st = new StringTokenizer(mainString, delimiter);
    		int count = st.countTokens();
    		if(count>1)
    			return true;
    		else
    			return false;
		}
    	catch(Exception exp)
		{
			//System.out.println("error : " + exp);
    		return true;
		}

    }
    
    public String delimiterExcludingGiven(String ignoreList)
    {
    	String spChars = "!@#$%^&*()=+\\|{[ ]}\'\";:/?.>,<`~ -_";
//    	//System.out.println("Original : " + spChars);
    	StringBuffer sb = new StringBuffer(spChars);
//    	//System.out.println("SB : " +sb);
    	StringBuffer retStr = new StringBuffer();

    	try
		{
    		char spIgnoreChars[] = ignoreList.toCharArray();
    		for(int spCharCount =0; spCharCount<spIgnoreChars.length ; spCharCount++)
    		{
    			char chkChar = spIgnoreChars[spCharCount];
//    			//System.out.println(chkChar);
    			int chInd = sb.indexOf(""+chkChar );
    			while(chInd !=-1)
    			{
    				sb = sb.deleteCharAt(chInd );
    				chInd = sb.indexOf(""+chkChar );
//    				//System.out.println("\t"+sb);
    			}
    			
    		}
    		retStr = sb;
//    		//System.out.println("\n\nRetStr : " + retStr);
		}
    	catch(Exception exp)
		{
			//System.out.println("error : " + exp);
		}
    	
    	return retStr.toString(); 
    }

    
   // ----------------------------------- Date Validation -------------------- 
    
    private boolean isValidDatePattern(String checkDate)
    {
    	boolean result = true;
    	try
		{
    		//System.out.println("checkDate in isValidDatePattern : " +checkDate);
    		Pattern re = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}", Pattern.CASE_INSENSITIVE);
    		Matcher  mat =re.matcher(checkDate); 
    		result = mat.matches();
    		//System.out.println("is Valid Date Pattern : - : "+result);
    		dtCh  = Constants.DATE_SEPARATOR ; 
    		// check for  / separator
    		if(!result)
    		{
        		re = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}", Pattern.CASE_INSENSITIVE);
        		mat =re.matcher(checkDate); 
        		result = mat.matches();
        		//System.out.println("is Valid Date Pattern : / : "+result);
        		dtCh  = Constants.DATE_SEPARATOR_SLASH   ; 
    		}
		}
    	catch(Exception exp)
		{
			Logger.out.error("IsValidDatePattern : exp : " + exp);
    		return false;
		}
    	//System.out.println("dtCh : " +dtCh );
    	return result;
    }

    String dtCh= Constants.DATE_SEPARATOR;
    int minYear = Integer.parseInt(Constants.MIN_YEAR);
    int maxYear = Integer.parseInt(Constants.MAX_YEAR);
    
    private int daysInFebruary (int year)
    {
    	// February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }

    private int[] DaysArray(int n)
    {
    	int dayArray[] = new int[n+1];
    	dayArray[0] = 0;
    	for (int i = 1; i <= n; i++) 
    	{
    		dayArray[i] = 31;
    		if (i==4 || i==6 || i==9 || i==11)
    		{
    			dayArray [i] = 30;
    		}
    		if (i==2)
    		{
    			dayArray[i] = 29;
    		}
        } 
       return dayArray; 
    }

    
    private boolean isDate(String dtStr)
    {
    	try
		{
    		Logger.out.debug("In isDate : dtCh : " + dtCh + " | dtStr : " + dtStr  );
    		//System.out.println("In isDate : dtCh : " + dtCh + " | dtStr : " + dtStr );
	    	int []daysInMonth = DaysArray(12);
	    	int pos1=dtStr.indexOf(dtCh);
	    	int pos2=dtStr.indexOf(dtCh,pos1+1);
	    	String strMonth=dtStr.substring(0,pos1);
	    	String strDay=dtStr.substring(pos1+1,pos2);
	    	String strYear=dtStr.substring(pos2+1);
	    	//System.out.println("pos1 : "+pos1 + " pos2 : "+pos2 + " strMonth : "+strMonth + "strDay : "+strDay + "strYear : "+strYear  );
	    	String strYr=strYear;
	    	
	    	if (strDay.charAt(0)=='0' && strDay.length()>1) strDay=strDay.substring(1);
	    	
	    	if (strMonth.charAt(0)=='0' && strMonth.length()>1) strMonth=strMonth.substring(1);
	    	for (int i = 1; i <= 3; i++)
	    	{
	    		if (strYr.charAt(0)=='0' && strYr.length()>1) strYr=strYr.substring(1);
	    	}
	    	int month=Integer.parseInt(strMonth);
	    	int day=Integer.parseInt(strDay);
	    	int year=Integer.parseInt(strYr);
	    	
	    	if (pos1==-1 || pos2==-1){
	    		Logger.out.debug("The date format should be : mm/dd/yyyy");
	    		//System.out.println("The date format should be : mm/dd/yyyy");
	    		return false;
	    	}
	    	if (strMonth.length()<1 || month<1 || month>12)
	    	{
	    		Logger.out.debug("Please enter a valid month");
	    		//System.out.println("Please enter a valid month");
	    		return false;
	    	}
	    	if (strDay.length()<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month])
	    	{
	    		Logger.out.debug("Please enter a valid day");
	    		//System.out.println("Please enter a valid day");
	    		return false;
	    	}
	    	if (strYear.length() != 4 || year==0 || year<minYear || year>maxYear){
	    		Logger.out.debug("Please enter a valid 4 digit year between "+minYear+" and "+maxYear);
	    		//System.out.println("Please enter a valid 4 digit year between "+minYear+" and "+maxYear);
	    		return false;
	    	}
	    return true;
		}
    	catch(Exception exp)
		{
			Logger.out.error("exp in isDate : "+ exp);
			exp.printStackTrace();
			//System.out.println(exp);
			return false;
		}

    }
    
    
    public boolean checkDate(String checkDate)
    {
    	boolean result = true;
    	try
		{
    		Logger.out.debug("checkDate : " + checkDate); 
    		if(isEmpty(checkDate))
    		{
    			result = false;
    		}
    		else
    		{
    			if(isValidDatePattern(checkDate ))
    			{
    				result = isDate(checkDate ); 
    			}
    			else
    				result = false;
    		}
		}
    	catch(Exception exp)
		{
			Logger.out.error("Check Date : exp : "+ exp);
			result = false;
		}
		Logger.out.debug("CheckDate : "+result);
    	return result;
    }
    
    public boolean compareDateWithCurrent(String dateToCheck)
    {
    	boolean result = true;
    	try
		{
    		Date currentDate = Calendar.getInstance().getTime();
    		//System.out.println("1 : -- "+ dateToCheck );
    		//System.out.println("currentDate : "+ currentDate );
    		//String pattern="MM-dd-yyyy";
    		String pattern="MM" + dtCh  +"dd" + dtCh + "yyyy";
    		//System.out.println("\n\n\n\n\n\n------------\n"+pattern+"\n\n\n\n\n\n------------\n");
			SimpleDateFormat dF = new SimpleDateFormat(pattern);
    		Date toCheck = 	dF.parse(dateToCheck );

    		//System.out.println("2");
    		//System.out.println("toCheck : "+ toCheck);
    		int dateCheckResult = currentDate.compareTo(toCheck );
//    		Logger.out.debug("currentDate.compareTo(toCheck ) : " + dateCheckResult );
    		if(dateCheckResult < 0 )
    			result = false;
		}
    	catch(Exception exp)
		{
			//Logger.out.error("compareDateWithCurrent : " + dateToCheck + " : exp : "+ exp);
    		////System.out.println("compareDateWithCurrent : " + dateToCheck + " : exp : "+ exp);
			result = false;
		}

    	return result;
    }
    
    // -- method to compare two dates
    public boolean compareDates(String startDate, String endDate)
    {
    	boolean result = true;
    	try
		{
    		// currentdate = enddate
//    		Date currentDate = Calendar.getInstance().getTime();
    		
    		//System.out.println("1 : -- "+ startDate );
    		isValidDatePattern(startDate  );
    		String pattern="MM" + dtCh  +"dd" + dtCh + "yyyy";
    		//System.out.println("\n------------\n"+pattern+"\n------------\n");
			SimpleDateFormat dF = new SimpleDateFormat(pattern);
    		Date toCheck = 	dF.parse(startDate );

    		//System.out.println(toCheck.toString()+"\n-------------\n" );
    		
    		//System.out.println("2 : -- "+ endDate );
    		isValidDatePattern(endDate  );
    		String pattern1 = "MM" + dtCh  +"dd" + dtCh + "yyyy";
    		//System.out.println("\n------------\n"+pattern1+"\n------------\n");
    		SimpleDateFormat dF1 = new SimpleDateFormat(pattern1);
    		Date maxDate = 	dF1.parse(endDate );

    		//System.out.println("2");
    		//System.out.println("toCheck : "+ toCheck);
    		int dateCheckResult = maxDate.compareTo(toCheck );
    		if(dateCheckResult < 0 )
    			result = false;
		}
    	catch(Exception exp)
		{
    		//System.out.println(exp);
			result = false;
		}
    	//System.out.println("Date : "+ endDate + " > " + startDate + " : - " + result  );
    	return result;
        
    }
    
    // --- method to check date and format the errors accordingly.
    // Error messages for empty date, Invalid date, and Future dates are formatted .
    public String validateDate(String strDate, boolean checkFutureDate)
    {
    	Logger.out.debug("Validate Date : " + strDate  );
    	String returnString = "";
		if(isEmpty(strDate))
		{
			returnString = "errors.item.required";
		}
		else
		{
			if(isValidDatePattern(strDate ))
			{
				if(isDate(strDate) )
				{
					if(checkFutureDate)
					{
						if(!compareDateWithCurrent(strDate ))
						{
							returnString = "errors.invalid.date";
						}
					}	
				}
				else
				{
					//System.out.println("validateDate : Date invalid");
					returnString = "errors.date.format";
				}
			}
			else
			{
				//System.out.println("validateDate : Pattern invalid");
				returnString = "errors.date.format";
			}
		}
		Logger.out.debug("validateDate Return : " +returnString );
		return returnString;
    }

    //Returns TRUE if operator is valid else return FALSE
    public boolean isOperator(String value)
    {
    	if(value == null)
    	{
    		return false;
    	}
    	else if(value.equals(Constants.ANY) || value.equals("-1"))
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
    public boolean isValue(String value)
    {
    	//Purposefully not checked for value==null.
    	if(value == null)
    	{
    		return true;
    	}
    	else if(value.trim().length() == 0)
    	{
    		return false;
    	}
    	else if(value.equals("-1") || value.equals(Constants.SELECT_OPTION))
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
    // ------------------------------Date Validation ends-----------------------------------------------------------------------
    
    /**
     * returns true if zip code is valid else returns false
     */
    public boolean isValidZipCode(String zipCode)
    {
    	boolean result=false;
    	try
    	{
    		// valid format for zip code are '99999-9999' or '99999'
    		Pattern p = Pattern.compile("\\d{5}\\-\\d{4}|\\d{5}"); 
			Matcher m = p.matcher(zipCode);
			result = m.matches();
    	}	
		catch(Exception exp)
		{
			//Logger.out.error("Check Zip Code : exp : "+ exp);
			result = false;
		}
		return result;
    }
    
    /**
     * returns true if Phone/Fax number is valid else returns false
     */
    public boolean isValidPhoneNumber(String phoneNumber)
    {
    	boolean result=false;
    	try
    	{
    		// valid format for phone/fax number ar '(999)999-9999' or '999-999-9999'
    		Pattern p = Pattern.compile("\\(\\d{3}\\)\\d{3}-\\d{4}|\\d{3}\\-\\d{3}-\\d{4}"); // for fax and phone
			Matcher m = p.matcher(phoneNumber);
			result = m.matches();
    	}	
		catch(Exception exp)
		{
			//Logger.out.error("Check Phone/Fax Number : exp : "+ exp);
			result = false;
		}
		return result;
    }

    public static void main(String[] args)
    {
        Validator validator = new Validator();

//        // Data for Date validation
//        String sdate = "10-10-1000";
//        //System.out.println(validator.checkDate(sdate ));
//
//        sdate = "18/13/1901";
//        //System.out.println(validator.checkDate(sdate ));
//        
//        sdate = "12.43.1901";
//        //System.out.println(validator.checkDate(sdate ));
//
//        sdate = "02.30/1901";
//        //System.out.println(validator.checkDate(sdate ));
//        
//        sdate = "02 13 1901";
//        //System.out.println(validator.checkDate(sdate ));
//
//        sdate = "10-10-2000";
//        //System.out.println(validator.checkDate(sdate ));
//
//        sdate = "11/13/1901";
//        //System.out.println(validator.checkDate(sdate ));
//        
//        sdate = "12.23.1901";
//        //System.out.println(validator.checkDate(sdate ));
//
      String sdate = "10-10-2000";
// //     //System.out.println("validator.validateDate(sdate,false ) : " + validator.validateDate(sdate,false ));
//
      String sdate1 = "11/13/2001";
      //System.out.println("Check Date : \n");
      //System.out.println("validator.checkDate(sdate1 ) : " +validator.checkDate(sdate1 ));
      ////      //System.out.println("validator.validateDate(sdate1,false ) : "+ validator.validateDate(sdate1,false ));
      //System.out.println("\n=========================================\n");
      //System.out.println("Compare Date : \n");
      //System.out.println("validator.compareDates(sdate,sdate1   ) : " +validator.compareDates(sdate1,sdate   )); 

      //System.out.println("\n=========================================\n");
      //System.out.println("validator.compareDates(sdate,sdate1   ) : " +validator.compareDates(sdate,sdate1   )); 

      //
//        
//
//        // -- Check code for date comparison	
//        validator.dtCh = Constants.DATE_SEPARATOR ; 
//        String dt = "12-12-2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "12-23-2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "10-23-2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "11-08-2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "ssf";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//
//        validator.dtCh = Constants.DATE_SEPARATOR_SLASH  ;
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "12/23/2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "10/23/2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );
//        dt = "11/08/2005";
//        //System.out.println("validator.compareDateWithCurrent(dt) : " + validator.compareDateWithCurrent(dt) );

        //        

//        String str = "mandar; deshmukh";
//        String delim=";,";
//        //System.out.println("\nstr: "+str);
//        //System.out.println("\ndelim: "+delim);
//        //System.out.println("\nContains : " + validator.containsSpecialCharacters(str,delim )); 
//        
//        String s= new String("- _");
//        String delimitedString = validator.delimiterExcludingGiven(s );
//        //System.out.println("\n\n" + delimitedString );
//        //System.out.println(delimitedString.indexOf(s ));
        
        
//        String str = new String("mandar_deshmukh@persistent.co.in");
//        boolean boo = validator.isNumeric(str);
//        //System.out.println(boo);
//        boo = validator.isValidEmailAddress(str);
//        //System.out.println(boo);
//        //System.out.println("\n************************************\n\n\n\n");
//        str="mandar_deshmukh@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@pers@istent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str=".persi@stent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@.persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//
//        str="pers@istent..co.in";
//        boo = validator.isValidEmailId(str );
//        //System.out.println("\n\nEmail : " + str + " : " + boo );
//        try
//		{
//            BufferedReader br = new BufferedReader (new InputStreamReader(//System.in));
//            //System.out.println("\nEnter N/n to Quit \n\n");
//            //System.out.print("\n\nDo you want to check the Email Address : ");
//            String ch = br.readLine();
//            while(!ch.equalsIgnoreCase("N") )
//            {
//            	//System.out.println("\nEnter the Email Adress to Check: ");
//            	String email = br.readLine();
//            	boolean b = validator.isValidEmailAddress(email );
//            	//System.out.println("\n Is Valid : " + b);
//            	//System.out.println("---------------------");
//            	//System.out.print("Do you want to Continue : ");
//            	ch = br.readLine();
//            }
//            //System.out.print("\n\n**************** D O N E *************\n"); 
//		}
//        catch(Exception exp)
//		{
//        	//System.out.println("Error : " + exp);
//		}

        
        
    }
    
    /**
     * Returns true if the time is in invalid pattern else returns false.
     * @param time the time to be validated.
     * @param pattern the pattern in which it is to be validated.
     * @return true if the time is in invalid pattern else returns false.
     */
    public boolean isValidTime(String time, String pattern)
    {
        boolean isValid = true;
        
        try
        {
            Utility.parseDate(time, pattern);
        }
        catch (ParseException parseExp)
        {
            isValid = false;
        }
        
        return isValid;
    }

}