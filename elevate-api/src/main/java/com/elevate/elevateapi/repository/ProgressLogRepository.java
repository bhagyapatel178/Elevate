package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {
}
