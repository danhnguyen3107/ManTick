package com.med.system.ManTick.ticket.RequestResponse;




import com.med.system.ManTick.ticket.Level;
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
