package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.ReportingStructureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService{
    // Set up a logger for debugging purposes
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This method computes the number of direct reports for an 
     * employee with a given ID on the fly and returns it as an 
     * instance of the ReportingStructure class
     */
    @Override
    public ReportingStructure read(String id) {
        LOG.debug("update reading report of employee with id [{}]", id);
        ReportingStructure report = new ReportingStructure();
        report.setEmployee(employeeRepository.findByEmployeeId(id));
        report.setNumberOfReports(numReports(id));
        return report;
    }

    /**
     * This method recursively determines the number of reports for an employee
     * @param id is the EmployeeId 
     * @return the number of reports of the Employee with the given EmployeeId
     */
    public Integer numReports(String id){
        LOG.debug("Getting report of employee with id [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        Integer res = 0;
        if(employee.getDirectReports() != null){
            for (Employee emp : employee.getDirectReports()) {
                res += 1 + numReports(emp.getEmployeeId());
            }
        }
        return res;
    }
}
