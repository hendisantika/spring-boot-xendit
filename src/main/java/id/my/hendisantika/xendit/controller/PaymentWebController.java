package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.dto.PaymentRequestDTO;
import id.my.hendisantika.xendit.entity.Order;
import id.my.hendisantika.xendit.entity.Product;
import id.my.hendisantika.xendit.service.OrderService;
import id.my.hendisantika.xendit.service.ProductService;
import id.my.hendisantika.xendit.service.XenditService;
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

    private final OrderService orderService;
    private final ProductService productService;
    private final XenditService xenditService;

    @GetMapping("/checkout/{productId}")
    public String showCheckoutForm(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setProductId(productId);
        paymentRequest.setQuantity(1);

        model.addAttribute("product", product);
        model.addAttribute("paymentRequest", paymentRequest);
        return "payments/checkout";
    }

    @PostMapping("/process")
    public String processPayment(
            @ModelAttribute PaymentRequestDTO paymentRequest,
            RedirectAttributes redirectAttributes) {
        try {
            // Create order
            Order order = orderService.createOrder(
                    paymentRequest.getProductId(),
                    paymentRequest.getQuantity(),
                    paymentRequest.getCustomerName(),
                    paymentRequest.getCustomerEmail(),
                    paymentRequest.getCustomerName() // Using name as phone temporarily
            );

            // Create Xendit invoice
            Map<String, Object> invoiceData = xenditService.createInvoiceForOrder(order);

            redirectAttributes.addFlashAttribute("order", order);
            redirectAttributes.addFlashAttribute("invoiceData", invoiceData);
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to process payment: " + e.getMessage());
            return "redirect:/payments/checkout/" + paymentRequest.getProductId();
        }
    }
}
