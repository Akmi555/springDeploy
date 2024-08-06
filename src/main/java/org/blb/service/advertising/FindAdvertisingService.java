package org.blb.service.advertising;

import lombok.AllArgsConstructor;
import org.blb.DTO.advertisingDto.AdditionalResponseDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.blb.DTO.advertisingDto.ShortAdvertisingResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.advertising.Advertising;
import org.blb.repository.advertising.AdvertisingRepository;
import org.blb.service.util.AdvertisingConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FindAdvertisingService {
    private final AdvertisingRepository advertisingRepository;
    private final AdvertisingConverter advertisingConverter;

    public AdvertisingResponseDto findByAdvertiserEmail(String email) {
        Optional<Advertising> optionalAdvertising = advertisingRepository.findByAdvertiserEmail(email);
        if (optionalAdvertising.isPresent()) {
            return advertisingConverter.fromEntityToDto(optionalAdvertising.get());
        } else {
            throw new NotFoundException("Advertising not found");
        }
    }

    public List<AdvertisingResponseDto> findAll() {
        List<Advertising> advertisings = advertisingRepository.findAll();
        List<AdvertisingResponseDto> advertisingResponseDtos = advertisings.stream()
                .map(advertisingConverter::fromEntityToDto)
                .collect(Collectors.toList());
        return advertisingResponseDtos;
    }


//    public List<AdvertisingResponseDto>  findAll(){
//
//        List<Advertising> advertisings = advertisingRepository.findAll();
//        return advertisings.stream().map(advertisingConverter::fromEntityToDto).collect(Collectors.toList());
//
//    }




    public AdditionalResponseDto findAdvertisingById(Long id) {
        Advertising advertising = advertisingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertising with id = " + id + " not found"));

        if (!advertising.getId().equals(1L)) {
            advertising.setAdvertisingCounter(advertising.getAdvertisingCounter() -1);
            advertisingRepository.save(advertising);
        }

        return advertisingConverter.additionalResponseDto(advertising);
    }

    public List<ShortAdvertisingResponseDto> findFiveRandomAdvertisings() {
        List<Advertising> advertisings = advertisingRepository.findAllForThoseWhoHaveNotExpiredAndCounterBiggerZero(LocalDate.now(), 0);

        Collections.shuffle(advertisings);

        Advertising ourAdvertisement = advertisingRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Advertising with id = 1 not found"));

        List<Advertising> filteredAdvertisings = advertisings.stream()
                .filter(advertising -> !advertising.getId().equals(1L))
                .limit(6)
                .collect(Collectors.toList());

        filteredAdvertisings.add(0, ourAdvertisement);

        return filteredAdvertisings.stream()
                .map(advertisingConverter::shortAdvertisingFromEntityToDto)
                .collect(Collectors.toList());
    }
}
