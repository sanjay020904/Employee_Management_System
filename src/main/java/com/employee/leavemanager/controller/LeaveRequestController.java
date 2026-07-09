package com.employee.leavemanager.controller;

import com.employee.leavemanager.dto.LeaveApplyRequest;
import com.employee.leavemanager.dto.LeaveStatusUpdateRequest;
import com.employee.leavemanager.model.LeaveRequest;
import com.employee.leavemanager.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveApplyRequest request) {
        try {
            LeaveRequest leaveRequest = leaveRequestService.applyLeave(request);
            return ResponseEntity.ok(leaveRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getLeaveHistory(@PathVariable Long employeeId) {
        try {
            List<LeaveRequest> history = leaveRequestService.getLeaveHistory(employeeId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequest>> getPendingLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getPendingLeaveRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeaveRequestById(@PathVariable Long id) {
        return leaveRequestService.getLeaveRequestById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable Long id, @RequestBody LeaveStatusUpdateRequest request) {
        try {
            LeaveRequest updatedRequest = leaveRequestService.updateLeaveStatus(id, request);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveApplyRequest requestDetails) {
        try {
            LeaveRequest updatedRequest = leaveRequestService.updateLeaveRequest(id, requestDetails);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable Long id) {
        try {
            leaveRequestService.deleteLeaveRequest(id);
            return ResponseEntity.ok(Map.of("message", "Leave request deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
