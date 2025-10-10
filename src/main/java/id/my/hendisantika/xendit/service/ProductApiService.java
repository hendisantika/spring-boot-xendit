package id.my.hendisantika.xendit.service;

import id.my.hendisantika.xendit.dto.ApiResponse;
import id.my.hendisantika.xendit.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.11
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class ProductApiService {

    private final WebClient webClient;

    public List<Product> getAllProducts() {
        ApiResponse<List<Product>> response = webClient.get()
                .uri("/api/products")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<Product>>>() {
                })
                .block();
        return response != null ? response.getData() : List.of();
    }

    public Product getProductById(Long id) {
        ApiResponse<Product> response = webClient.get()
                .uri("/api/products/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Product>>() {
                })
                .onErrorResume(e -> Mono.empty())
                .block();
        return response != null ? response.getData() : null;
    }
}
