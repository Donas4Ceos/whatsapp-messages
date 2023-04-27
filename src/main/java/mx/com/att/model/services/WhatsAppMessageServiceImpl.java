package mx.com.att.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.com.att.model.entity.WhatsAppMessage;
import mx.com.att.model.repository.WhatsAppMessageRepository;

@Service
public class WhatsAppMessageServiceImpl implements IWhatsAppMessageService{
	
	@Autowired
	private WhatsAppMessageRepository repository;
	

//	@Override
//	@Transactional(readOnly = true)
//	public List<WhatsAppResponseMessage> findAll() {
//		return (List<WhatsAppResponseMessage>) repository.findAll();
//	}
	
	@Override
	@Transactional(readOnly = true)
	public WhatsAppMessage findById(String id) {
		return repository.findById(id).orElse(null);		
	}

}
