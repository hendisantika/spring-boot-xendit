package id.my.hendisantika.xendit.controller;

import id.my.hendisantika.xendit.entity.Product;
import id.my.hendisantika.xendit.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 06.24
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductApiService productApiService;

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String search,
            Model model) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productApiService.searchProducts(search);
            model.addAttribute("search", search);
        } else {
            products = productApiService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "products/list";
    }
}
