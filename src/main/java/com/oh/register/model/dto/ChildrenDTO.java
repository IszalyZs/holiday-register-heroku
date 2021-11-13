package com.oh.register.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChildrenDTO {
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
    @JoinColumn(name = "employeeDTO_id")
    private EmployeeDTO employeeDTO;

    @JsonIgnore
    private Long employeeId;

    @Override
    public String toString() {
        return String.format("id=%d, firstName=%s, lastName=%s, birthDay=%s ", id, firstName, lastName, birthDay.toString());
    }
}
