package itmo.remedictes.orderMicroservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SurogateKey implements Serializable {
    @Column(name= "surogateordid")
    private long ordID;

    @Column(name= "surogateitemid")
    private long itemID;

    public SurogateKey(long itemID){
        this.itemID = itemID;
    }

    public SurogateKey(long ordID, long itemID){
        this.ordID = ordID;
        this.itemID = itemID;
    }
}
