package pathology_specimen.common;

import javax.xml.namespace.QName;


public interface PathologySpecimenConstants {
	public static final String SERVICE_NS = "http://pathology_specimen/PathologySpecimen";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "PathologySpecimenKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "PathologySpecimenResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName DOMAINMODEL = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
