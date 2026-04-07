# Zaptilo WhatsApp API - Spring Boot Starter

## Overview

This is a Spring Boot starter for sending WhatsApp Business messages via the Zaptilo API. Drop it into any Spring Boot application and start sending text, media, and template messages with one line of code.

* Auto-configured Spring Bean
* Send text, media, and template messages
* Built on Spring's `RestTemplate` — no external HTTP libraries
* Compatible with Spring Boot 2.x and 3.x
* Java 17+

---

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ai.zaptilo</groupId>
    <artifactId>zaptilo-whatsapp-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or for Gradle (`build.gradle`):

```groovy
implementation 'ai.zaptilo:zaptilo-whatsapp-spring-boot-starter:1.0.0'
```

---

## Configuration

Add your Zaptilo API token to `application.properties`:

```properties
zaptilo.api.token=your_token_here
zaptilo.api.base-url=https://web.zaptilo.ai
```

Or `application.yml`:

```yaml
zaptilo:
  api:
    token: your_token_here
    base-url: https://web.zaptilo.ai
```

Get your token from the [Zaptilo Dashboard](https://web.zaptilo.ai) under **Developer Tools → Access Tokens**.

---

## Usage

Inject the service into any Spring component:

```java
import ai.zaptilo.whatsapp.ZaptiloWhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private ZaptiloWhatsAppService whatsApp;

    public void sendOrderConfirmation(String customerNumber, String orderId) {
        whatsApp.sendTemplate(
            customerNumber,
            "order_confirmation",
            "en",
            List.of("John Doe", orderId),
            null
        );
    }

    public void sendWelcomeText(String number) {
        whatsApp.sendMessage(number, "Welcome to our service!");
    }

    public void sendInvoice(String number, String invoiceUrl) {
        whatsApp.sendMedia(number, invoiceUrl, "document", "Your invoice");
    }
}
```

### From a REST controller

```java
@RestController
public class WebhookController {

    @Autowired
    private ZaptiloWhatsAppService whatsApp;

    @PostMapping("/order-placed")
    public ResponseEntity<?> onOrderPlaced(@RequestBody Order order) {
        whatsApp.sendTemplate(
            order.getCustomerPhone(),
            "order_confirmation",
            "en",
            List.of(order.getCustomerName(), order.getId()),
            null
        );
        return ResponseEntity.ok().build();
    }
}
```

---

## API Reference

| Method | Description |
|---|---|
| `sendMessage(number, message)` | Send a text message |
| `sendMedia(number, mediaUrl, mediaType, caption)` | Send image, video, or document |
| `sendTemplate(number, templateName, language, bodyValues, headerValues)` | Send a template message |
| `getTemplates()` | List approved templates |
| `getBalance()` | Get current credit balance |
| `verify()` | Verify API token and subscription status |

---

## Error Handling

Wrap calls in try/catch to handle API failures:

```java
try {
    whatsApp.sendMessage("919876543210", "Hello!");
} catch (HttpClientErrorException e) {
    log.error("Zaptilo API error: {}", e.getResponseBodyAsString());
}
```

---

## Documentation

- [Zaptilo Website](https://zaptilo.ai)
- [API Documentation](https://zaptilo.ai/developers)
- [Dashboard / Sign Up](https://web.zaptilo.ai)

## License

MIT
