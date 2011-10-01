/**
 * Catissue_DEResourceProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package de_entity.stubs;

public class Catissue_DEResourceProperties  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel;
    private gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata;
    private org.cagrid.dataservice.cql.support.QueryLanguageSupport queryLanguageSupport;

    public Catissue_DEResourceProperties() {
    }

    public Catissue_DEResourceProperties(
           gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel,
           org.cagrid.dataservice.cql.support.QueryLanguageSupport queryLanguageSupport,
           gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
           this.domainModel = domainModel;
           this.serviceMetadata = serviceMetadata;
           this.queryLanguageSupport = queryLanguageSupport;
    }


    /**
     * Gets the domainModel value for this Catissue_DEResourceProperties.
     * 
     * @return domainModel
     */
    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel getDomainModel() {
        return domainModel;
    }


    /**
     * Sets the domainModel value for this Catissue_DEResourceProperties.
     * 
     * @param domainModel
     */
    public void setDomainModel(gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel) {
        this.domainModel = domainModel;
    }


    /**
     * Gets the serviceMetadata value for this Catissue_DEResourceProperties.
     * 
     * @return serviceMetadata
     */
    public gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }


    /**
     * Sets the serviceMetadata value for this Catissue_DEResourceProperties.
     * 
     * @param serviceMetadata
     */
    public void setServiceMetadata(gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }


    /**
     * Gets the queryLanguageSupport value for this Catissue_DEResourceProperties.
     * 
     * @return queryLanguageSupport
     */
    public org.cagrid.dataservice.cql.support.QueryLanguageSupport getQueryLanguageSupport() {
        return queryLanguageSupport;
    }


    /**
     * Sets the queryLanguageSupport value for this Catissue_DEResourceProperties.
     * 
     * @param queryLanguageSupport
     */
    public void setQueryLanguageSupport(org.cagrid.dataservice.cql.support.QueryLanguageSupport queryLanguageSupport) {
        this.queryLanguageSupport = queryLanguageSupport;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Catissue_DEResourceProperties)) return false;
        Catissue_DEResourceProperties other = (Catissue_DEResourceProperties) obj;
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
              this.serviceMetadata.equals(other.getServiceMetadata()))) &&
            ((this.queryLanguageSupport==null && other.getQueryLanguageSupport()==null) || 
             (this.queryLanguageSupport!=null &&
              this.queryLanguageSupport.equals(other.getQueryLanguageSupport())));
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
        if (getQueryLanguageSupport() != null) {
            _hashCode += getQueryLanguageSupport().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Catissue_DEResourceProperties.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://de_entity/Catissue_DE", ">Catissue_DEResourceProperties"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryLanguageSupport");
        elemField.setXmlName(new javax.xml.namespace.QName("http://org.cagrid.dataservice.cql/QueryLanguageSupport", "QueryLanguageSupport"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://org.cagrid.dataservice.cql/QueryLanguageSupport", "QueryLanguageSupport"));
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
