package com.napier.devops;

import com.napier.devops.App;
import com.napier.devops.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);

    }

    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530);
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");
    }

    @Test
    void testGetEmployeeByName()
    {
        // Get employee by first and last name
        Employee emp = app.getEmployeeByName("Ronghao", "Garigliano");

        // Check employee data
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }

    @Test
    void testDisplayEmployee()
    {
        // Get known employee from database
        Employee emp = app.getEmployeeByName("Ronghao", "Garigliano");

        // Display employee information
        app.displayEmployee(emp);

        // Check that the employee object is not null
        assertNotNull(emp);

        // Check basic fields
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }


    @Test
    void testGetDepartment()
    {
        // Get department by name
        Department dept = app.getDepartment("Development");

        // Check department data
        assertEquals("Development", dept.dept_name);
        assertNotNull(dept.manager);
    }


    @Test
    void testGetSalariesByDepartment()
    {
        // Get department object
        Department dept = app.getDepartment("Development");

        // Get salaries by department
        ArrayList<Employee> employees = app.getSalariesByDepartment(dept);

        // Check that list is not empty
        assertTrue(employees.size() > 0);
    }


    @Test
    void testPrintSalaries()
    {
        // Get department and employees
        Department dept = app.getDepartment("Development");
        ArrayList<Employee> employees = app.getSalariesByDepartment(dept);

        // Print salaries
        app.printSalaries(employees);

        // Check that list is not null and has data
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
    }

    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        app.addEmployee(emp);
        emp = app.getEmployee(500000);
        assertEquals(emp.emp_no, 500000);
        assertEquals(emp.first_name, "Kevin");
        assertEquals(emp.last_name, "Chalmers");
    }

}