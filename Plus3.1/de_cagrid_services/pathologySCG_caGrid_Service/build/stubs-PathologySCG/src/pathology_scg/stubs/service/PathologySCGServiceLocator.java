/**
 * PathologySCGServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package pathology_scg.stubs.service;

public class PathologySCGServiceLocator extends org.apache.axis.client.Service implements pathology_scg.stubs.service.PathologySCGService {

    public PathologySCGServiceLocator() {
    }


    public PathologySCGServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PathologySCGServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PathologySCGPortTypePort
    private java.lang.String PathologySCGPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getPathologySCGPortTypePortAddress() {
        return PathologySCGPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PathologySCGPortTypePortWSDDServiceName = "PathologySCGPortTypePort";

    public java.lang.String getPathologySCGPortTypePortWSDDServiceName() {
        return PathologySCGPortTypePortWSDDServiceName;
    }

    public void setPathologySCGPortTypePortWSDDServiceName(java.lang.String name) {
        PathologySCGPortTypePortWSDDServiceName = name;
    }

    public pathology_scg.stubs.PathologySCGPortType getPathologySCGPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PathologySCGPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPathologySCGPortTypePort(endpoint);
    }

    public pathology_scg.stubs.PathologySCGPortType getPathologySCGPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            pathology_scg.stubs.bindings.PathologySCGPortTypeSOAPBindingStub _stub = new pathology_scg.stubs.bindings.PathologySCGPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getPathologySCGPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPathologySCGPortTypePortEndpointAddress(java.lang.String address) {
        PathologySCGPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (pathology_scg.stubs.PathologySCGPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                pathology_scg.stubs.bindings.PathologySCGPortTypeSOAPBindingStub _stub = new pathology_scg.stubs.bindings.PathologySCGPortTypeSOAPBindingStub(new java.net.URL(PathologySCGPortTypePort_address), this);
                _stub.setPortName(getPathologySCGPortTypePortWSDDServiceName());
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
        if ("PathologySCGPortTypePort".equals(inputPortName)) {
            return getPathologySCGPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pathology_scg/PathologySCG/service", "PathologySCGService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pathology_scg/PathologySCG/service", "PathologySCGPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("PathologySCGPortTypePort".equals(portName)) {
            setPathologySCGPortTypePortEndpointAddress(address);
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
