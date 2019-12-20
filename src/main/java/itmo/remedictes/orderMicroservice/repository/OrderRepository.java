package itmo.remedictes.orderMicroservice.repository;

import itmo.remedictes.orderMicroservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsById(Long id);
}
