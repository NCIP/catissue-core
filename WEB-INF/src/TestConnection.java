import net.sf.hibernate.Session;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.dbManager.DBUtil;

/*
 * Created on Aug 29, 2005
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
public class TestConnection
{
	public static void main(String[] args) throws Exception
	{
//		Class.forName("org.gjt.mm.mysql.Driver");
//		Connection conn = DriverManager.getConnection(
//				"jdbc:mysql://navsari:3306/catissuecore",
//				"catissue_core",
//				"catissue_core");
//		System.out.println("Connection "+conn.getMetaData().getURL());
//		conn.close();
		
		Variables.catissueHome = System.getProperty("user.dir");
		Session session = DBUtil.currentSession();
		System.out.println("session "+session);
		System.out.println("Connection "+session.connection().getMetaData().getURL());
		
		DBUtil.closeSession();
	}
}
