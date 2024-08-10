package org.blb.service.util.regionMapping;

import org.blb.DTO.region.RegionDTO;
import org.blb.models.region.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegionConverterTest {

    private RegionConverter regionConverter;

    @BeforeEach
    void setUp() {
        regionConverter = new RegionConverter();
    }

    @Test
    void testToDTO_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("RegionName");

        RegionDTO dto = regionConverter.toDTO(region);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("RegionName", dto.getRegionName());
    }

    @Test
    void testToDTO_NullValues() {
        Region region = new Region();
        region.setId(null);
        region.setRegionName(null);

        RegionDTO dto = regionConverter.toDTO(region);

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getRegionName());
    }

    @Test
    void testFromDTO_Success() {
        RegionDTO dto = new RegionDTO();
        dto.setId(1L);
        dto.setRegionName("RegionName");

        Region region = regionConverter.fromDTO(dto);

        assertNotNull(region);
        assertEquals("RegionName", region.getRegionName());
    }

    @Test
    void testFromDTO_NullValues() {
        RegionDTO dto = new RegionDTO();
        dto.setId(null);
        dto.setRegionName(null);

        Region region = regionConverter.fromDTO(dto);

        assertNotNull(region);
        assertNull(region.getRegionName());
    }
}