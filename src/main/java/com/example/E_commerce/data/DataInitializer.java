package com.example.E_commerce.data;

import com.example.E_commerce.Entity.Role;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.repository.RoleRepository;
import com.example.E_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeRoles();
        createDefaultUsersIfNotExists();
        createDefaultAdminsIfNotExists();
    }

    private void initializeRoles() {
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    private void createDefaultUsersIfNotExists() {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found in database!"));

        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (!userRepository.existsByEmail(defaultEmail)) {
                saveUser("User", "User" + i, defaultEmail, "123456" + i, userRole);
            }
        }
    }

    private void createDefaultAdminsIfNotExists() {
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found in database!"));

        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (!userRepository.existsByEmail(defaultEmail)) {
                saveUser("Admin", "Admin" + i, defaultEmail, "123456" + i, adminRole);
            }
        }
    }

    private void saveUser(String firstName, String lastName, String email, String rawPassword, Role role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }
}
