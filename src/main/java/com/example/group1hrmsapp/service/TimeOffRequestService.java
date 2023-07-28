package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * The TimeOffRequestService interface defines the standard operations
 * to be performed on a TimeOffRequest.
 */
public interface TimeOffRequestService {
    /**
     * Returns all time off requests in the system.
     * @return a list of TimeOffRequest objects.
     */
    List<TimeOffRequest> getAllTimeOffRequests();

    /**
     * Creates a new time off request.
     * @param request the TimeOffRequest object to be saved.
     */
    void createTimeOffRequest(TimeOffRequest request);

    /**
     * Cancels a time off request.
     * @param requestId the id of the TimeOffRequest to be cancelled.
     * @return the cancelled TimeOffRequest object.
     */
    TimeOffRequest cancelTimeOffRequest(Long requestId);

    /**
     * Approves a time off request.
     * @param requestId the id of the TimeOffRequest to be approved.
     * @param managerId the id of the manager who approves the request.
     * @return the approved TimeOffRequest object.
     */
    TimeOffRequest approveTimeOffRequest(Long requestId, Long managerId);

    /**
     * Rejects a time off request.
     * @param requestId the id of the TimeOffRequest to be rejected.
     * @param managerId the id of the manager who rejects the request.
     * @return the rejected TimeOffRequest object.
     */
    TimeOffRequest rejectTimeOffRequest(Long requestId, Long managerId);

    /**
     * Returns a paginated list of TimeOffRequest objects.
     * @param pageNo the page number.
     * @param pageSize the size of the page.
     * @param sortField the field by which to sort the TimeOffRequest objects.
     * @param sortDirection the direction of the sort (asc or desc).
     * @return a Page of TimeOffRequest objects.
     */
    Page<TimeOffRequest> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    /**
     * Returns a paginated and filtered list of TimeOffRequest objects.
     * @param spec the specification defining the filtering criteria.
     * @param pageable the paging parameters.
     * @return a Page of TimeOffRequest objects that match the specification.
     */
    Page<TimeOffRequest> findFilteredAndPaginated(Specification<TimeOffRequest> spec, Pageable pageable);

    /**
     * Prepares a Specification object based on provided filter criteria.
     * @param startDate the start date for the time off.
     * @param endDate the end date for the time off.
     * @param timeOffType the type of the time off.
     * @param timeOffStatus the status of the time off.
     * @return a Specification object.
     */
    Specification<TimeOffRequest> prepareSpecification(String startDate, String endDate, String timeOffType, String timeOffStatus);

}
