package com.oh.register.converter;

import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.entity.Children;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChildrenToChildrenDTO {
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenToChildrenDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ChildrenDTO getChildrenDTO(Children children) {
        return modelMapper.map(children, ChildrenDTO.class);
    }
}
