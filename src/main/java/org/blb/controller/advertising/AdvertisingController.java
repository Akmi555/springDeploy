package org.blb.controller.advertising;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.advertisingDto.AdditionalResponseDto;
import org.blb.DTO.advertisingDto.AdvertisingRequestDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.blb.DTO.advertisingDto.ShortAdvertisingResponseDto;
import org.blb.DTO.appDTO.StandardResponseDto;
//import org.blb.controller.api.advertising.AdvertisingApi;
import org.blb.controller.api.advertising.AddAdvertising;
import org.blb.controller.api.advertising.AdvertisingApi;
import org.blb.controller.api.advertising.DeleteAdvertising;
import org.blb.service.advertising.CreateAdvertisingService;
import org.blb.service.advertising.DeleteAdvertisingService;
import org.blb.service.advertising.FindAdvertisingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@RestController
//@RequiredArgsConstructor
//public class AdvertisingController  {
//    private final CreateAdvertisingService createAdvertisingService;
//    private final DeleteAdvertisingService deleteAdvertisingService;
//    private final FindAdvertisingService findAdvertisingService;
//    private final AdvertisingRepository advertisingRepository;
//
//    public ResponseEntity<ShortAdvertisingResponseDto> addAdvertising(AdvertisingRequestDto dto) {
//        createAdvertisingService.addAdvertising(dto);
//        return ResponseEntity.ok(new ShortAdvertisingResponseDto());
//    }
//
//
//    public ResponseEntity<StandardResponseDto> deleteAdvertising(StandardDelRequest dto) {
//        deleteAdvertisingService.deleteAdvertising(dto.getId());
//        return ResponseEntity.ok(new StandardResponseDto("Advertising removed successfully"));
//    }
//
//
//    public ResponseEntity<StandardResponseDto> updateAdvertising(AdvertisingRequestDto dto) {
//        return null;
//    }
//
//
//
//    public ResponseEntity<AdvertisingResponseDto> findByAdvertiserEmail(String email) {
//        AdvertisingResponseDto responseDto = findAdvertisingService.findByAdvertiserEmail(email);
//        return ResponseEntity.ok(responseDto);
//    }
//
//    @Override
//    public ResponseEntity<List<ShortAdvertisingResponseDto>> findAll() {
//        List<ShortAdvertisingResponseDto> responseDto = findAdvertisingService.findAll();
//        return ResponseEntity.ok(responseDto);
//    }
//
//    @Override
//    public ResponseEntity<ShortAdvertisingResponseDto> findAdvertisingById(Long id) {
//        ShortAdvertisingResponseDto response = findAdvertisingService.findAdvertisingById(id);
//        return ResponseEntity.ok(response);
//
//    }
//
//    @Override
//    public ResponseEntity<List<ShortAdvertisingResponseDto>> findFiveRandomAdvertisings() {
//        List<ShortAdvertisingResponseDto> response = findAdvertisingService.findFiveRandomAdvertisings();
//        return ResponseEntity.ok(response);
//    }
//}




@RestController
@RequiredArgsConstructor
public class AdvertisingController implements AdvertisingApi, DeleteAdvertising, AddAdvertising {
    private final CreateAdvertisingService createAdvertisingService;
    private final DeleteAdvertisingService deleteAdvertisingService;
    private final FindAdvertisingService findAdvertisingService;

    @Override
    public ResponseEntity<AdvertisingResponseDto> addAdvertising(@RequestBody @Valid AdvertisingRequestDto requestDto) {
        AdvertisingResponseDto responseDto = createAdvertisingService.addAdvertising(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<StandardResponseDto> deleteAdvertising(@PathVariable Long id) {
        deleteAdvertisingService.deleteAdvertising(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AdvertisingResponseDto> findByAdvertiserEmail(@PathVariable String email) {
        AdvertisingResponseDto responseDto = findAdvertisingService.findByAdvertiserEmail(email);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<AdditionalResponseDto> findAdvertisingById(@PathVariable Long id) {
        AdditionalResponseDto responseDto = findAdvertisingService.findAdvertisingById(id);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<List<ShortAdvertisingResponseDto>> findFiveRandomAdvertisings() {
        List<ShortAdvertisingResponseDto> responseDtos = findAdvertisingService.findFiveRandomAdvertisings();
        return ResponseEntity.ok(responseDtos);
    }

    @Override
    public ResponseEntity<List<AdvertisingResponseDto>> findAll() {
        List<AdvertisingResponseDto> responseDtos = findAdvertisingService.findAll();
        return ResponseEntity.ok(responseDtos);
    }
}