

package edu.wustl.catissuecore.jaxb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="class">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Molecular"/>
 *               &lt;enumeration value="Tissue"/>
 *               &lt;enumeration value="Fluid"/>
 *               &lt;enumeration value="Cell"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="DNA"/>
 *               &lt;enumeration value="RNA"/>
 *               &lt;enumeration value="RNA, poly-A enriched"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pathologicalStatus">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Non-Malignant"/>
 *               &lt;enumeration value="Non-Malignant, Diseased"/>
 *               &lt;enumeration value="Pre-Malignant"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="isAvailable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tissueSite">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Small intestine, NOS"/>
 *               &lt;enumeration value="Cecum"/>
 *               &lt;enumeration value="Pre-Malignant"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="collectionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="accessionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clinicalDiagnosis">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value=""/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="clinicalStatus">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value=""/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="participantEmpi" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="storageSiteName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="storageSiteCoordinator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "clazz",
    "type",
    "pathologicalStatus",
    "quantity",
    "isAvailable",
    "tissueSite",
    "collectionDate",
    "accessionNumber",
    "clinicalDiagnosis",
    "clinicalStatus",
    "participantEmpi",
    "storageSiteName",
    "storageSiteCoordinator"
})
@XmlRootElement(name = "Specimen")
public class Specimen {

    protected long id;
    @XmlElement(name = "class")
    protected String clazz="";
    @XmlElement
    protected String type="";
    @XmlElement
    protected String pathologicalStatus="";
    protected double quantity;
    protected boolean isAvailable;
    @XmlElement
    protected String tissueSite="";
    @XmlElement
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar collectionDate;
    @XmlElement
    protected String accessionNumber="";
    @XmlElement
    protected String clinicalDiagnosis="";
    @XmlElement
    protected String clinicalStatus="";
    @XmlElement
    protected String participantEmpi="";
    @XmlElement
    protected String storageSiteName="";
    @XmlElement
    protected String storageSiteCoordinator="";

    /**
     * Gets the value of the id property.
     *
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     */
    public void setId(long value) {
        id = value;
    }

    /**
     * Gets the value of the clazz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClazz(String value) {
    	if(value!=null)
		{
			clazz = value;
		}
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value) {
    	if(value!=null)
		{
			type = value;
		}
    }

    /**
     * Gets the value of the pathologicalStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPathologicalStatus() {
        return pathologicalStatus;
    }

    /**
     * Sets the value of the pathologicalStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPathologicalStatus(String value) {
    	if(value!=null)
		{
			pathologicalStatus = value;
		}
    }

    /**
     * Gets the value of the quantity property.
     *
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     *
     */
    public void setQuantity(double value) {

        quantity = value;
    }

    /**
     * Gets the value of the isAvailable property.
     *
     */
    public boolean isIsAvailable() {
        return isAvailable;
    }

    /**
     * Sets the value of the isAvailable property.
     *
     */
    public void setIsAvailable(boolean value) {
        isAvailable = value;
    }

    /**
     * Gets the value of the tissueSite property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTissueSite() {
        return tissueSite;
    }

    /**
     * Sets the value of the tissueSite property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTissueSite(String value) {
    	if(value!=null)
		{
			tissueSite = value;
		}
    }

    /**
     * Gets the value of the collectionDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCollectionDate() {
        return collectionDate;
    }

    /**
     * Sets the value of the collectionDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setCollectionDate(XMLGregorianCalendar value) {
        collectionDate = value;
    }

    /**
     * Gets the value of the accessionNumber property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccessionNumber() {

        return accessionNumber;
    }

    /**
     * Sets the value of the accessionNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccessionNumber(String value) {
    	if(value!=null)
		{
			accessionNumber = value;
		}
    }

    /**
     * Gets the value of the clinicalDiagnosis property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClinicalDiagnosis() {
        return clinicalDiagnosis;
    }

    /**
     * Sets the value of the clinicalDiagnosis property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClinicalDiagnosis(String value) {
    	if(value!=null)
		{
			clinicalDiagnosis = value;
		}
    }

    /**
     * Gets the value of the clinicalStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClinicalStatus() {
        return clinicalStatus;
    }

    /**
     * Sets the value of the clinicalStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClinicalStatus(String value) {
    	if(value!=null)
		{
			clinicalStatus = value;
		}
    }

    /**
     * Gets the value of the participantEmpi property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParticipantEmpi() {
        return participantEmpi;
    }

    /**
     * Sets the value of the participantEmpi property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParticipantEmpi(String value) {
    	if(value!=null)
		{
			participantEmpi = value;
		}
    }

    /**
     * Gets the value of the storageSiteName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStorageSiteName() {
        return storageSiteName;
    }

    /**
     * Sets the value of the storageSiteName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStorageSiteName(String value) {
    	if(value!=null)
		{
			storageSiteName = value;
		}
    }

    /**
     * Gets the value of the storageSiteCoordinator property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStorageSiteCoordinator() {
        return storageSiteCoordinator;
    }

    /**
     * Sets the value of the storageSiteCoordinator property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStorageSiteCoordinator(String value) {
    	if(value!=null)
		{
			storageSiteCoordinator = value;
		}
    }

}
