package itmo.remedictes.orderMicroservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="orderItem")
@JsonIgnoreProperties({"order","orditID"})
@Data
@NoArgsConstructor
public class OrderItem {
    @EmbeddedId
    private OrderItemKey orditID;

    @ManyToOne
    @MapsId("ordID")
    private Order ord;

    @ManyToOne
    @MapsId("itemID")
    private Item item;

    private int amount;

    public OrderItem(Item item, int amount){
        this.item = item;
        this.amount = amount;
        this.orditID = new OrderItemKey(item.getItemId());
    }

    public OrderItem(Order ord, Item item, int amount){
        this.ord = ord;
        this.item = item;
        this.amount = amount;
        this.orditID = new OrderItemKey(ord.getOrderID(), item.getItemId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return amount == orderItem.amount &&
                ord.equals(orderItem.ord) &&
                item.equals(orderItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ord, item, amount);
    }
}
