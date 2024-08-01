package org.blb.service.region;

import lombok.AllArgsConstructor;
import org.blb.DTO.region.RegionDTO;
import org.blb.exeption.RestException;
import org.blb.models.region.Region;
import org.blb.repository.region.RegionRepository;
import org.blb.service.util.regionMapping.RegionConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FindRegionService {

    private final RegionRepository regionRepository;
    private final RegionConverter regionConverter;

    public ResponseEntity<List<RegionDTO>> findAllRegions() {
        List<Region> regions = regionRepository.findAll();
        List<RegionDTO> DTOs = regions.stream().map(regionConverter::toDTO).toList();
        if (!regions.isEmpty()) {
            return new ResponseEntity<>(DTOs, HttpStatus.OK);
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "No regions found");
        }
    }

    public RegionDTO findRegionById(Long id) {
        Optional<Region> foundedRegionOpt = regionRepository.findById(id);

        if (foundedRegionOpt.isPresent()) {
            return regionConverter.toDTO(foundedRegionOpt.get());
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Region with ID = " + id + " not found");
        }
    }

    public RegionDTO findRegionByName(String name) {
        Optional<Region> foundedRegionOpt = regionRepository.findByRegionName(name);

        if (foundedRegionOpt.isPresent()) {
            return regionConverter.toDTO(foundedRegionOpt.get());
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Region with name '" + name + "' not found");
        }
    }

    public Region getRegionById(Long id) {
        Optional<Region> foundedRegionOpt= regionRepository.findById(id);

        if (foundedRegionOpt.isPresent()) {
            return foundedRegionOpt.get();
        }else {
            throw new RestException(HttpStatus.NOT_FOUND, "Region with ID = " + id + " not found");
        }
    }

    public Optional<Region> findRegionByNameOptional(String name) {
        return regionRepository.findByRegionName(name);
    }

}
