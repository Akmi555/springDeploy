package org.blb.service.newsComment;

import lombok.AllArgsConstructor;
import org.blb.DTO.newsComment.UpdateCommentRequestDTO;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsComment;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UpdateNewsCommentService {
    private final NewsCommentRepository newsCommentRepository;
    private final UserFindService userFindService;
    private final RoleRepository roleRepository;

    @Transactional
    public void updateNewsComment(UpdateCommentRequestDTO dto) {
        User user = userFindService.getUserFromContext();
        NewsComment comment = newsCommentRepository.findById(dto.getId())
                .orElseThrow(()-> new NotFoundException("Comment with ID = " + dto.getId() + " not found"));
        Role role = roleRepository.findByRole("ADMIN");
        if (user != comment.getUser() && user.getRole() != role){
            throw new RestException(HttpStatus.CONFLICT, "You don't have permission to update this comment");
        }
        newsCommentRepository.updateCommentById(dto.getComment(), LocalDateTime.now(), dto.getId());
    }
}
