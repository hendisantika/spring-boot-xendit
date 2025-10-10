package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.dto.PaymentRequestDTO;
import id.my.hendisantika.xendit.service.XenditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.19
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final XenditService xenditService;

    @PostMapping("/create-invoice")
    public ResponseEntity<Map<String, Object>> createInvoice(
            @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        try {
            Map<String, Object> invoiceData = xenditService.createInvoice(paymentRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Invoice created successfully");
            response.put("data", invoiceData);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<Map<String, Object>> getInvoiceStatus(@PathVariable String invoiceId) {
        try {
            Map<String, Object> invoiceStatus = xenditService.getInvoiceStatus(invoiceId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", invoiceStatus);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        // Handle Xendit webhook callback
        // You should verify the callback token here for security
        log.info("Received webhook: {}", payload);

        // Process the payment status update
        String status = (String) payload.get("status");
        String externalId = (String) payload.get("external_id");

        if ("PAID".equals(status)) {
            // Handle successful payment
            log.info("Payment successful for: {}", externalId);
        }

        return ResponseEntity.ok("OK");
    }
}
