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
            // 1. 检查是否存在 admin 用户
            if (userRepository.findByUsername("admin").isEmpty()) {
                
                // 2. 如果不存在，创建一个
                User admin = new User();
                admin.setUsername("admin");
                // ⭐ 关键点：这里让 Spring 自动加密 "password"，不用你自己去算哈希值
                admin.setPassword(passwordEncoder.encode("password")); 
                admin.setRole("ROLE_ADMIN");
                
                userRepository.save(admin);
                System.out.println("✅ 管理员用户已自动创建: admin / password");
            } else {
                System.out.println("ℹ️ 管理员用户已存在，跳过创建。");
            }
        };
    }
}