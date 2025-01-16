package hu.udinfopark.jpa.optimistic.dto;

import hu.udinfopark.common.backend.api.entity.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OptimisticDTO extends BaseDTO {
    private String descripton;

    private boolean isDeleted;
}
