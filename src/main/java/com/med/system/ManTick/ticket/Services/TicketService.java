
package com.med.system.ManTick.ticket.Services;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.ticket.Level;
import com.med.system.ManTick.ticket.Status;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.Repository.TicketRepository;
import com.med.system.ManTick.ticket.RequestResponse.AssignTicketRequest;
import com.med.system.ManTick.ticket.RequestResponse.CloseRequest;
import com.med.system.ManTick.ticket.RequestResponse.TicketRequest;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    public Ticket createTicket(TicketRequest request) {
        // Validate and fetch the requester
        User requester = userRepository.findByEmail(request.getRequesterName())
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        
        Level level = request.getLevel();
        if (level == null) level = Level.LOW; 
        
        // Create and save the ticket
        Ticket ticket = Ticket.builder()
                .subject(request.getSubject())
                .description(request.getDescription())
                .createdAt(new Date(System.currentTimeMillis()))
                .requesterName(requester)
                .status(Status.OPEN)
                .level(level)
                .build();

        return ticketRepository.save(ticket);
    }
    public Ticket assignTicket(AssignTicketRequest assignTicketRequest) {
        // Validate and fetch the ticket

        Ticket ticket = ticketRepository.findById(assignTicketRequest.getTicketId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Validate and fetch the technician
        User technician = userRepository.findByEmail(assignTicketRequest.getTechnician())
                .orElseThrow(() -> new IllegalArgumentException("Technician not found"));

        // Update the ticket with the technician
        ticket.setTechnician(technician);
        ticket.setStatus(Status.IN_PROGRESS);
        ticket.setUpdatedAt(new Date(System.currentTimeMillis()));

        return ticketRepository.save(ticket);

        
    }
    public Ticket closeTicket(CloseRequest ticketId) {
        
        Ticket ticket = ticketRepository.findById(ticketId.getTicketId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setStatus(Status.CLOSED);
        ticket.setUpdatedAt(new Date(System.currentTimeMillis()));

        return ticketRepository.save(ticket);
        
    }
}
