package ai.zaptilo.whatsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Boot service for sending WhatsApp Business messages via the Zaptilo API.
 *
 * <p>Add to your application.properties:
 * <pre>
 * zaptilo.api.token=your_token_here
 * zaptilo.api.base-url=https://web.zaptilo.ai
 * </pre>
 *
 * <p>Then inject and use:
 * <pre>
 * &#064;Autowired
 * private ZaptiloWhatsAppService whatsApp;
 *
 * whatsApp.sendMessage("919876543210", "Hello!");
 * </pre>
 *
 * @see <a href="https://zaptilo.ai/developers">Zaptilo API Docs</a>
 */
@Service
public class ZaptiloWhatsAppService {

    @Value("${zaptilo.api.token}")
    private String apiToken;

    @Value("${zaptilo.api.base-url:https://web.zaptilo.ai}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ZaptiloWhatsAppService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Send a plain text WhatsApp message.
     *
     * @param number  phone number with country code (e.g. "919876543210")
     * @param message message text
     * @return API response as a Map
     */
    public Map<String, Object> sendMessage(String number, String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("number", number);
        payload.put("message", message);
        return post("/api/send", payload);
    }

    /**
     * Send a media message (image, video, or document).
     *
     * @param number     phone number with country code
     * @param mediaUrl   public URL of the media file
     * @param mediaType  one of "image", "video", "document"
     * @param caption    optional caption (can be null)
     * @return API response as a Map
     */
    public Map<String, Object> sendMedia(String number, String mediaUrl, String mediaType, String caption) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("number", number);
        payload.put("media_url", mediaUrl);
        payload.put("media_type", mediaType);
        if (caption != null && !caption.isEmpty()) {
            payload.put("caption", caption);
        }
        return post("/api/send/media", payload);
    }

    /**
     * Send a template message.
     *
     * @param number       phone number with country code
     * @param templateName approved template name
     * @param language     language code (e.g. "en")
     * @param bodyValues   list of body parameter values
     * @param headerValues list of header parameter values (can be null)
     * @return API response as a Map
     */
    public Map<String, Object> sendTemplate(
            String number,
            String templateName,
            String language,
            List<String> bodyValues,
            List<String> headerValues) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("number", number);
        payload.put("template_name", templateName);
        payload.put("language", language);
        if (bodyValues != null && !bodyValues.isEmpty()) {
            payload.put("body_values", bodyValues);
        }
        if (headerValues != null && !headerValues.isEmpty()) {
            payload.put("header_values", headerValues);
        }
        return post("/api/send/template", payload);
    }

    /**
     * List all approved message templates.
     *
     * @return API response as a Map
     */
    public Map<String, Object> getTemplates() {
        return get("/api/templates");
    }

    /**
     * Get current credit balance.
     *
     * @return API response as a Map containing the balance
     */
    public Map<String, Object> getBalance() {
        return get("/api/balance");
    }

    /**
     * Verify the API token and subscription status.
     *
     * @return API response as a Map
     */
    public Map<String, Object> verify() {
        return get("/api/verify");
    }

    // ----------------------------------------------------------------
    // Internal helpers
    // ----------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private Map<String, Object> post(String endpoint, Map<String, Object> body) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + endpoint, request, Map.class);
        return response.getBody();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> get(String endpoint) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + endpoint,
                org.springframework.http.HttpMethod.GET,
                request,
                Map.class
        );
        return response.getBody();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);
        return headers;
    }
}
