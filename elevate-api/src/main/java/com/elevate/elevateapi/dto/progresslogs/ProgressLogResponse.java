package com.elevate.elevateapi.dto.progresslogs;

import java.time.LocalDate;

public record ProgressLogResponse (
    Long id,
    String liftType,
    String variation,
    Double weight,
    Integer reps,
    LocalDate date,
    Long userId
) {}
