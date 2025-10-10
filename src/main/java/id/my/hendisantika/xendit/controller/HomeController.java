package id.my.hendisantika.xendit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.14
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }
}
