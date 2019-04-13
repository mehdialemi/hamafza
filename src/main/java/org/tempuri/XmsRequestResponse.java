
package org.tempuri;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="XmsRequestResult" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
    "xmsRequestResult"
})
@XmlRootElement(name = "XmsRequestResponse")
public class XmsRequestResponse {

    @XmlElement(name = "XmsRequestResult")
    protected Object xmsRequestResult;

    /**
     * Gets the value of the xmsRequestResult property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getXmsRequestResult() {
        return xmsRequestResult;
    }

    /**
     * Sets the value of the xmsRequestResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setXmsRequestResult(Object value) {
        this.xmsRequestResult = value;
    }

}
