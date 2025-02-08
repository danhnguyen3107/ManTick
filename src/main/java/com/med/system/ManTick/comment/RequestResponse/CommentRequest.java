
package com.med.system.ManTick.comment.RequestResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Long ticketId;
    private String subject;
    private String message;
    private String fromUser;
    private String toUser;


    public static void validateCommentRequest(CommentRequest commentRequest) {
        if (commentRequest.getSubject() == null || commentRequest.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (commentRequest.getMessage() == null || commentRequest.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (commentRequest.getFromUser() == null || commentRequest.getFromUser().isEmpty()) {
            throw new IllegalArgumentException("FromUser cannot be null or empty");
        }
        if (commentRequest.getToUser() == null || commentRequest.getToUser().isEmpty()) {
            throw new IllegalArgumentException("ToUser cannot be null or empty");
        }
        if (commentRequest.getTicketId() == null || commentRequest.getTicketId()  <= 0) {
            throw new IllegalArgumentException("TicketId must be greater than zero");
        }

    }
}
