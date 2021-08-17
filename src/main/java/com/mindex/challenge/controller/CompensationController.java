package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    // Set up a logger for debugging purposes
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    /**
     * Handles the REST POST request for Compensation
     * @param compensation the Compensation instance to be stored in the database
     * @return the final Compensation instance stored in the database
     */
    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received Compensation POST request for [{}]", compensation);
        return compensationService.create(compensation);
    }

    /**
     * Handles the REST GET request for Compensation
     * @param id is the EmployeeId of the Employee whose 
     *           Compensation is to be retrieved from the database
     * @return the Compensation requested
     */
    @GetMapping("/compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received Compensation GET request for id [{}]", id);
        return compensationService.read(id);
    }
}