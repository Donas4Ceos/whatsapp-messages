package mx.com.att.model.vo;

import java.io.Serializable;

public class WhatsAppMessageTemplate implements Serializable{
	
	private String messaging_product;
	private String recipient_type;
	private String to;
	private String type;
	private WhatsMessageTemplate template;

	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}

	public String getRecipient_type() {
		return recipient_type;
	}

	public void setRecipient_type(String recipient_type) {
		this.recipient_type = recipient_type;
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public WhatsMessageTemplate getTemplate() {
		return template;
	}
	public void setTemplate(WhatsMessageTemplate template) {
		this.template = template;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	{
	    "messaging_product": "whatsapp",
	    "to": "{{Recipient-Phone-Number}}",
	    "type": "template",
	    "template": {
	        "name": "hello_world",
	        "language": {
	            "code": "en_US"
	        }
	    }
	}**/

}
