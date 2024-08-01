package org.blb.controller.api.newsComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.newsComment.NewsCommentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface FindNewsCommentApi {
    @Operation(summary = "Getting all comments on news", description = "The operation is available to everyone, return all comments on news")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return all comments on news",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsCommentResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Comments are not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Comments are not found\"}")))
    })

    @GetMapping("/comments")
    ResponseEntity<List<NewsCommentResponseDTO>> findAllNewsComments();

    @Operation(summary = "Getting all comments for one news with ID", description = "The operation is available to everyone, return all comments for one news with ID ...")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return array with all comments objects for one news with ID ...",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsCommentResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Comments for news with ID = ... are not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Comments for news with ID = 2 are not found\"}")))
    })

    @GetMapping("/{newsId}/comments")
    ResponseEntity<List<NewsCommentResponseDTO>> findAllNewsCommentsByNewsId(@PathVariable Long newsId);

    @Operation(summary = "Getting news comment by ID", description = "The operation is available to everyone, return comment by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return object comment by ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsCommentResponseDTO.class))),

            @ApiResponse(responseCode = "404", description = "Comment with ID = ...  not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Comment with ID = 5 not found\"}")))
    })

    @GetMapping("/comments/{commentId}")
    ResponseEntity<NewsCommentResponseDTO> findNewsCommentById(@PathVariable Long commentId);
}
