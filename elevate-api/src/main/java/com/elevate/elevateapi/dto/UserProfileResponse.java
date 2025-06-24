package com.elevate.elevateapi.dto;

public record UserProfileResponse (
        String username,
        String email,
        String gender,
        int age,
        String preferredUnitSystem,
        double height,
        double weight

){}
