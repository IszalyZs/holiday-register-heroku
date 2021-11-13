package com.oh.register.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "HolidayDay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[2][0-9]{3}", message = "The year length should be exactly 4 numbers and the first number has to be 2!")
    @NotBlank(message = "The year field can't be empty!")
    @Column(unique = true, nullable = false)
    private String year;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<LocalDate> localDate= new ArrayList<>();
}
