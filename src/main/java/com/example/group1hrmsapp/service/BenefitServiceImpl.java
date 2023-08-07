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
     * Retrieves all Benefit Objects.
     * @return a list of Benefit Objects.
     */
    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    /**
     * Creates and stores a new Benefit entry into the database.
     * Before adding the benefit, the method checks if the logged-in user
     * has the necessary privileges to create a benefit by checking their special type.
     * Only users with the special type "HR" are allowed to create new benefits.
     *
     * @param benefit The benefit object to be saved into the database.
     * @throws RuntimeException If no user is currently logged in or if the logged-in user
     *                          doesn't have the HR special type.
     */
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

    /**
     * Allows an authenticated user to enroll in a specific benefit identified by the benefitId.
     * <p>
     * This method first retrieves the currently authenticated user from the security context.
     * It then fetches the benefit from the repository using the given benefitId.
     * If the logged-in employee isn't already enrolled in the specified benefit, the method enrolls
     * the employee in the benefit by adding the benefit to the employee's benefit list
     * with the current date as the enrollment date.
     * </p>
     *
     * @param benefitId The unique identifier of the benefit to enroll in.
     * @return true if the enrollment was successful; false if the employee was already enrolled.
     * @throws RuntimeException If no user is currently logged in.
     * @throws NoSuchElementException If there's no benefit with the specified benefitId in the repository.
     */
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

    /**
     * Allows an authenticated user to withdraw from a specific benefit identified by the benefitId.
     * <p>
     * This method first retrieves the currently authenticated user from the security context.
     * It then fetches the benefit from the repository using the provided benefitId.
     * If the logged-in employee is currently enrolled in the specified benefit, the method
     * withdraws the employee from the benefit by removing the benefit from the employee's benefit list.
     * </p>
     *
     * @param benefitId The unique identifier of the benefit to withdraw from.
     * @return true if the withdrawal was successful; false if the employee wasn't enrolled in the benefit.
     * @throws RuntimeException If no user is currently logged in.
     * @throws NoSuchElementException If there's no benefit with the specified benefitId in the repository.
     */
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

    /**
     * Retrieves a paginated list of benefits that match the provided specification criteria.
     * <p>
     * This method is used to obtain a subset of benefits based on filter criteria defined in the specification
     * and also limits the results based on pagination parameters.
     * </p>
     *
     * @param spec      The specification criteria used to filter benefits. This encapsulates the conditions for
     *                  querying the database.
     * @param pageable  The pagination information including page size, page number, and sort order.
     * @return A page of benefits that match the provided criteria.
     */
    @Override
    public Page<Benefit> findFilteredAndPaginated(Specification<Benefit> spec, Pageable pageable) {
        return benefitRepository.findAll(spec, pageable);
    }

    /**
     * Prepares a JPA {@link Specification} for the {@link Benefit} entity based on the provided filter criteria.
     * <p>
     * This method creates a dynamic JPA Specification using the provided filter criteria. The filters include:
     * coverage type, benefit name, benefit cost, and the coverage provider. It builds a predicate for each
     * non-null criterion and ultimately combines them using the AND logical operator. This dynamic specification
     * can be used for querying and filtering the {@link Benefit} entities from the database based on the specified criteria.
     * </p>
     *
     * @param coverageType     The type of coverage (e.g. HEALTH, DENTAL, VISION, etc.). Can be null.
     * @param benefitName      The name of the benefit. Can be null.
     * @param benefitCost      The maximum cost associated with the benefit. Can be null.
     * @param coverageProvider The provider offering the coverage (e.g. AETNA, HUMANA, etc.). Can be null.
     * @return A {@link Specification} that can be used for querying {@link Benefit} entities based on the provided criteria.
     * @throws IllegalArgumentException If an invalid value for coverageType or coverageProvider is provided.
     */
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

    /**
     * Retrieves the {@link Employee} object associated with the currently authenticated user.
     * <p>
     * This method fetches the current user's authentication details from the security context,
     * and then fetches the corresponding {@link AppUser} from the database. It finally returns
     * the associated {@link Employee} object.
     * </p>
     *
     * @return The {@link Employee} object associated with the currently logged-in user.
     * @throws RuntimeException If no user is currently authenticated or logged in.
     */
    @Override
    public Employee getLoggedInUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String loggedInUsername = auth.getName();
    AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
            .orElseThrow(()-> new RuntimeException("No user logged in"));

    return loggedInUser.getEmployee();
    }
}
