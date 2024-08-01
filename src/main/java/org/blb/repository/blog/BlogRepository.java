package org.blb.repository.blog;


import org.blb.models.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
//    @Modifying
//    @Query("UPDATE Blog b set b.comments = b.comments + 1 where b.id = :id" )
//    public void updateComment(@Param("id") Integer id);
//    @Modifying
//    @Query("UPDATE Blog b set b.views = b.views + 1 where b.id = :id" )
//    public void updateViews(@Param("id") Integer id);

}
