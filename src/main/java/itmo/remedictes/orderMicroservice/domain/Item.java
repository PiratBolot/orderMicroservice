package itmo.remedictes.orderMicroservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="item")
@JsonIgnoreProperties("orders")
@Data
@NoArgsConstructor
public class Item {
    @Id
    private int itemId;

    private String name;

    private BigDecimal price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orders = new HashSet<>();

    public Item(int itemId, String name, BigDecimal price){
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }
}
