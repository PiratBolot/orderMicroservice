package itmo.remedictes.orderMicroservice.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Entity
@Table(name ="orders")
@Data
@ToString()
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderidkek")
    @ColumnDefault(value = "1")
    private int orderId;

    @Column(name= "orderstatus")
    private OrderStatus status;

    @Column(name= "ordertotalcost")
    private BigDecimal totalCost;

    @Column(name= "sssss")
    private int totalAmount;

    @Column(name= "orderusername")
    private String username;

    @OneToMany(mappedBy = "orderInstance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;

    public Order(){
        this.status = OrderStatus.COLLECTING;
    }

    public Order(BigDecimal totalCost, int totalAmount, String username, OrderItem... items){
        this.status = OrderStatus.COLLECTING;
        this.username = username;
        this.totalAmount = totalAmount;
        this.totalCost = totalCost.multiply(BigDecimal.valueOf(totalAmount));
        this.orderItems = Stream.of(items).collect(Collectors.toSet());
        this.orderItems.forEach(x -> x.setOrderInstance(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, totalCost, totalAmount, username);
    }
}
