package com.oh.register.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "Holiday")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Long id;

    @ManyToOne
    private Employee employee;

    @NotNull(message = "The start date field can't be empty!")
    private LocalDate startDate;

    @NotNull(message = "The finish date field can't be empty!")
    private LocalDate finishDate;
}
