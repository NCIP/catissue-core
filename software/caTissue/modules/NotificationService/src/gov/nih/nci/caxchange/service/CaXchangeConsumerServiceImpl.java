package gov.nih.nci.caxchange.service;

import gov.nih.nci.caxchange.consumer.CaXchangeConsumerException;
import gov.nih.nci.caxchange.consumer.CaXchangeConsumerFactory;
import gov.nih.nci.caxchange.consumer.CaXchangeMessageConsumer;

import java.rmi.RemoteException;

import messaging.ConsumerResponseMessage;
import messaging.Message;
import messaging.MessagePayload;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** 
 * Implements the caXchange consumer service. It extracts the payload from the caXchange Request
 * and invokes the caXchange consumer 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class CaXchangeConsumerServiceImpl extends CaXchangeConsumerServiceImplBase {
   Logger logger = LogManager.getLogger(CaXchangeConsumerServiceImpl.class);
	
	public CaXchangeConsumerServiceImpl() throws RemoteException {
		super();
	}
  /**
   * Processes a caXchange Request.
   * 
   * @param caXchangeRequestMessage
   * @return
   * @throws RemoteException
   */	
  public ConsumerResponseMessage process(Message caXchangeRequestMessage) throws RemoteException {
     String messageType = getMessageType(caXchangeRequestMessage);
     CaXchangeMessageConsumer consumer = getConsumer(messageType);
     Node node = extractPayload(caXchangeRequestMessage);
     Node response = consumer.process(node);
     ConsumerResponseMessage consumerResponseMessage = new ConsumerResponseMessage();
     if (response != null) {
    	MessageElement messageElement = null; 
    	if (response instanceof Document) {
    		messageElement = new MessageElement(((Document)response).getDocumentElement());	
    	}
    	else if (response instanceof Element) {
    		messageElement = new MessageElement((Element)response);	
    	}
    	else if (response instanceof Node) {
    		messageElement = new MessageElement(response.getOwnerDocument().getDocumentElement());	
    	}    
    	if (messageElement != null) {
           consumerResponseMessage.set_any(new MessageElement[]{messageElement});
    	}
     }
     return consumerResponseMessage;
  }
  
  /**
   * Extracts payload from the caXchange request message.
   * 
   * @param caXchangeRequestMessage
   * @return
   * @throws CaXchangeConsumerException
   */
  public Node extractPayload(Message caXchangeRequestMessage) throws CaXchangeConsumerException {
	  try {
	     MessagePayload messagePayload = caXchangeRequestMessage.getRequest().getBusinessMessagePayload();
	     MessageElement[] elements = messagePayload.get_any();
	     Node payload = null;
	     if ((elements!=null)&&(elements.length>0)) {
		     payload = elements[0].getAsDocument();
	     }
	     return payload;
	  }catch(Exception e){
		 logger.error("Error extracting payload.");
		 throw new CaXchangeConsumerException("Error extracting payload.");
	  }
  }
  /**
   * Gets the message type from the caXchange Request message.
   * 
   * @param caXchangeRequestMessage
   * @return
   * @throws CaXchangeConsumerException
   */
  public String getMessageType(Message caXchangeRequestMessage) throws CaXchangeConsumerException {
	  try {
		  String messageType = caXchangeRequestMessage.getMetadata().getServiceType();
		  return messageType;
	  }catch(Exception e){
			 logger.error("Error getting message type.");
			 throw new CaXchangeConsumerException("Error getting message type.");
	  }

  }
  /**
   * Gets the consumer configured for the given message type.
   * 
   * @param messageType
   * @return
   * @throws CaXchangeConsumerException
   */
  public CaXchangeMessageConsumer getConsumer(String messageType) throws CaXchangeConsumerException {
	  CaXchangeMessageConsumer consumer = CaXchangeConsumerFactory.getConsumer(messageType);
	  if (consumer == null) {
		  throw new CaXchangeConsumerException("No consumer configured for :"+messageType);
	  }
	  return CaXchangeConsumerFactory.getConsumer(messageType);
	  
  }
  
  
  
  
}

