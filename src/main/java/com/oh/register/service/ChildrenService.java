package com.oh.register.service;

import com.oh.register.converter.ChildrenDTOToChildren;
import com.oh.register.converter.ChildrenToChildrenDTO;
import com.oh.register.converter.EmployeeToEmployeeDTO;
import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.entity.Children;
import com.oh.register.model.entity.Employee;
import com.oh.register.repository.ChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChildrenService {

    private final ChildrenRepository childrenRepository;
    private final ChildrenDTOToChildren childrenDTOToChildren;
    private final ChildrenToChildrenDTO childrenToChildrenDTO;
    private final EmployeeService employeeService;
    private final EmployeeToEmployeeDTO employeeToEmployeeDTO;

    @Autowired
    public ChildrenService(ChildrenRepository childrenRepository, ChildrenDTOToChildren childrenDTOToChildren, ChildrenToChildrenDTO childrenToChildrenDTO, EmployeeService employeeService, EmployeeToEmployeeDTO employeeToEmployeeDTO) {
        this.childrenRepository = childrenRepository;
        this.childrenDTOToChildren = childrenDTOToChildren;
        this.childrenToChildrenDTO = childrenToChildrenDTO;
        this.employeeService = employeeService;
        this.employeeToEmployeeDTO = employeeToEmployeeDTO;
    }

    public List<ChildrenDTO> findAll() {
        List<Children> childrenList = childrenRepository.findAll();
        if (childrenList.size() == 0) {
            throw new RegisterException("The children entities don't exist!");
        }
        return childrenList.stream().map(childrenToChildrenDTO::getChildrenDTO).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        try {
            Long employeeId = this.findById(id).getEmployeeId();
            childrenRepository.deleteById(id);
            employeeService.setExtraLeave(null, employeeId, null);
        } catch (Exception exception) {
            throw new RegisterException("No children entity with id: " + id + "!");
        }
    }

    public ChildrenDTO findById(Long id) {
        Optional<Children> optionalChildren = childrenRepository.findById(id);
        if (optionalChildren.isPresent()) {
            Children children = optionalChildren.get();
            return childrenToChildrenDTO.getChildrenDTO(children);
        }
        throw new RegisterException("The children entity doesn't exist with id: " + id + "!");
    }

    public ChildrenDTO save(ChildrenDTO childrenDTO) {
        Employee employee = employeeService.findById(childrenDTO.getEmployeeId());
        childrenDTO.setEmployeeDTO(employeeToEmployeeDTO.getEmployeeDTO(employee));
        Children children = childrenRepository.save(childrenDTOToChildren.getChildren(childrenDTO));

        employee.getChildrenList().add(children);
        employeeService.saveEmployee(employee);

        employeeService.setExtraLeave(childrenDTO, null, null);
        return childrenToChildrenDTO.getChildrenDTO(children);
    }

    public ChildrenDTO update(ChildrenDTO childrenDTO) {
        List<Children> childrenList = childrenRepository.findAll();
        Optional<Children> optionalChildren = childrenList.stream().filter(children -> children.getId() == childrenDTO.getId()).findAny();
        Employee employee;
        if (optionalChildren.isPresent()) employee = optionalChildren.get().getEmployee();
        else
            throw new RegisterException("Employee entity doesn't exist with this children id: " + childrenDTO.getId() + "!");
        Children children = childrenDTOToChildren.getChildren(childrenDTO);
        children.setEmployee(employee);
        Children savedChildren = childrenRepository.save(children);
        return childrenToChildrenDTO.getChildrenDTO(savedChildren);
    }
}
