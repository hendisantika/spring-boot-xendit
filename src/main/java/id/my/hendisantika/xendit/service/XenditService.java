package id.my.hendisantika.xendit.service;

import com.xendit.exception.XenditException;
import com.xendit.model.Invoice;
import id.my.hendisantika.xendit.dto.PaymentRequestDTO;
import id.my.hendisantika.xendit.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
@Service
@RequiredArgsConstructor
public class XenditService {

    private final ProductService productService;

    public Map<String, Object> createInvoice(PaymentRequestDTO paymentRequest) {
        try {
            Product product = productService.getProductById(paymentRequest.getProductId());

            BigDecimal totalAmount = product.getPrice()
                    .multiply(BigDecimal.valueOf(paymentRequest.getQuantity()));

            Map<String, Object> params = new HashMap<>();
            params.put("external_id", "invoice_" + System.currentTimeMillis());
            params.put("amount", totalAmount);
            params.put("payer_email", paymentRequest.getCustomerEmail());
            params.put("description", "Payment for " + paymentRequest.getQuantity() +
                    "x " + product.getName());
            params.put("currency", "IDR");

            Invoice invoice = Invoice.create(params);

            productService.reduceStock(product.getId(), paymentRequest.getQuantity());

            Map<String, Object> response = new HashMap<>();
            response.put("invoice_id", invoice.getId());
            response.put("invoice_url", invoice.getInvoiceUrl());
            response.put("external_id", invoice.getExternalId());
            response.put("status", invoice.getStatus());
            response.put("amount", invoice.getAmount());
            response.put("product_name", product.getName());
            response.put("quantity", paymentRequest.getQuantity());

            return response;

        } catch (XenditException e) {
            throw new RuntimeException("Failed to create Xendit invoice: " + e.getMessage());
        }
    }

}
