package com.oh.register.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HolidayDTO {

    private Long id;

    @JsonIgnore
    private Long employeeId;

    @NotNull(message = "The start date field can't be empty!")
    private LocalDate startDate;

    @NotNull(message = "The finish date field can't be empty!")
    private LocalDate finishDate;

    @Override
    public String toString() {
        return String.format("id=%d, employeeId=%d, startDate=%s, finishDate=%s", id, employeeId, startDate.toString(), finishDate.toString());
    }
}
