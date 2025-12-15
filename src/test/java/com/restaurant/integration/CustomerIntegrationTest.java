package com.restaurant.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // ✅ 必须导入这个
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // ✅ 关键：模拟一个拥有 ADMIN 权限的用户
    // 你的 SecurityConfig 里可能写了 .hasRole("ADMIN")，所以这里 roles = "ADMIN"
    @WithMockUser(username = "admin", roles = {"ADMIN"}) 
    void createCustomer_ShouldReturn201() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("Integration Test User");
        dto.setEmail("test@integration.com");
        dto.setPhone("1234567890");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    // ✅ 关键：即使是测 400 错误，也得先通过权限验证，否则会先报 403
    @WithMockUser(username = "admin", roles = {"ADMIN"}) 
    void createCustomer_InvalidInput_ShouldReturn400() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setName(""); // 无效名字
        dto.setEmail("invalid-email"); 

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // 期望 400
    }
}