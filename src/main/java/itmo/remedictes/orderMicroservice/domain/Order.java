package itmo.remedictes.orderMicroservice.domain;

import itmo.remedictes.orderMicroservice.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private OrderStatus status;

    private Double totalCost;

    private Long totalAmount;

    private String username;

    @OneToMany(mappedBy ="order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDto> items;

}
