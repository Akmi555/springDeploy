package org.blb.DTO.region;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionJustWithNameDto {
    @NotBlank(message = "Region name must be not blank.")
    @NotNull(message = "Region name must be not null.")
    private String regionName;

}
