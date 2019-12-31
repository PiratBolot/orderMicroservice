package itmo.remedictes.orderMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import itmo.remedictes.orderMicroservice.domain.OrderItem;
import itmo.remedictes.orderMicroservice.domain.OrderStatus;
import lombok.*;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderDto {
    @NonNull
    private int orderID;

    @NonNull
    private OrderStatus status;

    @NonNull
    private String username;

    @NonNull
    private BigDecimal totalCost;

    @NonNull
    private int totalAmount;

    @NonNull
    private Set<OrderItem> orderItems;

    public OrderDto(int orderID){
        this.orderID = orderID;
    }

    public OrderDto(int orderID, OrderStatus status){
        this.orderID = orderID;
        this.status = status;
    }
}
