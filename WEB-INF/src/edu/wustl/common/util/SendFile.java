package edu.wustl.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import edu.wustl.common.util.logger.Logger;

public class SendFile 
{
	public static void sendFileToClient(HttpServletResponse response,String filePath,String fileName)
	{
		//String filename = null;
		//String fileName = null;
		StringBuffer sb = new StringBuffer();
		//	        HttpSession session = null;
		//indicates the invertigator id
		int invid;
		String prefix = null;
		try
		{
			//filename = Variables.absolutePathForDownloadRoot+"/"+request.getParameter("idno")+"/"+request.getParameter("dwnFile");
			//fileName = request.getParameter("dwnFile");
			//prefix = Constants.Logger_Inv+ ": ";
			
			//Getting the DownLoad Bean object from the session object
			if ( filePath != null && (false == (filePath.length()==0)) )
			{
				File f = new File(filePath);
				//Logger.log(prefix+"FileName to Download = "+filename,Logger.INFO);
				if (f.exists())
				{
					response.setContentType("application/csv");
					response.setHeader("Content-Disposition", " filename=\""+fileName+"\";");
					response.setContentLength((int) f.length());
					
					try
					{
						OutputStream os = response.getOutputStream();
						//writer
						
						BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
						
						  int count;
	                        byte buf[] = new byte[4096];
	                        
	                        while ((count = bis.read(buf)) > -1) {
	                            os.write(buf, 0, count);
	                        }
							
						os.flush();
						bis.close();
						f.delete();
						//os.close();
					}
					catch (Exception ex)
					{
						throw new Exception(ex.getMessage());
					}//end try/catch
				}
				else
				{
					
					throw new Exception("Sorry Cannot Download as fileName is null");
				}//end if
			}//end if    
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			//	            boolean success = (new File(filename)).delete();
			//Logger.log(prefix+"Sorry Cannot Download - Error Occured = "+e.getMessage(),Logger.WARNING);
			//	            if (!success)
			//	            {
			//	                // Deletion failed
			//	                Logger.log(prefix+"deletion failed for file  "+filename,Logger.INFO);
			//	             
			//	            }
		}
	}

}
