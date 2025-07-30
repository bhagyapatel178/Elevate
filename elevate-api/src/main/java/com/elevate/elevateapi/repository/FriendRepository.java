package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
