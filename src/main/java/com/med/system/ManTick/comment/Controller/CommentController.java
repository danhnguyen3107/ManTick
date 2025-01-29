
package com.med.system.ManTick.comment.Controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.RequestResponse.CommentResponse;
import com.med.system.ManTick.comment.Service.CommentService;
import com.med.system.ManTick.comment.entity.Comment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
class CommentController {
    
    private final CommentService commentService;

    @PostMapping("/{ticketId}")
    public ResponseEntity<CommentResponse> sendComment(
        @RequestBody CommentRequest request,
        @PathVariable long ticketId
        ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        request.setFromUser(authentication.getName());
        request.setTicketId(ticketId);
        CommentRequest.validateCommentRequest(request);

        Comment comment = commentService.sendMessage(request);
        CommentResponse commentResponse = CommentResponse.convertToCommentResponse(comment);


        return ResponseEntity.ok(commentResponse);
    }
}
