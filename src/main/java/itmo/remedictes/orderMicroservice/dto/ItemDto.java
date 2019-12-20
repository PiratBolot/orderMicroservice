package itmo.remedictes.orderMicroservice.dto;

import itmo.remedictes.orderMicroservice.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "ReservedItem")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order itemOrder;
}
