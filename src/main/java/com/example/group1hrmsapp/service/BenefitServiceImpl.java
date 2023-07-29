package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Benefit;
import com.example.group1hrmsapp.repository.BenefitRepository;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * Returns all benefits.
     * @return a list of benefits
     */
    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
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
}
