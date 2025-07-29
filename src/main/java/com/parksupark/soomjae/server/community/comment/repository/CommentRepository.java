package com.parksupark.soomjae.server.community.comment.repository;

import com.parksupark.soomjae.server.community.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostTypeAndPostIdAndDeletedTimeIsNull(String postType, Long postId);

    Optional<Comment> findByIdAndDeletedTimeIsNull(Long commentId);
}
