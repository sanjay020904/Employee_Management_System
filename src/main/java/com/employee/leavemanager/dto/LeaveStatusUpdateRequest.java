package com.employee.leavemanager.dto;

import com.employee.leavemanager.model.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveStatusUpdateRequest {
    private LeaveStatus status; // APPROVED, REJECTED
    private String adminRemarks;
}
