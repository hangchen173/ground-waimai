package com.restaurant.config;

import com.restaurant.entity.User;
import com.restaurant.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. 初始化管理员
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole("ROLE_ADMIN"); // ⭐ 角色是 ADMIN
                userRepository.save(admin);
                System.out.println("✅ 管理员已创建: admin / password");
            }

            // 2. 初始化普通顾客 (新增代码)
            if (userRepository.findByUsername("customer").isEmpty()) {
                User customer = new User();
                customer.setUsername("customer");
                customer.setPassword(passwordEncoder.encode("password")); // 密码也是 password，方便记
                customer.setRole("ROLE_CUSTOMER"); // ⭐ 角色是 CUSTOMER
                userRepository.save(customer);
                System.out.println("✅ 测试顾客已创建: customer / password");
            }
        };
    }
}