

package com.med.system.ManTick.admin.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.med.system.ManTick.Users.User;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstname) LIKE LOWER(CONCAT('%', :firstname, '%'))")
    List<User> findByFirstnameContainingIgnoreCase(@Param("firstname") String firstname);

    @Query("SELECT u FROM User u WHERE LOWER(u.lastname) LIKE LOWER(CONCAT('%', :lastname, '%'))")
    List<User> findByLastnameContainingIgnoreCase(@Param("lastname") String lastname);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
    "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByUnknowCategory(@Param("searchTerm") String searchTerm);
}
