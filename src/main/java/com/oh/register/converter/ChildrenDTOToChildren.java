package com.oh.register.converter;

import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.entity.Children;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ChildrenDTOToChildren {
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenDTOToChildren(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Children getChildren(ChildrenDTO childrenDTO) {
        return modelMapper.map(childrenDTO, Children.class);
    }
}
