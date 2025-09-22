package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.friends.FriendResponse;
import com.elevate.elevateapi.dto.friends.MostRecentLift;
import com.elevate.elevateapi.entity.ProgressLog;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.FriendRepository;
import com.elevate.elevateapi.repository.ProgressLogRepository;
import com.elevate.elevateapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {

    // list friends (String username)
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final ProgressLogRepository progressLogRepository;

    public FriendService(FriendRepository friendRepository,UserRepository userRepository, ProgressLogRepository progressLogRepository){
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.progressLogRepository = progressLogRepository;
    }

    public List<FriendResponse> listFriends(String username){
        Long userId = userRepository.findByUsername(username).getId();
        List<Long> friendIds = friendRepository.findFriendsOf(userId);
        if(friendIds.isEmpty()) return List.of();

        Map<Long, String> usernameById = userRepository.findAllById(friendIds).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        Map<Long, LocalDate> lastDateById = progressLogRepository.findLastDatesByUserIds(friendIds).stream()
                .collect(Collectors.toMap(
                        ProgressLogRepository.LastDateView::getUserId,
                        ProgressLogRepository.LastDateView::getLastDate
                ));

        List<FriendResponse> friendResponseList = new ArrayList<>(friendIds.size());

        for(Long fid: friendIds){
            String friendUsername = usernameById.get(fid);
            LocalDate lastDate = lastDateById.get(fid);

            if (lastDate == null){
                friendResponseList.add(new FriendResponse(fid, friendUsername, null, null));
                continue;
            }

            Optional<ProgressLog> mostRecentLift = progressLogRepository.findTopByUser_IdAndDateOrderByWeightKgDescRepsDesc(fid, lastDate);

            if(mostRecentLift.isEmpty()){
                friendResponseList.add(new FriendResponse(fid, friendUsername, lastDate, null));
                continue;
            }

            ProgressLog recentLift = mostRecentLift.get();

            List<ProgressLog> setRows = progressLogRepository.findSetsForChosenLiftOnDate(fid, lastDate, mostRecentLift.get().getLiftType(), recentLift.getVariation());

            List<com.elevate.elevateapi.dto.friends.LiftSet> sets = setRows.stream()
                    .map(s -> new com.elevate.elevateapi.dto.friends.LiftSet(s.getWeightKg(), s.getReps()))
                    .toList();

            String liftName = recentLift.getLiftType().name();
            if(recentLift.getVariation() != null && !recentLift.getVariation().isBlank()){
                liftName = liftName + "(" + recentLift.getVariation() + ")";
            }
            MostRecentLift mostRecent = new MostRecentLift(liftName, sets);
            friendResponseList.add(new FriendResponse(fid, friendUsername, lastDate, mostRecent));
        }

        return friendResponseList;
    }

    public void removeFriend(String username, Long friendUserId) {

        Long myId = userRepository.findByUsername(username).getId();

        Long a = Math.min(myId, friendUserId);
        Long b = Math.max(myId, friendUserId);

        friendRepository.deleteSymmetric(a, b);
    }

}
