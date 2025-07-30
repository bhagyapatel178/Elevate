package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {
}
