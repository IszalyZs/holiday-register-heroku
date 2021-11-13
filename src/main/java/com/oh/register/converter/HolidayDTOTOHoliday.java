package com.oh.register.converter;

import com.oh.register.model.dto.HolidayDTO;
import com.oh.register.model.entity.Employee;
import com.oh.register.model.entity.Holiday;
import com.oh.register.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HolidayDTOTOHoliday {

    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;

    @Autowired
    public HolidayDTOTOHoliday(ModelMapper modelMapper, EmployeeService employeeService) {
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
    }

    public Holiday getHoliday(HolidayDTO holidayDTO) {
        Holiday holiday = modelMapper.map(holidayDTO, Holiday.class);
        Employee employee = employeeService.findById(holidayDTO.getEmployeeId());
        holiday.setEmployee(employee);
        return holiday;
    }
}
