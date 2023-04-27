package mx.com.att.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.att.model.entity.WhatsAppMessageWebhook;
import mx.com.att.model.entity.WhatsAppMessageWebhookPK;

public interface WhatsAppMessageWebhookRepository extends JpaRepository<WhatsAppMessageWebhook, WhatsAppMessageWebhookPK>{

}
