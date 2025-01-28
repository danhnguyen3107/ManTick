

package com.med.system.ManTick.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.med.system.ManTick.Users.User;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {

}
