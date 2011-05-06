
package edu.wustl.catissuecore.interceptor.wmq;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueueConnectionFactory;

/**
 * This class starts the message listening from Queue.
 */
public final class MessageListener
{

	private static final int ARGS_JMX_RMI_PORT = 6;
	private static final int ARGS_JMX_RMI_HOST = 5;
	private static final int ARGS_WMQ_MANAGER = 4;
	private static final int ARGS_WMQ_QUEUE_NAME = 3;
	private static final int ARGS_WMQ_PORT = 2;
	private static final int ARGS_WMQ_CHANNEL = 1;
	private static final int ARGS_WMQ_HOST = 0;
	/**
	 * The logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(MessageListener.class);

	/**
	 * Private Constructor
	 */
	private MessageListener()
	{

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
	 * Message queue name.
	 */
	private static String mqQueueName = "";

	private static String jmxRmiPort = "";

	private static String jmxRmiHost = "";

	/**
	 * Queue Receiver instance being wrapped.
	 */
	private static QueueReceiver queueReceiver;

	/** The connection. */
	private static QueueConnection connection;

	private static  QueueSession session;

	/**
	 *
	 * @param args
	 * @throws JMSException
	 */
	private static void init(final String[] args)
	{
		initializeWmqParameters(args);
		initializeRMIRegistryServer(args);

		// Creating Message Factory
		final MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
		try
		{
			factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			factory.setQueueManager(mqQueueManager);
			factory.setHostName(mqHost);
			factory.setChannel(mqChannel);
			factory.setPort(Integer.parseInt(mqHostPort));

			// Creating connection and initiating a session
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			TextMessage message = session.createTextMessage("test message");

			new MessageQueueListener(mqQueueName, session).writeMessageToQueue(message);
			// Reading message from Queue
			final Queue receiverQueue = session.createQueue(mqQueueName);
			queueReceiver = session.createReceiver(receiverQueue);

			// Setting Listener
			queueReceiver.setMessageListener(new MessageQueueListener(mqQueueName, session));

			// Inititalise the JMX Agent here
			//ListenerJMXAgent agent = new ListenerJMXAgent(jmxRmiHost, jmxRmiPort);

			// NOTE - This method will wait for a shutdown command send on the
			// JMX server. Hence it is a blocking call & should ALWAYS be the
			// last statement in this logical group of statements.
			//agent.manageListenerState(); // SHOULD ALWAYS BE THE LAST STATEMENT.
			LOGGER.error("Shutting down message listener ...");

			// The listener is going in shutdown mode.
			// NO STATEMENTS SHOULD BE HERE, UNLESS IT IS RELATED TO ANY CLEANUP
			// ACTIVITIES.

		}
		catch (JMSException e)
		{

		}
		finally
		{
			cleanup();
		}

		// while (true)
		// {
		//
		//
		// }
	}

	/**
	 * Cleanup.
	 */
	private static void cleanup()
	{
		try
		{
			if (queueReceiver != null)
			{
				queueReceiver.close();
			}
			if(session!=null)
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
	 * Initialize rmi registery server.
	 *
	 * @param args the args
	 * @throws MessageListenerException the message listener exception
	 */
	private static void initializeRMIRegistryServer(String[] args)
	{
		jmxRmiHost = args[ARGS_JMX_RMI_HOST];
		jmxRmiPort = args[ARGS_JMX_RMI_PORT];

		try
		{
			LocateRegistry.createRegistry(Integer.valueOf(jmxRmiPort));
			LOGGER.info("RMI registry ready on " + jmxRmiHost + ":" + jmxRmiPort);

		}
		catch (RemoteException e)
		{
			LOGGER.error("Unable to get the remote registry at " + jmxRmiHost + ":" + jmxRmiPort
					+ "\n", e);

		}

	}

	/**
	 * Initialize WMQ parameters.
	 *
	 * @param args
	 *            the arguments
	 */
	private static void initializeWmqParameters(final String[] args)
	{
		// All WMQ related variables are initialized
		mqHost = args[ARGS_WMQ_HOST];
		mqChannel = args[ARGS_WMQ_CHANNEL];
		mqHostPort = args[ARGS_WMQ_PORT];
		mqQueueName = args[ARGS_WMQ_QUEUE_NAME];
		mqQueueManager = args[ARGS_WMQ_MANAGER];
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the args
	 *
	 * @throws JMSException
	 *             the JMS exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args)
	{
//host channel port quename manager rmihost rmiport
		args = new String[13];
		args[0] = "192.168.1.2";
		args[1] = "QM_APPLE_CHANNEL";
		args[2] = "1415";
		args[3] = "queue:///Q1";
		args[4] = "QM_APPLE";
		args[5] = "localhost";
		args[6] = "1098";

		/*try
		{
			Constants.properties = new Properties();
			Constants.properties.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("ApplicationResources.properties"));
		}
		catch (IOException e)
		{
			LOGGER.debug("Can't Load MessageListsner.properties");
		}*/

		if (args.length < 12)
		{
			LOGGER.debug("incorrect arguments");
		}
		else
		{
			/**
			 * args sequence is WMQ Host, WMQ Channel, WMQ Port, WMQ Outbound
			 * queue
			 */

				init(args);

		}
	}
}
