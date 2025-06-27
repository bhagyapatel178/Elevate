package com.elevate.elevateapi.dto;

public record UserProfileResponse (
        String username,
        String email,
        String gender,
        Integer age,
        String preferredUnitSystem,
        Double height,
        Double weight
){}

