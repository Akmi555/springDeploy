package org.blb.controller.api.mainPage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.blb.DTO.mainPageDto.MpResponseDTO;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mainpage")
public interface MainPageAPi {
    @Operation(summary = "Get data for main page", description = "All data for main page except weather")
    @ApiResponse(responseCode = "200", description ="return content.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MpResponseDTO.class)))
    @GetMapping()
    ResponseEntity<MpResponseDTO> getMainPage();


    @GetMapping("/weather")
    @Operation(summary = "Get data for main page", description = "Data -  weather")
    @ApiResponse(responseCode = "200", description ="return content.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MpWeatherDTO.class)))
    ResponseEntity<MpWeatherDTO> weather(HttpServletRequest request);
}
