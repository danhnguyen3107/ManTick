

package com.med.system.ManTick.ticket.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.RequestResponse.AssignTicketRequest;
import com.med.system.ManTick.ticket.RequestResponse.CloseRequest;
import com.med.system.ManTick.ticket.RequestResponse.TicketRequest;
import com.med.system.ManTick.ticket.RequestResponse.TicketResponse;
import com.med.system.ManTick.ticket.Services.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    private ResponseEntity<?> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        
        List<TicketResponse> response = tickets.stream().map(this::convertTicketToTicketRequest).toList();
        return ResponseEntity.ok(response);
        
    }
    @PostMapping
    private ResponseEntity<?> createTicket(@RequestBody TicketRequest ticketRequest) {
        // ticketService.createTicket(ticket);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ticketRequest.setRequesterName(authentication.getName());

        if (!validateTicketRequest(ticketRequest)) return ResponseEntity.badRequest().body("Invalid ticket request");
        
        Ticket ticket = ticketService.createTicket(ticketRequest);


        return ResponseEntity.ok(convertTicketToTicketRequest(ticket));
    }

    @PostMapping("/assignTicket")
    private ResponseEntity<?> assignTicket(@RequestBody AssignTicketRequest assignTicketRequest) {

        // Validate the ticket request fields
        if (!validateAssignTicketRequest(assignTicketRequest)) return ResponseEntity.badRequest().body("Invalid assign ticket request");

        Ticket ticket = ticketService.assignTicket(assignTicketRequest);
        return ResponseEntity.ok(convertTicketToTicketRequest(ticket));
       
    }
    @PostMapping("/closeTicket")
    private ResponseEntity<?> closeTicket(@RequestBody CloseRequest closeRequest) {
        Ticket ticket = ticketService.closeTicket(closeRequest);
        return ResponseEntity.ok(convertTicketToTicketRequest(ticket));
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
