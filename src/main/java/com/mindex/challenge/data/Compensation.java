package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    /**
     * Instance variables for the Compensation class
     */
    private Employee employee;
    private Integer salary;
    private Date date;

    /**
     * Constructor for a new empty Compensation
     */
    public Compensation(){
    }

    //Getter for Employee Salary Effective Datewithin the Compensation Class
    public Date getDate() {
        return date;
    }

    //Setter for Employee Salary Effective Date within the Compensation Class
    public void setDate(Date date) {
        this.date = date;
    }

    //Getter for Employee within the Compensation Class
    public Employee getEmployee() {
        return employee;
    }

    //Setter for Employee within the Compensation Class
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    //Getter for Employee Salary within the Compensation Class
    public Integer getSalary() {
        return salary;
    }

    //Setter for Employee Salary within the Compensation Class
    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
