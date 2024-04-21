package com.yassine.demoecommerce.repository;

import com.yassine.demoecommerce.entity.User;
import com.yassine.demoecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


  Optional<User> findByEmail(String email);
  User findByUserRole(UserRole userRole);

}
