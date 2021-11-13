package com.oh.register.converter;

import com.oh.register.model.dto.EmployeeDTO;
import com.oh.register.model.entity.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToEmployeeDTO {
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeToEmployeeDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO getEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }
}
