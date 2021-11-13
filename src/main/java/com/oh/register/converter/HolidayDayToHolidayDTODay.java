package com.oh.register.converter;

import com.oh.register.model.dto.HolidayDayDTO;
import com.oh.register.model.entity.HolidayDay;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayDayToHolidayDTODay {
    private final ModelMapper modelMapper;

    @Autowired
    public HolidayDayToHolidayDTODay(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HolidayDayDTO getHolidayDTO(HolidayDay holidayDay) {
        return modelMapper.map(holidayDay, HolidayDayDTO.class);
    }
}
