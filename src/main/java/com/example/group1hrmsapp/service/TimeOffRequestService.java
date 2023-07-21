package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TimeOffRequestService {
    public List<TimeOffRequest> getAllTimeOffRequests();
    public void createTimeOffRequest(TimeOffRequest request);
    public TimeOffRequest cancelTimeOffRequest(Long requestId);
    public TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId);
    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId);
    Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
