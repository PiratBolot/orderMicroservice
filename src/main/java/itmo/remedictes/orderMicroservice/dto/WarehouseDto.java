package itmo.remedictes.orderMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class WarehouseDto implements Serializable {
    @NonNull
    private int orderID;

    @NonNull
    private int itemID;

    @NonNull
    private int amount;
}
