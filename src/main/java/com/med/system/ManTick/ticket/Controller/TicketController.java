

package com.med.system.ManTick.ticket.Controller;



import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.med.system.ManTick.admin.RequestResponse.SearchRequest;
import com.med.system.ManTick.chatAI.ChatAIService;
import com.med.system.ManTick.comment.RequestResponse.CommentRequest;
import com.med.system.ManTick.comment.RequestResponse.CommentResponse;
import com.med.system.ManTick.comment.Service.CommentService;
import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.ticket.Status;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.RequestResponse.AssignTicketRequest;
import com.med.system.ManTick.ticket.RequestResponse.CloseRequest;
import com.med.system.ManTick.ticket.RequestResponse.TicketRequest;
import com.med.system.ManTick.ticket.RequestResponse.TicketResponse;
import com.med.system.ManTick.ticket.Services.TicketService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {


    private final TicketService ticketService;

    private final CommentService commentService;
    private final ChatAIService chatAiService;

    @GetMapping
    private ResponseEntity<?> getAllTickets(@RequestParam(value = "status", required = false) String statusStr) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        boolean isAdminOrManager = this.isAdmin();
        

        Status status = null;

        // Convert status string to enum
        if (statusStr != null) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status: " + statusStr);
            }
        }
    
        List<Ticket> tickets;
    
        if (isAdminOrManager) {
            tickets = (status != null) ? ticketService.findByStatus(status) : ticketService.getAllTickets();
        } else {
            tickets = (status != null) ? ticketService.findByRequesterAndStatus(username, status) 
                                        : ticketService.getTicketsByRequesterName(username);
        }

        List<TicketResponse> response = tickets.stream().map(this::convertTicketToTicketRequest).toList();
        return ResponseEntity.ok(response);
        
    }
    @PostMapping
    private ResponseEntity<?> createTicket(
        @RequestPart(name ="image", required = false) MultipartFile file,
        // @RequestBody TicketRequest ticketRequest
        @RequestPart("ticketRequest") TicketRequest ticketRequest
        ) throws IOException, InterruptedException{
        
        // ticketService.createTicket(ticket);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ticketRequest.setRequesterName(authentication.getName());

        if (!validateTicketRequest(ticketRequest)) return ResponseEntity.badRequest().body("Invalid ticket request");
        

        Ticket ticket = ticketService.createTicket(ticketRequest);
        

        CommentRequest commentRequest = CommentRequest.builder()
                                    .ticketId(ticket.getId().longValue())
                                    .subject(ticket.getSubject())
                                    .message(ticket.getDescription())
                                    .fromUser(authentication.getName())
                                    .toUser("admin@gmail.com")
                                    .build();
        if (file != null && !file.isEmpty()) {
            commentService.sendMessage(commentRequest, file);
        }
        else {
            commentService.sendMessage(commentRequest);
        }

        // requestChatAI is asynchronous, but it will be called only after sendMessage completes
        chatAiService.requestChatAI(ticket, authentication.getName());

        
        return ResponseEntity.ok(convertTicketToTicketRequest(ticket));
    }

    @PutMapping("/assignTicket")
    // @PreAuthorize("hasAuthority('admin:update') or hasAuthority('management:update')")
    private ResponseEntity<?> assignTicket(@RequestBody AssignTicketRequest assignTicketRequest) {

        // Validate the ticket request fields
        if (!validateAssignTicketRequest(assignTicketRequest)) return ResponseEntity.badRequest().body("Invalid assign ticket request");

        Ticket ticket = ticketService.assignTicket(assignTicketRequest);
        return ResponseEntity.ok(convertTicketToTicketRequest(ticket));
       
    }

    @PutMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(
                @RequestBody List<CloseRequest> changeStatusRequest, 
                @RequestParam(value = "status") String statusStr){
  

       
        try {
            Status status = Status.valueOf(statusStr.toUpperCase());
            List<Ticket> closedTickets = changeStatusRequest.stream()
            .map(
                change -> 
                    ticketService.changeStatus(change, status)
                ).toList();

            List<TicketResponse> response = closedTickets.stream().map(this::convertTicketToTicketRequest).toList();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + statusStr);
        }

    }
    @GetMapping("/{ticketId}")
    private ResponseEntity<?> getAllComment(@PathVariable long ticketId) {
        List<Comment> comments = commentService.getAllCommentsByTicketID(ticketId);

        List<CommentResponse> response = comments.stream()
            .map(CommentResponse::convertToCommentResponse)
            .toList();

        return ResponseEntity.ok(response);
    }



    @GetMapping("/search")
    // @PreAuthorize("hasAuthority('admin:read')")
    public List<TicketResponse> searchTickets(@RequestBody SearchRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        boolean isAdmin = this.isAdmin();


        List<Ticket> tickets = isAdmin ?  ticketService.searchTicketsBySubject(request.getRequest()) 
                                        : ticketService.searchTicketsBySubjectAndEmail(request.getRequest(), username);

        List<TicketResponse> response = tickets.stream()
            .map(this::convertTicketToTicketRequest)
            .toList();

        return response;

    }


    private boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // String username = userDetails.getUsername();
        return userDetails.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_MANAGER"));
 
    }

    private boolean validateAssignTicketRequest(AssignTicketRequest assignTicketRequest) {
        // Validate the ticket request fields
        if (assignTicketRequest.getTicketId() == null || 
            assignTicketRequest.getTechnician() == null || 
            assignTicketRequest.getTechnician().isEmpty()) {

            return false;
        }

        return true;
    }

    
    private boolean validateTicketRequest(TicketRequest ticketRequest) {
        // Validate the ticket request fields
        if (ticketRequest.getSubject() == null || 
            ticketRequest.getSubject().isEmpty() ||
            ticketRequest.getRequesterName() == null || 
            ticketRequest.getRequesterName().isEmpty() || 
            ticketRequest.getLevel() == null) {

            return false;
        }

        return true;
    }

    private TicketResponse convertTicketToTicketRequest(Ticket ticket) {
        TicketResponse ticketRequest = TicketResponse.builder()
        .id(ticket.getId())
        .subject(ticket.getSubject())
        .description(ticket.getDescription())
        .technician(ticket.getTechnician() != null ? ticket.getTechnician().getEmail() : null)
        .requesterName(ticket.getRequesterName().getEmail())
        .status(ticket.getStatus())
        .level(ticket.getLevel())
        .createdAt(ticket.getCreatedAt())
        .updatedAt(ticket.getUpdatedAt() != null ? ticket.getUpdatedAt() : null)
        
        .build();

        // Set other fields as necessary
        return ticketRequest;
    }

}
