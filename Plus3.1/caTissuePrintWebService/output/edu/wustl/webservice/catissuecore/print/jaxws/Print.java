
package edu.wustl.webservice.catissuecore.print.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://print.catissuecore.webservice.wustl.edu/", name = "print")
@XmlType(namespace = "http://print.catissuecore.webservice.wustl.edu/", name = "print")
@XmlAccessorType(XmlAccessType.FIELD)
public class Print {

    @XmlElement(namespace = "", name = "arg0")
    private String arg0;

    public String getArg0() {
        return this.arg0;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

}
