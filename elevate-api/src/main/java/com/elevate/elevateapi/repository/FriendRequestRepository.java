package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.dto.IncomingFriendRequests;
import com.elevate.elevateapi.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    @Query("""
    select new com.elevate.elevateapi.dto.IncomingFriendRequests(
        fr.id,
        fr.sender.id,
        fr.sender.username
    )
    from FriendRequest fr
    where fr.receiver.username = :username
      and fr.status = com.elevate.elevateapi.entity.FriendRequest.Status.PENDING
""")
    List<IncomingFriendRequests> findIncoming(String username);
}
