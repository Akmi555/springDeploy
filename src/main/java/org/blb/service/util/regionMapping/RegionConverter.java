package org.blb.service.util.regionMapping;

import lombok.AllArgsConstructor;
import org.blb.DTO.region.RegionDTO;
import org.blb.models.region.Region;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegionConverter {
    public RegionDTO toDTO(Region region) {
        RegionDTO DTO = new RegionDTO();
        DTO.setId(region.getId());
        DTO.setRegionName(region.getRegionName());
        return DTO;
    }

    public Region fromDTO(RegionDTO DTO) {
        Region region = new Region();
        region.setRegionName(DTO.getRegionName());
        return region;
    }
}
