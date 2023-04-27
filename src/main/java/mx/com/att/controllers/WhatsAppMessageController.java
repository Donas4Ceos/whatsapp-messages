package mx.com.att.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Date;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.com.att.model.entity.WhatsAppClientes;
import mx.com.att.model.entity.WhatsAppMessage;
import mx.com.att.model.entity.WhatsAppMessageWebhook;
import mx.com.att.model.entity.WhatsAppMessageWebhookPK;
import mx.com.att.model.repository.WhatsAppClientesRepository;
import mx.com.att.model.repository.WhatsAppMessageRepository;
import mx.com.att.model.repository.WhatsAppMessageWebhookRepository;
import mx.com.att.model.services.IWhatsAppMessageService;
import mx.com.att.model.vo.Components;
import mx.com.att.model.vo.Context;
import mx.com.att.model.vo.LanguageMessage;
import mx.com.att.model.vo.RequestMessageResponse;
import mx.com.att.model.vo.RequestMessageSend;
import mx.com.att.model.vo.WhatsAppMessageTemplate;
import mx.com.att.model.vo.WhatsAppMessageText;
import mx.com.att.model.vo.WhatsAppResponseMessage;
import mx.com.att.model.vo.WhatsMessageTemplate;
import mx.com.att.model.vo.WhatsMessageText;

@RestController
//@CrossOrigin(origins = {"http://localhost:8001","http://localhost:8080","https://28ed-192-151-180-180.ngrok.io"})
public class WhatsAppMessageController {

	private static final Logger logger = LoggerFactory.getLogger(WhatsAppMessageController.class);

	private static final String token ="Bearer EAAKUbSZAWSPkBABpDHsbcaeGnvtL2xExdjt7SPGtO4OsaTiXw5mQwbKyrVjNJOzgoEuQ8tBTRz21Idy4JglnnplAdMtVTH1r6q05ea8Vzon6kDOEoNICZApEZBOKVc6sOk63Kj5ZBnP5PVtFm5ZC7rQ4dO7Uj3BlqH1MEgo17mFZCrW6Q1aOoTaOOnQRv4BBkK1RvS2ZCaoMQZDZD";

	@Autowired
	private IWhatsAppMessageService whatsappService;

	@Autowired
	private RestTemplate clienteRest;

	@Autowired
	private WhatsAppMessageRepository messageRepo;
	
	@Autowired
	private WhatsAppMessageWebhookRepository messageWebHookRepo;

	@Autowired
	private WhatsAppClientesRepository clientesRepository;

	//	@GetMapping("/readMessages")
	//	public List<WhatsAppResponseMessage> readMessages(){
	//		return whatsappService.findAll();
	//	}

	@GetMapping("/readMessages/{id}")
	public ResponseEntity<WhatsAppMessage> readMessageById(@PathVariable String id){
		return ResponseEntity.ok(whatsappService.findById(id));
	}

	@PostMapping("/sendMessage")//De momento se incluye phoneNumberId:mdn
	public ResponseEntity<String> sendMessage(@RequestBody RequestMessageSend requestMessageSend) {

		String respuestaWhats = null;		
		
		WhatsAppMessageTemplate requestMessage = new WhatsAppMessageTemplate();
		requestMessage.setTo(requestMessageSend.getMdn());
		requestMessage.setMessaging_product("whatsapp");
		WhatsMessageTemplate WTemplate = new WhatsMessageTemplate();
		LanguageMessage language = new LanguageMessage();
//		language.setCode("en_US");
		language.setCode("es_MX");
		WTemplate.setLanguage(language);
//		WTemplate.setName("hello_world");
//		WTemplate.setName("renovaciones");
		WTemplate.setName(requestMessageSend.getPlantilla_id());
		requestMessage.setTemplate(WTemplate);
		requestMessage.setType("template");


		if("promocion_att".equals(requestMessageSend.getPlantilla_id())) {

			Components[] componentsTemplate = {new Components()};
			componentsTemplate[0].setType("body");

			Optional<WhatsAppClientes> cliente = clientesRepository.findById(requestMessageSend.getMdn());

			String[] parametersBody = {"{\"type\":\"text\",\"text\":\""+cliente.get().getName_cliente()+"\"}"};

			final JSONParser parser = new JSONParser();
			JSONObject objParam = null;
			try {
				objParam = (JSONObject)parser.parse(parametersBody[0]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("objParam - > " + objParam.toJSONString());

			Object[] arrayParams= {objParam};

			componentsTemplate[0].setParameters(arrayParams);
			requestMessage.getTemplate().setComponents(componentsTemplate);
		}

		String jsonSMS = new Gson().toJson(requestMessage);
		logger.info("requestSendMessage.body() - > " + jsonSMS);

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("https://graph.facebook.com/v15.0/"+requestMessageSend.getPhone_number_id()+"/messages"))
					.header("Authorization", token)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonSMS))
					.build();
			HttpClient http = HttpClient.newHttpClient();
			HttpResponse<String> response = http.send(request,BodyHandlers.ofString());

