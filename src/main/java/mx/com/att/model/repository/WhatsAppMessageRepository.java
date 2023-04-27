package mx.com.att.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.att.model.entity.WhatsAppMessage;

@Repository
public interface WhatsAppMessageRepository extends JpaRepository<WhatsAppMessage, String>{
	
}
