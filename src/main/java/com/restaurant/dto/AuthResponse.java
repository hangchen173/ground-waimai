package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 自动生成带参数的构造函数，方便 Controller 直接 new AuthResponse(token)
@NoArgsConstructor  // 生成无参构造函数，防止某些序列化工具报错
public class AuthResponse {
    
    private String token;
    
    // 你以后可以在这里扩展返回 username 或 role
    // private String username;
}