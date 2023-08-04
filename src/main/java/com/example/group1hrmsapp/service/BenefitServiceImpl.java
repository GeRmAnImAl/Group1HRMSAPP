package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import com.example.group1hrmsapp.repository.BenefitRepository;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A service class that implements the BenefitService interface to provide core Benefit-related functionalities.
 * This service is responsible for handling all operations related to Benefit objects, such as retrieving, saving,
 * and deleting Benefits from the repository.
 */
@Service
public class BenefitServiceImpl implements BenefitService{
    @Autowired
    private BenefitRepository benefitRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Returns all benefits.
     * @return a list of benefits
     */
    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    @Override
    public void createBenefit(Benefit benefit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        if(!loggedInEmployee.getSpecialType().equals(SpecialType.HR)){
            throw new RuntimeException("Only Human Resource Professionals May Create New Benefits");
        }

        benefitRepository.save(benefit);
    }

    /**
     * Returns the benefit with the given ID.
     * @param id ID of the benefit
     * @return the benefit
     * @throws RuntimeException if the benefit is not found
     */
    @Override
    public Benefit getBenefitById(Long id) {
        Optional<Benefit> optional = benefitRepository.findById(id);
        Benefit benefit = null;
        if(optional.isPresent()){
            benefit = optional.get();
        }
        else{
            throw new RuntimeException("Benefit not found for id :: " + id);
        }
        return benefit;
    }

    /**
     * Saves a benefit to the database.
     * @param benefit the benefit to save
     */
    @Override
    public void saveBenefit(Benefit benefit) {
        benefitRepository.save(benefit);
    }

    /**
     * Updates a benefit in the database.
     * @param benefit the benefit to update
     */
    @Override
    public void updateBenefit(Benefit benefit) {
        benefitRepository.save(benefit);
    }

    /**
     * Deletes the benefit with the given ID from the database.
     * @param id the ID of the benefit to delete
     * @throws RuntimeException if the benefit is not found
     */
    @Override
    public void deleteBenefitById(Long id) {
        Optional<Benefit> optionalBenefit = benefitRepository.findById(id);
        if (optionalBenefit.isPresent()) {
            Benefit benefitToDelete = optionalBenefit.get();

            benefitRepository.delete(benefitToDelete);
        } else {
            throw new RuntimeException("Benefit not found for id :: " + id);
        }
    }

    @Override
    public boolean enrollInBenefit(Long benefitId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(()-> new NoSuchElementException("There is no benefit with id :: " + benefitId));

        if(!loggedInEmployee.getBenefitList().containsKey(benefit)){
            loggedInEmployee.getBenefitList().put(benefit, LocalDate.now());
            employeeRepository.save(loggedInEmployee);
            return true;
        }

        return false;
    }

    @Override
    public boolean withdrawFromBenefit(Long benefitId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(()-> new RuntimeException("No user logged in"));
        Employee loggedInEmployee = loggedInUser.getEmployee();
        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(()-> new NoSuchElementException("There is no benefit with id :: " + benefitId));

        if(!loggedInEmployee.getBenefitList().containsKey(benefit)){

            return false;
        }

        loggedInEmployee.getBenefitList().remove(benefit);
        employeeRepository.save(loggedInEmployee);

        return true;
    }

    /**
     * Returns a paginated view of the benefits.
     * @param pageNo the page number
     * @param pageSize the number of benefits per page
     * @param sortField the field to sort by
     * @param sortDirection the direction to sort by (ASC or DESC)
     * @return a page of benefits
     */
    @Override
    public Page<Benefit> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return this.benefitRepository.findAll(pageable);
    }


    @Override
    public Page<Benefit> findFilteredAndPaginated(Specification<Benefit> spec, Pageable pageable) {
        return benefitRepository.findAll(spec, pageable);
    }

    @Override
    public Specification<Benefit> prepareSpecification(String coverageType, String benefitName, Double benefitCost, String coverageProvider) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(coverageType != null && !coverageType.isEmpty()){
                CoverageType coverageTypeEnum = CoverageType.valueOf(coverageType.toUpperCase());
                predicates.add(criteriaBuilder.equal(root.get("coverageType"), coverageTypeEnum));
            }

            if (benefitName != null && !benefitName.isEmpty()) {
                String partialBenefitName = "%" + benefitName + "%";
                predicates.add(criteriaBuilder.like(root.get("benefitName"), partialBenefitName));
            }

            if (benefitCost != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("benefitCost"), benefitCost));
            }

            if (coverageProvider != null && !coverageProvider.isEmpty()) {
                CoverageProvider coverageProviderEnum = CoverageProvider.valueOf(coverageProvider.toUpperCase());
                predicates.add(criteriaBuilder.equal(root.get("coverageProvider"), coverageProviderEnum));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Employee getLoggedInUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String loggedInUsername = auth.getName();
    AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
            .orElseThrow(()-> new RuntimeException("No user logged in"));

    return loggedInUser.getEmployee();

    }
}
