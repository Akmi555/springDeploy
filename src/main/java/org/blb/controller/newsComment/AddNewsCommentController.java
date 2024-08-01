package org.blb.controller.newsComment;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.newsComment.NewsCommentRequestDTO;
import org.blb.controller.api.newsComment.AddNewsCommentApi;
import org.blb.service.newsComment.AddNewsCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddNewsCommentController implements AddNewsCommentApi {
    private final AddNewsCommentService addNewsCommentService;

    @Override
    public ResponseEntity<StandardResponseDto> addNewsComment(NewsCommentRequestDTO DTO) {
        addNewsCommentService.addNewsComment(DTO);
        return ResponseEntity.ok(new StandardResponseDto("Comment to news with ID = "+ DTO.getNewsId() +" added successfully"));
    }
}
