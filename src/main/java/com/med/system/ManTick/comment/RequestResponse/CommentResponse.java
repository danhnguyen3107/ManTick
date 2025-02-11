

package com.med.system.ManTick.comment.RequestResponse;


import com.med.system.ManTick.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Integer id;
    private String subject;
    private String message;
    private String fromUser;
    private String toUser;
    private long ticketId;


    public static CommentResponse convertToCommentResponse(Comment comment){
        return CommentResponse.builder()
            .id(comment.getId())
            .subject(comment.getSubject())
            .message(comment.getMessage())
            .fromUser(comment.getFromUser().getUsername())
            .toUser(comment.getToUser().getUsername())
            .ticketId(comment.getTicket().getId())
            .build();

                                            
    }

}
