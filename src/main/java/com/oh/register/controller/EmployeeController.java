package com.oh.register.controller;

import com.oh.register.config.BindingErrorHandler;
import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.MessageDTO;
import com.oh.register.model.dto.EmployeeDTO;
import com.oh.register.model.dto.ErrorDTO;
import com.oh.register.model.entity.Employee;
import com.oh.register.service.EmployeeService;
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
//@CrossOrigin(origins = "*")
@RequestMapping("/employee")
@Tag(name = "Operations on Employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final BindingErrorHandler bindingErrorHandler;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EmployeeController(EmployeeService employeeService, BindingErrorHandler bindingErrorHandler) {
        this.employeeService = employeeService;
        this.bindingErrorHandler = bindingErrorHandler;
    }
    @GetMapping("/all")
    @Operation(summary = "list all employee", description = "list all employee")
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "delete employee by id", description = "delete employee by id")
    public ResponseEntity<MessageDTO> deleteById(@PathVariable("id") Long id) {
        employeeService.deleteById(id);
        String message="The entity was deleted with id: " + id + "!";
        return ResponseEntity.ok(new MessageDTO(message));
    }

    @GetMapping("/{id}/get")
    @Operation(summary = "list employee by id", description = "list employee by id")
    public ResponseEntity<Employee> findById(@PathVariable("id") Long id) {
        if (id == null) throw new RegisterException("The given id mustn't be null!");
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PostMapping("/add")
    @Operation(summary = "save employee", description = "save employee")
    public ResponseEntity<?> save(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if (employeeDTO.getId() != null) employeeDTO.setId(null);
        Employee response;
        String logMessage = "Posted employee entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        try {
            response = employeeService.save(employeeDTO);
        } catch (DataIntegrityViolationException exception) {
            String message = "Duplicate entry at employees identity number:" + employeeDTO.getIdentityNumber() + " is already exists!";
            return new ResponseEntity<>(new ErrorDTO(message), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "update employee by id", description = "update employee by id")
    public ResponseEntity<?> update(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        Employee response;
        String logMessage = "Updated employee entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        try {
            employeeDTO.setId(id);
            response = employeeService.update(employeeDTO);
        } catch (DataIntegrityViolationException exception) {
            String message = "Duplicate entry at employees identity number:" +employeeDTO.getIdentityNumber() + " is already exists!";
            return new ResponseEntity<>(new ErrorDTO(message), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
}
