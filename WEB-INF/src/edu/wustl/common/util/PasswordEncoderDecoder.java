/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.common.util;

import edu.wustl.common.util.logger.Logger;



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class PasswordEncoderDecoder
{
    public static String encode(String input)
    {

        char char_O = 'O';
        char char_F = 'f';
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char ch1 = 'A';
        String key = new String("" + ch1);
        key += "Copyright" + char_O;
        String in = "";
        key += char_F;
        key += "WashingtonUniversitySchoolOfMedicineStLouisGeneChipCoreALongStringLongerThanAllPossiblePasswords";
        for (int i = 0; i < input.length(); i++)
        {
            in += input.substring(i, i + 1);
            in += key.substring(i, i + 1);
        }

        try
        {
            byte[] bytes = in.getBytes();
            StringBuffer s = new StringBuffer(bytes.length * 2);

            for (int i = 0; i < bytes.length; i++)
            {
                byte b = bytes[i];
                s.append(digits[(b & 0xf0) >> 4]);
                s.append(digits[b & 0x0f]);
            }

            return s.toString();
        }
        catch (Exception e)
        {
            Logger.out.warn("Problems in Encryption/Decryption in CommonJdao "
                    );
            Logger.out.warn("Exception= " + e.getMessage());
        }
        return null;
    }
    
    public static String decode(String s)
    {
        try
        {
            int len = s.length();
            byte[] r = new byte[len / 2];
            for (int i = 0; i < r.length; i++)
            {
                int digit1 = s.charAt(i * 2);
                int digit2 = s.charAt(i * 2 + 1);
                if ((digit1 >= '0') && (digit1 <= '9'))
                    digit1 -= '0';
                else if ((digit1 >= 'a') && (digit1 <= 'f'))
                    digit1 -= 'a' - 10;
                if ((digit2 >= '0') && (digit2 <= '9'))
                    digit2 -= '0';
                else if ((digit2 >= 'a') && (digit2 <= 'f'))
                    digit2 -= 'a' - 10;
                r[i] = (byte) ((digit1 << 4) + digit2);
            }
            String sin = new String(r);
            String sout = "";
            for (int i = 0; i < sin.length(); i += 2)
            {
                sout += sin.substring(i, i + 1);
            }
            return sout;
        }
        catch (Exception e)
        {
            Logger.out.warn("Problems in Decription/Encription");
            Logger.out.warn("Exception= " + e.getMessage());
        }
        return null;
    }
    
//    public static void main(String[] args)
//    {
//        System.out.println(decode("6c416f43676f69706e79317232693367"));
//    }
}
