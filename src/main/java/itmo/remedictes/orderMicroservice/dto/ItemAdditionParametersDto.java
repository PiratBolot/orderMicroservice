package itmo.remedictes.orderMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ItemAdditionParametersDto {
    private int id;

    private String name;

    private int amount;

    private BigDecimal price;

    private String username;
}
