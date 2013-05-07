/**
 * CAService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package clinical_annotation.stubs.service;

public interface CAService extends javax.xml.rpc.Service {
    public java.lang.String getCAPortTypePortAddress();

    public clinical_annotation.stubs.CAPortType getCAPortTypePort() throws javax.xml.rpc.ServiceException;

    public clinical_annotation.stubs.CAPortType getCAPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
