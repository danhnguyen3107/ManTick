package com.med.system.ManTick.ticket.RequestResponse;

import java.sql.Date;

import com.med.system.ManTick.ticket.Level;
import com.med.system.ManTick.ticket.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {

    private Integer id;
    private String subject;
    private String description;

    private Date createdAt;
    private Date updatedAt;
    private String technician;

 
    private String requesterName;

    private Status status;

    private Level level;
}
