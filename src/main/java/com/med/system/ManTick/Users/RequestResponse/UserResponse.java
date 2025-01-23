

package com.med.system.ManTick.Users.RequestResponse;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.med.system.ManTick.Users.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private Role role;
}
