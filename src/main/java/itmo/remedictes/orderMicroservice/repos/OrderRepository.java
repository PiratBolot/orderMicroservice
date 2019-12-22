package itmo.remedictes.orderMicroservice.repos;

import itmo.remedictes.orderMicroservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
