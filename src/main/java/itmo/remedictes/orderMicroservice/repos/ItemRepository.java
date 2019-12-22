package itmo.remedictes.orderMicroservice.repos;

import itmo.remedictes.orderMicroservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
