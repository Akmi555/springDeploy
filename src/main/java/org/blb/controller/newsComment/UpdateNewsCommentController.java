package org.blb.controller.newsComment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.newsComment.UpdateCommentRequestDTO;
import org.blb.controller.api.newsComment.UpdateNewsCommentApi;
import org.blb.service.newsComment.UpdateNewsCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class UpdateNewsCommentController implements UpdateNewsCommentApi {
    private final UpdateNewsCommentService updateNewsCommentService;

    @Override
    @PutMapping("comment")
    public ResponseEntity<StandardResponseDto> updateNewsCommentById(@Valid @RequestBody UpdateCommentRequestDTO DTO) {
        updateNewsCommentService.updateNewsComment(DTO);
        return ResponseEntity.ok(new StandardResponseDto("Comment with ID = "+ DTO.getId() +" updated successfully"));
    }
}
