
package com.med.system.ManTick.ticket.Services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@AllArgsConstructor
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    public List<Ticket> getTicketsByRequesterName(String requesterName) {
        return ticketRepository.findByRequesterName(requesterName);
    }

    public Optional<Ticket> findById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public Ticket closeTicket(CloseRequest ticketId) {
        
        Ticket ticket = ticketRepository.findById(ticketId.getTicketId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setStatus(Status.CLOSED);
        ticket.setUpdatedAt(new Date(System.currentTimeMillis()));

        return ticketRepository.save(ticket);
        
    }

    public List<Ticket> searchTicketsBySubject(String subject) {
        return ticketRepository.findBySubject(subject);
    }

    public List<Ticket> searchTicketsBySubjectAndEmail(String subject, String requesterName) {
        return ticketRepository.findBySubjectAndRequesterName(subject, requesterName);
    }


    public List<Ticket> findByStatus(Status status) {
        return ticketRepository.findByStatus(status);
    }
    
    public List<Ticket> findByRequesterAndStatus(String requesterName, Status status) {
        return ticketRepository.findByRequesterNameAndStatus(requesterName, status);
    }
}
