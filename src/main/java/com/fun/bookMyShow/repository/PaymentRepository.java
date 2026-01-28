package com.fun.bookMyShow.repository;

import com.fun.bookMyShow.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {


    Optional<Payment> findByTransectionId(String transectionId);

}
