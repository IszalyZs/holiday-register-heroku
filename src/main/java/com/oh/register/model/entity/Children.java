package com.oh.register.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Entity(name = "Children")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Children {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The first name field can't be empty!")
    private String firstName;

    @NotBlank(message = "The last name field can't be empty!")
    private String lastName;

    @Past(message = "Date of birth must be less than today!")
    @NotNull(message = "The birthday field can't be empty!")
    private LocalDate birthDay;

    @ManyToOne
    @JsonIgnore
    private Employee employee;
}
