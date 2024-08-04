package org.blb.controller.api.blog;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;
import org.blb.DTO.blog.blogs.ContentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public interface BlogsApi {
    @Operation(summary = "Getting last blogs", description = "The operation is available to everyone, return 10 blogs from latest")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="return total page count, current page, and 10 blogs if available.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogsResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Region with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Region with ID = 25 not found\"}")))

    })

    @GetMapping()
    ResponseEntity<BlogsResponseDTO> getBlogs(@RequestParam Integer page, @RequestParam Long region);

    @Operation(summary = "Getting last blogs by current user", description = "The operation is available to registered user, return 10 blogs from latest")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="return total page count, current page, and 10 blogs if available.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogsResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Region with ID = .. not found")

    })

    @GetMapping("/user")
    ResponseEntity<BlogsResponseDTO> getBlogsByUser(@RequestParam Integer page);


    @Operation(summary = "Getting content of the blog by id", description = "The operation is available to everyone, return content of a blog")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="return content.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Blog with ID  .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Blog with id .. not found\"}")))

    })
    @GetMapping("/{id}")
    ResponseEntity<ContentResponseDTO> getBlog(@PathVariable Long id);
}
