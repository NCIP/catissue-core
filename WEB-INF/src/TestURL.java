import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.InstitutionForm;

/*
 * Created on Mar 31, 2005
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
public class TestURL
{
	public static void maina(String[] args) throws Exception
	{
		System.out.println(String.class.getName());
		
	}
	public static void main(String[] args) throws Exception
	{
		InstitutionForm insForm = new InstitutionForm();
		insForm.setName("Test3");
		insForm.setOperation("add");
		
		
		URL aURL = new URL("http://localhost:8080/catissuecore/InstituteAddHTTP.do");
		URLConnection aConn = aURL.openConnection();
		
		aConn.setDoOutput(true);
		aConn.setRequestProperty("CONTENT-TYPE","BI");
		System.out.println(aConn.getRequestProperties());
		ObjectOutputStream oos = new ObjectOutputStream (aConn.getOutputStream());
		oos.writeObject(insForm);
		oos.flush();
		oos.close();
		
		ObjectInputStream ois = new ObjectInputStream(aConn.getInputStream());
		Object obj = ois.readObject();
		ois.close();
		System.out.println(obj);
		
		ActionMessages msgs = (ActionMessages)obj;
		
		Iterator it = msgs.get();
		while(it.hasNext())
		{
			ActionMessage msg = (ActionMessage)it.next();
			System.out.println(msg);
			System.out.println(msg.getKey());
			Object[] objects = msg.getValues();
			for (int i = 0; i < objects.length; i++)
			{
				System.out.println(objects[i]);
			}
			System.out.println();
		}
//		oos.flush();
//		oos.close();
		
//		BufferedReader aBuffReader = new BufferedReader(new InputStreamReader(aConn.getInputStream()));
//		String str = "";
//		while((str=aBuffReader.readLine())!=null)
//		{
//			System.out.println(str);
//		}
	}
}
