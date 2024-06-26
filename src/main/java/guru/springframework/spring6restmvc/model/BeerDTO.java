package guru.springframework.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    private String beerName;
    //@NotBlank
    @NotNull

    private BeerStyle beerStyle;
    @NotBlank
    @NotNull
    private String upc;
    private Integer quantityOnHand;
    //@NotBlank
    @NotNull
    //@No
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
