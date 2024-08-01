package org.blb.service.advertising;

import lombok.AllArgsConstructor;
import org.blb.DTO.advertisingDto.AdvertisingRequestDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.blb.models.advertising.Advertising;
import org.blb.repository.advertising.AdvertisingRepository;
import org.blb.service.util.AdvertisingConverter;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAdvertisingService {
    private final AdvertisingRepository advertisingRepository;
    private final AdvertisingConverter advertisingConverter;

    public AdvertisingResponseDto addAdvertising(AdvertisingRequestDto requestDto) {
        Advertising advertising = advertisingConverter.fromDtoToEntity(requestDto);
        advertisingRepository.save(advertising);
        return advertisingConverter.fromEntityToDto(advertising);
    }

}
