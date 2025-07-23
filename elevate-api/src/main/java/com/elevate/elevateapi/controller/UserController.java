package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.dto.*;
import com.elevate.elevateapi.entity.ProgressLog;
import com.elevate.elevateapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /*
    * POST /api/users/register - create an account -> username password email
    * PUT /api/users/{id} - update profile information
    * GET /api/users/{id}    - get profile details
    * DELETE /api/users/{id} - delete an account
    * GET /api/users/{id}/logs - view progress logs
    * */

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterUserRequest request){
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Account created"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginUserRequest loginUserRequest){
        String token =  userService.verify(loginUserRequest);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PutMapping("edit")
    public ResponseEntity<Map<String, String>> editUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateUserRequest updateUserRequest){
        String username = userDetails.getUsername();
        boolean accountExists = userService.existsByUsername(username);
        if(accountExists){
            userService.editUser(username, updateUserRequest);
            return ResponseEntity.ok(Map.of("message","Account details updated"));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable("id") Long id){
        boolean accountExists = userService.exists(id);
        if (accountExists){
            return ResponseEntity.ok(userService.getUser(id));
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        boolean accountExists = userService.existsByUsername(username);
        if (accountExists) {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("me")
//    public ResponseEntity<UserProfileResponse> getUserbyUsername(@RequestBody String username){
//        boolean accountExists = userService.existsByUsername(username);
//        if (accountExists){
//            return ResponseEntity.ok(userService.getUserByUsername(username));
//        }
//        else{
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
        boolean accountDeleted = userService.deleteUser(id);
        if (accountDeleted){
            return ResponseEntity.ok("Account deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/me/logs")
    public ResponseEntity<List<ProgressLogResponse>> getLogs(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        boolean accountExists = userService.existsByUsername(username);
        if (accountExists) {
            return ResponseEntity.ok(userService.getLogs(username));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
