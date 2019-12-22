package itmo.remedictes.orderMicroservice.repos;

import itmo.remedictes.orderMicroservice.domain.OrderItem;
import itmo.remedictes.orderMicroservice.domain.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {
}
