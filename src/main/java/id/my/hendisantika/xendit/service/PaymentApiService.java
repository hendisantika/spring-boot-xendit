package id.my.hendisantika.xendit.service;

import id.my.hendisantika.xendit.dto.ApiResponse;
import id.my.hendisantika.xendit.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.13
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class PaymentApiService {

    private final WebClient webClient;

    public Map<String, Object> createInvoice(PaymentRequestDTO paymentRequest) {
        ApiResponse<Map<String, Object>> response = webClient.post()
                .uri("/api/payments/create-invoice")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {
                })
                .block();
        return response != null ? response.getData() : null;
    }
}
