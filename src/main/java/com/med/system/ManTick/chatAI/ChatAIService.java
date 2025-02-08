

package com.med.system.ManTick.chatAI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.Service.CommentService;
import com.med.system.ManTick.ticket.Status;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.Services.TicketService;



import org.slf4j.Logger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatAIService {


    private static final Logger logger = LoggerFactory.getLogger(ChatAIService.class);
    private static final String CLOSED_TICKET_RESPONSE = "--closed_ticket--";
    private static final String CHAT_AI_EMAIL = "chatAi@gmail.com";

    private final CommentService commentService;
    private final TicketService ticketService;

    @Value("${application.chatAI.url}")
    private String chatAiUrl;

    @Value("${application.chatAI.port}")
    private String chatAiPort;

    @Value("${application.chatAI.dest}")
    private String chatAiDest;


    @Async
    @Transactional
    public void requestChatAI(Ticket ticket, String username) {
        logger.info("Requesting Chat AI for ticket {}...", ticket.getId());
        String jsonPayload = createJsonPayload(ticket.getSubject(), ticket.getDescription());

        String extractedValue = callChatAI(jsonPayload);
        if (extractedValue == null) {
            logger.error("Chat AI returned null response for ticket {}", ticket.getId());
            return;
        }

        CommentRequest chatCommentRequest = CommentRequest.builder()
                .ticketId(ticket.getId().longValue())
                .subject(ticket.getSubject())
                .message(extractedValue)
                .fromUser(CHAT_AI_EMAIL)
                .toUser(username)
                .build();

        commentService.sendMessage(chatCommentRequest);
        logger.info("Chat AI response for ticket {}: {}", ticket.getId(), extractedValue);

        if (CLOSED_TICKET_RESPONSE.equals(extractedValue)) {
            ticketService.changeStatus(ticket.getId(), Status.CLOSED);
            logger.info("Ticket {} closed based on Chat AI response", ticket.getId());
        }
    }


    @Async
    @Transactional
    public void requestMessageChatAI(CommentRequest commentRequest, String username) {
        logger.info("Requesting Chat AI for comment on ticket {}...", commentRequest.getTicketId());
        String jsonPayload = createJsonPayload(commentRequest.getSubject(), commentRequest.getMessage());

        String extractedValue = callChatAI(jsonPayload);
        if (extractedValue == null) {
            logger.error("Chat AI returned null response for comment request on ticket {}",
                    commentRequest.getTicketId());
            return;
        }

        CommentRequest chatCommentRequest = CommentRequest.builder()
                .ticketId(commentRequest.getTicketId())
                .subject(commentRequest.getSubject())
                .message(extractedValue)
                .fromUser(CHAT_AI_EMAIL)
                .toUser(commentRequest.getFromUser())
                .build();

        logger.info("Chat AI response for comment on ticket {}: {}",
                commentRequest.getTicketId(), extractedValue);

        if (CLOSED_TICKET_RESPONSE.equals(extractedValue)) {
            if (isTicketOwner(commentRequest.getTicketId(), commentRequest.getFromUser())) {
                chatCommentRequest.setMessage("The ticket is completed. Wait for the technicians to close the ticket.");
                commentService.sendMessage(chatCommentRequest);
                ticketService.changeStatus(commentRequest.getTicketId(), Status.COMPLETED);
                logger.info("Ticket {} marked as COMPLETED", commentRequest.getTicketId());
            } else {
                chatCommentRequest.setMessage("You're not the owner of this ticket, so it cannot be automatically completed");
                commentService.sendMessage(chatCommentRequest);
                logger.info("User {} is not the owner of ticket {}",
                        commentRequest.getFromUser(), commentRequest.getTicketId());
            }
        } else {
            commentService.sendMessage(chatCommentRequest);
        }
    }

 
    private String createJsonPayload(String subject, String description) {
        // Note: For production, consider using a proper JSON library for building JSON.
        return "{\"subject\": \"" + escapeJson(subject) + "\", \"description\": \"" + escapeJson(description) + "\"}";
    }

  
    private String escapeJson(String value) {
        return (value == null) ? "" : value.replace("\"", "\\\"");
    }


    private String callChatAI(String jsonPayload) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + chatAiUrl + ":" + chatAiPort + "/" + chatAiDest))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.body());
            return responseJson.path("ai_response").asText();
        } catch (IOException | InterruptedException e) {
            logger.error("Failed to request Chat AI", e);
            throw new RuntimeException("Failed to request chat AI", e);
        }
    }


    private boolean isTicketOwner(Long ticketId, String username) {
        Ticket ticket = ticketService.findById(ticketId).orElse(null);
        return ticket != null &&
               ticket.getRequesterName() != null &&
               ticket.getRequesterName().getEmail().equals(username);
    }


}
