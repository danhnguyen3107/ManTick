

package com.med.system.ManTick.comment.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.comment.Repository.CommentRepository;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.images.ImageData;
import com.med.system.ManTick.images.services.ImageDataService;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.Services.TicketService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;
    private final ImageDataService imageDataService;



    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public List<Comment> getAllCommentsByTicketID(long ticketId){
        return commentRepository.findByTicketId(ticketId);
    }

    @Transactional
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

    @Transactional
    public Comment sendMessage(CommentRequest commentRequest, MultipartFile image) throws IOException{

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
        
        ImageData imageData = imageDataService.uploadImage(image);
        comment.addImage(imageData);


        return commentRepository.save(comment);
    }
}
