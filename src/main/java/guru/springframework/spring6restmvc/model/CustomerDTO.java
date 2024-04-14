package guru.springframework.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
//@Component
public class CustomerDTO {
    private UUID id;
    private String name;
    private Integer version;
    private LocalDate crtDate;
    private LocalDate modDate;
}
