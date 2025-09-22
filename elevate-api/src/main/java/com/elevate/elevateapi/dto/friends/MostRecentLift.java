package com.elevate.elevateapi.dto.friends;

public record MostRecentLift(
        String liftName,
        java.util.List<LiftSet> sets
){}
