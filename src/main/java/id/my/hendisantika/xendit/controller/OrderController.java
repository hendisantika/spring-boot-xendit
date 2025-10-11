package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.entity.Order;
import id.my.hendisantika.xendit.service.OrderService;
import id.my.hendisantika.xendit.service.XenditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 10.50
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final XenditService xenditService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);

        // If invoice exists and order is still pending, check status
        if (order.getXenditInvoiceId() != null && order.getPaymentStatus() == Order.PaymentStatus.PENDING) {
            try {
                Map<String, Object> invoiceStatus = xenditService.getInvoiceStatus(order.getXenditInvoiceId());
                model.addAttribute("invoiceStatus", invoiceStatus);
            } catch (Exception e) {
                // Ignore error
            }
        }

        return "orders/detail";
    }

    @GetMapping("/check-status/{id}")
    public String checkOrderStatus(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getXenditInvoiceId() != null) {
            try {
                xenditService.getInvoiceStatus(order.getXenditInvoiceId());
                // Refresh order from database
                order = orderService.findById(id).orElseThrow();
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Failed to check payment status");
            }
        }

        return "redirect:/orders/" + id;
    }
}
