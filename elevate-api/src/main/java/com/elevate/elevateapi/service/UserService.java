package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.LoginUserRequest;
import com.elevate.elevateapi.dto.RegisterUserRequest;
import com.elevate.elevateapi.dto.UserProfileResponse;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,JWTService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registerUser(RegisterUserRequest request){
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        userRepository.save(user);
    }

    public boolean deleteUser(Long id){
        if (exists(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UserProfileResponse getUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String gender = user.getGender() != null ? user.getGender().toString() : null;
        String preferredUnitSystem = user.getPreferredUnitSystem() != null ? user.getPreferredUnitSystem().toString() : null;
        return new UserProfileResponse(
                user.getUsername(),
                user.getEmail(),
                gender,
                user.getAge(),
                preferredUnitSystem,
                user.getHeight(),
                user.getWeight()
        );
    }

    public String verify (LoginUserRequest loginUserRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.username(), loginUserRequest.password()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(loginUserRequest.username());
        }
        return "Fail";
    }

    public boolean exists(Long id){
        return userRepository.existsById(id);
    }

}
