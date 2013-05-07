/**
 * CAServiceAddressingLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package clinical_annotation.stubs.service;

public class CAServiceAddressingLocator extends clinical_annotation.stubs.service.CAServiceLocator implements clinical_annotation.stubs.service.CAServiceAddressing {
    public clinical_annotation.stubs.CAPortType getCAPortTypePort(org.apache.axis.message.addressing.EndpointReferenceType reference) throws javax.xml.rpc.ServiceException {
	org.apache.axis.message.addressing.AttributedURI address = reference.getAddress();
	if (address == null) {
		throw new javax.xml.rpc.ServiceException("No address in EndpointReference");
	}
	java.net.URL endpoint;
	try {
		endpoint = new java.net.URL(address.toString());
	} catch (java.net.MalformedURLException e) {
		throw new javax.xml.rpc.ServiceException(e);
	}
	clinical_annotation.stubs.CAPortType _stub = getCAPortTypePort(endpoint);
	if (_stub != null) {
		org.apache.axis.message.addressing.AddressingHeaders headers =
			new org.apache.axis.message.addressing.AddressingHeaders();
		headers.setTo(address);
		headers.setReferenceProperties(reference.getProperties());
		((javax.xml.rpc.Stub)_stub)._setProperty(org.apache.axis.message.addressing.Constants.ENV_ADDRESSING_SHARED_HEADERS, headers);
	}
	return _stub;
    }


}
