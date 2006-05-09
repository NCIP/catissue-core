/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.util;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;



/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class PasswordManager
{
	public static final int PASSWORD_MIN_LENGTH=6;
	public static final int SUCCESS=0;
	public static final int FAIL_LENGTH=1;
	public static final int FAIL_SAME_AS_OLD=2;
	public static final int FAIL_SAME_AS_USERNAME=3;
	public static final int FAIL_IN_PATTERN=4;
	public static final int FAIL_SAME_SESSION=5;
	public static final int FAIL_WRONG_OLD_PASSWORD=6;
	public static final int FAIL_INVALID_SESSION=7;
	
	
	/**
     * Generate random alpha numeric password.
     * @param loginName the loginName of a user.
     * @return Returns the generated password.
     */
	public static String generatePassword()
    {
    	//Define a Constants alpha-numeric String 
    	final String CHAR_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	
    	// Generate password of length 6
    	final int PASSWORD_LENGTH = 6;
    	
    	Random random = new Random();
    	StringBuffer passwordBuff = new StringBuffer(); 
    	
    	for (int i = 0; i < PASSWORD_LENGTH ; i++)
		{
    		//Generate a random number from 0(inclusive) to lenght of CHAR_STRING(exclusive).
    		int randomVal = random.nextInt(CHAR_STRING.length());
    		
    		//Get the character corrosponding to random number and append it to password buffer.
    		passwordBuff.append(CHAR_STRING.charAt(randomVal));
		}
    	return passwordBuff.toString();
    }
    
    public static String encode(String input)
    {

        char char_O = 'O';
        char char_F = 'f';
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char ch1 = 'A';
        String key = new String("" + ch1);
        key += "WelcomeTocaTISSUECORE" + char_O;
        String in = "";
        key += char_F;
        key += "ThisIsTheFirstReleaseOfcaTISSUECOREDevelopedByWashUAtPersistentSystemsPrivateLimited";
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
    /**
     * 
     * @param newPassword New Password value
     * @param oldPassword Old Password value
     * @param httpSession HttpSession object
     * @param abstractForm UserForm object  
     * @return SUCCESS (constant int 0) if all condition passed 
     *   else return respective error code (constant int) value  
     */
    public static int validate(String newPassword,String oldPassword,HttpSession httpSession)
	{
    	// get SessionDataBean objet from session 
    	Object obj = httpSession.getAttribute(Constants.SESSION_DATA);
        SessionDataBean sessionData=null;
        String userName="";
        if(obj!=null)
		{
        	sessionData = (SessionDataBean) obj;
        	// get User Name
        	userName=sessionData.getUserName();
		}
        else
        {
        	return FAIL_INVALID_SESSION;
        }
    	// to check whether user entered correct old password
    	
    	
    	try
    	{
    		// retrieve User DomainObject by user name
    		
    		AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
    		List userList = bizLogic.retrieve(User.class.getName(), "loginName",userName);
    		User user= null;
    		if(userList!=null && !userList.isEmpty())
    		{
    			user =(User)userList.get(0);
    			Logger.out.debug("got user domain obj---"+user);
    		}
	        // compare password stored in database with value of old password currently entered by 
    		// user for Change Password operation
    		Logger.out.debug("User name from domainobg--"+user.getLoginName());
    		if (!oldPassword.equals(PasswordManager.decode(user.getPassword())))
	        {
	            return FAIL_WRONG_OLD_PASSWORD; //retun value is int 6
	        }
    	}
       	catch(Exception e)
    	{
    		// if error occured during password comparision
       		Logger.out.error(e.getMessage(),e);
    		return FAIL_WRONG_OLD_PASSWORD;
    	}
       	
    	// to check whether password change in same session
       	// get attribute (Boolean) from session object stored when password is changed successfully 
    	Boolean b = null;
    	b = (Boolean)httpSession.getAttribute(Constants.PASSWORD_CHANGE_IN_SESSION);
    	Logger.out.debug("b---" + b);
    	if(b!=null && b.booleanValue()==true)
    	{
    		// return error code if attribute (Boolean) is in session   
    		Logger.out.debug("Attempt to change Password in same session Returning FAIL_SAME_SESSION");
    		return FAIL_SAME_SESSION; // return int value 5
    	}
    	// to Check length of password,if not valid return FAIL_LENGTH = 2
    	if(newPassword.length()<PASSWORD_MIN_LENGTH)
		{
    		Logger.out.debug("Password is not valid returning FAIL_LENGHT");
    		return FAIL_LENGTH; // return int value 1
		}

    	// to Check new password is different as old password ,if bot are same return FAIL_SAME_AS_OLD = 3    	
		if(newPassword.equals(oldPassword))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_OLD");
			return FAIL_SAME_AS_OLD; //return int value 2
		}
		
		// to check password is differnt than user name if same return FAIL_SAME_AS_USERNAME =4
		// eg. username=abc@abc.com newpassword=abc is not valid
		int usernameBeforeMailaddress=userName.indexOf('@');
		// get substring of username before '@' character    
		String name=userName.substring(0,usernameBeforeMailaddress);
		Logger.out.debug("usernameBeforeMailaddress---" + name);
		if(name!=null && newPassword.equals(name))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_USERNAME");
			return FAIL_SAME_AS_USERNAME; // return int value 3
		}
		//to check password is differnt than user name if same return FAIL_SAME_AS_USERNAME =4
		// eg. username=abc@abc.com newpassword=abc@abc.com is not valid
		if(newPassword.equals(userName))
		{
			Logger.out.debug("Password is not valid returning FAIL_SAME_AS_USERNAME");
			return FAIL_SAME_AS_USERNAME; // return int value 3
		}
		
		
		// following is to checks pattern i.e password must include atleast one UCase,LCASE and Number
		// and must not contain space charecter.
		// define get char array whose length is equal to length of new password string.
		char dest[]=new char[newPassword.length()]; 
		// get char array where values get stores in dest[]
		newPassword.getChars(0,newPassword.length(),dest,0);
		boolean foundUCase=false; // boolean to check UCase character found in string
		boolean foundLCase=false; // boolean to check LCase character found in string
		boolean foundNumber=false; // boolean to check Digit/Number character found in string
		boolean foundSpace=false;
		 
		for(int i=0;i<dest.length;i++)
		{
			// to check if character is a Space. if true break from loop
			if(Character.isSpaceChar(dest[i]))
			{
				foundSpace=true;
				Logger.out.debug("Found Space in Password");
				break;
			}
			// to check whether char is Upper Case. 
			if(foundUCase==false&&Character.isUpperCase(dest[i])==true)
			{
				//foundUCase=true if char is Upper Case and Upper Case is not found in previous char.
				foundUCase=true;
				Logger.out.debug("Found UCase in Password");
			}
			
			// to check whether char is Lower Case 
			if(foundLCase==false&&Character.isLowerCase(dest[i])==true)
			{
				//foundLCase=true if char is Lower Case and Lower Case is not found in previous char.
				foundLCase=true;
				Logger.out.debug("Found LCase in Password");
			}
			
			// to check whether char is Number/Digit
			if(foundNumber==false&&Character.isDigit(dest[i])==true)
			{
			//	foundNumber=true if char is Digit and Digit is not found in previous char.
				foundNumber=true;
				Logger.out.debug("Found Number in Password");
			}	
		}
		// condition to check whether all above condotion is satisfied
		if(foundUCase==false||foundLCase==false||foundNumber==false||foundSpace==true)
		{
			Logger.out.debug("Password is is valid returning FAIL_IN_PATTERN");
			return FAIL_IN_PATTERN; // return int value 4
		}
		Logger.out.debug("Password is Valid returning SUCCESS");
		return SUCCESS;
	}
    /**
     * 
     * @param errorCode int value return by validate() method
     * @return String error message with respect to error code 
     */
    public static String getErrorMessage(int errorCode)
    {
    	String errMsg="";
    	
    	switch(errorCode)
    	{
    		case FAIL_LENGTH:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.length");
    			break;
    		case FAIL_SAME_AS_OLD:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameAsOld");
    			break;
    		case FAIL_SAME_AS_USERNAME:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameAsUserName");
    			break;
    		case FAIL_IN_PATTERN:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.pattern");
    			break;	
    		case FAIL_SAME_SESSION:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.sameSession");
    			break;
    		case FAIL_WRONG_OLD_PASSWORD:
    			errMsg=ApplicationProperties.getValue("errors.oldPassword.wrong");
    			break;
    		case FAIL_INVALID_SESSION:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.genericmessage");
				break;
    		default:
    			errMsg=ApplicationProperties.getValue("errors.newPassword.genericmessage");
				break;
    	}	
    	return errMsg;
    	
    }
    public static void main(String[] args)
    {
    	String pwd = "forgot";
    	String encodedPWD = encode(pwd);
    	System.out.println("encodedPWD "+encodedPWD);
        System.out.println(decode("6c416f576765696c6e63316f326d3365"));
        System.out.println("Package :  " + PasswordManager.class.getPackage().getName());  

        //Mandar 08-May-06
        if(args.length > 1 )
    	{
    		String filename = args[0];
    		String password = args[1];
    		encodedPWD = encode(password);
    		System.out.println("Filename : "+filename + " : password : "+password+" : encoded"+encodedPWD ); 
    		writeToFile(filename,encodedPWD  );
    	}
    }
    
    /**
     * This method writes the encoded password to the file.
     * @param filename File to be written. 
     * @param encodedPassword Encoded password.
     */
	public static void writeToFile(String filename,String encodedPassword)
    {
    	try
		{
    		File fileObject = new File(filename );
    		FileWriter writeObject = new FileWriter(fileObject );
    		
    		writeObject.write("first.admin.encodedPassword="+encodedPassword+"\n" ) ;
			writeObject.close();

		}
    	catch(Exception ioe)
		{
    		System.out.println("Error : " + ioe);
		}
    }

}
