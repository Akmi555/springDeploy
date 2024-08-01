package org.blb.models.weather;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather")
public class WeatherDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String latitude;
    private String longitude;
    private String cityName;
    private String temperature;
    private String appTemperature;
    private String icon;
    private String description;
    private String humidity;
    private String windCdir;
    private String windCdirFull;
    private String windSpd;


    @Column(name = "'create'")
    private LocalDateTime timeCreate;
}
