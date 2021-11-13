package com.oh.register.converter;

import com.oh.register.model.dto.HolidayDayDTO;
import com.oh.register.model.entity.HolidayDay;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayDayDTOToHolidayDay {
    private final ModelMapper modelMapper;

    @Autowired
    public HolidayDayDTOToHolidayDay(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public HolidayDay getHoliday(HolidayDayDTO holidayDayDTO) {
        return modelMapper.map(holidayDayDTO, HolidayDay.class);
    }
}
