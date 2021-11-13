package com.oh.register.repository;

import com.oh.register.model.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Long> {
  List<Holiday> findByEmployee_Id(Long id);
}
