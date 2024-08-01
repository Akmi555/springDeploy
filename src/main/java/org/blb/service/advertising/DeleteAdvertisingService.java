package org.blb.service.advertising;

import lombok.AllArgsConstructor;
import org.blb.exeption.NotFoundException;
import org.blb.models.advertising.Advertising;
import org.blb.repository.advertising.AdvertisingRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteAdvertisingService {
    private final AdvertisingRepository advertisingRepository;

    public void deleteAdvertising(Long advertisingId) {
        Advertising advertising = advertisingRepository.findById(advertisingId)
                .orElseThrow(() ->new NotFoundException("Advertising not found"));
        advertisingRepository.delete(advertising);
    }
}
