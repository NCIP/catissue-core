/**
 * CAResourceProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package clinical_annotation.stubs;

public class CAResourceProperties  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel;
    private gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata;

    public CAResourceProperties() {
    }

    public CAResourceProperties(
           gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel,
           gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
           this.domainModel = domainModel;
           this.serviceMetadata = serviceMetadata;
    }


    /**
     * Gets the domainModel value for this CAResourceProperties.
     * 
     * @return domainModel
     */
    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel getDomainModel() {
        return domainModel;
    }


    /**
     * Sets the domainModel value for this CAResourceProperties.
     * 
     * @param domainModel
     */
    public void setDomainModel(gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel) {
        this.domainModel = domainModel;
    }


    /**
     * Gets the serviceMetadata value for this CAResourceProperties.
     * 
     * @return serviceMetadata
     */
    public gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }


    /**
     * Sets the serviceMetadata value for this CAResourceProperties.
     * 
     * @param serviceMetadata
     */
    public void setServiceMetadata(gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CAResourceProperties)) return false;
        CAResourceProperties other = (CAResourceProperties) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.domainModel==null && other.getDomainModel()==null) || 
             (this.domainModel!=null &&
              this.domainModel.equals(other.getDomainModel()))) &&
            ((this.serviceMetadata==null && other.getServiceMetadata()==null) || 
             (this.serviceMetadata!=null &&
              this.serviceMetadata.equals(other.getServiceMetadata())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDomainModel() != null) {
            _hashCode += getDomainModel().hashCode();
        }
        if (getServiceMetadata() != null) {
            _hashCode += getServiceMetadata().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CAResourceProperties.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://clinical_annotation/CA", ">CAResourceProperties"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domainModel");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceMetadata");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
