package itmo.remedictes.orderMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemAdditionParametersDto {
    @NonNull
    private Long id;

    @NonNull
    private Long amount;

    @NonNull
    private String username;
}
