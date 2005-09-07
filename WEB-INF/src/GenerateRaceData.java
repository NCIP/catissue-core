import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
public class GenerateRaceData
{
	private static String[] parseLine(String str)
	{
		String data[] = new String[2];
		
		StringTokenizer strTk = new StringTokenizer(str,"\t");
		//System.out.println("strTk "+strTk.countTokens());
		
		data[0] = strTk.nextToken(); 
		data[1] = strTk.nextToken();
		
		return data;
	}
	
	private static String createStatement(String data[], String publicId )
	{
		String str = "INSERT INTO CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER, VALUE, PARENT_IDENTIFIER, PUBLIC_ID) VALUES (";
		str = str + "'" + data[0] + "' , ";
		str = str + "'" + data[1] + "' , ";
		
		String pid = null;
		int index = data[0].lastIndexOf(".");
		if(index!=-1)
		{
			pid = data[0].substring(0, index);
		}
		
		if(pid!=null)
			str = str + "'" + pid + "',";
		else
			str = str + pid + ",";
		
		str = str + "'" + publicId + "');";
		System.out.println(str);
		return str;
		
//		  (1, 'C34022','Anatomic Surface','C34022',null,'Anatomic Surface','2003987' );

	}
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("Ethnicity-out.txt"));
		BufferedReader reader = new BufferedReader(new FileReader("Ethnicity.xls"));
		String line = "";
		while((line=reader.readLine())!=null)
		{
			if(!line.trim().equals(""))
			{
				//System.out.println(line);
				String data[] = parseLine(line);
				
				String statement = createStatement(data,"Ethnicity_PID");
				writer.write(statement+"\n");
			}
		}
		writer.close();
	}
}
