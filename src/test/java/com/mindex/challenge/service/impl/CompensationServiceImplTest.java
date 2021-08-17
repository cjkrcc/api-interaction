package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testCreateRead() {
        // Create five test employees 
        Employee testEmployee1 = createEmployee("1");
        Employee testEmployee2 = createEmployee("2");
        Employee testEmployee3 = createEmployee("3");
        Employee testEmployee4 = createEmployee("4");

        // Create Employees so we set and can get their id
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();
        
        // Test with date
        Date date = new Date(1997080200);
        Compensation testCompensation1 = createCompensation(date, 100000, createdEmployee1);

        // Test with no date
        Compensation testCompensation2 = createCompensation(null, 100000, createdEmployee2);

        // Test with no compensation
        Compensation testCompensation3 = createCompensation(new Date(), null, createdEmployee3);

        // Test with negative compensation
        Compensation testCompensation4 = createCompensation(new Date(), -100000, createdEmployee4);

        // Test with no Employee
        Compensation testCompensation5 = createCompensation(new Date(), 100000, null);



        // Create checks
        Compensation createdCompensation1 = restTemplate.postForEntity(compensationUrl, testCompensation1, Compensation.class).getBody();
        Compensation createdCompensation2 = restTemplate.postForEntity(compensationUrl, testCompensation2, Compensation.class).getBody();
        Compensation createdCompensation3 = restTemplate.postForEntity(compensationUrl, testCompensation3, Compensation.class).getBody();
        Compensation createdCompensation4 = restTemplate.postForEntity(compensationUrl, testCompensation4, Compensation.class).getBody();
        Compensation createdCompensation5 = restTemplate.postForEntity(compensationUrl, testCompensation5, Compensation.class).getBody();

        // Ensure the incorrectly submitted compensations have null values
        assertNull(createdCompensation3.getEmployee());
        assertNull(createdCompensation4.getEmployee());
        assertNull(createdCompensation5.getEmployee());

        // Ensure the test and created are equivalent for correctly formatted compensation
        assertCompensationEquivalence(testCompensation1, createdCompensation1);

        // Ensure the null date is not null once created
        assertNotNull(createdCompensation2.getDate());


        // Read checks
        Compensation readCompensation1 = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation1.getEmployee().getEmployeeId()).getBody();
        Compensation readCompensation2 = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation2.getEmployee().getEmployeeId()).getBody();
    
        // Ensure the stored compensation matches what was given
        assertCompensationEquivalence(createdCompensation1, readCompensation1);
        assertCompensationEquivalence(createdCompensation2, readCompensation2);
    }

    private Compensation createCompensation(Date date, Integer salary, Employee employee){
        Compensation testCompensation = new Compensation();
        testCompensation.setDate(date);
        testCompensation.setSalary(salary);
        testCompensation.setEmployee(employee);
        return testCompensation;
    }

    private Employee createEmployee(String firstname){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName(firstname);
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        return testEmployee;
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getDate(), actual.getDate());
    }

}
