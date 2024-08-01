package org.blb.repository.weather;

import org.blb.models.weather.WeatherDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherDataEntity, Long> {
    Optional<WeatherDataEntity> findByLatitudeAndLongitude (String lat, String lon);
}
