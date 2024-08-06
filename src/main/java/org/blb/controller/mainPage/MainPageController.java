package org.blb.controller.mainPage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.mainPageDto.MpResponseDTO;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.controller.api.mainPage.MainPageAPi;
import org.blb.service.mainPage.MainPageService;
import org.blb.service.weather.OutGeoLocationApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController implements MainPageAPi {
    private final MainPageService mainPageService;
    private final OutGeoLocationApi outGeoLocationApi;

    @Override
    public ResponseEntity<MpResponseDTO> getMainPage() {
        return ResponseEntity.ok(mainPageService.getData());
    }

    @Override
    public ResponseEntity<MpWeatherDTO> weather(HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        if(ip.equals("0:0:0:0:0:0:0:1")){
            ip = "85.214.132.117";
        }
        WeatherLatLonDTO position = outGeoLocationApi.getLatLonFromGeoLocation(ip);
        System.out.println(position);
        return ResponseEntity.ok(mainPageService.getWeather(position));
    }
}
