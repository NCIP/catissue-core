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
		StringBuffer sb = new StringBuffer();
		int invid;
		String prefix = null;
		try
		{
			if ( filePath != null && (false == (filePath.length()==0)) )
			{
				File f = new File(filePath);
				if (f.exists())
				{
					response.setContentType("application/csv");
					response.setHeader("Content-Disposition", " filename=\""+fileName+"\";");
					response.setContentLength((int) f.length());
					try
					{
						OutputStream os = response.getOutputStream();
						BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
						int count;
	                    byte buf[] = new byte[4096];
                        while ((count = bis.read(buf)) > -1) 
                        {
                        	os.write(buf, 0, count);
	                    }
						os.flush();
						bis.close();
						f.delete();
					}
					catch (Exception ex)
					{
						throw new Exception(ex.getMessage());
					}
				}
				else
				{
					
					throw new Exception("Sorry Cannot Download as fileName is null");
				}
			}   
		}
		catch (Exception e)
		{
			Logger.out.error(e.getMessage(),e);
		}
	}
}
