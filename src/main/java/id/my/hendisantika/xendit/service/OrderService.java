package id.my.hendisantika.xendit.service;

import id.my.hendisantika.xendit.entity.Order;
import id.my.hendisantika.xendit.entity.OrderItem;
import id.my.hendisantika.xendit.entity.Product;
import id.my.hendisantika.xendit.repository.OrderRepository;
import id.my.hendisantika.xendit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(Long productId, Integer quantity, String customerName, String customerEmail, String customerPhone) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setCustomerPhone(customerPhone);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductName(product.getName());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        order.getOrderItems().add(orderItem);
        order.setTotalAmount(orderItem.getSubtotal());

        // Decrease stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created: {}", savedOrder.getOrderNumber());
        return savedOrder;
    }

    @Transactional
    public void updateOrderPaymentStatus(String xenditInvoiceId, Order.PaymentStatus status, String paymentMethod) {
        orderRepository.findByXenditInvoiceId(xenditInvoiceId).ifPresent(order -> {
            order.setPaymentStatus(status);
            order.setPaymentMethod(paymentMethod);
            if (status == Order.PaymentStatus.PAID) {
                order.setPaidAt(LocalDateTime.now());
            }
            orderRepository.save(order);
            log.info("Order {} payment status updated to {}", order.getOrderNumber(), status);
        });
    }

    @Transactional
    public void updateOrderWithXenditInfo(Long orderId, String xenditInvoiceId, String invoiceUrl, LocalDateTime expiredAt) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setXenditInvoiceId(xenditInvoiceId);
            order.setXenditInvoiceUrl(invoiceUrl);
            order.setExpiredAt(expiredAt);
            orderRepository.save(order);
        });
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Order> findOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp;
    }
}
