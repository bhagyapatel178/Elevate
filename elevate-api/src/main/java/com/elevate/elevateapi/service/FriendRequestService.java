package com.elevate.elevateapi.service;

import com.elevate.elevateapi.entity.FriendRequest;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.FriendRequestRepository;
import com.elevate.elevateapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository,UserRepository userRepository){
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    // send friend request (string senderusername, string receiverusername)

    // delete friend request (string senderusername, string receiverusername)

    // list recieved friend requests (string username)

//    public void sendRequest(String username, Long id) {
//        Long senderId = userRepository.findByUsername(username).getId();
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setSender(userRepository.findByUsername(username));
//        friendRequest.setReceiver(userRepository.getReferenceById(id));
//        friendRequest.setStatus(FriendRequest.Status.PENDING);
//
//        friendRequestRepository.save(friendRequest);
//    }

    public void sendRequest(String senderUsername, Long receiverId) {

        User sender   = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (sender.equals(receiver))
            throw new IllegalArgumentException("Cannot friend yourself");

//        if (friendshipRepo.existsBetween(sender.getId(), receiverId))
//            throw new IllegalStateException("Already friends");

//        if (friendRequestRepository.existsPending(sender.getId(), receiverId))
//            throw new IllegalStateException("Request already pending");

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequestRepository.save(friendRequest);
    }
}
