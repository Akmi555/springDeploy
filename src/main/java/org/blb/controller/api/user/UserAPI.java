package org.blb.controller.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.user.UserResponseDTO;
import org.blb.DTO.user.UserRoleDTO;
import org.blb.DTO.user.UserStateDTO;
import org.blb.DTO.user.UsersRolesDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/admin/user")
public interface UserAPI {
    @Operation(summary = "Getting user list", description = "The operation is available to admin user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Only Admin can perform this operation",
                    content = @Content(mediaType = "application/json"))

    })

    @GetMapping("/{page}")

    ResponseEntity<List<UserResponseDTO>> getUsers(@PathVariable Integer page);

    @Operation(summary = "Return to Admin list of user roles and user statuses", description = "The operation is available to admin user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersRolesDTO.class))),
            @ApiResponse(responseCode = "403", description = "Only Admin can perform this operation",
                    content = @Content(mediaType = "application/json"))

    })

    @GetMapping()

    ResponseEntity<UsersRolesDTO> getUsersRole();

    @Operation(summary = "Change user  state", description = "The operation is available to admin user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about users",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"User update successfully\"}"))),
            @ApiResponse(responseCode = "403", description = "Only Admin can perform this operation",
                    content = @Content(mediaType = "application/json"))

    })

    @PutMapping ()
    ResponseEntity<StandardResponseDto> updateUsersState(@RequestBody @Valid UserStateDTO state);

    @Operation(summary = "Change user role", description = "The operation is available to admin user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about users",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"User update successfully\"}"))),
            @ApiResponse(responseCode = "403", description = "Only Admin can perform this operation",
                    content = @Content(mediaType = "application/json"))
   })

    @PostMapping ()
    ResponseEntity<StandardResponseDto> changeUserRole(@RequestBody @Valid UserRoleDTO dto);



}
