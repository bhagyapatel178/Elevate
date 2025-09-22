package com.elevate.elevateapi.dto.progresslogs;

public record CreateProgressLogRequest (
        String liftType,
        String variation,
        Double weight,
        Integer reps
){}
