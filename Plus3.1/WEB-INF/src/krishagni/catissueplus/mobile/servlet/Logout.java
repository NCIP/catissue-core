package krishagni.catissueplus.mobile.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class Logout   extends HttpServlet{
	
	/**
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,
			IOException
	{
		doPost(req, res);
	}

	
	/**
	 * This method is used to download files saved in database.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{		
		 final HttpSession session = request.getSession();

	        // Delete Advance Query table if exists
	     final SessionDataBean sessionData = (edu.wustl.common.beans.SessionDataBean) session.getAttribute(Constants.SESSION_DATA);;
	        // Advance Query table name with userID attached
	     if(sessionData!=null){
		     final String tempTableName = Constants.QUERY_RESULTS_TABLE + "_" + sessionData.getUserId();
	
		/*        final JDBCDAO jdbcDao = AppUtility.openJDBCSession();
		        jdbcDao.deleteTable(tempTableName);
		        AppUtility.closeJDBCSession(jdbcDao);
	*/
		     session.invalidate();
	     }

	}

}
