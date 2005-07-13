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
            if (!hasNameAndDomain(aEmailAddress))
            {
                result = false;
            }
        }
        catch (Exception ex)
        {
            result = false;
        }
        return result;
    }

    private boolean hasNameAndDomain(String aEmailAddress)
    {
        StringTokenizer token = new StringTokenizer(aEmailAddress, "@");

        if (token.countTokens() == 2)
        {
            return true;
        }
        return false;
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
    
    public static void main(String[] args)
    {
        Validator validator = new Validator();
        String str = new String("aaaa");
        boolean boo = validator.isNumeric(str);
        System.out.println(boo);
    }

}