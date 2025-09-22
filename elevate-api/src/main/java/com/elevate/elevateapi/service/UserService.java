package com.elevate.elevateapi.service;

import com.elevate.elevateapi.dto.friends.UserSearchResult;
import com.elevate.elevateapi.dto.logins.GoogleToken;
import com.elevate.elevateapi.dto.logins.LoginUserRequest;
import com.elevate.elevateapi.dto.logins.RegisterUserRequest;
import com.elevate.elevateapi.dto.progresslogs.ProgressLogResponse;
import com.elevate.elevateapi.dto.user.UpdateUserRequest;
import com.elevate.elevateapi.dto.user.UserProfileResponse;
import com.elevate.elevateapi.entity.ProgressLog;
import com.elevate.elevateapi.entity.User;
import com.elevate.elevateapi.repository.ProgressLogRepository;
import com.elevate.elevateapi.repository.UserRepository;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

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

    private JsonFactory jsonFactory;


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

    public void editUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username);

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

    public String googleVerify (GoogleToken token) throws AuthenticationException, GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("162014496609-g3sutimbg134rcaonprpi8qoaeqddq06.apps.googleusercontent.com"))
                .build();

        String retToken = "";
        GoogleIdToken idToken = verifier.verify(token.idToken());
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            String provider = "google";
            String providerId = payload.getSubject(); // Google sub

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            Optional<User> existingUser = Optional.ofNullable(userRepository.findByProviderAndProviderId(provider, providerId));
            if (existingUser.isPresent()) {
                // Returning Google user → generate JWT
                retToken = jwtService.generateToken(existingUser.get().getUsername());
            } else if (!existsByEmail(email)) {
                // First Google login → create user with provider+providerId
                String username = email.substring(0, email.indexOf("@")); // gets prefix of email
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setProvider(provider);
                newUser.setProviderId(providerId);
                userRepository.save(newUser);
                retToken = jwtService.generateToken(newUser.getUsername());
            } else {
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.CONFLICT, "Email already registered with password login"
                );
                }

        } else {
            throw new org.springframework.security.core.AuthenticationException("Invalid Google ID token") {};
        }
        return retToken;
    }

    public boolean exists(Long id){
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email){return userRepository.existsByEmail(email);}

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

    public UserSearchResult searchByUsername (String username){
        User user =  userRepository.findByUsername(username);
        return new UserSearchResult(user.getId(), user.getUsername());
    }

}
