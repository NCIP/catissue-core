
package edu.wustl.catissuecore.interceptor.wmq;

import java.io.IOException;
import java.sql.Timestamp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 *
 *
 */

public class MessageQueueListener implements MessageListener
{
	/**
	 * The logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(MessageQueueListener.class);

	/** The xml string. */
	private transient String xmlString;

	private final transient QueueSession wmqSession;
	private static String mqQueueName = "";


	/**
	 * Constructor for the message queue listener.
	 * @param appUrl application URL.
	 * @param loginId userName of the application.
	 * @param loginPassword password of the application.
	 */
	public MessageQueueListener(String messageQueueName,QueueSession session)
	{
		mqQueueName = messageQueueName;
		wmqSession = session;
	}

	/**
	 * Check server status.
	 *
	 * @return true, if successful
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private boolean checkServerStatus()
	{
		boolean isJbossAlive = false;
		/*try
		{
			LOGGER.info("Checking Jboss server status.");
			URL url = new URL(applicationUrl);
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			final int code = conn.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK)
			{
				isJbossAlive = true;
				LOGGER.info("Is Clinportal Jboss server available :"+isJbossAlive);
			}
			else
			{
				LOGGER.warn("Error connecting to server.Server returned code: " + code);
			}
		}
		catch(IOException e)
		{
			//isJbossAlive = false;
			LOGGER.error("Exception occurred while checking clinportal server status." + e.getMessage());
			LOGGER.debug("Exception occurred while checking clinportal server status.",e);
		}*/
		return isJbossAlive;
	}


	/**
	 * Write message back to queue.
	 *
	 * @param textMessage the text message
	 */
	public void  writeMessageToQueue(final TextMessage textMessage)
	{
		try
		{
			final Queue senderQueue = wmqSession.createQueue(mqQueueName);
			final QueueSender queueSender = wmqSession.createSender(senderQueue);

			final TextMessage outMessage = wmqSession.createTextMessage();
			outMessage.setText(textMessage.getText());
			queueSender.send(outMessage);
			queueSender.close();
		}
		catch(JMSException e)
		{
			LOGGER.debug("Exception occurred while writing message back to Queue.");
		}
	}
	/**
	 * @param message
	 * This methods is called by JMS when a message is available in Queue
	 */
	public void onMessage(final Message message)
	{
		if (message instanceof TextMessage)
		{
			try
			{
				final TextMessage textMessage = (TextMessage) message;
					//Process message if JBoss is available.
					LOGGER.info("Server is available, so processing messages");
					xmlString = textMessage.getText();
			}
			catch (JMSException e)
			{

			}

		}
	}

	/**
	 * This method returns the current time stamp required for file name
	 * @return timeStamp
	 */
	private String getFileNameTimestamp()
	{
		java.util.Date today = new java.util.Date();
	    Timestamp todayTimestamp = new  Timestamp(today.getTime());
	    String timeStamp  =todayTimestamp.toString();
	    timeStamp = timeStamp.replace(":","-");
		timeStamp = timeStamp.replace(" ","_");
		timeStamp = timeStamp.replace(".","-");
	    return timeStamp;
	}





}
