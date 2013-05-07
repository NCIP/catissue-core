
package edu.wustl.webservice.catissuecore.print.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://print.catissuecore.webservice.wustl.edu/", name = "printResponse")
@XmlType(namespace = "http://print.catissuecore.webservice.wustl.edu/", name = "printResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrintResponse {

    @XmlElement(namespace = "", name = "return")
    private String return;

    public String getReturn() {
        return this.return;
    }

    public void setReturn(String return) {
        this.return = return;
    }

}
