package itmo.remedictes.orderMicroservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="orderItem")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "orderInstance")
public class OrderItem {
    @EmbeddedId
    @NonNull
    @Column(name = "orderItemId")
    private SurogateKey orderItemId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Order orderInstance;

    @NonNull
    @Column(name= "orderitemname")
    private String itemName;

    @NonNull
    @Column(name= "orderitemamount")
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return amount == orderItem.amount &&
                itemName == orderItem.itemName &&
                orderInstance.equals(orderItem.orderInstance) &&
                orderItemId.equals(orderItem.orderItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, orderInstance, itemName, amount);
    }
}
