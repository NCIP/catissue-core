/**
 * Catissue_DEServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package de_entity.stubs.service;

public class Catissue_DEServiceLocator extends org.apache.axis.client.Service implements de_entity.stubs.service.Catissue_DEService {

    public Catissue_DEServiceLocator() {
    }


    public Catissue_DEServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Catissue_DEServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Catissue_DEPortTypePort
    private java.lang.String Catissue_DEPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getCatissue_DEPortTypePortAddress() {
        return Catissue_DEPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Catissue_DEPortTypePortWSDDServiceName = "Catissue_DEPortTypePort";

    public java.lang.String getCatissue_DEPortTypePortWSDDServiceName() {
        return Catissue_DEPortTypePortWSDDServiceName;
    }

    public void setCatissue_DEPortTypePortWSDDServiceName(java.lang.String name) {
        Catissue_DEPortTypePortWSDDServiceName = name;
    }

    public de_entity.stubs.Catissue_DEPortType getCatissue_DEPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Catissue_DEPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCatissue_DEPortTypePort(endpoint);
    }

    public de_entity.stubs.Catissue_DEPortType getCatissue_DEPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de_entity.stubs.bindings.Catissue_DEPortTypeSOAPBindingStub _stub = new de_entity.stubs.bindings.Catissue_DEPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getCatissue_DEPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCatissue_DEPortTypePortEndpointAddress(java.lang.String address) {
        Catissue_DEPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de_entity.stubs.Catissue_DEPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                de_entity.stubs.bindings.Catissue_DEPortTypeSOAPBindingStub _stub = new de_entity.stubs.bindings.Catissue_DEPortTypeSOAPBindingStub(new java.net.URL(Catissue_DEPortTypePort_address), this);
                _stub.setPortName(getCatissue_DEPortTypePortWSDDServiceName());
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
        if ("Catissue_DEPortTypePort".equals(inputPortName)) {
            return getCatissue_DEPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://de_entity/Catissue_DE/service", "Catissue_DEService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://de_entity/Catissue_DE/service", "Catissue_DEPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("Catissue_DEPortTypePort".equals(portName)) {
            setCatissue_DEPortTypePortEndpointAddress(address);
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
