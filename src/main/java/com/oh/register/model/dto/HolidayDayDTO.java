package com.oh.register.model.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HolidayDayDTO {
    private Long id;

    @Pattern(regexp = "^[2][0-9]{3}", message = "The year length should be exactly 4 numbers and the first number has to be 2!")
    @NotBlank(message = "The year field can't be empty!")
    @Column(unique = true, nullable = false)
    private String year;

    private List<LocalDate> localDate = new ArrayList<>();

    @Override
    public String toString() {
        return "id=" + id + ", year=" + year + ", localDate=" + localDate;
    }
}