			if(response.body() != null || !response.body().isEmpty()) {
				WhatsAppResponseMessage messageResponse = new Gson().fromJson(response.body(), WhatsAppResponseMessage.class);
				WhatsAppMessage messageBD = new WhatsAppMessage();
				messageBD.setIdMessage(messageResponse.getMessages().get(0).getId());
				messageBD.setRequestMessage(jsonSMS);
				messageBD.setResponseMessage(response.body());
				messageBD.setTokenMessage(token);
				messageBD.setTemplateMessage(WTemplate.getName());//nombre de plantilla
				messageBD.setDateMessage(new Date());
				messageRepo.save(messageBD);
			}
			respuestaWhats = response.body();
			logger.info("responseSendMessage.body() - > " + response.body());

		} catch (URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
		}	

		return ResponseEntity.ok(respuestaWhats);
	}
	
	@PostMapping("/responseMessage")
	public ResponseEntity<String> responseMessage(@RequestBody RequestMessageResponse requestMessageResponse) {

		WhatsAppMessageText requestMessage = new WhatsAppMessageText();
		
		String responseEntity = null;
		
		requestMessage.setMessaging_product("whatsapp");
		Context context = new Context();
		context.setMessage_id(requestMessageResponse.getWam_id());
		requestMessage.setTo(requestMessageResponse.getMdn());
		WhatsMessageText text = new WhatsMessageText();
		text.setPreview_url(false);
		text.setBody(requestMessageResponse.getText_message_chat());
		requestMessage.setText(text);
		requestMessage.setType("text");


		String jsonSMS = new Gson().toJson(requestMessage);
		logger.info("requestResponseMessage.body() - > " + jsonSMS);

		//"{ \"messaging_product\": \"whatsapp\", \"recipient_type\": \"individual\", \"to\": \"525530302387\", \"type\": \"template\", \"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }"))

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("https://graph.facebook.com/v15.0/"+requestMessageResponse.getPhone_number_id()+"/messages"))
					.header("Authorization", token)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonSMS))
					.build();
			HttpClient http = HttpClient.newHttpClient();
			HttpResponse<String> response = http.send(request,BodyHandlers.ofString());

			if(response.body() != null || !response.body().isEmpty()) {
				
				responseEntity = response.body();
				
//				Configuration conf = Configuration.defaultConfiguration()
//						.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
//						.addOptions(Option.SUPPRESS_EXCEPTIONS);
//				DocumentContext document = JsonPath.using(conf).parse(response.body());
//				
//				Object rawData = document.read("$.entry");
//				
//				System.out.println("Objeto entry -> " + rawData.toString());
				
				WhatsAppResponseMessage messageResponse = new Gson().fromJson(response.body(), WhatsAppResponseMessage.class);
				
				WhatsAppMessage messageBD = new WhatsAppMessage();
				messageBD.setIdMessage(messageResponse.getMessages() != null ? messageResponse.getMessages().get(0).getId():"wam_id.default");
				messageBD.setRequestMessage(jsonSMS);
				messageBD.setResponseMessage(response.body());
				messageBD.setTokenMessage(token);
				messageBD.setTemplateMessage("chatResponseTextOK");//nombre de plantilla
				messageBD.setDateMessage(new Date());
				messageRepo.save(messageBD);
			}

			logger.info("responseMessage.body() -> " + response.body());

		} catch (URISyntaxException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(responseEntity);

	}

	@GetMapping("/webhook")
	public String getWebhook(HttpServletRequest request, HttpServletResponse response) throws IOException {

		final String verify_token = "pruebaWebhook";

		String mode = request.getParameter("hub.mode");
		String token = request.getParameter("hub.verify_token");
		String challenge = request.getParameter("hub.challenge");

		logger.info(" ### WebHook Get ### " + "mode ->" +mode + " verify_token ->" +token + " challenge ->" +challenge);

		if(token.equals(verify_token)) {
			response.setStatus(200);
		}else {
			response.setStatus(403);
			return "TOKEN_INVALIDO";
		}

		return challenge;
	}

	@PostMapping("/webhook")
	public void postWebhook(HttpServletRequest request, HttpServletResponse response) throws IOException {

		WhatsAppMessageWebhook webHookBD = new WhatsAppMessageWebhook();
		WhatsAppMessageWebhookPK webHookBDPk = new WhatsAppMessageWebhookPK(); 

		StringBuilder jsonWebHook = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jsonWebHook.append(line);
		} catch (Exception e) { /*report an error*/ }
		logger.info(" ### WebHook Post ### " + jsonWebHook.toString());

		final JSONParser parser = new JSONParser();
		JSONObject jsonRespWebhook = null;
		try {
			jsonRespWebhook = (JSONObject) parser.parse(jsonWebHook.toString());

			final String objectWBA = (String) jsonRespWebhook.get("object");
			if(objectWBA.equals("whatsapp_business_account")) {
				//				System.out.println("object -> " + objectWBA);
			}

			final JSONArray arrayEntry = (JSONArray) jsonRespWebhook.get("entry");

			//			System.out.println("entry[0] -> " + arrayEntry.get(0));

			JSONObject jsonEntry = (JSONObject) parser.parse(arrayEntry.get(0).toString());

			final JSONArray arrayChange = (JSONArray) jsonEntry.get("changes");

			//			System.out.println("change[0] -> " + arrayChange.get(0));

			JSONObject jsonChange = (JSONObject) parser.parse(arrayChange.get(0).toString());

			final JSONObject jsonValue = (JSONObject) parser.parse(jsonChange.get("value").toString());

			//			System.out.println("value[0] -> " + jsonValue);

			JSONArray arrayStatuses = (JSONArray) jsonValue.get("statuses");

			if(arrayStatuses != null) {// si no trae estatuses no es mensaje de respuesta del cliente

				final JSONObject objStatuses = (JSONObject) arrayStatuses.get(0);

				//				System.out.println("entry[0].changes[0].value[0].statuses[0] -> " + objStatuses);

				webHookBD.setFechaMessage(new Date());
				webHookBD.setStatusMessage(objStatuses.get("status").toString());
				webHookBD.setTextoMessage("{\"body\": \"Texto Plantilla\"}");
				webHookBD.setJsonWebhook(jsonWebHook.toString());
				webHookBDPk.setIdMessage(objStatuses.get("id").toString());
				//				//SETEAMOS ID_MESSAGE EN ID_MESSAGE_2
				//				String wam_id = messageRepo.getIdMessageSend(objStatuses.get("recipient_id").toString().replaceFirst("1", ""));
				webHookBDPk.setMdnMessage(objStatuses.get("recipient_id").toString());
				webHookBD.setPrimaryKey(webHookBDPk);

				messageWebHookRepo.save(webHookBD);

				//				System.out.println("entry[0].changes[0].value[0].statuses[0].status -> " + objStatuses.get("status"));

			}else{// si trae contacts es respuesta del cliente
				JSONArray arrayContacts = (JSONArray) jsonValue.get("contacts");
				JSONArray arrayMessages = (JSONArray) jsonValue.get("messages");

				final JSONObject objContacts = (JSONObject) arrayContacts.get(0);
				final JSONObject objMessages = (JSONObject) arrayMessages.get(0);

				final String typeMessage = (String) objMessages.get("type");

				//				System.out.println("entry[0].changes[0].value[0].contacts -> " + arrayContacts.get(0));
				//				System.out.println("entry[0].changes[0].value[0].messages -> " + arrayMessages.get(0));
				logger.info("entry[0].changes[0].value[0].messages[0].type -> " + typeMessage);

				if("button".equals(typeMessage)) {

					JSONObject button = (JSONObject) objMessages.get("button");
					String textButton = (String)button.get("text");

					webHookBD.setStatusMessage(textButton);
					webHookBD.setTextoMessage(button.toJSONString());

					final JSONObject context = (JSONObject) objMessages.get("context");
					final String idMessageContext = (String) context.get("id");

					webHookBDPk.setIdMessage(idMessageContext);

				}else if("text".equals(typeMessage)) {

					JSONObject text = (JSONObject) objMessages.get("text");

					webHookBD.setStatusMessage("clientResponse");
					webHookBD.setTextoMessage(text.toJSONString());
					webHookBDPk.setIdMessage(objMessages.get("id").toString());
				}

				webHookBD.setFechaMessage(new Date());


				webHookBD.setJsonWebhook(jsonWebHook.toString());
				webHookBDPk.setMdnMessage(objMessages.get("from").toString());//Se podria guardar sin el 1 que agrega
				webHookBD.setPrimaryKey(webHookBDPk);
				
				messageWebHookRepo.save(webHookBD);
			}			

		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
