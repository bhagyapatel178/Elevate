package com.elevate.elevateapi.dto;

public record IncomingFriendRequests (
    Long requestId,      // needed to accept / decline
    Long senderId,
    String senderUsername
){}