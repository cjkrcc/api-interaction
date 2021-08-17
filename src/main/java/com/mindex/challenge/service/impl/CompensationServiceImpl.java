package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class CompensationServiceImpl implements CompensationService{
    // Set up a logger for debugging purposes
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This method stores the given Compensation in the compensation repository.
     * If the given Compensation instance does not have a provided
     * effectiveDate, the current date will be assigned to it.
     */
    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        if(compensation.getDate() == null){
            Date date = new Date();
            LOG.debug("Created date for today [{}]", date);
            compensation.setDate(date);
        }
        // In order to be stored, a compensation must have a positive, non null salary and non null employee
        if(compensation.getSalary() < 0 || compensation.getSalary() == null || compensation.getEmployee() == null){
            throw new RuntimeException("Invalid compensation");
        }
        compensationRepository.insert(compensation);
        return compensation;
    }

    /**
     * This method reads the Compensation of the Employee with the given
     * EmployeeId from the compensation repository
     */
    @Override
    public Compensation read(String id) {
        LOG.debug("Getting compensation of employee with id [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        Compensation compensation = compensationRepository.findByEmployee(employee);
        if (compensation == null) {
            throw new RuntimeException("Invalid compensation employeeId: " + id);
        }
        return compensation;
    }
    
}
