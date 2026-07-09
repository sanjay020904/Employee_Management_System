package com.employee.leavemanager.service;

import com.employee.leavemanager.dto.LeaveApplyRequest;
import com.employee.leavemanager.dto.LeaveStatusUpdateRequest;
import com.employee.leavemanager.model.LeaveRequest;
import com.employee.leavemanager.model.LeaveStatus;
import com.employee.leavemanager.model.User;
import com.employee.leavemanager.repository.LeaveRequestRepository;
import com.employee.leavemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveRequest applyLeave(LeaveApplyRequest request) {
        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeeId()));

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .leaveType(request.getLeaveType())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getLeaveHistory(Long employeeId) {
        // Validate user existence first
        if (!userRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
    }

    public Optional<LeaveRequest> getLeaveRequestById(Long leaveId) {
        return leaveRequestRepository.findById(leaveId);
    }

    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatusUpdateRequest request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + leaveId));

        leaveRequest.setStatus(request.getStatus());
        leaveRequest.setAdminRemarks(request.getAdminRemarks());

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest updateLeaveRequest(Long leaveId, LeaveApplyRequest requestDetails) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + leaveId));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Cannot modify leave request that has already been processed (Status: " + leaveRequest.getStatus() + ")");
        }

        leaveRequest.setStartDate(requestDetails.getStartDate());
        leaveRequest.setEndDate(requestDetails.getEndDate());
        leaveRequest.setLeaveType(requestDetails.getLeaveType());
        leaveRequest.setReason(requestDetails.getReason());

        return leaveRequestRepository.save(leaveRequest);
    }

    public void deleteLeaveRequest(Long leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + leaveId));
        leaveRequestRepository.delete(leaveRequest);
    }
}
