/**
 * CAServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package clinical_annotation.stubs.service;

public class CAServiceLocator extends org.apache.axis.client.Service implements clinical_annotation.stubs.service.CAService {

    public CAServiceLocator() {
    }


    public CAServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CAServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CAPortTypePort
    private java.lang.String CAPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getCAPortTypePortAddress() {
        return CAPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CAPortTypePortWSDDServiceName = "CAPortTypePort";

    public java.lang.String getCAPortTypePortWSDDServiceName() {
        return CAPortTypePortWSDDServiceName;
    }

    public void setCAPortTypePortWSDDServiceName(java.lang.String name) {
        CAPortTypePortWSDDServiceName = name;
    }

    public clinical_annotation.stubs.CAPortType getCAPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CAPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCAPortTypePort(endpoint);
    }

    public clinical_annotation.stubs.CAPortType getCAPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            clinical_annotation.stubs.bindings.CAPortTypeSOAPBindingStub _stub = new clinical_annotation.stubs.bindings.CAPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getCAPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCAPortTypePortEndpointAddress(java.lang.String address) {
        CAPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (clinical_annotation.stubs.CAPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                clinical_annotation.stubs.bindings.CAPortTypeSOAPBindingStub _stub = new clinical_annotation.stubs.bindings.CAPortTypeSOAPBindingStub(new java.net.URL(CAPortTypePort_address), this);
                _stub.setPortName(getCAPortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CAPortTypePort".equals(inputPortName)) {
            return getCAPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://clinical_annotation/CA/service", "CAService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://clinical_annotation/CA/service", "CAPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("CAPortTypePort".equals(portName)) {
            setCAPortTypePortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
