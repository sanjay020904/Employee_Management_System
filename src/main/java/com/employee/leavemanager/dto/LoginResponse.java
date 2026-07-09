package com.employee.leavemanager.dto;

import com.employee.leavemanager.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Long id;
    private String username;
    private String fullName;
    private Role role;
    private String department;
}
