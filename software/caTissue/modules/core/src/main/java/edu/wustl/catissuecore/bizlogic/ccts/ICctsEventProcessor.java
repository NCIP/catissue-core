/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import edu.wustl.catissuecore.domain.ccts.EventType;
import edu.wustl.catissuecore.domain.ccts.Notification;

/**
 * Defines an interface for processing of CCTS events. Implementors to implement
 * event-specific processing, such as Participant Creation/Update or Protocol
 * Registration.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface ICctsEventProcessor {

	/**
	 * Responds to an event by performing necessary processing of the CCTS-wide
	 * object identified by the given Grid ID. The object can be a Subject
	 * (Participant), a Registration (Protocol Registration), a Study Calendar,
	 * or something else.<br>
	 * <br>
	 * Callers are encouraged to use {@link #supports(EventType)} methods in
	 * order to determine whether or not this event processor can process the
	 * event.
	 * 
	 * @param gridId
	 * @return Payload associated with the event processing, if processing was
	 *         successful
	 * @throws ProcessingException
	 *             if processing was unsuccessful.
	 */
	String process(String gridId, Notification notification) throws ProcessingException;

	/**
	 * Returns true if this event processor can handle the given type of event.
	 * False otherwise.
	 * 
	 * @param eventType
	 * @return
	 */
	boolean supports(EventType eventType);

}
