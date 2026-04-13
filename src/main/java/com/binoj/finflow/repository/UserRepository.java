package com.binoj.finflow.repository;

import com.binoj.finflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobile(String mobile);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrMobile(String email, String mobile);
}