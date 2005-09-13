/**
 * <p>Title: Validator Class>
 * <p>Description:  This Class contains the methods used for validation of the fields in the userform.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 1, 2005
 */

package edu.wustl.catissuecore.util.global;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

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
/*
    private boolean hasNameAndDomain(String aEmailAddress)
    {
        StringTokenizer token = new StringTokenizer(aEmailAddress, "@");

        if (token.countTokens() == 2)
        {
            return true;
        }
        return false;
    }
    
  */  
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
        	System.out.println("Error : "+exp);
            return false;
        }
    	
    }

    public boolean isDouble(String dblString,int zero)
    {
        try
        {
            double dblValue = Double.parseDouble(dblString);

            if (Double.isNaN(dblValue)) 
            {
                return false;
            }
            if (zero==0 &&  dblValue <= 0) 
            {
                return false;
            }
      
            return true;
        }
        catch(NumberFormatException exp)
        {
        	System.out.println("Error : "+exp);
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
			System.out.println("5");
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
			System.out.println("error : " + exp);
    		return true;
		}

    }
    
    
    public static void main(String[] args)
    {
        Validator validator = new Validator();
        String str = "mandar; deshmukh";
        String delim=";,";
        System.out.println("\nstr: "+str);
        System.out.println("\ndelim: "+delim);
        System.out.println("\nContains : " + validator.containsSpecialCharacters(str,delim )); 
//        String str = new String("mandar_deshmukh@persistent.co.in");
//        boolean boo = validator.isNumeric(str);
//        System.out.println(boo);
//        boo = validator.isValidEmailAddress(str);
//        System.out.println(boo);
//        System.out.println("\n************************************\n\n\n\n");
//        str="mandar_deshmukh@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@pers@istent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@@persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str=".persi@stent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        
//        str="@.persistent.co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//
//        str="pers@istent..co.in";
//        boo = validator.isValidEmailId(str );
//        System.out.println("\n\nEmail : " + str + " : " + boo );
//        try
//		{
//            BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
//            System.out.println("\nEnter N/n to Quit \n\n");
//            System.out.print("\n\nDo you want to check the Email Address : ");
//            String ch = br.readLine();
//            while(!ch.equalsIgnoreCase("N") )
//            {
//            	System.out.println("\nEnter the Email Adress to Check: ");
//            	String email = br.readLine();
//            	boolean b = validator.isValidEmailAddress(email );
//            	System.out.println("\n Is Valid : " + b);
//            	System.out.println("---------------------");
//            	System.out.print("Do you want to Continue : ");
//            	ch = br.readLine();
//            }
//            System.out.print("\n\n**************** D O N E *************\n"); 
//		}
//        catch(Exception exp)
//		{
//        	System.out.println("Error : " + exp);
//		}

        
        
    }

}