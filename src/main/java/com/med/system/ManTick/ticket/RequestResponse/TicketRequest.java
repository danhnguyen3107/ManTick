package com.med.system.ManTick.ticket.RequestResponse;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.med.system.ManTick.Users.User;
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
public class TicketRequest {

    private String subject;
    private String description;
    private String requesterName;
    private Level level;
}
