package mx.com.att.model.vo;

import java.io.Serializable;

public class WhatsAppMessageText implements Serializable{

	private String messaging_product;
	private Context context;
	private String to;
	private String type;
	private WhatsMessageText text;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
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
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public WhatsMessageText getText() {
		return text;
	}
	public void setText(WhatsMessageText text) {
		this.text = text;
	}


	/**
	{
  "messaging_product": "whatsapp",
  "context": {
     "message_id": "wamid.HBgNNTIxNTUzMDMwMjM4NxUCABEYEjc3RTRFQ0E5NUM2MzA2MkQxNQA="
  },
  "to": "525530302387",
  "type": "text",
  "text": {
    "preview_url": false,
    "body": "Bienvenido al grupo"
  }
}**/

}
