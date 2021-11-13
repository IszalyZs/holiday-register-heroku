package com.oh.register.converter;

import com.oh.register.model.dto.HolidayDTO;
import com.oh.register.model.entity.Holiday;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayToHolidayDTO {

    private final ModelMapper modelMapper;

    @Autowired
    public HolidayToHolidayDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HolidayDTO getHolidayDTO(Holiday holiday) {
        HolidayDTO holidayDTO = modelMapper.map(holiday, HolidayDTO.class);
        holidayDTO.setEmployeeId(holiday.getEmployee().getId());
        return holidayDTO;
    }
}
