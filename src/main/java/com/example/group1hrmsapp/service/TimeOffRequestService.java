package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TimeOffRequestService {
    List<TimeOffRequest> getAllTimeOffRequests();
    void createTimeOffRequest(TimeOffRequest request);
    TimeOffRequest cancelTimeOffRequest(Long requestId);
    TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId);
    TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId);
    Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<TimeOffRequest> findFilteredAndPaginated(Specification<TimeOffRequest> spec, Pageable pageable);
    Specification<TimeOffRequest> prepareSpecification(String startDate, String endDate, String timeOffType, String timeOffStatus);

}
