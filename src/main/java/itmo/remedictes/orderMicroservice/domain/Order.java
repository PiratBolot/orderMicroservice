package itmo.remedictes.orderMicroservice.domain;

import itmo.remedictes.orderMicroservice.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CustomerOrder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalCost;

    private Long totalAmount;

    private String username;

    @OneToMany(mappedBy ="itemOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDto> items;

}
