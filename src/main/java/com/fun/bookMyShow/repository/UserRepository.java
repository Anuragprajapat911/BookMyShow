package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long>
{

    Optional<User> findByEmail(String Email);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);

    Boolean existsByEmail(String email);
}
