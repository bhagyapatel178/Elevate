package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.dto.IncomingFriendRequests;
import com.elevate.elevateapi.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("")
    public List<IncomingFriendRequests> getIncomingFriendRequests(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return friendRequestService.listFriendRequests(username);
    }

    @PutMapping("/{requestId}/accept") // PUT /api/friend-requests/{id}/accept
    public ResponseEntity<Void> accept(@PathVariable Long requestId) {
        friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long requestId) {
        friendRequestService.cancelFriendRequest(requestId);
        return ResponseEntity.noContent().build();
    }

}
