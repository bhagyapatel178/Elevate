package com.elevate.elevateapi.service;

import com.elevate.elevateapi.repository.ProgressLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressLogService {

    private final ProgressLogRepository progressLogRepository;


    public ProgressLogService(ProgressLogRepository progressLogRepository) {
        this.progressLogRepository = progressLogRepository;
    }

}
