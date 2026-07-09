package com.employee.leavemanager.loader;

import com.employee.leavemanager.model.Role;
import com.employee.leavemanager.model.User;
import com.employee.leavemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin User
            User admin = User.builder()
                    .username("admin")
                    .password("admin123")
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .department("IT Operations")
                    .build();
            userRepository.save(admin);
            System.out.println("Seeded Default Admin User: admin / admin123");

            // Seed Employee User
            User employee = User.builder()
                    .username("employee")
                    .password("emp123")
                    .fullName("John Doe")
                    .role(Role.EMPLOYEE)
                    .department("Software Engineering")
                    .build();
            userRepository.save(employee);
            System.out.println("Seeded Default Employee User: employee / emp123");
        } else {
            System.out.println("Database already contains users. Skipping seeder.");
        }
    }
}
