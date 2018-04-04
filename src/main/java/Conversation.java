import org.jdom2.Element;

public class Conversation implements Myin {

    private int conversation_id;
    private String title;
    private int photo_id;
    private int creator_id;
    private String created_at;
    private String type;
    private String name_conversation;
    private String time_last_viev;

    private int id_last_msg;

    public Conversation() {}

    public Conversation(Element el) {
        conversation_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_ID).getText());

        title = el.getChild(MSG.XML_ELEMENT_TITLE).getText();
        photo_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_PHOTO_ID).getText());
        creator_id = Integer.parseInt(el.getChild(MSG.XML_ELEMENT_CONVERSATION_CREATOR_ID).getText());
        created_at = el.getChild(MSG.XML_ELEMENT_CREATED_AT).getText();
        type = el.getChild(MSG.XML_ELEMENT_TYPE).getText();
        name_conversation = el.getChild(MSG.XML_ELEMENT_NAME_CONVERSATION).getText();
        time_last_viev = el.getChild(MSG.XML_ELEMENT_CONVERSATION_TIME_LAST_VIEV).getText();


    }

    public Element getXMLElement() {


        Element element = new Element(MSG.XML_ELEMENT_CONVERSATION);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_ID, Integer.toString(conversation_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_TITLE, title);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_PHOTO_ID, Integer.toString(photo_id));
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_CREATOR_ID, Integer.toString(creator_id));

        element = MyXML.addChild(element, MSG.XML_ELEMENT_CREATED_AT, created_at);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_TYPE, type);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_NAME_CONVERSATION, name_conversation);
        element = MyXML.addChild(element, MSG.XML_ELEMENT_CONVERSATION_TIME_LAST_VIEV, time_last_viev);


        return element;


    }


    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName_conversation() {
        return name_conversation;
    }

    public void setName_conversation(String name_conversation) {
        this.name_conversation = name_conversation;
    }

    public String getTime_last_viev() {
        return time_last_viev;
    }

    public void setTime_last_viev(String time_last_viev) {
        this.time_last_viev = time_last_viev;
    }

    public int getId_last_msg() {
        return id_last_msg;
    }

    public void setId_last_msg(int id_last_msg) {
        this.id_last_msg = id_last_msg;
    }
}
