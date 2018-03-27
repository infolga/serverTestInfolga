import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
public class MyXML {

    private Element root;
    private Element buf;
    private Document doc;


    public MyXML(String str) throws JDOMException, IOException {

        InputStream is = new ByteArrayInputStream(str.getBytes());
        SAXBuilder saxBuilder = new SAXBuilder();
        doc = saxBuilder.build(is);
        root = doc.getRootElement();
        buf=root;
    }


    public int getAttributeResult() {
        return Integer.parseInt(buf.getAttributeValue("result"));
    }


    public String getTypeXML() {
        return root.getName();
    }

    public int getIdActionsXML() {
        buf = root.getChild("actions");
        return Integer.parseInt(buf.getAttributeValue("id"));
    }

    public String getValueInActionsXML(String s) {
        buf = root.getChild("actions");
        buf = buf.getChild(s);
        return buf.getText();
    }


    public MyXML(String nameRoot, int Actionid) {

        this.root = new Element(nameRoot);

        root.addContent(new Element("actions"));
        buf = root.getChild("actions");

        switch (Actionid) {
            case MSG.XML_USER_LOGIN:
                buf.setAttribute("id", "" + MSG.XML_USER_LOGIN);
                buf.addContent(new Comment("user.login"));
                break;
            case MSG.XML_USER_REGISTRATION:
                buf.setAttribute("id", "" + MSG.XML_USER_REGISTRATION);
                buf.addContent(new Comment("user.registration"));
                break;
            default:
                break;
        }
    }

    public MyXML setNameRoot(String name) {
        root.setName(name);

        return this;
    }

    public MyXML setAttributeRoot(String name, String value) {
        root.setAttribute(name,value);
        return this;
    }

    public MyXML removeAttributeRoot(String name){
        root.removeAttribute(name);
        return this;
    }
    public MyXML jumpToChildFromRoot(String name){
        buf = root.getChild(name);
        return this;
    }
    public MyXML jumpToChildFrom(String name){
        buf = buf.getChild(name);
        return this;
    }

    public MyXML setName (String name) {
        buf.setName(name);
        return this;
    }

    public MyXML setAttribute(String name, String value) {
        buf.setAttribute(name,value);
        return this;
    }

    public MyXML removeAttribute(String name){
        buf.removeAttribute(name);
        return this;
    }




    public MyXML addChild(String name) {
        buf.addContent(new Element(name));
        buf = buf.getChild(name);
        return this;
    }

    public MyXML addChild(String name, String text) {
        Element child = new Element(name);
        child.setText(text);
        buf.addContent(child);
        return this;
    }

    public MyXML setText(String name) {
        buf.setText(name);
        return this;
    }

    public MyXML setAtribute(String name, String value) {
        buf.setAttribute(name, value);
        return this;
    }

    public String toString() {
        Document d = root.getDocument();
        if (d == null) {
            d = new Document(root);
        }
        String s = (new XMLOutputter(Format.getPrettyFormat())).outputString(d);

        return s;
    }


}