

package com.med.system.ManTick.comment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.ticket.Ticket;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>  {

    List<Comment> findByTicketId(Long ticketId);
    @Modifying
    @Query("UPDATE Comment c SET c.fromUser = :trailingUser WHERE c.fromUser = :user")
    void updateCommentsByFromUser(@Param("user") User user, @Param("trailingUser") User trailingUser);

    @Modifying
    @Query("UPDATE Comment c SET c.toUser = :trailingUser WHERE c.toUser = :user")
    void updateCommentsByToUser(@Param("user") User user, @Param("trailingUser") User trailingUser);
}
