package itmo.remedictes.orderMicroservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class OrderItemKey implements Serializable {
    private int ordID;
    private int itemID;

    public OrderItemKey(int itemID){
        this.itemID = itemID;
    }

    public OrderItemKey(int ordID, int itemID){
        this.ordID = ordID;
        this.itemID = itemID;
    }
}
