package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.dto.PaymentRequestDTO;
import id.my.hendisantika.xendit.entity.Product;
import id.my.hendisantika.xendit.service.PaymentApiService;
import id.my.hendisantika.xendit.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentWebController {

    private final PaymentApiService paymentApiService;
    private final ProductApiService productApiService;

    @GetMapping("/checkout/{productId}")
    public String showCheckoutForm(@PathVariable Long productId, Model model) {
        Product product = productApiService.getProductById(productId);
        if (product == null) {
            return "redirect:/products";
        }

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setProductId(productId);
        paymentRequest.setQuantity(1);
        paymentRequest.setPaymentMethod("BANK_TRANSFER");

        model.addAttribute("product", product);
        model.addAttribute("paymentRequest", paymentRequest);
        return "payments/checkout";
    }

    @PostMapping("/create-invoice")
    public String createInvoice(
            @ModelAttribute PaymentRequestDTO paymentRequest,
            RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> invoiceData = paymentApiService.createInvoice(paymentRequest);
            redirectAttributes.addFlashAttribute("invoiceData", invoiceData);
            return "redirect:/payments/invoice";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create invoice: " + e.getMessage());
            return "redirect:/payments/checkout/" + paymentRequest.getProductId();
        }
    }
}
