/*
 * <p>Title: AppletServerCommunicator.java</p>
 * <p>Description:	This class initializes the fields of AppletServerCommunicator.java</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on Sep 15, 2006
 */

package edu.wustl.catissuecore.applet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.common.util.logger.Logger;

/**
 * @author
 *
 */
public class AppletServerCommunicator implements Serializable
{

	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(AppletServerCommunicator.class);
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -5749961403859624336L;

	/**
	 * This method will open an HttpCommunication with the
	 *  servlet/Server side comp. And returns the AppletModelInterface
	 *
	 * @param urlString
	 *            The url of server side component (Servlet/Struts Action)
	 * @param appletModelInterface
	 *            The Applet model interface to be sent to server
	 * @return AppletModelInterface
	 * @throws IOException
	 *             If IOException occurs
	 * @throws ClassNotFoundException
	 *             If ClassNotFoundException occurs
	 * @see edu.wustl.catissuecore.appletui.model.AppletModelInterface
	 */
	public static AppletModelInterface doAppletServerCommunication(String urlString,
			AppletModelInterface appletModelInterface) throws IOException, ClassNotFoundException
	{

		final URL url = new URL(urlString);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		//System.out.println("connection object created");
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		//System.out.println("connection.connect() method commented");
		//connection.connect();
		final ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
		outputStream.writeObject(appletModelInterface);
		//System.out.println(" flush() is uncommented");
		outputStream.flush();
		outputStream.close();

		ObjectInputStream inputStream = null;
		Object appletObject = null;
		try
		{
			inputStream = new ObjectInputStream(connection.getInputStream());
			appletObject = inputStream.readObject();
			appletModelInterface = (AppletModelInterface) appletObject;
		}
		catch (final IOException e)
		{
			AppletServerCommunicator.logger.error(e.getMessage(), e);
			//	System.out.println(" IO Exception " + e);
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			AppletServerCommunicator.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			//System.out.println("Before disconnect in finally");
			connection.disconnect();
		}
		if (inputStream != null)
		{
			inputStream.close();
		}

		//System.out.println("After close() ");
		return appletModelInterface;
	}
}
