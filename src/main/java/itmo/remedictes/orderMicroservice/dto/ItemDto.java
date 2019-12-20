package itmo.remedictes.orderMicroservice.dto;

import itmo.remedictes.orderMicroservice.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @Id
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long amount;

    @NonNull
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
