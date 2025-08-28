package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.friends.FriendResponse;
import com.elevate.elevateapi.repository.FriendRepository;
import com.elevate.elevateapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    // remove friend (String username, String friendusername)

    // list friends (String username)
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository,UserRepository userRepository){
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public List<FriendResponse> listFriends(String username){
        Long userId = userRepository.findByUsername(username).getId();
        return friendRepository.findFriendsOf(userId);
    }

    public void removeFriend(String username, Long friendUserId) {

        Long myId = userRepository.findByUsername(username).getId();

        Long a = Math.min(myId, friendUserId);
        Long b = Math.max(myId, friendUserId);

        friendRepository.deleteSymmetric(a, b);
    }

}
