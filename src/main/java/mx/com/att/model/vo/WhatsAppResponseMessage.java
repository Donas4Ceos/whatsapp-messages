package mx.com.att.model.vo;

import java.io.Serializable;
import java.util.List;

public class WhatsAppResponseMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messaging_product;
	private List<Contacts> contacts;
	private List<Messages> messages;
	
	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}
	public List<Contacts> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}
	public List<Messages> getMessages() {
		return messages;
	}
	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}
	
	
	
	/**
	 * 
	 * 
	 * {
    "messaging_product": "whatsapp",
    "contacts": [
        {
            "input": "525530302387",
            "wa_id": "5215530302387"
        }
    ],
    "messages": [
        {
            "id": "wamid.HBgNNTIxNTUzMDMwMjM4NxUCABEYEkQxQzU2NDAzODY4Qzg4OUU3NAA="
        }
    ]
}
	 */

}
