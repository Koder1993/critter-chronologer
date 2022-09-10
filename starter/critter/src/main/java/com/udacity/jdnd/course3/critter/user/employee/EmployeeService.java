package com.udacity.jdnd.course3.critter.user.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    public void updateEmployee(long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(DayOfWeek date, Set<EmployeeSkill> skills) {
        List<Employee> employeeList = employeeRepository.findAllByDaysAvailable(date);
        List<Employee> requiredEmployees = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getSkills().containsAll(skills)) {
                requiredEmployees.add(employee);
            }
        }
        return requiredEmployees;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
