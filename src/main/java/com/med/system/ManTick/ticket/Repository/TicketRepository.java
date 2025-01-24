

package com.med.system.ManTick.ticket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.ticket.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
    
}
