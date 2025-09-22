package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {

    List<ProgressLog> findByUserUsername(String username);




    // A. last workout date for each friend (batched)
    @Query("""
        select pl.user.id as userId, max(pl.date) as lastDate
        from ProgressLog pl
        where pl.user.id in :userIds
        group by pl.user.id
    """)
    List<LastDateView> findLastDatesByUserIds(List<Long> userIds);

    interface LastDateView {
        Long getUserId();
        LocalDate getLastDate();
    }

    // B. on that date, pick the "top set" (heaviest, then most reps)
    Optional<ProgressLog> findTopByUser_IdAndDateOrderByWeightKgDescRepsDesc(
            Long userId, LocalDate date);


    // C. all sets for that chosen lift/variation on that date, sorted for display
    @Query("""
  select pl
  from ProgressLog pl
  where pl.user.id  = :userId
    and pl.date     = :date
    and pl.liftType = :liftType
    and ( (:variation is null and pl.variation is null) or pl.variation = :variation )
  order by pl.weightKg desc, pl.reps desc
""")
    List<ProgressLog> findSetsForChosenLiftOnDate(
            Long userId, LocalDate date, ProgressLog.LiftType liftType, String variation);

}
