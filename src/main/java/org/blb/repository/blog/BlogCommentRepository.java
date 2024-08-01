package org.blb.repository.blog;


import org.blb.models.blog.Blog;
import org.blb.models.blog.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findAllByBlog(Blog blog);
}
