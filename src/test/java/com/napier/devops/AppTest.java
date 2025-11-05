package com.napier.devops;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    /**
     * Test for printSalaries
     */

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        app.printSalaries(employess);
    }

    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }

    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

    /**
     * Test for displaySalaries
     */

    @Test
    void displayEmployeeTestNull()
    {
        assertDoesNotThrow(() -> app.displayEmployee(null));
    }

    @Test
    void displayEmployeeTestValid()
    {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Employee emp = new Employee();
        emp.emp_no = 10001;
        emp.first_name = "John";
        emp.last_name = "Smith";
        emp.title = "Engineer";
        emp.salary = 50000;

        Department dept = new Department();
        dept.dept_name = "Engineering";
        emp.dept = dept;

        Employee manager = new Employee();
        manager.first_name = "Alice";
        manager.last_name = "Brown";
        emp.manager = manager;

        app.displayEmployee(emp);

        String output = outContent.toString();
        assertTrue(output.contains("John"));
        assertTrue(output.contains("Engineering"));
        assertTrue(output.contains("Manager: Alice Brown"));

        System.setOut(originalOut);
        System.out.println(output);
    }


    @Test
    void displayEmployeeTestMissingDepartment()
    {
        Employee emp = new Employee();
        emp.emp_no = 10002;
        emp.first_name = "Kevin";
        emp.last_name = "Lee";
        emp.title = "Analyst";
        emp.salary = 48000;
        emp.dept = null;

        Employee manager = new Employee();
        manager.first_name = "Lisa";
        manager.last_name = "Chen";
        emp.manager = manager;

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }

    @Test
    void displayEmployeeTestMissingManager()
    {
        Employee emp = new Employee();
        emp.emp_no = 10003;
        emp.first_name = "Sara";
        emp.last_name = "Kim";
        emp.title = "Technician";
        emp.salary = 46000;

        Department dept = new Department();
        dept.dept_name = "Maintenance";
        emp.dept = dept;
        emp.manager = null;

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }

}