/**
 * 
 */
package edu.wustl.catissue.grid;

import edu.wustl.catissue.dao.NotificationDAO;
import edu.wustl.catissuecore.domain.ccts.Application;
import edu.wustl.catissuecore.domain.ccts.EventType;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.domain.ccts.ObjectIdType;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;
import gov.nih.nci.caxchange.consumer.CaXchangeConsumerException;
import gov.nih.nci.caxchange.consumer.CaXchangeMessageConsumer;

import java.io.IOException;
import java.util.Date;

import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.w3c.dom.Node;

/**
 * This class is responsible for receiving CCTS notifications from iHub,
 * validating them, and storing in caTissue's internal queue for further
 * processing.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class NotificationMessageConsumer implements
		CaXchangeMessageConsumer {

	private static final Log log = LogFactory
			.getLog(NotificationMessageConsumer.class);
	private static final String CATISSUE_GRID_NS = "edu.wustl.catissue.grid";

	private NotificationDAO notificationDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.caxchange.consumer.CaXchangeMessageConsumer#commit(org.w3c
	 * .dom.Node)
	 */
	public void commit(Node arg0) throws CaXchangeConsumerException {
		log.warn("edu.wustl.catissue.grid.NotificationMessageConsumer.commit(Node): nothing to do here. CCTS notifications do not participate in transactions. ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.caxchange.consumer.CaXchangeMessageConsumer#process(org.w3c
	 * .dom.Node)
	 */
	public Node process(Node node) throws CaXchangeConsumerException {
		try {
			DOMBuilder builder = new DOMBuilder();
			Document notification = builder.build((org.w3c.dom.Document) node);
			processNotification(notification);
			return getSuccessNode();
		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new CaXchangeConsumerException(e.toString(), e);
		}
	}

	private void processNotification(Document notification) {
		if (log.isDebugEnabled()) {
			log.debug("About to process the following notification:");
			log.debug(new XMLOutputter(Format.getPrettyFormat())
					.outputString(notification));
		}
		// decompose the notification into individual items, then pass them to
		// caTissue core.
		try {
			final Text appTextNode = (Text) xpath(
					"/ccts:notification/application/text()").selectSingleNode(
					notification);
			String application = appTextNode != null ? appTextNode
					.getTextTrim() : "";
			String eventType = ((Text) xpath(
					"/ccts:notification/eventType/text()").selectSingleNode(
					notification)).getTextTrim();
			String objIdValue = ((Text) xpath(
					"/ccts:notification/objectId/value/text()")
					.selectSingleNode(notification)).getTextTrim();
			String objIdType = ((Text) xpath(
					"/ccts:notification/objectId/type/text()")
					.selectSingleNode(notification)).getTextTrim();
			final Text tsTextNode = (Text) xpath(
					"/ccts:notification/timestamp/text()").selectSingleNode(
					notification);
			Date timestamp = tsTextNode == null ? null : DatatypeFactory
					.newInstance()
					.newXMLGregorianCalendar(tsTextNode.getTextNormalize())
					.toGregorianCalendar().getTime();
			saveNotification(application, eventType, objIdValue, objIdType,
					timestamp);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new RuntimeException(
					"Unable to parse the notification. Either the notification schema has changed or the notification has malformed data.",
					e);
		}
	}

	private void saveNotification(String application, String eventType,
			String objIdValue, String objIdType, Date timestamp) {
		Notification notification = new Notification();
		notification
				.setApplication(StringUtils.isNotBlank(application) ? Application
						.valueOf(application) : null);
		notification.setEventType(EventType.valueOf(eventType));
		notification.setObjectIdValue(objIdValue);
		notification.setObjectIdType(ObjectIdType.valueOf(objIdType));
		notification.setProcessingStatus(ProcessingStatus.PENDING);
		notification.setDateSent(timestamp != null ? timestamp : new Date());
		notification.setDateReceived(new Date());
		// give caTissue a hand in eliminating duplicate notifications.
		if (notificationDAO.getCountByExample(notification) == 0) {
			notificationDAO.save(notification);
		} else {
			log.info("This is a duplicate notification. It will not be saved: "
					+ notification);
		}
	}

	private static XPath xpath(String path) throws JDOMException {
		XPath xPath = XPath.newInstance(path);
		xPath.addNamespace("ccts",
				"gme://ccts.cabig/1.0/gov.nih.nci.cabig.ccts.domain/Notifications");
		return xPath;
	}

	private Node getSuccessNode() throws JDOMException {
		Element result = new Element("result", CATISSUE_GRID_NS);
		result.setText("OK");
		Document doc = new Document(result);
		DOMOutputter output = new DOMOutputter();
		return output.output(doc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.caxchange.consumer.CaXchangeMessageConsumer#rollback(org.
	 * w3c.dom.Node)
	 */
	public void rollback(Node arg0) throws CaXchangeConsumerException {
		log.warn("edu.wustl.catissue.grid.NotificationMessageConsumer.rollback(Node): nothing to do here. CCTS notifications do not participate in transactions. ");
	}

	public static void main(String[] args) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder
				.build("file:///C:/Projects/Misc/NewFile.xml");
		System.out.print(xpath("/ccts:notification/application/text()")
				.selectSingleNode(document));
	}

	/**
	 * @return the notificationDAO
	 */
	public final NotificationDAO getNotificationDAO() {
		return notificationDAO;
	}

	/**
	 * @param notificationDAO
	 *            the notificationDAO to set
	 */
	public final void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

}
