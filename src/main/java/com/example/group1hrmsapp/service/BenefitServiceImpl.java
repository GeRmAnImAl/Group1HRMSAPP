package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.model.Benefit;
import com.example.group1hrmsapp.model.Employee;
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


    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

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

    @Override
    public void saveBenefit(Benefit benefit) {
        benefitRepository.save(benefit);

    }

    @Override
    public void updateBenefit(Benefit benefit) {
        benefitRepository.save(benefit);
    }

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
    public Page<Benefit> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return this.benefitRepository.findAll(pageable);
    }
}
