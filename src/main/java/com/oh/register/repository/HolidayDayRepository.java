package com.oh.register.repository;

import com.oh.register.model.entity.HolidayDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayDayRepository extends JpaRepository<HolidayDay,Long> {
    HolidayDay findByYear(String year);
}
