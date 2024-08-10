package org.blb.service.region;

import org.blb.DTO.region.RegionDTO;
import org.blb.exeption.RestException;
import org.blb.models.region.Region;
import org.blb.repository.region.RegionRepository;
import org.blb.service.util.regionMapping.RegionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindRegionServiceTest {

    @InjectMocks
    private FindRegionService findRegionService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RegionConverter regionConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllRegions_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("Region1");

        when(regionRepository.findAll()).thenReturn(Collections.singletonList(region));
        when(regionConverter.toDTO(region)).thenReturn(regionDTO);

        ResponseEntity<List<RegionDTO>> response = findRegionService.findAllRegions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Region1", response.getBody().get(0).getRegionName());
    }

    @Test
    void testFindAllRegions_EmptyList() {
        when(regionRepository.findAll()).thenReturn(Collections.emptyList());

        RestException thrown = assertThrows(RestException.class, () -> {
            findRegionService.findAllRegions();
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("No regions found", thrown.getMessage());
    }

    @Test
    void testFindRegionById_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("Region1");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));
        when(regionConverter.toDTO(region)).thenReturn(regionDTO);

        RegionDTO result = findRegionService.findRegionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Region1", result.getRegionName());
    }

    @Test
    void testFindRegionById_NotFound() {
        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        RestException thrown = assertThrows(RestException.class, () -> {
            findRegionService.findRegionById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Region with ID = 1 not found", thrown.getMessage());
    }

    @Test
    void testFindRegionByName_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("Region1");

        when(regionRepository.findByRegionName("Region1")).thenReturn(Optional.of(region));
        when(regionConverter.toDTO(region)).thenReturn(regionDTO);

        RegionDTO result = findRegionService.findRegionByName("Region1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Region1", result.getRegionName());
    }

    @Test
    void testFindRegionByName_NotFound() {
        when(regionRepository.findByRegionName("Region1")).thenReturn(Optional.empty());

        RestException thrown = assertThrows(RestException.class, () -> {
            findRegionService.findRegionByName("Region1");
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Region with name 'Region1' not found", thrown.getMessage());
    }

    @Test
    void testGetRegionById_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        Region result = findRegionService.getRegionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Region1", result.getRegionName());
    }

    @Test
    void testGetRegionById_NotFound() {
        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        RestException thrown = assertThrows(RestException.class, () -> {
            findRegionService.getRegionById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Region with ID = 1 not found", thrown.getMessage());
    }

    @Test
    void testFindRegionByNameOptional_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        when(regionRepository.findByRegionName("Region1")).thenReturn(Optional.of(region));

        Optional<Region> result = findRegionService.findRegionByNameOptional("Region1");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Region1", result.get().getRegionName());
    }

    @Test
    void testFindRegionByNameOptional_NotFound() {
        when(regionRepository.findByRegionName("Region1")).thenReturn(Optional.empty());

        Optional<Region> result = findRegionService.findRegionByNameOptional("Region1");

        assertFalse(result.isPresent());
    }
}