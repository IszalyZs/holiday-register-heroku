package com.oh.register.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oh.register.exception.RegisterException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The first name field can't be empty!")
    private String firstName;

    @NotBlank(message = "The last name field can't be empty!")
    private String lastName;

    @Pattern(regexp = "[0-9]{9}", message = "The identity number length should be exactly 9 numbers!")
    @Column(unique = true)
    private String identityNumber;

    @NotNull(message = "The birth date field can't be empty!")
    private LocalDate birthDate;

    @NotBlank(message = "The workplace field can't be empty!")
    private String workplace;

    @NotBlank(message = "The position field can't be empty!")
    private String position;

    @NotNull(message = "The time of entry field can't be empty!")
    private LocalDate dateOfEntry;

    private LocalDate beginningOfEmployment;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"employee"})
    private List<Children> childrenList = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Holiday> holiday = new ArrayList<>();

    private Integer basicLeave;

    private Long extraLeave = 0L;

    private Long sumHoliday = 0L;

    private Long nextYearLeave = 0L;

    private Long sumHolidayNextYear=0L;

    public void setBasicLeave() {
        LocalDate nowDate = LocalDate.now();
        LocalDate employeeBirthDate = this.getBirthDate();
        long age = ChronoUnit.YEARS.between(employeeBirthDate, nowDate);
        if (age < 16) throw new RegisterException("Your age is under 16!");
        else if (age >= 16 && age < 25) setBasicLeave(20);
        else if (age >= 25 && age < 28) setBasicLeave(21);
        else if (age >= 28 && age < 31) setBasicLeave(22);
        else if (age >= 31 && age < 33) setBasicLeave(23);
        else if (age >= 33 && age < 35) setBasicLeave(24);
        else if (age >= 35 && age < 37) setBasicLeave(25);
        else if (age >= 37 && age < 39) setBasicLeave(26);
        else if (age >= 39 && age < 41) setBasicLeave(27);
        else if (age >= 41 && age < 43) setBasicLeave(28);
        else if (age >= 43 && age < 45) setBasicLeave(29);
        else setBasicLeave(30);

    }

}
