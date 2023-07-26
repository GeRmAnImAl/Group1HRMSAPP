package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TimeOffRequestService {
    public List<TimeOffRequest> getAllTimeOffRequests();
    public void createTimeOffRequest(TimeOffRequest request);
    public TimeOffRequest cancelTimeOffRequest(Long requestId);
    public TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId);
    public TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId);
    Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<TimeOffRequest> findFilteredAndPaginated(Specification<TimeOffRequest> spec, Pageable pageable);
    public Specification<TimeOffRequest> prepareSpecification(String startDate, String endDate, String timeOffType, String timeOffStatus);

}
