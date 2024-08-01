package org.blb.controller.api.blog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.blog.BlogAddRequestDTO;
import org.blb.DTO.blog.BlogUpdateDTO;
import org.blb.DTO.blog.blogs.BlogCommentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blog")
@CrossOrigin(origins = "http://localhost:5173")
public interface BlogApi {
    @Operation(summary = "Adding new blog", description = "The operation is available to registered user, add new blog")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about blog adding",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Blog added successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Region with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Region with ID = 25 not found\"}")))

    })

    @PutMapping()
    ResponseEntity<StandardResponseDto> addBlog(@RequestBody @Valid BlogAddRequestDTO dto);

    @Operation(summary = "Deleting blog", description = "The operation is available to owner of the blog or admin")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about blog deleting",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Blog removed successfully\"}"))),
            @ApiResponse(responseCode = "409", description = "Only author can dell blog",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"You dont have permission to dell this blog\"}"))),
            @ApiResponse(responseCode = "404", description = "Region with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Blog not found\"}")))
    })

    @DeleteMapping()
    ResponseEntity<StandardResponseDto> delBlog(@RequestBody @Valid StandardDelRequest dto);

    @Operation(summary = "Updating blog", description = "The operation is available to owner of the blog or admin")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about blog updating",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Blog update successfully\"}"))),
            @ApiResponse(responseCode = "409", description = "Only author can dell blog",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"You dont have permission to update this blog\"}"))),
            @ApiResponse(responseCode = "404", description = "Region with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Blog not found\"}")))
    })

    @PostMapping()
    ResponseEntity<StandardResponseDto> updateBlog(@RequestBody @Valid BlogUpdateDTO dto);

    @Operation(summary = "Adding new blog comment", description = "The operation is available to registered user, add new comment")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information comment blog adding",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Comment added successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Blog not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \" Blog not found\"}")))

    })

    @PutMapping("/comment")

    ResponseEntity<StandardResponseDto> addComment(@RequestBody @Valid BlogCommentRequestDTO dto);

    @Operation(summary = "Deleting the comment", description = "The operation is available to user, who published the comment")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about comment deleting",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Comment deleted successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Blog not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \" Comment not found\"}")))

    })

    @DeleteMapping("/comment")
    ResponseEntity<StandardResponseDto> delComment(@RequestBody @Valid StandardDelRequest dto);

}
