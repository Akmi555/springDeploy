package org.blb.controller.region;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.region.RegionDTO;
import org.blb.models.region.Region;
import org.blb.repository.region.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class FindRegionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ObjectMapper objectMapper;

@Test
void testFindAllRegions() throws Exception {
    List<RegionDTO> regions = regionRepository.findAll()
            .stream()
            .map(region -> new RegionDTO(region.getId(), region.getRegionName()))
            .collect(Collectors.toList());

    mockMvc.perform(get("/regions")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(regions)));
}

@Test
void testFindRegionById() throws Exception {
    Long regionId = 2L;
    Optional<Region> optionalRegion = regionRepository.findById(regionId);

    if (optionalRegion.isPresent()) {
        Region region = optionalRegion.get();
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());

        mockMvc.perform(get("/regions/findById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id",String.valueOf(regionId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(regionDTO)));
    } else {
        throw new Exception("Region with ID " + regionId + " not found");
    }
}

@Test
void testFindRegionByNotExistingId() throws Exception {
        Long regionId = 111111111L;
        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("Region with ID = "+ regionId +" not found");

    mockMvc.perform(get("/regions/findById")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("id",String.valueOf(regionId)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
}
@Test
void testFindRegionByName() throws Exception {
        String regionName = "Berlin";
        Optional<Region> optionalRegion = regionRepository.findByRegionName(regionName);

        if (optionalRegion.isPresent()) {
            Region region = optionalRegion.get();
            RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());

            mockMvc.perform(get("/regions/findBy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("region",String.valueOf(regionName)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(regionDTO)));
        } else {
            throw new Exception("Region with name " + regionName + " not found");
        }
}
@Test
void testFindRegionByNotExistingName() throws Exception {
        String regionName = "Schweiz";
        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("Region with name '"+ regionName +"' not found");

        mockMvc.perform(get("/regions/findBy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("region",String.valueOf(regionName)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
}
@Test
void testFindRegionByEmptyName() throws Exception {
        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("Region with name '' not found");

        mockMvc.perform(get("/regions/findBy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("region",""))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
}

@Test
void testFindRegionById_InvalidIdFormat() throws Exception {
        String invalidId = "invalid";

        mockMvc.perform(get("/regions/findById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", invalidId))
                .andExpect(status().isBadRequest());
}

@Test
void testFindRegionById_NullId() throws Exception {
        mockMvc.perform(get("/regions/findById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", (String) null))
                .andExpect(status().isBadRequest());
}

}