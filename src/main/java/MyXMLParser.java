import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyXMLParser {


    private final String str;
    private Element root;
    private Element buf;
    private Document doc;

    public MyXMLParser(String str) throws JDOMException, IOException {
        this.str = str;
        InputStream is = new ByteArrayInputStream(str.getBytes());
        SAXBuilder saxBuilder = new SAXBuilder();
        doc = saxBuilder.build(is);
        root = doc.getRootElement();
    }

    public String getTypeXML() {
        return root.getName();
    }

    public int getIdActionsXML() {
        buf = root.getChild("actions");
        return Integer.parseInt(buf.getAttributeValue("id"));
    }


}
