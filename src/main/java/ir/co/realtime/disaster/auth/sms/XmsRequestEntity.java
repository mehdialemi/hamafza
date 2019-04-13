package ir.co.realtime.disaster.auth.sms;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "xmsrequest")
public class XmsRequestEntity {

    @XmlElement(name = "userid")
    String userId;

    @XmlElement(name = "password")
    String password;

    @XmlElement(name = "action")
    String action = "smssend";

    Body body;

    @XmlRootElement
    public static class Body {

        @XmlElement
        String type = "oto";

        @XmlElement
        Recipient recipient;
    }

    @XmlRootElement(name = "recipient")
    public static class Recipient {

        @XmlAttribute
        long mobile;

        @XmlAttribute
        String originator = "50005980";

        @XmlValue
        @NotNull
        String message;
    }

    public static XmsRequestEntity create(String userId, String password, long mobile, String message) {
        XmsRequestEntity xmsRequestEntity = new XmsRequestEntity();
        xmsRequestEntity.userId = userId;
        xmsRequestEntity.password = password;
        xmsRequestEntity.body = new Body();
        xmsRequestEntity.body.recipient = new Recipient();
        xmsRequestEntity.body.recipient.mobile = mobile;
        xmsRequestEntity.body.recipient.message = message;
        return xmsRequestEntity;
    }
}
