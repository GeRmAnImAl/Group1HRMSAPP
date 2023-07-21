package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.TimeOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, Long> {
    @Override
    Optional<TimeOffRequest> findById(Long id);

}
