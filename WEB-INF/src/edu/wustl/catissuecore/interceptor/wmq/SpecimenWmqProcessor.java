
package edu.wustl.catissuecore.interceptor.wmq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * This class initializes the framework for inserting the messages in the Queue.
 */
public final class SpecimenWmqProcessor
{

	private static SpecimenWmqProcessor processor;

	/**
	 * The logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SpecimenWmqProcessor.class);

	/**
	 * Private Constructor
	 */
	private SpecimenWmqProcessor()
	{

	}

	/**
	 * getInstance method to make it singleton.
	 * @return self instance.
	 */
	public static synchronized SpecimenWmqProcessor getInstance()
	{
		if (processor == null)
		{
			processor = new SpecimenWmqProcessor();
			init();
		}

		return processor;
	}

	/**
	 * Message Queue Host Machine
	 */
	private static String mqHost = "";
	/**
	 * Message queue host machine port.
	 */
	private static String mqHostPort = "";
	/**
	 * Message queue host channel.
	 */
	private static String mqChannel = "";
	/**
	 * Message queue queue manager.
	 */
	private static String mqQueueManager = "";

	/**
	 * Specifies whether or not WMQ is enabled or not.
	 */
	private static boolean isEnabled =false;
	/**
	 * sender Message queue name.
	 */
	private static String senderQueueName = "";




	/** The connection. */
	private static QueueConnection connection;

	private static QueueSession session;

	/**
	 *This method initializes the framework required to send messages on the queue.
	 */
	private static void init()
	{
		initializeWmqParameters();

		// Creating Message Factory
		final MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		try
		{
			if(isEnabled)
			{
				factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
				factory.setQueueManager(mqQueueManager);
				factory.setHostName(mqHost);
				factory.setChannel(mqChannel);
				factory.setPort(Integer.parseInt(mqHostPort));

				// Creating connection and initiating a session
				connection = factory.createQueueConnection();
				connection.start();
				session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			}

		}
		catch (JMSException e)
		{
			LOGGER.error("Error while creating WMQ connection with Cider.", e);
		}

	}

	/**
	 * Cleanup.
	 */
	public static void cleanup()
	{
		try
		{
			if (session != null)
			{
				session.close();
			}
			if (connection != null)
			{
				connection.close();
			}
			LOGGER.info("Cleaned up WMQ connections!!");
		}
		catch (JMSException e)
		{

		}
	}

	/**
	 * Initialize WMQ parameters.
	 */
	private static void initializeWmqParameters()
	{
		// All WMQ related variables are initialized
		mqHost = XMLPropertyHandler.getValue("CiderWMQServerName");
		mqChannel = XMLPropertyHandler.getValue("CiderWMQChannel");
		mqHostPort = XMLPropertyHandler.getValue("CiderWMQPort");
		senderQueueName ="queue:///"+ XMLPropertyHandler.getValue("CiderOutBoundQueue");
		//recieverQueueName = XMLPropertyHandler.getValue("CiderInBoundQueue");
		mqQueueManager = XMLPropertyHandler.getValue("CiderWMQMGRName");
		if(Constants.TRUE.equalsIgnoreCase(XMLPropertyHandler.getValue("CiderWmqEnabled")))
		{
			isEnabled=true;
		}
	}

	/**
	 * Write message back to queue.
	 *
	 * @param fileName name of file whose contents should be sent in queue
	 * @throws JMSException
	 */
	public void writeMessageToQueue(final String fileName) throws JMSException
	{
		if(isEnabled)
		{
			final TextMessage outMessage = session.createTextMessage();
			final StringBuilder fileContents = readFileContents(fileName);
			if (fileContents != null)
			{
				LOGGER.info("Contents to Queue are written from file :" + fileName);
				outMessage.setText(fileContents.toString());
			}
			final Queue senderQueue = session.createQueue(senderQueueName);
			final QueueSender queueSender = session.createSender(senderQueue);

			queueSender.send(outMessage);
			queueSender.close();
		}

	}

	/**
	  * This method reads q	contents from file.
	  * @param fileName name of the file.
	  * @return contents : String buffer
	  * @throws IOException
	  */
	private static StringBuilder readFileContents(String fileName)
	{
		final File file = new File(fileName);
		final StringBuilder contents = new StringBuilder();
		try
		{
			final BufferedReader input = new BufferedReader(new FileReader(file));
			String line = null;

			while ((line = input.readLine()) != null)
			{
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		}
		catch (IOException e)
		{
			LOGGER.error("Unable to read contents from XMl file.");
			//  throw new MessageListenerException("Error in opening file.",e);
		}

		return contents;
	}


	/*public static void main(String[] args) throws PasswordEncryptionException
	{
		System.out.println(URLEncoder.encode(PasswordManager.encrypt("formId=71&participantId=2&csmUserId=1&collectionProtocolEventId=1&collectionProtocolId=1&method=encryptData&eventEntryId=36198&visitNumber=1")));
		//PasswordManager.decrypt("zgfrFY7pd0PCWQlj4j0ShTAuzDpYz8bbFBwgbT8tBTDkd%252BpglfmTd%252B5k1N8%252FkJvuOI%252FZpclGdfWs%250AspdKxkWlcjCraHUekCOVRrRKOL7GXLsgpIKzqj7fUQIMc9VqN%252FQnyTxGf7XNCE1SMju7%252FO4IOPD2%250A43bhmUS5YIV%252Bjfy7rRkF2e8fFpmArZ6am7ZWYw33%252Fd3XmxTf%252Frhovl7sWqRXTw%253D%253D");
		final MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		try
		{
			factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			factory.setQueueManager("QM_APPLE");
			factory.setHostName("pavan-lappy");
			factory.setChannel("QM_APPLE_CHANNEL");
			factory.setPort(Integer.parseInt("1415"));

			// Creating connection and initiating a session
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// Reading message from Queue
			final Queue receiverQueue = session.createQueue("queue:///Q1");
			queueReceiver = session.createReceiver(receiverQueue);

			//queueReceiver.setMessageListener(new SpecimenWmqProcessor());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
