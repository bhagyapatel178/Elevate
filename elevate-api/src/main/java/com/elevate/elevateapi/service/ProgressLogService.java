package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.CreateProgressLogRequest;
import com.elevate.elevateapi.dto.ProgressLogResponse;
import com.elevate.elevateapi.dto.UpdateProgressLogRequest;
import com.elevate.elevateapi.entity.ProgressLog;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.ProgressLogRepository;
import com.elevate.elevateapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProgressLogService {

    private final ProgressLogRepository progressLogRepository;
    private final UserRepository userRepository;

    public ProgressLogService(ProgressLogRepository progressLogRepository, UserRepository userRepository) {
        this.progressLogRepository = progressLogRepository;
        this.userRepository = userRepository;
    }

    public void addLog(String username, CreateProgressLogRequest createProgressLogRequest) {
        User user = userRepository.findByUsername(username);
        if (createProgressLogRequest.reps() == null || createProgressLogRequest.weight() == null) {
            throw new IllegalArgumentException("Weight and reps are required");
        }

        ProgressLog progressLog = new ProgressLog();
        progressLog.setUser(user);
        progressLog.setLiftType(ProgressLog.LiftType.valueOf(createProgressLogRequest.liftType()));
        progressLog.setVariation(createProgressLogRequest.variation());
        progressLog.setWeightKg(createProgressLogRequest.weight());
        progressLog.setReps(createProgressLogRequest.reps());
        progressLog.setDate(LocalDate.now());
        progressLogRepository.save(progressLog);
    }

    public boolean deleteLog(Long id) {
        if (exists(id)){
            progressLogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean exists(Long id){
        return progressLogRepository.existsById(id);
    }

    public ProgressLogResponse getLog(Long id) {
        ProgressLog progressLog = progressLogRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Log not found"));
        String liftType = progressLog.getLiftType() != null ? progressLog.getLiftType().toString() : null;
        return new ProgressLogResponse(
                progressLog.getId(),
                liftType,
                progressLog.getVariation(),
                progressLog.getWeightKg(),
                progressLog.getReps(),
                progressLog.getDate(),
                progressLog.getUser().getId());
    }

    public void updateLog(Long id, UpdateProgressLogRequest updateProgressLogRequest) {
        ProgressLog progressLog = progressLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
        if(updateProgressLogRequest.liftType() != null) progressLog.setLiftType(ProgressLog.LiftType.valueOf(updateProgressLogRequest.liftType()));
        if(updateProgressLogRequest.variation() != null) progressLog.setVariation(updateProgressLogRequest.variation());
        if(updateProgressLogRequest.weight() != null) progressLog.setWeightKg(updateProgressLogRequest.weight());
        if(updateProgressLogRequest.reps() != null) progressLog.setReps(updateProgressLogRequest.reps());
        progressLogRepository.save(progressLog);
    }
}
