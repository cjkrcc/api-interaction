package com.mindex.challenge.data;

public class ReportingStructure {
    /**
     * Instance variables for the ReportingStructure class
     */
    private Employee employee;
    private Integer numberOfReports;

    /**
     * Constructor for a new empty ReportingStructure
     */
    public ReportingStructure() {
    }

    //Getter for the Employee within the ReportingStructure class
    public Employee getEmployee() {
        return employee;
    }

    //Setter for the Employee within the ReportingStructure class
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    //Getter for the NumberOfReports within the ReportingStructure class
    public Integer getNumberOfReports(){
        return numberOfReports;
    }

    //Setter for the NumberOfReports within the ReportingStructure class
    public void setNumberOfReports(Integer numberOfReports){
        this.numberOfReports = numberOfReports;
    }
}
