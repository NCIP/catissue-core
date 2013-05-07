/**
 * PathologySpecimenServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package pathology_specimen.stubs.service;

public class PathologySpecimenServiceLocator extends org.apache.axis.client.Service implements pathology_specimen.stubs.service.PathologySpecimenService {

    public PathologySpecimenServiceLocator() {
    }


    public PathologySpecimenServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PathologySpecimenServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PathologySpecimenPortTypePort
    private java.lang.String PathologySpecimenPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getPathologySpecimenPortTypePortAddress() {
        return PathologySpecimenPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PathologySpecimenPortTypePortWSDDServiceName = "PathologySpecimenPortTypePort";

    public java.lang.String getPathologySpecimenPortTypePortWSDDServiceName() {
        return PathologySpecimenPortTypePortWSDDServiceName;
    }

    public void setPathologySpecimenPortTypePortWSDDServiceName(java.lang.String name) {
        PathologySpecimenPortTypePortWSDDServiceName = name;
    }

    public pathology_specimen.stubs.PathologySpecimenPortType getPathologySpecimenPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PathologySpecimenPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPathologySpecimenPortTypePort(endpoint);
    }

    public pathology_specimen.stubs.PathologySpecimenPortType getPathologySpecimenPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            pathology_specimen.stubs.bindings.PathologySpecimenPortTypeSOAPBindingStub _stub = new pathology_specimen.stubs.bindings.PathologySpecimenPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getPathologySpecimenPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPathologySpecimenPortTypePortEndpointAddress(java.lang.String address) {
        PathologySpecimenPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (pathology_specimen.stubs.PathologySpecimenPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                pathology_specimen.stubs.bindings.PathologySpecimenPortTypeSOAPBindingStub _stub = new pathology_specimen.stubs.bindings.PathologySpecimenPortTypeSOAPBindingStub(new java.net.URL(PathologySpecimenPortTypePort_address), this);
                _stub.setPortName(getPathologySpecimenPortTypePortWSDDServiceName());
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
        if ("PathologySpecimenPortTypePort".equals(inputPortName)) {
            return getPathologySpecimenPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pathology_specimen/PathologySpecimen/service", "PathologySpecimenService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pathology_specimen/PathologySpecimen/service", "PathologySpecimenPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("PathologySpecimenPortTypePort".equals(portName)) {
            setPathologySpecimenPortTypePortEndpointAddress(address);
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
