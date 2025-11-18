package com.parksupark.soomjae.server.community.comment.repository;

import com.parksupark.soomjae.server.community.comment.entity.Comment;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostTypeAndPostIdAndDeletedTimeIsNull(String postType, Long postId);

    long countByPostTypeAndPostIdAndDeletedTimeIsNull(String postType, Long postId);

    @Query("select c.member from Comment c where c.postType = :postType "
        + "and c.postId = :postId and c.deletedTime is null")
    List<Member> findMembersByPost(String postType, Long postId);

    Optional<Comment> findByIdAndDeletedTimeIsNull(Long commentId);
}
