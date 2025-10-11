package id.my.hendisantika.xendit.config;

import com.xendit.Xendit;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-xendit
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/10/25
 * Time: 05.52
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class XenditConfig {

    @Value("${xendit.api.key}")
    private String xenditApiKey;

    @PostConstruct
    public void init() {
        Xendit.apiKey = xenditApiKey;
    }
}
