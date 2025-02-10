
package com.med.system.ManTick.Notification.RequestResponse;


import java.util.Set;
import com.med.system.ManTick.Users.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequestByrole {

    private String message;
    private Set<Role> roles;

}
