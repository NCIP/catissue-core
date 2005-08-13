/**
 * <p>Title: GeneratePassword Class>
 * <p>Description:	GeneratePassword is used to generate password for a approved user.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 27, 2005
 */
package edu.wustl.catissuecore.util.global;


/**
 * GeneratePassword is used to generate password for a approved user.
 * @author gautam_shetty
 */
public class GeneratePassword
{
    /**
     * Returns the password for a user.
     * @param loginName the loginName of a user.
     * @return the password for a user.
     */
    public static String getPassword()
    {
        return ("login"+"123");
    }

}
