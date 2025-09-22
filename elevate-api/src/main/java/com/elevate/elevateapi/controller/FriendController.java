package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.dto.friends.FriendResponse;
import com.elevate.elevateapi.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }

    @GetMapping("")
    public List<FriendResponse> listFriends(@AuthenticationPrincipal UserDetails userDetails){
        return friendService.listFriends(userDetails.getUsername());
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        friendService.removeFriend(userDetails.getUsername(), friendId);
        return ResponseEntity.noContent().build();
    }
}
