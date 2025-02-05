

package com.med.system.ManTick.chatAI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.Service.CommentService;
import com.med.system.ManTick.ticket.Ticket;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatAIService {

    private final CommentService commentService;

    @Value("${application.chatAI.url}")
    private String chatAi_url;

    @Value("${application.chatAI.port}")
    private String chatAi_port;

    @Value("${application.chatAI.dest}")
    private String chatAi_dest;


    @Async
    public void requestChatAI(Ticket ticket, String username) {
        System.out.println("Requesting Chat AI...");
        String json = "{\"subject\": \"" + ticket.getSubject() + "\", \"description\": \"" + ticket.getDescription() + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + chatAi_url + ":" + chatAi_port + "/" + chatAi_dest))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response to extract the "value" field.
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.body());
            String extractedValue = responseJson.path("ai_response").asText();


            CommentRequest chatCommentRequest = CommentRequest.builder()
                    .ticketId(ticket.getId().longValue())
                    .subject(ticket.getSubject())
                    .message(extractedValue)
                    .fromUser("chatAi@gmail.com")
                    .toUser(username)
                    .build();
            commentService.sendMessage(chatCommentRequest);
            System.out.println(extractedValue);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to request chat AI", e);
        }
    }

}
