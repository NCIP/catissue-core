
package edu.wustl.catissuecore.caties.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import edu.wustl.common.util.logger.Logger;

/**
 * Common class which is a thread to stop caties servers depending on there port.
 * @author vijay_pande
 */
public class StopServer extends Thread
{
	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(StopServer.class);
	/**
	 * port.
	 */
	private String port;

	/**
	 * Constructor for the class.
	 * @param port key to fetch port number from catissueCore-properties.xml file
	 */
	public StopServer(String port)
	{
		this.port = port;
	}

	/**
	 * Default method for the thread, which listen to socket and stop current thread when recieve any responce.
	 */
	public void run()
	{
		try
		{
			// get port number for server from catissueCore-properties.xml file
			int port = Integer.parseInt(CaTIESProperties.getValue(this.port));
			ServerSocket serv = new ServerSocket(port);
			BufferedReader r;
			Socket sock = serv.accept();
			r = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
			String str = r.readLine();

			logger.info("Stopping server");
			r.close();
			sock.close();
			// close server socket
			serv.close();
			// stop server
			System.exit(0);
		}
		catch (Exception e)
		{
			logger.error("Error stopping server ", e);
		}
	}
}
