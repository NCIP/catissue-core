/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import javax.xml.bind.JAXBElement;

import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Defines an interface for converting payloads contained in {@link DataQueue}
 * objects into actual caTissue domain object instances and back.
 * 
 * @author Denis G. Krylov
 * 
 */
public interface IDataConverter<T extends AbstractDomainObject> {

	/**
	 * Determines whether this converter can convert the given {@link DataQueue}
	 * item into a domain object. If true, this method will return the actual
	 * {@link Class} of the object, to which the conversion will be made.
	 * Otherwise, <code>null</code> is returned.
	 * 
	 * @param item
	 * @return
	 */
	Class<T> supports(DataQueue item);

	/**
	 * Extracts payload from the given {@link DataQueue} and converts it into a
	 * domain object.
	 * 
	 * @param item
	 * @return
	 */
	T convert(DataQueue item, IErrorsReporter errorsReporter);

	/**
	 * Similar to {@link #convert(DataQueue)}, except that conversion is applied
	 * to the given domain object instance.
	 * 
	 * @param domainObj
	 * @param item
	 */
	void convert(T domainObj, DataQueue item, IErrorsReporter errorsReporter);
	
	/**
	 * Converts an instance of domain class into its JAXB representation suitable for use in a service. 
	 * @param domainObj
	 * @return
	 */
	JAXBElement convert(T domainObj);

}
