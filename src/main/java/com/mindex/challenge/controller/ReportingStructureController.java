package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportingStructureController {
    // Set up a logger for debugging purposes
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportService;

    /**
     * Handles the REST GET request for the ReportingStructure
     * @param id is the EmployeeId of the Employee whose 
     *           ReportingStructure is to be computed and returned
     * @return The ReportingStructure of the given Employee
     */
    @GetMapping("/reportingstructure/{id}")
    public ReportingStructure read(@PathVariable String id) {
        // Track the beginning of the read request in the debugger
        LOG.debug("update Received report get request for id [{}]", id);
        ReportingStructure report = reportService.read(id);
        return report;
    }
}
