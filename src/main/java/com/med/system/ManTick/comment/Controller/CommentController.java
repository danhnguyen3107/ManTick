
package com.med.system.ManTick.comment.Controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.chatAI.ChatAIService;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.RequestResponse.CommentResponse;
import com.med.system.ManTick.comment.Service.CommentService;
import com.med.system.ManTick.comment.entity.Comment;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
class CommentController {
    
    private final CommentService commentService;
    private final ChatAIService chatAiService;
    private final UserRepository userRepository;


    @PostMapping("/{ticketId}")
    public ResponseEntity<CommentResponse> sendComment(
        @RequestPart(name ="image", required = false) MultipartFile file,
        @RequestPart("commentRequest") CommentRequest request,
        @PathVariable long ticketId
        ) throws IOException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        request.setFromUser(authentication.getName());
        request.setTicketId(ticketId);
        CommentRequest.validateCommentRequest(request);
        
        Comment comment = file != null && !file.isEmpty() ? commentService.sendMessage(request, file) : commentService.sendMessage(request);

        CommentResponse commentResponse = CommentResponse.convertToCommentResponse(comment);

        User user = userRepository.findByEmail(request.getToUser())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isAI()) {

            chatAiService.requestMessageChatAI(request, request.getToUser());
          
        }


        return ResponseEntity.ok(commentResponse);
    }
}
