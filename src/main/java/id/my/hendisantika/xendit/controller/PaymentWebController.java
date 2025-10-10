package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.service.PaymentApiService;
import id.my.hendisantika.xendit.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
