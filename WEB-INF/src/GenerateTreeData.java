import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/*
 * Created on Sep 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenerateTreeData
{
	private static String[] parseLine(String str)
	{
		String data[] = new String[3];
		
		StringTokenizer strTk = new StringTokenizer(str,"\t");
		//System.out.println("strTk "+strTk.countTokens());
		
		data[0] = strTk.nextToken(); 
		data[1] = strTk.nextToken();
		if(strTk.hasMoreTokens())
			data[2] = strTk.nextToken();
		
		return data;
	}
	
	private static String createStatement(String data[], String publicId )
	{
		String str = "INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES (";
		str = str + "'" + data[0] + "' , ";
		str = str + "'" + data[1] + "' , ";
		
		if(data[2]!=null)
			str = str + "'" + data[2] + "',";
		else
			str = str + data[2] + ",";
		
		str = str + "'" + publicId + "');";
		System.out.println(str);
		return str;
		
//		  (1, 'C34022','Anatomic Surface','C34022',null,'Anatomic Surface','2003987' );

	}
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader("Tissue_Site.txt"));
		String line = "";
		while((line=reader.readLine())!=null)
		{
			//System.out.println(line);
			String data[] = parseLine(line);
			
			String statement = createStatement(data,"Tissue_Site_PID");
		}
	}
}
