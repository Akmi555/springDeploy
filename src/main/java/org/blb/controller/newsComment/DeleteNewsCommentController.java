package org.blb.controller.newsComment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.controller.api.newsComment.DeleteNewsCommentApi;
import org.blb.service.newsComment.DeleteNewsCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteNewsCommentController implements DeleteNewsCommentApi {
    private final DeleteNewsCommentService deleteNewsCommentService;

    @Override
    public ResponseEntity<StandardResponseDto> deleteNewsCommentById(@Valid @RequestBody StandardDelRequest dto) {
        deleteNewsCommentService.deleteNewsCommentById(dto);
        return ResponseEntity.ok(new StandardResponseDto("News Comment with ID = "+ dto.getId() +" deleted successfully"));
    }
}
