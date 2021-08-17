package com.mindex.challenge.dao;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, Employee> {
    // Uses a given Employee instance to find the Compensation in the repository
    Compensation findByEmployee(Employee employee);
}
 