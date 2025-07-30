package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/friend-request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("send")
    public ResponseEntity<Map<String, String>> sendRequest
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody Long id){

        String username = userDetails.getUsername();
        friendRequestService.sendRequest(username, id);
        return ResponseEntity.ok(Map.of("message","Account details updated"));
    }
}
