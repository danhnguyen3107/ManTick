package com.med.system.ManTick.ticket.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.ticket.Status;
import com.med.system.ManTick.ticket.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

    @Query("SELECT t FROM Ticket t WHERE t.requesterName.email = :requesterName")
    List<Ticket> findByRequesterName(String requesterName);

    @Modifying
    @Query("UPDATE Ticket t SET t.requesterName = :trailingUser WHERE t.requesterName = :user")
    void updateTicketsByRequesterName(@Param("user") User user, @Param("trailingUser") User trailingUser);

    @Query("SELECT t FROM Ticket t WHERE LOWER(t.subject) LIKE LOWER(CONCAT('%', :subject, '%'))")
    List<Ticket> findBySubject(@Param("subject") String subject);

    @Query("SELECT t FROM Ticket t WHERE LOWER(t.subject) LIKE LOWER(CONCAT('%', :subject, '%')) AND t.requesterName.email = :requesterName")
    List<Ticket> findBySubjectAndRequesterName(@Param("subject") String subject, @Param("requesterName") String requesterName);


    @Query("SELECT t FROM Ticket t WHERE t.status = :status")
    List<Ticket> findByStatus(@Param("status") Status status);
    
    @Query("SELECT t FROM Ticket t WHERE t.requesterName.email = :requesterName AND t.status = :status")
    List<Ticket> findByRequesterNameAndStatus(@Param("requesterName") String requesterName, @Param("status") Status status);

}
