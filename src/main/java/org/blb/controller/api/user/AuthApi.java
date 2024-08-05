package org.blb.controller.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.ResponseErrors;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;
import org.blb.DTO.user.UserNewDTO;
import org.blb.security.dto.AuthRequest;
import org.blb.security.dto.AuthResponse;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public interface AuthApi {
    @Operation(summary = "User authentication", description = "The operation is available to everyone, method returned a token of user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="User token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),

    })
    @PostMapping("/auth")
    ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest request);

    @Operation(summary = "User registration", description = "The operation is available to everyone, the default role is USER")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description ="User registered",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Confirmation sanded to your email\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseErrors.class))),
            @ApiResponse(responseCode = "400", description = "User already exist",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"User already exists\"}"))),
                })
    @PostMapping("/registration")
    ResponseEntity<StandardResponseDto> registration(@RequestBody @Valid UserNewDTO user);

    @Operation(summary = "User email confirmation", description = "The operation is available to everyone, confirm user email ")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Confirmation completed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"User confirmed successfully\"}"))),
            @ApiResponse(responseCode = "400", description = "Confirmation failed",
                    content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"Invalid confirmation code\"}"))),
            @ApiResponse(responseCode = "404", description = "Confirmation failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"User not found\"}")))
           })

    @GetMapping("/confirmation")
    ResponseEntity<StandardResponseDto> confirmation(@RequestParam("data") String id,
                                               @RequestParam("code") String code);
}
