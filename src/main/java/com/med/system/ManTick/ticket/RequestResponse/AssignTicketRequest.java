
package com.med.system.ManTick.ticket.RequestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignTicketRequest {
    private Integer ticketId;
    private String technician;
}
