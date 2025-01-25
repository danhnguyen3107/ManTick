

package com.med.system.ManTick.comment.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.ticket.Ticket;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>  {

    List<Comment> findByTicketId(Long ticketId);

}
