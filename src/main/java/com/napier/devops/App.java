package com.napier.devops;

import java.sql.*;
import java.util.ArrayList;

public class App {
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Get an employee by employee number.
     * Includes department and manager information.
     */
    public Employee getEmployee(int ID) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, " +
                            "t.title, s.salary, d.dept_name, " +
                            "m.emp_no AS mgr_no, m.first_name AS mgr_first, m.last_name AS mgr_last " +
                            "FROM employees e " +
                            "JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                            "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                            "JOIN departments d ON de.dept_no = d.dept_no " +
                            "JOIN dept_manager dm ON d.dept_no = dm.dept_no AND dm.to_date = '9999-01-01' " +
                            "JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.emp_no = " + ID;

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                // Department object
                Department dept = new Department();
                dept.dept_name = rset.getString("dept_name");

                // Manager object
                Employee manager = new Employee();
                manager.emp_no = rset.getInt("mgr_no");
                manager.first_name = rset.getString("mgr_first");
                manager.last_name = rset.getString("mgr_last");

                dept.manager = manager;
                emp.dept = dept;
                emp.manager = manager;

                return emp;
            } else
                return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Get an employee by first name and last name (Exercise 3).
     */
    public Employee getEmployeeByName(String firstName, String lastName) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, " +
                            "t.title, s.salary, d.dept_name, " +
                            "m.emp_no AS mgr_no, m.first_name AS mgr_first, m.last_name AS mgr_last " +
                            "FROM employees e " +
                            "JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                            "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                            "JOIN departments d ON de.dept_no = d.dept_no " +
                            "JOIN dept_manager dm ON d.dept_no = dm.dept_no AND dm.to_date = '9999-01-01' " +
                            "JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.first_name = '" + firstName + "' AND e.last_name = '" + lastName + "'";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                Department dept = new Department();
                dept.dept_name = rset.getString("dept_name");

                Employee manager = new Employee();
                manager.emp_no = rset.getInt("mgr_no");
                manager.first_name = rset.getString("mgr_first");
                manager.last_name = rset.getString("mgr_last");

                dept.manager = manager;
                emp.dept = dept;
                emp.manager = manager;

                return emp;
            } else {
                System.out.println("Employee not found: " + firstName + " " + lastName);
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee by name");
            return null;
        }
    }

    /**
     * Display an employee's information.
     */
    public void displayEmployee(Employee emp) {
        if (emp == null) {
            System.out.println("No employee data.");
            return;
        }

        System.out.println("Emp No     First Name      Last Name      Title");
        System.out.println(String.format("%-10s %-15s %-15s %-20s",
                emp.emp_no, emp.first_name, emp.last_name,
                emp.title != null ? emp.title : "No Title"));

        System.out.println("Salary: " + emp.salary);

        if (emp.dept != null && emp.dept.dept_name != null)
            System.out.println("Department: " + emp.dept.dept_name);
        else
            System.out.println("Department: No Department");

        if (emp.manager != null)
            System.out.println("Manager: " + emp.manager.first_name + " " + emp.manager.last_name);
        else
            System.out.println("Manager: No Manager");

        System.out.println("---------------------------------------------");
    }



    /**
     * Get a department by name, including its manager (Exercise 1).
     */
    public Department getDepartment(String dept_name) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT d.dept_no, d.dept_name, e.emp_no, e.first_name, e.last_name " +
                            "FROM departments d " +
                            "JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "JOIN employees e ON dm.emp_no = e.emp_no " +
                            "WHERE d.dept_name = '" + dept_name + "' " +
                            "AND dm.to_date = '9999-01-01'";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");

                Employee manager = new Employee();
                manager.emp_no = rset.getInt("emp_no");
                manager.first_name = rset.getString("first_name");
                manager.last_name = rset.getString("last_name");

                dept.manager = manager;
                return dept;
            } else {
                System.out.println("Department not found: " + dept_name);
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }

    /**
     * Get all current employees and their salaries within a specific department.
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary " +
                            "FROM employees, salaries, dept_emp, departments " +
                            "WHERE employees.emp_no = salaries.emp_no " +
                            "AND employees.emp_no = dept_emp.emp_no " +
                            "AND dept_emp.dept_no = departments.dept_no " +
                            "AND salaries.to_date = '9999-01-01' " +
                            "AND departments.dept_no = '" + dept.dept_no + "' " +
                            "ORDER BY employees.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<>();

            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }

            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by department");
            return null;
        }
    }

    /**
     * Print a list of employees.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    public static void main(String[] args) {
        // Create new Application and connect to database
        App a = new App();

        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        Department dept = a.getDepartment("Development");
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);


        // Print salary report
        a.printSalaries(employees);

        // Disconnect from database
        a.disconnect();
    }
}
