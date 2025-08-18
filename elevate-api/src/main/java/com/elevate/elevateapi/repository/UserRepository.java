package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elevate.elevateapi.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByProviderAndProviderId(String provider, String providerId);
}
