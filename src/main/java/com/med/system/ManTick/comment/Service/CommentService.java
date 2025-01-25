

package com.med.system.ManTick.comment.Service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.comment.Repository.CommentRepository;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.Services.TicketService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;


    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public List<Comment> getAllCommentsByTicketID(long ticketId){
        return commentRepository.findByTicketId(ticketId);
    }
    public Comment sendMessage(CommentRequest commentRequest){

        String subject = commentRequest.getSubject();
        String message = commentRequest.getMessage();
        Date createdAt = new Date(System.currentTimeMillis());

        User fromUser = userRepository.findByEmail(commentRequest.getFromUser())
            .orElseThrow(() -> new RuntimeException("User not found"));

        User toUser = userRepository.findByEmail(commentRequest.getToUser())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Ticket ticket = ticketService.findById(commentRequest.getTicketId())
            .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Comment comment = Comment.builder()
                .subject(subject)
                .message(message)
                .createdAt(createdAt)
                .fromUser(fromUser)
                .toUser(toUser)
                .ticket(ticket)
                .build();
         
        
        return commentRepository.save(comment);
    }



}
