package mx.com.att.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class WhatsAppMessageWebhookPK implements Serializable{
	
	@Column(name = "ID")
	private Integer id;
	@Column(name = "ID_MESSAGE")
	private String idMessage;
	@Column(name = "MDN_MESSAGE")
	private String mdnMessage;
	
	public String getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}
	public String getMdnMessage() {
		return mdnMessage;
	}
	public void setMdnMessage(String mdnMessage) {
		this.mdnMessage = mdnMessage;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
