package id.my.hendisantika.xendit.config;

import id.my.hendisantika.xendit.entity.Product;
import id.my.hendisantika.xendit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (productRepository.count() == 0) {
                log.info("Initializing database with dummy products...");

                List<Product> products = Arrays.asList(
                        createProduct(
                                "Wireless Bluetooth Headphones",
                                "Premium noise-cancelling wireless headphones with 30-hour battery life",
                                new BigDecimal("299000"),
                                50,
                                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"
                        ),
                        createProduct(
                                "Smart Watch Series 5",
                                "Advanced fitness tracker with heart rate monitor and GPS",
                                new BigDecimal("1299000"),
                                30,
                                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"
                        ),
                        createProduct(
                                "Mechanical Gaming Keyboard",
                                "RGB backlit mechanical keyboard with Cherry MX switches",
                                new BigDecimal("899000"),
                                40,
                                "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500"
                        ),
                        createProduct(
                                "4K Ultra HD Webcam",
                                "Professional webcam with auto-focus and noise reduction",
                                new BigDecimal("450000"),
                                25,
                                "https://images.unsplash.com/photo-1614624532983-4ce03382d63d?w=500"
                        ),
                        createProduct(
                                "Portable SSD 1TB",
                                "High-speed portable solid state drive with USB-C connection",
                                new BigDecimal("1500000"),
                                60,
                                "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=500"
                        ),
                        createProduct(
                                "Wireless Gaming Mouse",
                                "Ergonomic wireless gaming mouse with 16000 DPI sensor",
                                new BigDecimal("550000"),
                                45,
                                "https://images.unsplash.com/photo-1527814050087-3793815479db?w=500"
                        ),
                        createProduct(
                                "USB-C Hub Multi-Port Adapter",
                                "7-in-1 USB-C hub with HDMI, USB 3.0, and SD card reader",
                                new BigDecimal("350000"),
                                70,
                                "https://images.unsplash.com/photo-1625948515291-69613efd103f?w=500"
                        ),
                        createProduct(
                                "Laptop Stand Aluminum",
                                "Adjustable ergonomic aluminum laptop stand with cooling design",
                                new BigDecimal("250000"),
                                80,
                                "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=500"
                        ),
                        createProduct(
                                "LED Monitor 27 inch",
                                "QHD 144Hz gaming monitor with FreeSync technology",
                                new BigDecimal("3500000"),
                                15,
                                "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500"
                        ),
                        createProduct(
                                "Wireless Power Bank 20000mAh",
                                "Fast charging power bank with wireless charging capability",
                                new BigDecimal("450000"),
                                55,
                                "https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=500"
                        )
                );

                productRepository.saveAll(products);
                log.info("Successfully initialized {} products", products.size());
            } else {
                log.info("Database already contains products, skipping initialization");
            }
        };
    }

    private Product createProduct(String name, String description, BigDecimal price, Integer stock, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setImageUrl(imageUrl);
        return product;
    }
}
