package com.elevate.elevateapi.dto.friends;

import java.time.LocalDate;

public record FriendResponse(
        Long id,
        String username,
        LocalDate lastWorkoutAt,           // null if never trained
        MostRecentLift mostRecentLift       // null if no logs on that date
){}
