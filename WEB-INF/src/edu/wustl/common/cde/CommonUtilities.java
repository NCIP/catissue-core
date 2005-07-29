/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.cde;

import java.util.StringTokenizer;


/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommonUtilities
{
    /**
     * 
     * @param num The String value that is to be used as a number
     * @param chk To check for the range or in general. 1 indicates check for
     *  the number to be in range of min and max.
     *  Any other number will beconsidered as general number validation.	 
     * @param min Indicates the minimum value for the number.
     * @param max Indicates the maximum value for the number.
     * @return It returns a Boolean value depending on the value passed as a num parameter.
     */
    public static boolean checknum(String num, int min, int max)
    {
        try
        {
            Integer i = new Integer(num);
            if ((min < max))
            {
                int z = i.intValue();
                if (!(z >= min) && !(z <= max))
                {
                    return false;
                }
            }
        } // try
        catch (NumberFormatException nfe)
        {
            return false;
        } // catch
        catch (Exception e1)
        {
            return false;
        } // catch
        return true;
    } // checknum
//------------------------------------------------
    /**
     * 
     * @param ipadd  It contains the IPAddress in String format.This value will be verified for IPAddress.
     * IPAddress will be in the format: A.B.C.D where A,B,C,D all are integers in the range 0 to 255.  
     * @return Returns boolean value depending on the ipaddress provided. 
     */
    public static boolean isvalidIP(String ipadd)
    {
        StringTokenizer st = new StringTokenizer(ipadd, ".");
        int tokenCnt = st.countTokens();
        if (tokenCnt == 4)
        {
            String ipPart[] = new String[4];
            int i = 0;
            while (st.hasMoreTokens())
            {
                ipPart[i] = st.nextToken();
                i++;
            }
            for (int z = 0; z < ipPart.length; z++)
            {
                if (checknum(ipPart[z], 0, 255) == false)
                {
                    return false;
                }
            }
            return true;
        } // if
        return false;
    } //isvalidip

    
}//CommonUtilities
