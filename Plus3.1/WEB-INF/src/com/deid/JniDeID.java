/**
 * @file    JniDeid.java
 * @author  Paul Hanbury (University of Pittsburgh, Health Sciences IT)
 *
 * @note
 * The JniDeid class provides a java wrapper for the De-ID library.  The
 * native functions are included with the JniDeid.DLL which is included
 * with the De-ID distribution.  (If you cannot find this file on your
 * computer, reinstall De-ID making sure that the Java Library option
 * is selected.)
 *
 * JniDeid.DLL is simply a JNI wrapper for the DeID6.DLL.  Each function
 * in JniDeid called on of the exposed functions in that library.
 *
 * To use this class make sure that the folder containing the DLL is
 * included in your Windows PATH variable.  You also must make sure that
 * its subfolder is included in your CLASSPATH.
 *
 * For example, if you create a class called Foo which uses JniDeid, it
 * can be comiled and run (assuming that De-ID is installed in the
 * default c:\\Program Files\\DeID folder) using the following commands:
 *
 *  @code
      javac -classpath "c:\program files" Foo.java
      java -classpath ".;c:\program files" Foo
    @endcode
 *
 *  @sa  deid_dll.h
*/

package com.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import edu.wustl.common.util.logger.Logger;

/**
 *
 * Wrapper class for calling native methods for deidentification.
 */
public class JniDeID
{

	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(JniDeID.class);

	/**
	 * loadDeidLibrary maps the DeID6 library into the applications.
	 *  addressspace via the Windows API function LoadLibrary
	 *  @return boolean
	 */
	public native static boolean loadDeidLibrary();

	/**
	 * CreateDeidentifier calls the CreateDeidentifier function in DeID6.DLL.
	 * The parameters here, correspond to the paramteters in the DLL function.
	 * @sa deid_dll.h CreateDeidentifier
	 * @param inputStr input string for deindentification
	 * @param outputStr deidentified output string
	 * @param configFile confguration file name
	 */
	public native void CreateDeidentifier(String inputStr, String outputStr, String configFile);

	/**
	 * CreateDeidentifierEx calls the CreateDeidentifierEx function in DeID6.DLL.
	 * @param inputStr input string for deindentification
	 * @param outputStr deidentified output string
	 * @param configFile confguration file name
	 * @param dnyLocation dictionary location
	 */
	public native void CreateDeidentifierEx(String inputStr, String outputStr, String configFile,
			String dnyLocation);

	/** unLoadDeidLibrary unmaps the DeID6 library from
	 *  the applications addressspace via the Windows API function FreeLibrary.
	 * @return boolean
	 */
	public native static boolean unloadDeidLibrary();

	/**
	 * This method is to load deid library using absolute path.
	 */
	private static void loadUsingAbsolutePath()
	{
		try
		{
			new JniDeID();
			final String appHome = System.getProperty("user.dir");
			String dllPath = appHome + File.separator + "JniDeId.dll";
			dllPath = dllPath.replaceAll("%20", " ");
			final File dllFile = new File(dllPath);
			dllPath = dllFile.getAbsolutePath();
			System.load(dllPath);
			logger.info("Loading dll file at " + dllPath);
		}
		catch (final Exception excep)
		{
			logger.error("Error in method loadUsingAbsolutePath of JniDeID",excep);
			excep.printStackTrace();
		}
	}

	/**
	 * main method for the JniDeID.
	 * @param str array CommandLine arguments
	 *
	 */
	public static void main(String[] str)
	{
		//loadUsingAbsolutePath();
		try
		{
			final JniDeID dummy = new JniDeID();
			System.load("D:\\testcodes\\caTIES_v2\\classes\\com\\deid\\JniDeID.dll");
			final File f = new File("predeid.xml");
			final File f2 = new File("D:\\testcodes\\caTIES_v2\\classes\\postdeid.tmp");
			final FileWriter fw = new FileWriter(f);
			fw.write("sdfsssd");
			fw.close();

			dummy.createDeidentifier(f.getAbsolutePath(), f2.getAbsolutePath() + "?XML",
					"D:\\testcodes\\caTIES_v2\\classes\\com\\deid");
			new BufferedReader(new FileReader(f2));
		}
		catch (final Exception excep)
		{
			logger.error("Error in main method of JniDeID",excep);
			excep.printStackTrace() ;
		}

	}

	/**
	 * In the static initializer section, we make sure that the JniDeid and the
	 * DeID6 libraries will be accessible when called.
	 *
	 * @pre The libraries are contained in the same folder as the JniDeid class
	 *      file. This is the defualt setting used by the De-ID setup program.
	 */
	static
	{
		loadUsingAbsolutePath();
	}

	/**
	 * This method call the native method CreateDeidentifierEx which is in DeID6.dll.
	 * @sa CreateDeidentifier
	 * @param inputStr input string for deindentification
	 * @param outputStr deidentified output string
	 * @param configFile confguration file name
	 */
	public void createDeidentifier(String inputStr, String outputStr, String configFile)
	{
		//
		// If the user specified a dictionary location, we use that.  Otherwise
		// we let De-ID try to guess the correct location.  This could be dangerous in
		// a Java application, since the Windows API call GetModuleFileName will often
		// assume that it should use the folder that conatins your "java.exe" file.
		if (this.dnyFolder.length() > 0)
		{
			this.CreateDeidentifierEx(inputStr, outputStr, configFile, this.dnyFolder);
		}
		else
		{
			this.CreateDeidentifier(inputStr, outputStr, configFile);
		}
	}

	/**
	 * Saves the location of the deiddata.bin file.
	 * @param folderName dictionary location
	 */
	public void setDictionaryLocation(String folderName)
	{
		this.dnyFolder = folderName;
	}

	/**
	 * dnyFolder.
	 */
	private String dnyFolder = new String();
}