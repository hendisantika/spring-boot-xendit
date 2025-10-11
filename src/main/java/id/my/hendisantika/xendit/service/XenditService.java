package id.my.hendisantika.xendit.service;

import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import id.my.hendisantika.xendit.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
 * Time: 06.09
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XenditService {

    private final OrderService orderService;

    public Map<String, Object> createInvoiceForOrder(Order order) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("external_id", order.getOrderNumber());
            params.put("amount", order.getTotalAmount());
            params.put("payer_email", order.getCustomerEmail());
            params.put("description", "Payment for Order " + order.getOrderNumber());
            params.put("currency", "IDR");

            Invoice invoice = Invoice.create(params);

            // Update order with Xendit info - set expiry to 24 hours from now
            LocalDateTime expiredAt = LocalDateTime.now().plusHours(24);
            orderService.updateOrderWithXenditInfo(
                    order.getId(),
                    invoice.getId(),
                    invoice.getInvoiceUrl(),
                    expiredAt
            );

            log.info("Xendit invoice created for order {}: {}", order.getOrderNumber(), invoice.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("invoice_id", invoice.getId());
            response.put("invoice_url", invoice.getInvoiceUrl());
            response.put("external_id", invoice.getExternalId());
            response.put("status", invoice.getStatus());
            response.put("amount", invoice.getAmount());
            response.put("expired_at", expiredAt);
            response.put("order_number", order.getOrderNumber());

            return response;

        } catch (XenditException e) {
            log.error("Failed to create Xendit invoice for order {}: {}", order.getOrderNumber(), e.getMessage());
            throw new RuntimeException("Failed to create Xendit invoice: " + e.getMessage());
        }
    }

    public Map<String, Object> getInvoiceStatus(String invoiceId) {
        try {
            Invoice invoice = Invoice.getById(invoiceId);

            // Update order payment status based on invoice status
            Order.PaymentStatus paymentStatus;
            switch (invoice.getStatus()) {
                case "PAID":
                    paymentStatus = Order.PaymentStatus.PAID;
                    break;
                case "EXPIRED":
                    paymentStatus = Order.PaymentStatus.EXPIRED;
                    break;
                default:
                    paymentStatus = Order.PaymentStatus.PENDING;
            }
            orderService.updateOrderPaymentStatus(invoiceId, paymentStatus, invoice.getPaymentMethod());

            Map<String, Object> response = new HashMap<>();
            response.put("invoice_id", invoice.getId());
            response.put("external_id", invoice.getExternalId());
            response.put("status", invoice.getStatus());
            response.put("amount", invoice.getAmount());
            response.put("paid_at", invoice.getPaidAt());
            response.put("payment_method", invoice.getPaymentMethod());

            return response;

        } catch (XenditException e) {
            log.error("Failed to get invoice status: {}", e.getMessage());
            throw new RuntimeException("Failed to get invoice status: " + e.getMessage());
        }
    }

    public void handleWebhook(Map<String, Object> payload) {
        try {
            String invoiceId = (String) payload.get("id");
            String status = (String) payload.get("status");
            String paymentMethod = (String) payload.get("payment_method");

            Order.PaymentStatus paymentStatus;
            switch (status) {
                case "PAID":
                    paymentStatus = Order.PaymentStatus.PAID;
                    break;
                case "EXPIRED":
                    paymentStatus = Order.PaymentStatus.EXPIRED;
                    break;
                default:
                    paymentStatus = Order.PaymentStatus.PENDING;
            }

            orderService.updateOrderPaymentStatus(invoiceId, paymentStatus, paymentMethod);
            log.info("Webhook processed for invoice {}: {}", invoiceId, status);

        } catch (Exception e) {
            log.error("Failed to process webhook: {}", e.getMessage());
        }
    }
}
