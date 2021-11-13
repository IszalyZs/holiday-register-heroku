package com.oh.register.controller;

import com.oh.register.config.BindingErrorHandler;
import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.MessageDTO;
import com.oh.register.model.dto.ErrorDTO;
import com.oh.register.model.dto.HolidayDayDTO;
import com.oh.register.service.HolidayDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/holidayday")
@Tag(name = "Operations on HolidayDay")
//@CrossOrigin(origins = "*")
public class HolidayDayController {
    private final HolidayDayService holidayDayService;
    private final BindingErrorHandler bindingErrorHandler;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HolidayDayController(HolidayDayService holidayDayService, BindingErrorHandler bindingErrorHandler) {
        this.holidayDayService = holidayDayService;
        this.bindingErrorHandler = bindingErrorHandler;
    }

    @GetMapping("/all")
    @Operation(summary = "list all holiday days", description = "list all holiday days")
    public ResponseEntity<List<HolidayDayDTO>> findAll() {
        return ResponseEntity.ok(holidayDayService.findAll());
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "delete holiday day by id", description = "delete holiday day by id")
    public ResponseEntity<MessageDTO> deleteById(@PathVariable("id") Long id) {
        holidayDayService.deleteById(id);
        String message="The entity was deleted with id: " + id + "!";
        return ResponseEntity.ok(new MessageDTO(message));
    }

    @GetMapping("/{id}/get")
    @Operation(summary = "list holiday day by id", description = "list holiday day by id")
    public ResponseEntity<HolidayDayDTO> findById(@PathVariable("id") Long id) {
        if(id==null) throw new RegisterException("The given id mustn't be null!");
        return ResponseEntity.ok(holidayDayService.findById(id));
    }

    @PostMapping("/add")
    @Operation(summary = "save holiday day", description = "save holiday day")
    public ResponseEntity<?> save(@Valid @RequestBody HolidayDayDTO holidayDayDTO, BindingResult bindingResult) {
        if (holidayDayDTO.getId() != null) holidayDayDTO.setId(null);
        HolidayDayDTO response;
        String logMessage = "Posted holiday day entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        try {
            response = holidayDayService.save(holidayDayDTO);
        } catch (DataIntegrityViolationException exception) {
            String message = "Duplicate entry at holiday days year:" + holidayDayDTO.getYear() + " is already exists!";
            return new ResponseEntity<>(new ErrorDTO(message), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "update holiday day by id", description = "update holiday day by id")
    public ResponseEntity<?> update(@Valid @RequestBody HolidayDayDTO holidayDayDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        HolidayDayDTO response;
        String logMessage = "Updated holiday day entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        try {
            holidayDayDTO.setId(id);
            response = holidayDayService.update(holidayDayDTO);
        } catch (DataIntegrityViolationException exception) {
            String message = "Duplicate entry at holiday days year:" + holidayDayDTO.getYear() + " is already exists!";
            return new ResponseEntity<>(new ErrorDTO(message), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

}
