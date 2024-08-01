package org.blb.service.blogComment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogCommentAddService {
//    private final UserFindService userFindService;
////    private final BlogFindService blogFindService;
////    private final BlogCommentRepository commentBlogRepository;
////    private final BlogUpdateService blogUpdateService;
////
////    public String addCommentBlog(BlCommentRequestDTO requestDTO) {
////        User user = userFindService.findUserById(requestDTO.getAuthorId());
////        Blog blog = blogFindService.findById(requestDTO.getBlogId());
////        Integer id = commentBlogRepository.save(new BlogComment(requestDTO.getComment(), user, blog)).getId();
////        blogUpdateService.increaseComments(blog.getId());
////        return "{\"blogCommentId\" : " + id+"}";
////    }
}
