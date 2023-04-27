package mx.com.att.model.services;

import mx.com.att.model.entity.WhatsAppMessage;

public interface IWhatsAppMessageService {
	
//	public List<WhatsAppResponseMesssage> findAll();
	public WhatsAppMessage findById(String id);

}
