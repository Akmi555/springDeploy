package org.blb.service.util;

import lombok.AllArgsConstructor;
import org.blb.DTO.advertisingDto.AdditionalResponseDto;
import org.blb.DTO.advertisingDto.AdvertisingRequestDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.blb.DTO.advertisingDto.ShortAdvertisingResponseDto;
import org.blb.models.advertising.Advertising;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdvertisingConverter {
    public Advertising fromDtoToEntity(AdvertisingRequestDto advertisingRequestDto) {
        Advertising advertising = new Advertising();

        if (advertisingRequestDto.getTitle() != null) {
            advertising.setTitle(advertisingRequestDto.getTitle());
        }

        if (advertisingRequestDto.getDescription() != null) {
            advertising.setDescription(advertisingRequestDto.getDescription());
        }

        if (advertisingRequestDto.getAdvertiserName() != null) {
            advertising.setAdvertiserName(advertisingRequestDto.getAdvertiserName());
        }
        if (advertisingRequestDto.getAdvertiserEmail() != null) {
            advertising.setAdvertiserEmail(advertisingRequestDto.getAdvertiserEmail());
        }
        if (advertisingRequestDto.getAdvertiserPhone() != null) {
            advertising.setAdvertiserPhone(advertisingRequestDto.getAdvertiserPhone());


        }
        if(advertisingRequestDto.getAdvertisingCounter() !=null){
            advertising.setAdvertisingCounter(advertisingRequestDto.getAdvertisingCounter());
        }
        if (advertisingRequestDto.getDiscount() != null) {
            advertising.setDiscount(advertisingRequestDto.getDiscount());
        }
        if (advertisingRequestDto.getCreateData() != null) {
            advertising.setCreateData(advertisingRequestDto.getCreateData());
        }
        if(advertisingRequestDto.getDescriptionOfTheCoupon()!= null){
            advertising.setDescriptionOfTheCoupon(advertisingRequestDto.getDescriptionOfTheCoupon());
        }

        System.out.println( advertising.getEndData()+"-----------------------------");
            advertising.setEndData(advertisingRequestDto.getEndData());

        return advertising;
    }
    public AdvertisingResponseDto fromEntityToDto(Advertising advertising) {
        return new AdvertisingResponseDto(
                advertising.getId(),
                advertising.getTitle(),
                advertising.getDescription(),
                advertising.getAdvertiserName(),
                advertising.getAdvertiserEmail(),
                advertising.getAdvertiserPhone(),
                advertising.getDiscount(),
                advertising.getCreateData(),
                advertising.getEndData(),
                advertising.getAdvertisingCounter(),
                advertising.getDescriptionOfTheCoupon()

        );

    }

    public ShortAdvertisingResponseDto shortAdvertisingFromEntityToDto(Advertising advertising) {

        return new ShortAdvertisingResponseDto(
                advertising.getId(),
                advertising.getTitle()

        );
    }

    public AdditionalResponseDto additionalResponseDto(Advertising advertising) {
        return new AdditionalResponseDto(
                advertising.getTitle(),
                advertising.getDescription(),
                advertising.getAdvertiserName(),
                advertising.getAdvertiserEmail(),
                advertising.getAdvertiserPhone(),
                advertising.getDiscount(),
                advertising.getDescriptionOfTheCoupon(),
                advertising.getCreateData(),
                advertising.getEndData()
        );
    }

}
