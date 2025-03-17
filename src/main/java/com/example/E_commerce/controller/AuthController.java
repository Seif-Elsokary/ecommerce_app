package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Role;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.repository.RoleRepository;
import com.example.E_commerce.repository.UserRepository;
import com.example.E_commerce.request.LoginRequest;
import com.example.E_commerce.request.RegisterRequest;
import com.example.E_commerce.request.UserRegisterRequest;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.response.JwtResponse;
import com.example.E_commerce.security.jwt.JwtUtils;
import com.example.E_commerce.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * تسجيل الدخول وإرجاع JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            return ResponseEntity.ok(new ApiResponse("Login Success", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * تسجيل مستخدم جديد
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email is already taken!", null));
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encodedPassword);

        Optional<Role> userRole = roleRepository.findByRoleName("USER");
        if (userRole.isPresent()) {
            newUser.setRoles(Set.of(userRole.get()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Default role 'USER' not found!", null));
        }

        userRepository.save(newUser);

        return ResponseEntity.ok(new ApiResponse("User registered successfully!", null));
    }


}
