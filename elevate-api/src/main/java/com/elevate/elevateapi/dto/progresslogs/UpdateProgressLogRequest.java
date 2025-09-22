package com.elevate.elevateapi.dto.progresslogs;

public record UpdateProgressLogRequest (
        String liftType,
        String variation,
        Double weight,
        Integer reps
){}
