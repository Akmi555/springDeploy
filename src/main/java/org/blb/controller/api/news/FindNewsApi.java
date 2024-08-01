package org.blb.controller.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.news.NewsDataPageResponseDto;
import org.blb.DTO.news.NewsDataResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface FindNewsApi {
    @Operation(summary = "Getting last news", description = "The operation is available to everyone, return 12 news from latest per 1 page")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return total page count, current page, and 12 news from latest if available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDataPageResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "No News found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No News found\"}")))
    })

    @GetMapping
    ResponseEntity<NewsDataPageResponseDto> findAllNews(@RequestParam Integer page);

    @Operation(summary = "Getting content of news by id", description = "The operation is available to everyone, return content of the news")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="return content.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDataResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "News with ID  .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"News with ID = 13 not found\"}")))

    })
    @GetMapping("/{newsId}")
    ResponseEntity<NewsDataResponseDto> findNewsById(@PathVariable Long newsId);


    @Operation(summary = "Getting last news by criteria (section OR/AND region)", description = "The operation is available to everyone, return 12 news from latest per 1 page by criteria")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return total page count, current page, and 12 news from latest if available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDataPageResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "News Data for section ... OR/AND region ... Not found",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "News Data for region", value = "{\"message\": \"News Data for region 'Berlin' Not found\"}"),
                                    @ExampleObject(name = "News Data for section", value = "{\"message\": \"News Data for section 'sport' Not found\"}"),
                                    @ExampleObject(name = "News Data for section and region", value = "{\"message\": \"News Data for section: 'sport' and region: 'Berlin' not found\"}")
                            }
                    )),
            @ApiResponse(responseCode = "400", description = "Both section and region name cannot be null or empty",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Both section and region name cannot be null or empty\"}")))
    })

    @GetMapping("/findBy")
    ResponseEntity<NewsDataPageResponseDto> findByCriteria(@RequestParam Integer page, @RequestParam String section, @RequestParam String region);

    @Operation(summary = "Getting last news by region ID", description = "The operation is available to everyone, return 12 news from latest per 1 page by region")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return total page count, current page, and 12 news from latest if available",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDataPageResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "News Data for region with ID = ... Not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"News Data for region with ID = 5 Not found\"}")))
    })

    @GetMapping("/region-id")
    ResponseEntity<NewsDataPageResponseDto> findAllNewsByRegionId(@RequestParam Integer page, @RequestParam Long regionId);


}
