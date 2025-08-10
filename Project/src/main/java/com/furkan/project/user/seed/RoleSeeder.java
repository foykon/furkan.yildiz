package com.furkan.project.user.seed;

import com.furkan.project.user.entity.ERole;
import com.furkan.project.user.entity.Role;
import com.furkan.project.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        seed(ERole.ROLE_USER, "Standard end-user role");
        seed(ERole.ROLE_ADMIN, "Administrator role");
    }

    private void seed(ERole name, String desc) {
        roleRepository.findByName(name).orElseGet(() -> {
            Role r = Role.builder().name(name).description(desc).build();
            return roleRepository.save(r);
        });
    }
}
