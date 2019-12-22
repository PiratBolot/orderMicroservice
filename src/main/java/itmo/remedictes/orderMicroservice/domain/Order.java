package itmo.remedictes.orderMicroservice.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Entity
@Table(name="orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

    private OrderStatus status;

    private BigDecimal totalCost;

    private int totalAmount;

    private String username;

    @OneToMany(mappedBy = "ord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;

    public Order(){
        this.status = OrderStatus.COLLECTING;
    }

    public Order(BigDecimal totalCost, int totalAmount, String username, OrderItem... items){
        this.status = OrderStatus.COLLECTING;
        this.username = username;
        this.totalAmount = totalAmount;
        this.totalCost = totalCost.multiply(BigDecimal.valueOf(totalAmount));
        for(OrderItem orderitems : items) orderitems.setOrd(this);
        this.orderItems = Stream.of(items).collect(Collectors.toSet());
    }
}
