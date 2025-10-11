package id.my.hendisantika.xendit.repository;

import id.my.hendisantika.xendit.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
