package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.dto.progresslogs.CreateProgressLogRequest;
import com.elevate.elevateapi.dto.progresslogs.ProgressLogResponse;
import com.elevate.elevateapi.dto.progresslogs.UpdateProgressLogRequest;
import com.elevate.elevateapi.service.ProgressLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/progress-logs")
public class ProgressLogController {

    private final ProgressLogService progressLogService;

    public ProgressLogController(ProgressLogService progressLogService){
        this.progressLogService = progressLogService;

    }
    /*
     * POST /api/progress-logs - create a log
     * PUT /api/progress-logs/{id} - update log information
     * GET /api/progress-logs/{id}    - get log information
     * DELETE /api/progress-logs/{id} - delete a log
     * */
    @PostMapping
    public ResponseEntity<Map<String, String>> addLog (@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateProgressLogRequest createProgressLogRequest) {
        String username = userDetails.getUsername();
        progressLogService.addLog(username,createProgressLogRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Progress Log created"));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateLog (@PathVariable("id") Long id, @RequestBody UpdateProgressLogRequest updateProgressLogRequest){
        progressLogService.updateLog(id,updateProgressLogRequest);
        return ResponseEntity.ok("Log updated");
    }


    @GetMapping("{id}")
    public ResponseEntity<ProgressLogResponse> getLog (@PathVariable("id") Long id){
        boolean logExists = progressLogService.exists(id);
        if (logExists){
            return ResponseEntity.ok(progressLogService.getLog(id));
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteLog(@PathVariable("id") Long id){
        boolean logDeleted = progressLogService.deleteLog(id);

        if(logDeleted){
            return ResponseEntity.ok("Log deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log not found");
    }
}


