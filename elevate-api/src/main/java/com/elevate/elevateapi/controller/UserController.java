package com.elevate.elevateapi.controller;

import com.elevate.elevateapi.dto.RegisterUserRequest;
import com.elevate.elevateapi.repository.UserRepository;
import com.elevate.elevateapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> register(RegisterUserRequest request){
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("account created");
    }


    @PutMapping("{id}")
    public void editUser(@PathVariable("id") Long id){}

    @GetMapping("{id}")
    public void getUser(@PathVariable("id") Long id){}


    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long id){}

}
