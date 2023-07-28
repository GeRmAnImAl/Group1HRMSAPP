package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The TimeOffRequestRepository interface provides the mechanisms for storage, retrieval, and search behavior
 * which a class can inherit by extending JpaRepository and JpaSpecificationExecutor.
 * It provides functionality to find TimeOffRequest by Id.
 */
import java.util.Optional;
@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, Long>, JpaSpecificationExecutor<TimeOffRequest>{
    /**
     * Retrieves an entity by its id.
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    Optional<TimeOffRequest> findById(Long id);

}
