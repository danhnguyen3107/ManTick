package com.med.system.ManTick.Users;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.med.system.ManTick.Notification.UserNotification;
import com.med.system.ManTick.comment.entity.Comment;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.token.Token;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    @OneToMany(mappedBy = "requesterName", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "fromUser",  fetch = FetchType.LAZY)
    private List<Comment> fromComments;

    @OneToMany(mappedBy = "toUser",  fetch = FetchType.LAZY)
    private List<Comment> toComments;

    @OneToOne(mappedBy = "technician")
    private Ticket assignTicket;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<UserNotification> userNotifications;
    
    
    @Column(name = "is_aI", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isAI;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        // Implement this method to return the username used to authenticate the user
        return email;
    }
    @Override
    public String getPassword() {
        // Implement this method to return the username used to authenticate the user
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                // ", role=" + role +
                '}';
    }
}
