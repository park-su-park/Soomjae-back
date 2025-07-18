package com.parksupark.soomjae.server.community.comment.repository;

import com.parksupark.soomjae.server.community.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostTypeAndPostIdAndDeletedTimeIsNull(String postType, Long postId,
            Pageable pageable);

    Optional<Comment> findByIdAndDeletedTimeIsNull(Long commentId);
}
