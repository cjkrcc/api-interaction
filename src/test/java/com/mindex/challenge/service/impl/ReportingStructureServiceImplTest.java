package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String reportIdUrl;
    private String employeeUrl;
    private String employeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportIdUrl = "http://localhost:" + port + "/reportingstructure/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }


    @Test
    public void testReportRead() {
        // Create five test employees 
        Employee testEmployee1 = createEmployee("1");
        Employee testEmployee2 = createEmployee("2");
        Employee testEmployee3 = createEmployee("3");
        Employee testEmployee4 = createEmployee("4");
        Employee testEmployee5 = createEmployee("5");

        // Create Employees so we set and can get their id
        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
        Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();
        Employee createdEmployee5 = restTemplate.postForEntity(employeeUrl, testEmployee5, Employee.class).getBody();
        
        // Verify the employee code is working
        assertNotNull(createdEmployee1.getEmployeeId());
        assertEmployeeEquivalence(testEmployee1, createdEmployee1);

        /**
         * Set the reporting hierarchy as such:
         * 
         *      1
         *     / \
         *    2   3
         *       / \
         *      4   5
         * 
         * This will be done with only the 'created' employees, because we need
         * their ids for setting up direct reports
         */
        createdEmployee1.setDirectReports(List.of(createdEmployee2, createdEmployee3));
        createdEmployee3.setDirectReports(List.of(createdEmployee4, createdEmployee5));

        // Update the employees above
        updateEmployee(createdEmployee1);
        updateEmployee(createdEmployee2);
        updateEmployee(createdEmployee3);
        updateEmployee(createdEmployee4);
        updateEmployee(createdEmployee5);

        // create the ReportingStructure for testing
        ReportingStructure testReport1 = createReportingStructure(createdEmployee1, 4);
        ReportingStructure testReport2 = createReportingStructure(createdEmployee2, 0);
        ReportingStructure testReport3 = createReportingStructure(createdEmployee3, 2);
        ReportingStructure testReport4 = createReportingStructure(createdEmployee4, 0);
        ReportingStructure testReport5 = createReportingStructure(createdEmployee5, 0);

        // Read check
        ReportingStructure readReport1 = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        ReportingStructure readReport2 = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee2.getEmployeeId()).getBody();
        ReportingStructure readReport3 = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee3.getEmployeeId()).getBody();
        ReportingStructure readReport4 = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee4.getEmployeeId()).getBody();
        ReportingStructure readReport5 = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee5.getEmployeeId()).getBody();
        
        // Verification of existence and equivalence
        assertNotNull(readReport1);
        assertNotNull(readReport1.getEmployee());
        assertNotNull(readReport1.getNumberOfReports());
        assertReportEquivalence(testReport1, readReport1);
        assertReportEquivalence(testReport2, readReport2);
        assertReportEquivalence(testReport3, readReport3);
        assertReportEquivalence(testReport4, readReport4);
        assertReportEquivalence(testReport5, readReport5);
    }

    private Employee createEmployee(String firstname){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName(firstname);
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        return testEmployee;
    }

    private Employee updateEmployee(Employee employee){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(employee, headers),
                        Employee.class,
                        employee.getEmployeeId()).getBody();
        return updatedEmployee;
    }

    private ReportingStructure createReportingStructure(Employee employee, Integer expected){
        ReportingStructure testReport = new ReportingStructure();
        testReport.setEmployee(employee);
        testReport.setNumberOfReports(expected);
        return testReport;
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void assertReportEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
