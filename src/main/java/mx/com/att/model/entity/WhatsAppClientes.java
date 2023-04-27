package mx.com.att.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="WHATSAPP_MESSAGE_CLIENTES", schema = "KAIROS")
public class WhatsAppClientes implements Serializable{

	@Id
	@Column(name="MDN_CLIENTE")
	private String mdn_cliente;
	
	@Column(name="NAME_CLIENTE")
	private String name_cliente;
		
	public String getMdn_cliente() {
		return mdn_cliente;
	}

	public void setMdn_cliente(String mdn_cliente) {
		this.mdn_cliente = mdn_cliente;
	}

	public String getName_cliente() {
		return name_cliente;
	}

	public void setName_cliente(String name_cliente) {
		this.name_cliente = name_cliente;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
