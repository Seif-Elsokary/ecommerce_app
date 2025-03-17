package com.example.E_commerce;

import com.example.E_commerce.Entity.Role;
import com.example.E_commerce.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Transactional
@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> defaultRoles = List.of("USER", "ADMIN");

        for (String roleName : defaultRoles) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
                System.out.println("✅ Role created: " + roleName);
            }
        }
    }
}
