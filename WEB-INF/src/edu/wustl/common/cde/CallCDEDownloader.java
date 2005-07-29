package edu.wustl.common.cde;

import edu.wustl.common.util.logger.Logger;

public class CallCDEDownloader
{

	public static void main(String[] args) throws Exception
	{
		String proxyhostip = "10.33.0.1";
		String vocabularyName = "NCI_Thesaurus";
//		String vocabularyName = "SNOMED_CT";
		String proxyport = "80";

		String username = "mandar_deshmukh";
		String password = "A!b2c3" ;
		String dbserver = "http://cabio.nci.nih.gov/cacore30/server/HTTPServer";
		String publicId = "106" ;
		int limit = 2;

		CDEConConfig con = new CDEConConfig(proxyhostip, proxyport, username, password, dbserver);
		

		CDEDownloader aCDEDownloader = new CDEDownloader();


		// to remove the Logger configuration from the file
		System.setProperty("catissue.home","D:\\tomcat\\webapps\\catissuecore"+ "/Logs");
		Logger.configure("D:\\tomcat\\webapps\\catissuecore\\WEB-INF\\classes\\ApplicationResources.properties");
		System.out.println(Logger.out);

		System.out.println("\n============ START ========================\n");
		CDE aa = aCDEDownloader.loadCDE(con,publicId,vocabularyName,limit);
//		System.out.println("\n\n\n" + aa.getLongName() + "\t"+aa.getPublicId() ); 
		System.out.println("\n============= END ======================\n");
	} // main

} // callcdedownloader
