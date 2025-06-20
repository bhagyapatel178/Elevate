package com.elevate.elevateapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elevate.elevateapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
