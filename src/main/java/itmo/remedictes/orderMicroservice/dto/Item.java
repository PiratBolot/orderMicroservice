package itmo.remedictes.orderMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    private Long id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private Long amount;

    @NonNull
    private Double price;
}
