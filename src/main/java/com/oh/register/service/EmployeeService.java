package com.oh.register.service;

import com.oh.register.converter.EmployeeDTOToEmployee;
import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.dto.EmployeeDTO;
import com.oh.register.model.entity.Children;
import com.oh.register.model.entity.Employee;
import com.oh.register.repository.ChildrenRepository;
import com.oh.register.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDTOToEmployee employeeDTOToEmployee;
    private final ChildrenRepository childrenRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeDTOToEmployee employeeDTOToEmployee, ChildrenRepository childrenRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeDTOToEmployee = employeeDTOToEmployee;
        this.childrenRepository = childrenRepository;
    }

    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() == 0) {
            throw new RegisterException("The employee entities don't exist!");
        }
        return employees;
    }

    public void deleteById(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception exception) {
            throw new RegisterException("No employee entity with id: " + id + "!");
        }
    }

    public Employee findById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        throw new RegisterException("The employee entity doesn't exist with id: " + id + "!");
    }

    public Employee save(EmployeeDTO employeeDTO) {
        checkingBeginningDate(employeeDTO.getBeginningOfEmployment(), employeeDTO.getDateOfEntry());
        return employeeRepository.save(setBasicLeave(employeeDTO));
    }


    public Employee update(EmployeeDTO employeeDTO) {
        checkingBeginningDate(employeeDTO.getBeginningOfEmployment(), employeeDTO.getDateOfEntry());
        List<Children> childrenList = childrenRepository.findAll().stream()
                .filter(children -> children.getEmployee().getId().longValue() == employeeDTO.getId().longValue())
                .collect(Collectors.toList());
        Employee employee = setBasicLeave(employeeDTO);
        employee.setChildrenList(childrenList);
        employee.setSumHoliday(findById(employeeDTO.getId()).getSumHoliday());
        employee.setSumHolidayNextYear(findById(employeeDTO.getId()).getSumHolidayNextYear());
        return setExtraLeave(null, null, employee);
    }

    public Employee saveEmployee(Employee employee) {
        checkingBeginningDate(employee.getBeginningOfEmployment(), employee.getDateOfEntry());
        employee.setBasicLeave();
        employee.setNextYearLeave(employee.getBasicLeave()+employee.getExtraLeave());
        return employeeRepository.save(employee);
    }


    private Employee setBasicLeave(EmployeeDTO employeeDTO) {
        Employee employee = employeeDTOToEmployee.getEmployee(employeeDTO);
        employee.setBasicLeave();
        employee.setNextYearLeave(employee.getBasicLeave()+employee.getExtraLeave());
        return employee;
    }


    private void checkingBeginningDate(LocalDate beginningDate, LocalDate dateOfEntry) {
        if (beginningDate != null && beginningDate.isBefore(dateOfEntry))
            throw new RegisterException("The date of entry must be earlier than the beginning of employment!");
    }


    public Employee setExtraLeave(ChildrenDTO childrenDTO, Long employeeId, Employee emp) {
        Employee employee;
        List<Children> childrenList;
        if (emp != null && childrenDTO == null && employeeId == null)
            employee = emp;
        else if (emp == null && childrenDTO != null && employeeId == null)
            employee = this.findById(childrenDTO.getEmployeeId());
        else if (emp == null && childrenDTO == null && employeeId != null)
            employee = this.findById(employeeId);
        else
            throw new RegisterException("You have to give one not null argument and two null argument to the setExtraLeave method!");

        childrenList = employee.getChildrenList();
        if (childrenList.size() == 1)
            employee.setExtraLeave(2L);
        else if (childrenList.size() == 2)
            employee.setExtraLeave(4L);
        else if (childrenList.size() > 2)
            employee.setExtraLeave(7L);
        else employee.setExtraLeave(0L);

        employee.setNextYearLeave(employee.getBasicLeave() + employee.getExtraLeave());

        return this.saveEmployee(employee);
    }
}
