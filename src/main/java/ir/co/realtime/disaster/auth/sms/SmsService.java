
package ir.co.realtime.disaster.auth.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tempuri.Sms;
import org.tempuri.SmsSoap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

@Service
public class SmsService {
    private static Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${sms.userId}")
    private String userId;

    @Value("${sms.password}")
    private String password;

    @Value("${sms.enable}")
    private boolean enable;

    private SmsSoap smsSoap;
    private Marshaller marshaller;

    @PostConstruct
    public void init() throws JAXBException {
        if (!enable)
            return;

        marshaller = JAXBContext.newInstance(XmsRequestEntity.class)
                .createMarshaller();
        Sms sms = new Sms();
        smsSoap = sms.getSmsSoap();
    }

    public boolean send(long mobile, String message) {
        if (!enable)
            return false;

        try {
            XmsRequestEntity xmsRequestEntity = XmsRequestEntity.create(userId, password, mobile, message);
            StringWriter sw = new StringWriter();
            marshaller.marshal(xmsRequestEntity, NoNamesWriter.filter(sw));
            String xml = sw.toString();
            String requestData = createSimpleXml(xml);
            String response = smsSoap.xmsRequest(requestData).toString();
            System.out.println(response);

            if (response.contains("status=\"40\""))
                return true;
            else {
                logger.warn("problem for sending message for mobile: {}, response: {}", mobile, response);
            }
        } catch (Exception e) {
            logger.error("Unable to send sms to mobile {}", mobile, e);
        }
        return false;
    }

    private static String createSimpleXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        Document document = docBuilder.parse(new InputSource(new StringReader(xml)));
        NodeList nodes  = document.getChildNodes();

        DOMSource source = new DOMSource();
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        for (int i = 0; i < nodes.getLength(); ++i) {
            source.setNode(nodes.item(i));
            transformer.transform(source, result);
        }

        return writer.toString();
    }
}