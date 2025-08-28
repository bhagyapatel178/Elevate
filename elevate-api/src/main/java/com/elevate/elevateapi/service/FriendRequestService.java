package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.friends.IncomingFriendRequests;
import com.elevate.elevateapi.entity.Friend;
import com.elevate.elevateapi.entity.FriendRequest;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.FriendRepository;
import com.elevate.elevateapi.repository.FriendRequestRepository;
import com.elevate.elevateapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;

    public FriendRequestService(
            FriendRequestRepository friendRequestRepository,
            UserRepository userRepository,
            FriendRepository friendRepository){
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public void sendRequest(String senderUsername, Long receiverId) {

        User sender   = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (sender.equals(receiver))
            throw new IllegalArgumentException("Cannot friend yourself");

        if (friendRepository.existsBetween(sender.getId(), receiverId))
            throw new IllegalStateException("Already friends");

//        if (friendRequestRepository.existsPending(sender.getId(), receiverId))
//            throw new IllegalStateException("Request already pending");

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequestRepository.save(friendRequest);
    }


    public List<IncomingFriendRequests> listFriendRequests(String username){
        return friendRequestRepository.findIncoming(username);
    }


    public void acceptFriendRequest(Long requestId) {

        FriendRequest fr = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        // Guard
        if (fr.getStatus() != FriendRequest.Status.PENDING)
            throw new IllegalStateException("Request already handled");

        // Create symmetrical friendship row
        User a = fr.getSender();
        User b = fr.getReceiver();
        insertSymmetricFriendship(a, b);

        //delete row
        friendRequestRepository.delete(fr);
    }
    private void insertSymmetricFriendship(User u1, User u2) {
        Long a = u1.getId();
        Long b = u2.getId();

        if (a < b && !friendRepository.existsBetween(a, b)){
            friendRepository.save(new Friend(u1, u2));
        }else{
            friendRepository.save(new Friend(u2, u1));
        }
    }

    public void cancelFriendRequest(Long requestId){
        friendRequestRepository.deleteById(requestId);
    }
}
