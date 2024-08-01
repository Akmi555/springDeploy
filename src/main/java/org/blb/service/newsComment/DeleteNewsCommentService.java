package org.blb.service.newsComment;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsComment;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.service.news.UpdateNewsDataService;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteNewsCommentService {
    private final NewsCommentRepository newsCommentRepository;
    private final UpdateNewsDataService updateNewsDataService;
    private final UserFindService userFindService;
    private final RoleRepository roleRepository;

    public void deleteNewsCommentById(StandardDelRequest dto) {

        User user = userFindService.getUserFromContext();
        NewsComment comment = newsCommentRepository.findById(dto.getId())
                .orElseThrow(()-> new NotFoundException("Comment with id = " + dto.getId() + " not found"));
        Role role = roleRepository.findByRole("ADMIN");
        if (user != comment.getUser() && user.getRole() != role){
            throw new RestException(HttpStatus.CONFLICT, "You don't have permission to delete this comment");
        }
        updateNewsDataService.reduceCommentsCount(comment.getNewsDataEntity());
        newsCommentRepository.delete(comment);
    }
}
