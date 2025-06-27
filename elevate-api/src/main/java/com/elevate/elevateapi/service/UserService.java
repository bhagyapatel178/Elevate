package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.*;
import com.elevate.elevateapi.entity.ProgressLog;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.ProgressLogRepository;
import com.elevate.elevateapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ProgressLogRepository progressLogRepository;


    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JWTService jwtService,
            ProgressLogRepository progressLogRepository
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.progressLogRepository = progressLogRepository;
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

    public void editUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (updateUserRequest.username() != null) user.setUsername(updateUserRequest.username());
        if (updateUserRequest.email() != null) user.setEmail(updateUserRequest.email());
        if (updateUserRequest.gender() != null) user.setGender(User.GenderType.valueOf(updateUserRequest.gender()));
        if (updateUserRequest.age() != null) user.setAge(updateUserRequest.age());
        if (updateUserRequest.preferredUnitSystem() != null) user.setPreferredUnitSystem(User.MeasurementUnitSystem.valueOf(updateUserRequest.preferredUnitSystem()));
        if (updateUserRequest.height() != null) user.setHeight(updateUserRequest.height());
        if (updateUserRequest.weight() != null) user.setWeight(updateUserRequest.weight());
        userRepository.save(user);
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

    public boolean existsByUsername(String username){return userRepository.existsByUsername(username);}

    public UserProfileResponse getUserByUsername(String username){
        User user = userRepository.findByUsername(username);

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


    public List<ProgressLogResponse> getLogs(String username) {
        List<ProgressLog> logs = progressLogRepository.findByUserUsername(username);

        return logs.stream()
                .map(log -> new ProgressLogResponse(
                        log.getId(),
                        log.getLiftType().name(),
                        log.getVariation(),
                        log.getWeightKg(),
                        log.getReps(),
                        log.getDate(),
                        log.getUser().getId()
                ))
                .collect(Collectors.toList());
    }



//    public List<ProgressLog> getLogs(String username) {
//        return progressLogRepository.findByUserUsername(username);
//    }

//    public List<ProgressLogResponse> getLogs(String username) {
//        return progressLogRepository.findByUserUsername(username).stream()
//                .map(log -> new ProgressLogResponse(
//                        log.getId(),
//                        log.getLiftType().toString(),
//                        log.getVariation(),
//                        log.getWeightKg(),
//                        log.getReps(),
//                        log.getDate(),
//                        log.getUser().getId()
//                ))
//                .toList();
//    }

}
