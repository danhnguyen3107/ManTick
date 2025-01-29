
package com.med.system.ManTick.admin.services;



import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.med.system.ManTick.Users.Role;
import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.admin.repository.AdminRepository;
import com.med.system.ManTick.comment.Repository.CommentRepository;
import com.med.system.ManTick.ticket.Ticket;
import com.med.system.ManTick.ticket.Repository.TicketRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final  CommentRepository commentRepository;
    private final  TicketRepository ticketRepository;
    public List<User> getAllUsers() {
        return adminRepository.findAll();
    }

    @Transactional
    public boolean deleteUserById(long id) {
        Optional<User> userOptional = adminRepository.findById(id);

        if (userOptional.isEmpty()) {
            return false;
        }
    
        User user = userOptional.get();
    
        // Prevent deletion of ADMIN users
        if (user.getRole() == Role.ADMIN) {
            return false;
        }
    
        // Find a replacement user for reassignment
        User trailingUser = userRepository.findByEmail("trailingUser@gmail.com")
                .orElseThrow(() -> new RuntimeException("Trailing user not found"));
    
        // Reassign comments
        commentRepository.updateCommentsByFromUser(user, trailingUser);
        commentRepository.updateCommentsByToUser(user, trailingUser);
    
        // Reassign tickets
        ticketRepository.updateTicketsByRequesterName(user, trailingUser);
    
        // Clear assigned ticket reference
        Ticket assignedTicket = user.getAssignTicket();
        if (assignedTicket != null) {
            assignedTicket.setTechnician(null);
            ticketRepository.save(assignedTicket);
        }
    
        // Delete the user
        adminRepository.delete(user);
        return true;
    }


    

    public List<User> searchUserByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public List<User> searchUsersByFirstname(String firstname) {
        return adminRepository.findByFirstnameContainingIgnoreCase(firstname);
    }

    public List<User> searchUsersByLastname(String lastname) {
        return adminRepository.findByLastnameContainingIgnoreCase(lastname);
    }

    public List<User> searchUsers(String request){
  

        return adminRepository.findByUnknowCategory(request);
    }

}
