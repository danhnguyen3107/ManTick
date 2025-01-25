

package com.med.system.ManTick.ticket.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.ticket.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

    @Query("SELECT t FROM Ticket t WHERE t.requesterName.email = :requesterName")
    List<Ticket> findByRequesterName(String requesterName);
}
